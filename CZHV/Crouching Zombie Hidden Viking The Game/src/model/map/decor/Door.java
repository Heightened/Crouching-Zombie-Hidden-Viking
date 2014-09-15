package model.map.decor;

import model.character.Character;

public class Door extends Decor
{
	@Override
	public void interact(Character interacter)
	{
		
	}
	
	public boolean isPassible()
	{
		return true;
	}
	
	public boolean isPassible(Character c)
	{
		return c.hasSkill(Character.Skill.OPEN_DOOR);
	}
}
