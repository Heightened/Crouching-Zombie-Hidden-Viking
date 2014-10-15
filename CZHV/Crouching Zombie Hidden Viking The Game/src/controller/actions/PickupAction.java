package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.character.Inventory;


public class PickupAction implements Action{
	private GameCharacter chara;
	
	public PickupAction(GameCharacter chara){
		this.chara = chara;
	}
	
	@Override
	public boolean perform(Game g){
		if(chara.getBag().addItem(chara.getCell().getItemHolder().getItem())){
			chara.getCell().getItemHolder().removeItem();
			return true;
		}
		return false;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "";
	}
}
