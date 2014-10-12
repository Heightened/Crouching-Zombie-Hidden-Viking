package controller.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
	}

	@Override
	public void update()
	{
		long dtime = System.currentTimeMillis() - this.time;
		this.time  = System.currentTimeMillis();
		
		if(!this.leaderChooser.loyal(0, dtime))
			this.setLeader(this.leaderChooser.chooseLeader(this.getCloseAllies()));
		
		Strategy strategy = null;
		
		if(this.leader != null)
			strategy = this.leader.getOrders(this);
		if(strategy == null)
			strategy = this.strategyChooser.choose(this.leader, dtime);
		
		this.commands = strategy.getCommandSet(this, dtime);
		
		if(this.commands.getAction() != null)
			this.getGame().getActionBuffer().add(this.commands.getAction());
	}
	
	public Collection<GameCharacter> getCloseAllies()
	{
		Collection<Cell> cells = this.getGame().getMap().getNearbyCells(
				this.getCharacter().getCell().getX(),
				this.getCharacter().getCell().getY(),
				10);
		
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
		
		if(this.followers.contains(c))
			return true;
		
		for(AIController f : this.followers)
			if(f.isFollower(c))
				return true;
		
		return false;
	}
	
	public void setLeader(GameCharacter leader)
	{
		this.leader = this.controlBinding.get(leader);
	}

	@Override
	public int getFollowerCount()
	{
		int count = 0;
		for(AIController c : this.followers)
		{
			count += c.getFollowerCount()+1;
		}
		
		return count;
	}
	
	@Override
	public int getGroupSize()
	{
		if(this.leader == null)
			return this.getFollowerCount();
		else
			return this.leader.getFollowerCount();
	}

	@Override
	public float getSatisfactionLevel()
	{
		float sum = 0;
		
		for(AIController c : this.followers)
		{
			sum += c.getSatisfaction();
		}
		
		if(this.getFollowerCount() == 0)
			return 0;
		else
			return sum/this.getFollowerCount();
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
}