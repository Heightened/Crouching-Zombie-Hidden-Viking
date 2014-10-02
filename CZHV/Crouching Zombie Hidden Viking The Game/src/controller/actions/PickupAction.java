package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;


public class PickupAction implements Action{
	private Cell c;
	private GameCharacter chara;
	
	public PickupAction(GameCharacter chara, Cell c){
		this.chara = chara;
		this.c = c;
	}
	
	@Override
	public boolean perform(Game g){
		chara.getBag().addItem(c.getItemHolder().getItem());
		c.getItemHolder().removeItem();
		return true;
	}
}
