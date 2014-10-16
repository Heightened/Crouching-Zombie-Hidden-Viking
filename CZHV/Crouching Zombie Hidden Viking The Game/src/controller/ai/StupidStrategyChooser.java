package controller.ai;

import controller.AIController;
import controller.ai.strategy.Follow;
import controller.ai.strategy.LeaderControlledAggro;
import controller.ai.strategy.Strategy;
import controller.ai.strategy.Wander;

public class StupidStrategyChooser implements StrategyChooser
{
	public Strategy currentStrategy;
	public long timeSinceLastConsideration = 0;
	public static final long RECONSIDER_DELAY = 5000;
	
	@Override
	public Strategy choose(AIController leader, long dtime)
	{
		return this.choose(leader, dtime, false);
	}
	
	@Override
	public Strategy choose(AIController leader, long dtime, boolean justFollowed)
	{
		if(leader != null && justFollowed)
			this.currentStrategy = new Follow(leader.getCharacter());
		
		if(this.currentStrategy == null)
			if(leader ==  null)
				this.currentStrategy = new LeaderControlledAggro();
			else
				this.currentStrategy = new Follow(leader.getCharacter());
		else
			this.timeSinceLastConsideration += dtime;
		
		if(this.timeSinceLastConsideration > RECONSIDER_DELAY)
		{
			this.reconsider();
			this.timeSinceLastConsideration = 0;
		}
		
		return this.currentStrategy;
	}
	
	protected void reconsider()
	{
		//
	}
}
