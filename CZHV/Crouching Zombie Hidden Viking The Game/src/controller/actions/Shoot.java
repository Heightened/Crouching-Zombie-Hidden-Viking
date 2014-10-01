package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.character.ItemSlot;
import model.item.Weapon;

public class Shoot implements Action {
	GameCharacter c1, c2;

	public Shoot(GameCharacter c1, GameCharacter c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public boolean perform(Game g) {
		int appliedDamage = 0;
		ItemSlot[] inventory = c1.getBag().getInventory();
		for (int i = 0; i < inventory.length; i++) {
			//TODO: multiple weapons?
			if(inventory[i].getItem() instanceof Weapon){
				Weapon w = (Weapon) inventory[i].getItem();
				appliedDamage = w.getPower();
				if(w.isMeleeWeapon()){
					appliedDamage += c1.getStrength();
				}
				c2.applyDamage(appliedDamage);
				
				if(c2.isDead()){
					//TODO: remove character from the map
				}
				return true;
			}
		}
		return false;
	}

}
