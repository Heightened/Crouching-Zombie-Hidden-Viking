package model.item;

import view.renderer3D.core.Dummy3DObj;
import model.Interactable;
import model.character.GameCharacter;

public class Item extends Dummy3DObj implements Interactable {
	
	public Item(){
		
	}

	public void interact(GameCharacter interacter) {
		// pick up from map?
	}
}
