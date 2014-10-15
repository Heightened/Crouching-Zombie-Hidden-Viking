package model.item;

public class Weapon extends Item {
	private boolean melee;
	private String name;
	private int power;
	private int range;
	private int accuracy;

	
	public Weapon(String name, int power ,boolean isMelee, int range, int accuracy){
		this.name = name;
		this.melee = isMelee;
		this.power = power;
		this.range = range;
		this.accuracy = accuracy;
	}
	
	/* once a weapon is created one shouldn't be able to 
	 * change it unless we add some enhancement mechanic 
	 * or something
	 */
	
	public final int getAccuracy(){
		return accuracy;//XXX: maybe scale this as a function of range
	}
	
	public final int getRange(){
		return range;
	}

	public final boolean isMeleeWeapon() {
		return melee;
	}

	public final int getPower() {
		return power;
	}

	public final String getName() {
		return name;
	}
}
