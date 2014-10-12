package controller.ai.strategy;

import java.util.HashMap;
import java.util.Map;

import controller.AIController;
import controller.actions.Action;

public class CommandSet
{
	private Action myAction;
	private Map<AIController, Strategy> commands = new HashMap<>();
	
	public CommandSet(Action action, Map<AIController, Strategy> commands)
	{
		this.myAction = action;
		this.commands = commands;
	}
	
	public Action getAction()
	{
		return this.myAction;
	}
	
	public Strategy getStrategy(AIController follower)
	{
		if(this.commands == null)
			return null;
		
		if(this.commands.containsKey(follower))
			return this.commands.get(follower);
		else
			return null;
	}
	
	protected void setAction(Action action)
	{
		this.myAction = action;
	}
	
	protected void setStrategy(AIController follower, Strategy strategy)
	{
		this.commands.put(follower, strategy);
	}
}
