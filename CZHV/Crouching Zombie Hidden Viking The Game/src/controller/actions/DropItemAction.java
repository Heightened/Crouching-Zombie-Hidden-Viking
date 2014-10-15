package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.item.Item;
import model.map.Cell;


public class DropItemAction implements Action {
	private GameCharacter chara;
	private Item i;
	private Cell c;
	
	public DropItemAction(Item i, GameCharacter chara, Cell c){
		this.chara = chara;
		this.i = i;
	}

	@Override
	public boolean perform(Game g) {
		c.getItemHolder().setItem(i);
		chara.getBag().removeItem(i);
		return true;
	}

	@Override
	public String type() {
		return "";
	}
}
