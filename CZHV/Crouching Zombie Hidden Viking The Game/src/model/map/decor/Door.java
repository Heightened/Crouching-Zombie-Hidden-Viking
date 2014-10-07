package model.map.decor;

import model.character.GameCharacter;

public class Door extends Decor
{
	public Door(int i, int j) {
		super(i, j);
	}

	@Override
	public boolean interact(GameCharacter interacter)
	{
		return false;
		
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
