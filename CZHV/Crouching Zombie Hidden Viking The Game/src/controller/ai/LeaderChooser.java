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
	
	private static final int TIME_BETWEEN_LOYALTY_CHECKS = 1000;
	
	public LeaderChooser(AIController controller, Map<GameCharacter, AIController> controlBinding)
	{
		this.controlBinding = controlBinding;
		this.controller     = controller;
	}
	
	public boolean loyal(GameCharacter leader, float satisfaction, long dtime)
	{
		this.timeSinceLastLoyaltyCheck -= dtime;
		
		if(Rand.randInt(0, TIME_BETWEEN_LOYALTY_CHECKS) < dtime) //this.timeSinceLastLoyaltyCheck < 0)
		{
			this.timeSinceLastLoyaltyCheck = TIME_BETWEEN_LOYALTY_CHECKS;
			
			float d = Math.min(this.satisfaction, 1-this.satisfaction);
			if(d < 0.01)
				d = 0.01f;
			
			this.satisfaction += satisfaction/d;
			
			if(!this.loyaltyCheck(leader))
			{
				this.satisfaction = 0.5f;
				return false;
			}
		}
		
		return true;
	}
	
	public GameCharacter chooseLeader(Collection<GameCharacter> options)
	{
		int followerCount = this.controller.getFollowerCount(null);
		GameCharacter leader = null;
		float bestLeaderRate = 0;
		
		if(followerCount > this.idealGroupSize)
		{
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
		
		return leader;
	}
	
	private boolean loyaltyCheck(GameCharacter leader)
	{
		int followerCount = this.controller.getFollowerCount(null);
		int groupSize     = this.controller.getGroupSize(null);
		float DTL;
		if(leader == null)
			DTL = 0;
		else
			DTL = this.controller.getCharacter().distanceTo(leader);
		
		return DTL < 5 && followerCount/this.idealGroupSize >= this.satisfaction * groupSize/this.idealGroupSize;
	}
	
	private float rateLeader(int followerCount, GameCharacter character)
	{
		AIController controller = this.controlBinding.get(character);
		if(controller == null)
			return -2;
		
		if(this.controller.isFollower(controller))
			return -1;
		
		return controller.getFollowerCount(null) * controller.getSatisfactionLevel() + followerCount;
	}
	
	public float getSatisfaction()
	{
		return this.satisfaction;
	}
}
