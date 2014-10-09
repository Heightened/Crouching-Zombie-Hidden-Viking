package controller.ai;

import java.util.Collection;
import java.util.Map;

import controller.AIController;
import model.Game;
import model.character.GameCharacter;

public class LeaderChooser
{
	private float satisfaction = 0.5f;
	private float groupSize = 100f;
	private Map<GameCharacter, AIController> controlBinding;
	private AIController controller;
	private float timeSinceLastLoyaltyCheck = 0;
	
	private static final float TIME_BETWEEN_LOYALTY_CHECKS = 3000;
	
	public LeaderChooser(AIController controller, Map<GameCharacter, AIController> controlBinding)
	{
		this.controlBinding = controlBinding;
		this.controller     = controller;
	}
	
	public boolean loyal(int followerCount, float satisfaction, float dtime)
	{
		if(this.timeSinceLastLoyaltyCheck > TIME_BETWEEN_LOYALTY_CHECKS)
		{
			this.satisfaction += satisfaction;
			
			if(this.satisfaction < followerCount/groupSize)
			{
				this.satisfaction = 0.5f;
				return false;
			}
		}
		
		return true;
	}
	
	public GameCharacter chooseLeader(int followerCount, Collection<GameCharacter> options)
	{
		GameCharacter leader = null;
		float bestLeaderRate = 0;
		
		if(followerCount > groupSize)
			return null;
		
		for(GameCharacter c : options)
		{
			float tempRate = this.rateLeader(followerCount, c);
			if(bestLeaderRate < tempRate)
			{
				leader = c;
				bestLeaderRate = tempRate;
			}
		}
		
		return leader;
	}
	
	private float rateLeader(int followerCount, GameCharacter character)
	{
		AIController controller = this.controlBinding.get(character);
		
		if(this.controller.isFollower(controller))
			return -1;
		
		return controller.getFollowerCount() * controller.getSatisfactionLevel() + followerCount;
	}
	
	public float getSatisfaction()
	{
		return this.satisfaction;
	}
}
