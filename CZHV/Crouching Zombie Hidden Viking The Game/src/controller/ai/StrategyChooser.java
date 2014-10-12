package controller.ai;

import controller.AIController;
import controller.ai.strategy.Strategy;

public interface StrategyChooser
{
	public Strategy choose(AIController leader, long dtime);
}
