package model.character;

import model.Container;
import model.item.Item;

public class ItemSlot extends Container<Item> {
	public ItemSlot(){
		super();
	}
	public ItemSlot(Item i){
		super(i);
	}
}
