package controller.ai.strategy;

import java.util.HashMap;

import controller.AIController;
import controller.actions.MoveAction;

public class GoTo extends Strategy
{
	private float x,y;
	
	public GoTo(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public CommandSet getCommandSet(AIController commander, long dtime)
	{
		CommandSet commands = new CommandSet(
				new MoveAction(commander.getCharacter(), x, y),
				new HashMap<AIController, Strategy>()
			);
		
		for(AIController f : commander.getFollowers())
			commands.setStrategy(f, new GoTo(this.x, this.y));
		
		return commands;
	}

}
