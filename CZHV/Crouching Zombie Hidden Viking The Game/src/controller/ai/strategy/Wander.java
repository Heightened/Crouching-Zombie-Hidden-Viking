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
	private int x=0,y=0;

	@Override
	public CommandSet getCommandSet(AIController commander, long dtime)
	{
		if(Rand.randInt(0, 3000) < dtime) // roughly every 3 seconds
		{
			Collection<Cell> cells = commander.getGame().getMap().getNearbyCells(
					commander.getCharacter().getCell().getX()+x,
					commander.getCharacter().getCell().getY()+y,
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
			
			if(possibleTargets.size() != 0)
			{
				Cell target = possibleTargets.get(Rand.randInt(0,possibleTargets.size()-1));
				float x = target.getX()+Rand.randInt(-50, +49)/100f;
				float y = target.getY()+Rand.randInt(-50, +49)/100f;
				this.x = (int)Math.min(x, 0.7 * Wander.WANDER_DISTANCE);
				this.y = (int)Math.min(y, 0.7 * Wander.WANDER_DISTANCE);
				
				action = new MoveAction(
						commander.getCharacter(),
						x, y
					);
			}
			else
				System.out.println("No cells to wander to.. at ("+(commander.getCharacter().getCell().getX()+x)+", "+(commander.getCharacter().getCell().getY()+y)+")");
			
			CommandSet commands = new CommandSet(
					action,
					new HashMap<AIController, Strategy>()
				);

			Collection<AIController> followers = commander.getFollowers();
			
			synchronized(followers)
			{
				for(AIController f : followers)
				{
					if(commander.getCharacter().distanceTo(f.getCharacter()) > 5)
						commands.setStrategy(f, new Follow(commander.getCharacter()));
					else
						commands.setStrategy(f, new GoTo(x, y));
					
				}
			}
			
			return commands;
		}
		else
			return new CommandSet(null, null);
	}
}
