package controller.ai.strategy;

import java.util.ArrayList;
import java.util.Collection;
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
			
			return new CommandSet(
					new MoveAction(
							commander.getCharacter(),
							target.getX()+Rand.randInt(-50, +49)/100f,
							target.getY()+Rand.randInt(-50, +49)/100f
						),
					null
				);
		}
		else
			return new CommandSet(null, null);
	}
}
