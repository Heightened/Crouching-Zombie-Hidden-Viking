package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import controller.actions.MoveAction;
import util.Rand;
import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

public class SimpleAIController extends AIController
{
	private Controller leader;
	private Collection<SimpleAIController> followers;
	private State currentState = State.WANDER;
	private long time;
	
	public SimpleAIController(Game game, GameCharacter gameChar)
	{
		super(game, gameChar);
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
		
		this.wander(dtime);
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
}
