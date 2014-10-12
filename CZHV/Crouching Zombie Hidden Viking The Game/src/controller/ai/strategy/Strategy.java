package controller.ai.strategy;

import controller.AIController;

public abstract class Strategy
{
	public abstract CommandSet getCommandSet(AIController commander, long dtime);
}
