package model.map.decor;

import view.renderer3D.core.Dummy3DObj;
import model.Interactable;
import model.character.GameCharacter;

public abstract class Decor extends Dummy3DObj implements Interactable
{
	// returns false only if permanently impassible
	public abstract boolean isPassible();
	
	// takes into account specific skills
	public abstract boolean isPassible(GameCharacter c);
}
