package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.item.Item;


public class PickupAction implements Action {
	private GameCharacter chara;
	
	public PickupAction(GameCharacter chara){
		this.chara = chara;
	}

	@Override
	public boolean perform(Game g){
		Item item = chara.getCell().getItemHolder().getItem();
		if(item != null){
			if(chara.getBag().addItem(item)){
				chara.getCell().getItemHolder().removeItem();
				return true;
			}
		}
		return false;
	}
}
