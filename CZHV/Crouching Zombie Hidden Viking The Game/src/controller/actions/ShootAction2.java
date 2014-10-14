package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.character.ItemSlot;
import model.item.Weapon;

public class ShootAction2 implements Action {
	GameCharacter c1, c2;
	float accuracy = 1;	//accuracy

	public ShootAction2(GameCharacter c1, GameCharacter c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	

	public ShootAction2(GameCharacter c1, GameCharacter c2, float accuracy){
		this(c1,c2);
		this.accuracy = accuracy;
	}

	@Override
	public boolean perform(Game g) {
		int appliedDamage = 0;
		ItemSlot[] inventory = c1.getBag().getInventory();
		for (int i = 0; i < inventory.length; i++) {
			if(inventory[i].getItem() instanceof Weapon){
				Weapon w = (Weapon) inventory[i].getItem();
				appliedDamage = w.getPower();
				if(w.isMeleeWeapon()){
					appliedDamage += c1.getStrength();
				}
				if(hitSuccess()){
					c2.applyDamage(appliedDamage);
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean hitSuccess(){
		return Math.random() < accuracy;
	}

}
