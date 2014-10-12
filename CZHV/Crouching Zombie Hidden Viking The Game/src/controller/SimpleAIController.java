package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import controller.actions.MoveAction;
import controller.ai.LeaderChooser;
import util.Rand;
import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

public class SimpleAIController extends AIController
{
	private Controller leader;
	private Collection<SimpleAIController> followers;
	private Map<GameCharacter, AIController> controlBinding;
	
	private LeaderChooser leaderChooser;
	
	private State currentState = State.WANDER;
	private long time;
	
	public SimpleAIController(Game game, GameCharacter gameChar, Map<GameCharacter, AIController> controlBinding)
	{
		super(game, gameChar);
		this.controlBinding = controlBinding;
		this.followers = new HashSet<>();
		this.leaderChooser = new LeaderChooser(this, controlBinding);
	}

	enum State
	{
		WANDER,
		ATTACK,
		FOLLOW,
		FLEE;
	}

	@Override
	public void update()
	{
		long dtime = System.currentTimeMillis() - this.time;
		this.time  = System.currentTimeMillis();
		
		
		if(!this.leaderChooser.loyal(this.followers.size(), 0, dtime))
			this.setLeader(this.leaderChooser.chooseLeader(this.getFollowerCount(), this.getCloseAllies()));
		
		this.wander(dtime);
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
	
	public void wander(long dtime)
	{
		if(Rand.randInt(0, 3000) < dtime) // roughly every 3 seconds
		{
			Collection<Cell> cells = this.getGame().getMap().getNearbyCells(
					this.getCharacter().getCell().getX(),
					this.getCharacter().getCell().getY(),
					10
				);
			
			List<Cell> possibleTargets = new ArrayList<>();
			
			for(Cell c : cells)
			{
				if(c.isFree(this.getCharacter()))
				{
					possibleTargets.add(c);
				}
			}
			
			Cell target = possibleTargets.get(Rand.randInt(0,possibleTargets.size()-1));
			
			this.getGame().getActionBuffer().add(
					new MoveAction(
						this.getCharacter(),
						target.getX()+Rand.randInt(-50, +49)/100f,
						target.getY()+Rand.randInt(-50, +49)/100f
					)
				);
		}
	}

	@Override
	public int getFollowerCount()
	{
		return this.followers.size();
	}

	@Override
	public float getSatisfactionLevel()
	{
		
		float sum = 0;
		
		for(AIController c : this.followers)
		{
			sum += c.getSatisfaction();
		}
		
		return sum/this.getFollowerCount();
	}
	
	public float getSatisfaction()
	{
		return this.leaderChooser.getSatisfaction();
	}
}
