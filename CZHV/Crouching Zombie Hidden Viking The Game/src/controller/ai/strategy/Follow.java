package controller.ai.strategy;

import controller.AIController;
import controller.actions.MoveAction;
import model.character.GameCharacter;

public class Follow extends Strategy
{
	private GameCharacter followee;
	
	public Follow(GameCharacter followee)
	{
		assert followee != null;
		
		this.followee = followee;
	}
	
	public GameCharacter getFollowee()
	{
		return this.followee;
	}
	
	long timeSinceLastFollow = 0;
	@Override
	public CommandSet getCommandSet(AIController follower, long dtime)
	{
		timeSinceLastFollow += dtime;
		
		if(timeSinceLastFollow > 1000 && this.followee != null)
		{
			timeSinceLastFollow = 0;
			
			return new CommandSet(
					new MoveAction(follower.getCharacter(), this.followee.getAbsX(), this.followee.getAbsY()),
					null
				);
		}
		else
			return new CommandSet(null, null);
	}
}
