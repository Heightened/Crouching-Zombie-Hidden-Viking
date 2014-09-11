package model.character;

public class Character {
	
	private Inventory bag;
	
	private int maxHp = 0;
	private int currentHp = 0;
	private int strength = 0;
	private int speed = 0;
	
	public Character(){	}
	
	public Character(int maxHp, int strength, int speed, int inventory_size){
		setMaxHp(maxHp);
		setStrength(strength);
		setSpeed(speed);
		setBag(new Inventory(inventory_size));
		currentHp = maxHp;
	}
	
	public Inventory getBag() {
		return bag;
	}

	public void setBag(Inventory bag) {
		this.bag = bag;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int hp) {
		this.maxHp = hp;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getCurrentHp() {
		if(currentHp<0){
			return 0;
		}
		return currentHp;
	}

	public void applyDamage(int Damage) {
		currentHp = currentHp-Damage;
	}
	
	public void heal(int hp){
		currentHp = currentHp+hp;
	}
	
	public boolean isDead(){
		return getCurrentHp()<=0;
	}
}
