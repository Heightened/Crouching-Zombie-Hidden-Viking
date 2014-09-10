package model.map;

import model.ItemHolder;
import model.character.Item;

public class Cell implements ItemHolder {
	
	private Item item;
	
	@Override
	public Item getItem() {
		return item;
	}

	@Override
	public void setItem(Item i) {
		this.item = i;
	}
	
	@Override
	public void removeItem(){
		setItem(null);
	}
	
	@Override
	public boolean isEmpty() {
		return item == null;
	}
}
