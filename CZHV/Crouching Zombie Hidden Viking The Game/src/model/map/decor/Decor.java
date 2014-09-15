package model.map.decor;

import model.Interactable;
import model.character.Character;

public abstract class Decor implements Interactable
{
	// returns false only if permanently impassible
	public abstract boolean isPassible();
	
	// takes into account specific skills
	public abstract boolean isPassible(Character c);
}
