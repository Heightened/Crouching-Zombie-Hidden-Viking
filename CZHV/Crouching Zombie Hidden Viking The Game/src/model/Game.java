package model;

import java.util.Collection;
import model.map.Cell;
import model.map.Map;
import model.character.Character;
import model.character.Inventory;

public class Game {
	Map map;
	
	public Game() {
		//TODO: fill in game mechanic-y things
	}
	
	public void moveCharacterOnGrid(Cell fromCell, Cell toCell){
		//TODO: pathfinding
	}
	
	public void pickupItem(Cell c){
		Interactable item = c.getItemHolder().getItem();
		Character chara = c.getCharacterHolder().getItem();
		item.interact(chara);	
	}
	
	public boolean dropItem(Cell c, Item i){
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
