package model.map;

import model.character.GameCharacter;

public interface ChangeListener<T>
{
	public void setActive(T changed);
	
	public void setInactive(T changed);
	
	public void characterMoved(GameCharacter character);
}
