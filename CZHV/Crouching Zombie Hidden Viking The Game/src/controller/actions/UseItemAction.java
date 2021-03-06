package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.item.Item;

public class UseItemAction implements Action{
	private GameCharacter c;
	private Item i;
	
	public UseItemAction(GameCharacter c, Item i){
		this.c = c;
		this.i = i;
	}

	@Override
	public boolean perform(Game g) {
		return i.interact(c);
	}

}
