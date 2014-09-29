package controller.actions;

import java.util.Collection;

import model.Item;
import model.character.Character;
import model.character.Inventory;
import model.map.Cell;

public class DropItem implements Action {

	@Override
	public boolean perform() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private boolean dropItem(Cell c, Item i){
		Character chara = c.getCharacterHolder().getItem();
		Inventory inven = chara.getBag();
		if(c.getItemHolder().isEmpty()){
			c.getItemHolder().setItem(i);
			inven.removeItem(i);
			return true;
		}else{
			Collection<Cell> cells = c.getNeighbours();
			for(Cell cell: cells){
				if(cell.getItemHolder().isEmpty()){
					cell.getItemHolder().setItem(i);
					inven.removeItem(i);
					return true;
				}
			}
		}
		return false;
	}
	
}
