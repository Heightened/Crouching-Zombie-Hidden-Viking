package model.character;

public class Inventory {
	private ItemSlot[] inventory;
	
	public Inventory(int numSlots){
		setInventory(new ItemSlot[numSlots]);
	}

	public ItemSlot[] getInventory() {
		return inventory;
	}

	public void setInventory(ItemSlot[] inventory) {
		this.inventory = inventory;
	}
	
	public boolean addItem(Item item){
		for(int i = 0; i<inventory.length; i++){
			if(inventory[i].isEmpty()){
				inventory[i].setItem(item);
				return true;
			}
		}
		return false;
	}
	
	public void removeItem(Item item){
		for(int i = 0; i<inventory.length; i++){
			if(inventory[i].getItem().equals(item)){
				inventory[i].removeItem();
			}
		}
	}
	
}
