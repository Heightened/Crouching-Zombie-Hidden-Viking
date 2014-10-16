package controller.ai.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import util.Rand;
import controller.AIController;
import controller.actions.Action;
import controller.actions.MoveAction;
import model.map.Cell;

public class Wander extends Strategy
{
	public static final int WANDER_DISTANCE = 10;
	private int wanderDirectionX=0,yWanderDirectionY=0;

	@Override
	public CommandSet getCommandSet(AIController commander, long dtime)
	{
		if(Rand.randInt(0, 3000) < dtime) // roughly every 3 seconds
		{
			Collection<Cell> cells = commander.getGame().getMap().getNearbyCells(
					commander.getCharacter().getCell().getX()+wanderDirectionX,
					commander.getCharacter().getCell().getY()+yWanderDirectionY,
					Wander.WANDER_DISTANCE
				);
			
			List<Cell> possibleTargets = new ArrayList<>();
			
			for(Cell c : cells)
			{
				if(c.isFree(commander.getCharacter()))
				{
					possibleTargets.add(c);
				}
			}
			
			Action action = null;

			CommandSet commands = new CommandSet(
					action,
					new HashMap<AIController, Strategy>()
				);
			
			float wanderTargetX;
			float wanderTargetY;
			if(possibleTargets.size() != 0)
			{
				Cell target = possibleTargets.get(Rand.randInt(0,possibleTargets.size()-1));
				wanderTargetX = this.wanderDirectionX +target.getX()+Rand.randInt(-50, +49)/100f;
				wanderTargetY = this.yWanderDirectionY+target.getY()+Rand.randInt(-50, +49)/100f;
				this.wanderDirectionX  = (int)Math.max(0.7 * Wander.WANDER_DISTANCE, Math.min(wanderTargetX-commander.getCharacter().getAbsX(), 0.7 * Wander.WANDER_DISTANCE));
				this.yWanderDirectionY = (int)Math.max(0.7 * Wander.WANDER_DISTANCE, Math.min(wanderTargetY-commander.getCharacter().getAbsY(), 0.7 * Wander.WANDER_DISTANCE));
				
				commands.setAction(new MoveAction(
						commander.getCharacter(),
						wanderTargetX, wanderTargetY
					));
				

				Collection<AIController> followers = commander.getFollowers();
				synchronized(followers)
				{
					for(AIController f : followers)
					{
						if(commander.getCharacter().distanceTo(f.getCharacter()) > 5)
							commands.setStrategy(f, new Follow(commander.getCharacter()));
						else if(action != null)
							commands.setStrategy(f, new GoTo(wanderTargetX, wanderTargetY));
					}
				}
			}
			else
			{
				System.out.println("No cells to wander to.. at ("+(commander.getCharacter().getCell().getX()+wanderDirectionX)+", "+(commander.getCharacter().getCell().getY()+yWanderDirectionY)+")");
				
				Collection<AIController> followers = commander.getFollowers();
				synchronized(followers)
				{
					for(AIController f : followers)
					{
						if(commander.getCharacter().distanceTo(f.getCharacter()) > 5)
							commands.setStrategy(f, new Follow(commander.getCharacter()));
						else if(action != null)
							commands.setStrategy(f, null);
					}
				}
			}
			
			return commands;
		}
		else
			return new CommandSet(null, null);
	}
}
