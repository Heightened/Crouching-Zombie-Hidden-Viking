package model.character;

public class Character {
	
	private final static int NUM_SLOTS = 5;
	private Inventory bag;
	
	public Character(){
		setBag(new Inventory(NUM_SLOTS));
	}
	
	public Inventory getBag() {
		return bag;
	}

	public void setBag(Inventory bag) {
		this.bag = bag;
	}
}
