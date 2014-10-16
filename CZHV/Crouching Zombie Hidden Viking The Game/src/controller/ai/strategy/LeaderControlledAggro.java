package controller.ai.strategy;

import java.util.Collection;
import java.util.HashMap;

import util.Rand;
import model.character.GameCharacter;
import model.map.Cell;
import controller.AIController;
import controller.actions.MoveAction;
import controller.actions.ShootAction;

public class LeaderControlledAggro extends Wander
{
	private boolean firstTime = true;
	@Override
	public CommandSet getCommandSet(AIController commander, long dtime)
	{
		if(firstTime || Rand.randInt(0, 500) < dtime) // roughly every second
		{
			firstTime = false;
			
			Collection<Cell> surroundingCells = commander.getGame().getMap().getNearbyCells(
					commander.getCharacter().getCell().getX(),
					commander.getCharacter().getCell().getY(),
					Wander.WANDER_DISTANCE
				);
			
			GameCharacter target = null;
			float distance = -1;
			for(Cell c : surroundingCells)
				synchronized(c.getCharacterHolder().getItem())
				{
					for(GameCharacter gc : c.getCharacterHolder().getItem())
						if(!gc.isInfected() && distance < 0 || distance > commander.getCharacter().distanceTo(gc))
						{
							target = gc;
						}
				}
			
			if(target == null)
				return super.getCommandSet(commander, dtime);
			
			CommandSet commands;
			
			if(distance < commander.getCharacter().getBestRange())
			{
				commands = new CommandSet(
						new ShootAction(commander.getCharacter(), target),
						new HashMap<AIController, Strategy>()
					);
				
				for(AIController follower : commander.getFollowers())
					commands.setStrategy(follower, new Attack(target));
			}
			else
			{
				commands = new CommandSet(
						new MoveAction(commander.getCharacter(), target.getAbsX(), target.getAbsY()),
						new HashMap<AIController, Strategy>()
					);
				
				for(AIController follower : commander.getFollowers())
					commands.setStrategy(follower, new Attack(target));
			}
			
			return commands;
		}
		else
			return new CommandSet(null, null);
	}
	
}
