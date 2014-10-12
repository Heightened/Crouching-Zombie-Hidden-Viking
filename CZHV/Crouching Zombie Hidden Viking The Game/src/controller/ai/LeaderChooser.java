package controller.ai;

import java.util.Collection;
import java.util.Map;

import util.Rand;
import controller.AIController;
import model.Game;
import model.character.GameCharacter;

public class LeaderChooser
{
	private float satisfaction = 0.5f;
	private float idealGroupSize = 2f;
	private Map<GameCharacter, AIController> controlBinding;
	private AIController controller;
	private float timeSinceLastLoyaltyCheck = 0;
	
	private static final int TIME_BETWEEN_LOYALTY_CHECKS = 10000;
	
	public LeaderChooser(AIController controller, Map<GameCharacter, AIController> controlBinding)
	{
		this.controlBinding = controlBinding;
		this.controller     = controller;
	}
	
	public boolean loyal(float satisfaction, long dtime)
	{
		this.timeSinceLastLoyaltyCheck -= dtime;
		
		if(this.timeSinceLastLoyaltyCheck < 0)
		{
			this.timeSinceLastLoyaltyCheck = TIME_BETWEEN_LOYALTY_CHECKS;
			
			float d = Math.min(this.satisfaction, 1-this.satisfaction);
			if(d < 0.01)
				d = 0.01f;
			
			//this.satisfaction += satisfaction/d;
			
			if(this.loyaltyCheck())
			{
				this.satisfaction = 0.5f;
				System.out.println("Ima go follow someone else");
				return false;
			}
		}
		
		return true;
	}
	
	public GameCharacter chooseLeader(Collection<GameCharacter> options)
	{
		int followerCount = this.controller.getFollowerCount();
		GameCharacter leader = null;
		float bestLeaderRate = 0;
		
		if(followerCount > this.idealGroupSize)
		{
			System.out.println("Ima be a leaderrr!");
			return null;
		}
		
		for(GameCharacter c : options)
		{
			float tempRate = this.rateLeader(followerCount, c);
			if(bestLeaderRate <= tempRate)
			{
				leader = c;
				bestLeaderRate = tempRate;
			}
		}
		
		System.out.println("Ima follow "+leader);
		
		return leader;
	}
	
	private boolean loyaltyCheck()
	{
		int followerCount = this.controller.getFollowerCount();
		int groupSize     = this.controller.getGroupSize();
		
		return followerCount - idealGroupSize <= this.satisfaction * groupSize;
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
