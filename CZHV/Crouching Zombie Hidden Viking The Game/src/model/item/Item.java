package model.item;

import view.renderer3D.core.Dummy3DObj;
import model.Interactable;
import model.character.GameCharacter;

public class Item extends Dummy3DObj implements Interactable {
	
	public Item(int i, int j){
		super(i, j);
	}

	public boolean interact(GameCharacter interacter) {
		return false;
	}
}
