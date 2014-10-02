package model.item;

public class Weapon extends Item {
	private boolean melee;
	private String name;
	private int power;
	
	public Weapon(String name, int power ,boolean isMelee){
		this.name = name;
		this.melee = isMelee;
		this.power = power;
	}
	
	/* once a weapon is created one shouldn't be able to 
	 * change it unless we add some enhancement mechanic 
	 * or something
	 */

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
