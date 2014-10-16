package controller.actions;

import controller.itemCallback;
import model.Game;
import model.character.GameCharacter;
import model.item.Item;


public class PickupAction implements Action {
	private GameCharacter chara;
	private itemCallback call;
	
	public PickupAction(GameCharacter chara){
		this.chara = chara;
	}
	
	public PickupAction(GameCharacter chara, itemCallback call) {
		this.chara = chara;
		this.call = call;
	}

	@Override
	public boolean perform(Game g){
		Item item = chara.getCell().getItemHolder().getItem();
		if(item != null){
			if(chara.getBag().addItem(item)){
				System.out.println("ITEM PICKED UP");
				chara.getCell().getItemHolder().removeItem();
				if(call!=null){
					call.itemPickedUp();
				}
				return true;
			}
		}
		return false;
	}
}
