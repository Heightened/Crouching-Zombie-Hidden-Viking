package controller.ai.strategy;

import controller.AIController;
import controller.actions.MoveAction;
import model.character.GameCharacter;

public class Follow extends Strategy
{
	private GameCharacter followee;
	
	public Follow(GameCharacter followee)
	{
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
		
		if(timeSinceLastFollow > 1000)
		{
			timeSinceLastFollow = 0;
			
			return new CommandSet(
					new MoveAction(follower.getCharacter(), this.followee.getAbsX(), this.followee.getAbsY()),
					//null,
					null
				);
		}
		else
			return new CommandSet(null, null);
	}
}