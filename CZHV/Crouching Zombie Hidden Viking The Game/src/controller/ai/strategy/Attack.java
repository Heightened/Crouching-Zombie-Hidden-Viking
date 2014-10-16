package controller.ai.strategy;

import java.util.HashMap;

import model.character.GameCharacter;
import controller.AIController;
import controller.actions.Action;
import controller.actions.MoveAction;
import controller.actions.ShootAction;

public class Attack extends Strategy
{
	protected GameCharacter target;
	
	public Attack(GameCharacter target)
	{
		super();
		
		this.target = target;
	}

	@Override
	public CommandSet getCommandSet(AIController commander, long dtime)
	{
		Action action;
		if(commander.getCharacter().getBestRange() > commander.getCharacter().distanceTo(this.target))
			action = new ShootAction(commander.getCharacter(), target);
		else
			action = new MoveAction(commander.getCharacter(), target.getAbsX(), target.getAbsY());
		
		CommandSet commands = new CommandSet(
				action,
				new HashMap<AIController, Strategy>()
			);
		
		for(AIController aic : commander.getFollowers())
			commands.setStrategy(aic, new Attack(this.target));
		
		return commands;
	}

}
