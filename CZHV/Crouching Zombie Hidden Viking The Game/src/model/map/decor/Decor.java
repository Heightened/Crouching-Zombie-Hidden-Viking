package model.map.decor;

import view.renderer3D.core.Dummy3DObj;
import model.Interactable;
import model.character.GameCharacter;

public abstract class Decor extends Dummy3DObj implements Interactable
{
	public Decor(int i, int j) {
		super(i, j);
	}

	// returns false only if permanently impassible
	public abstract boolean isPassible();
	
	// takes into account specific skills
	public abstract boolean isPassible(GameCharacter c);
}
