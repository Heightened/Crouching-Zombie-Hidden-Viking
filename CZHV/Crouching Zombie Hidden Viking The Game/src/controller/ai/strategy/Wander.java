package controller.ai.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import util.Rand;
import controller.AIController;
import controller.actions.MoveAction;
import model.map.Cell;

public class Wander extends Strategy
{
	public static final int WANDER_DISTANCE = 10;

	@Override
	public CommandSet getCommandSet(AIController commander, long dtime)
	{
		if(Rand.randInt(0, 3000) < dtime) // roughly every 3 seconds
		{
			Collection<Cell> cells = commander.getGame().getMap().getNearbyCells(
					commander.getCharacter().getCell().getX(),
					commander.getCharacter().getCell().getY(),
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
			
			Cell target = possibleTargets.get(Rand.randInt(0,possibleTargets.size()-1));
			float x = target.getX()+Rand.randInt(-50, +49)/100f;
			float y = target.getY()+Rand.randInt(-50, +49)/100f;
			
			CommandSet commands = new CommandSet(
					new MoveAction(
							commander.getCharacter(),
							x, y
						),
					new HashMap<AIController, Strategy>()
				);

			Collection<AIController> followers = commander.getFollowers();
			
			synchronized(followers)
			{
				for(AIController f : followers)
				{
					if(commander.getCharacter().distanceTo(f.getCharacter()) > 4)
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
