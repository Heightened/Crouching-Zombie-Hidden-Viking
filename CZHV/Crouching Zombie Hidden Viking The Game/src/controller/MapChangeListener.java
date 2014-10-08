package controller;

import model.character.GameCharacter;

public interface MapChangeListener {
	public void setActive(GameCharacter character);
	public void setInactive(GameCharacter character);
}
