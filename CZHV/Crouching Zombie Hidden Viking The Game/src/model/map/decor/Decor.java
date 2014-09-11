package model.map.decor;

import model.Interactable;

public abstract class Decor implements Interactable
{
	// returns false only if permanently impassible
	public abstract boolean isPassible();
}
