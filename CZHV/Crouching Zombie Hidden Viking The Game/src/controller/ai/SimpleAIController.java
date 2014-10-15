package controller.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.AIController;
import controller.Controller;
import controller.actions.MoveAction;
import controller.ai.strategy.CommandSet;
import controller.ai.strategy.Strategy;
import util.Rand;
import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

public class SimpleAIController extends AIController
{
	private AIController leader;
	private Collection<AIController> followers;
	private Map<GameCharacter, AIController> controlBinding;
	
	private LeaderChooser leaderChooser;
	private StrategyChooser strategyChooser;
	
	private CommandSet commands;
	
	private long time;
	
	public SimpleAIController(Game game, GameCharacter gameChar, Map<GameCharacter, AIController> controlBinding)
	{
		super(game, gameChar);
		this.controlBinding = controlBinding;
		this.followers = new HashSet<>();
		this.leaderChooser = new LeaderChooser(this, controlBinding);
		this.strategyChooser = new StupidStrategyChooser();
		
		if(Game.AI_DRAW_HIERARCHY)
		{
			this.getCharacter().setFollowers(this.getFollowers());
		}
	}

	@Override
	public void update()
	{
		long dtime = System.currentTimeMillis() - this.time;
		this.time  = System.currentTimeMillis();
		
		GameCharacter leaderCharacter;
		boolean justChoseLeader = false;
		
		if(this.leader != null)
			leaderCharacter = this.leader.getCharacter();
		else
			leaderCharacter = null;
		
		if(!this.leaderChooser.loyal(leaderCharacter, 0, dtime))
		{
			this.setLeader(this.leaderChooser.chooseLeader(this.getCloseAllies()));
			justChoseLeader = true;
		}
		
		Strategy strategy = null;
		
		if(this.leader != null)
			strategy = this.leader.getOrders(this);
		if(strategy == null)
			strategy = this.strategyChooser.choose(this.leader, dtime, justChoseLeader);
		
		this.commands = strategy.getCommandSet(this, dtime);
		
		if(this.commands.getAction() != null)
			this.getGame().getActionBuffer().add(this.commands.getAction());
	}
	
	public Collection<GameCharacter> getCloseAllies()
	{
		Collection<Cell> cells = this.getGame().getMap().getNearbyCells(
				this.getCharacter().getCell().getX(),
				this.getCharacter().getCell().getY(),
				5);
		
		Collection<GameCharacter> allies = new ArrayList<>();
		for(Cell c : cells)
			for(GameCharacter character : c.getCharacterHolder().getItem())
				if(character.isInfected() == this.getCharacter().isInfected())
					allies.add(character);
		
		return allies;
	}
	
	public boolean isFollower(AIController c)
	{
		if(c == this)
			return true;
		
		synchronized(this.followers)
		{
			if(this.followers.contains(c))
				return true;
			
			for(AIController f : this.followers)
				if(f.isFollower(c))
					return true;
		}
		
		return false;
	}
	
	public void setLeader(GameCharacter leader)
	{
		if(this.leader != null)
			this.leader.unregister(this);
		this.leader = this.controlBinding.get(leader);
		if(this.leader != null)
			this.leader.register(this);
	}
	
	@Override
	public Collection<AIController> getFollowers()
	{
		return this.followers;
	}

	@Override
	public int getFollowerCount(AIController source)
	{
		if(source == this)
			return 0;
		if(source == null)
			source = this;
		
		int count = 0;
		
		synchronized(this.followers)
		{
			for(AIController c : this.followers)
			{
				count += c.getFollowerCount(source)+1;
			}
		}
		
		return count;
	}
	
	@Override
	public int getGroupSize(int depth)
	{
		if(depth <= 0 || this.leader == null)
			return this.getFollowerCount(null)+1;
		else
			return this.leader.getGroupSize(depth-1);
	}

	@Override
	public float getSatisfactionLevel()
	{
		float sum = 0;
		
		synchronized(this.followers)
		{
			for(AIController c : this.followers)
			{
				sum += c.getSatisfaction();
			}
		}
		
		if(this.getFollowerCount(null) == 0)
			return 0;
		else
			return sum/this.getFollowerCount(null);
	}
	
	public float getSatisfaction()
	{
		return this.leaderChooser.getSatisfaction();
	}

	@Override
	public Strategy getOrders(AIController c)
	{
		if(this.commands == null)
			return null;
		else
			return this.commands.getStrategy(c);
	}

	@Override
	public void register(AIController c)
	{
		synchronized(this.followers)
		{
			this.followers.add(c);
		}
	}

	@Override
	public void unregister(AIController c)
	{
		synchronized(this.followers)
		{
			this.followers.remove(c);
		}
	}
}
