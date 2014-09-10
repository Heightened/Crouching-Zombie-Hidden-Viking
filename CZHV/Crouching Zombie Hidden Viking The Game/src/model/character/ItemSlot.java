package model.character;

import model.ItemHolder;

public class ItemSlot implements ItemHolder{
	private Item item;

	public ItemSlot(){}
	
	public ItemSlot(Item i){
		setItem(i);
	}
	
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
