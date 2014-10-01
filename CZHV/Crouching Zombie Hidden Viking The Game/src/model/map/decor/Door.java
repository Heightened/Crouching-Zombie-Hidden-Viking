package model.map.decor;

import model.character.GameCharacter;

public class Door extends Decor
{
	@Override
	public void interact(GameCharacter interacter)
	{
		
	}
	
	public boolean isPassible()
	{
		return true;
	}
	
	public boolean isPassible(GameCharacter c)
	{
		return c.hasSkill(GameCharacter.Skill.OPEN_DOOR);
	}
}
