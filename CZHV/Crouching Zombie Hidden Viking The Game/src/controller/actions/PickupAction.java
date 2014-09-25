package controller.actions;

import model.Game;
import model.Interactable;
import model.character.Character;
import model.map.Cell;

public class PickupAction implements Action{
	Game g;
	
	public PickupAction(Game g){
		this.g = g;
	}
	
	@Override
	public boolean perform() {
		//TODO g.pickupitem
		return false;
	}

	private void pickupItem(Cell c){
		Interactable item = c.getItemHolder().getItem();
		Character chara = c.getCharacterHolder().getItem();
		item.interact(chara);	
	}
}
