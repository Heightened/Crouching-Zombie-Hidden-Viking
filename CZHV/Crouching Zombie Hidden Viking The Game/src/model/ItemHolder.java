package model;

import model.character.Item;

public interface ItemHolder {
	public Item getItem();
	public void setItem(Item item);
	public void removeItem();
	public boolean isEmpty();
}
