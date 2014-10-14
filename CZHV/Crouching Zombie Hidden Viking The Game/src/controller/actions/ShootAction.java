package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.item.Weapon;

public class ShootAction implements Action {
	private Weapon w;
	private GameCharacter target;
	private GameCharacter source;
	private int accuracy;
	
	public ShootAction(Weapon w, GameCharacter source, GameCharacter target){
		this.w = w;
		this.target = target;
		this.source = source;
	}
	
	public ShootAction(Weapon w, GameCharacter source, GameCharacter target, int accuracy){
		this.w = w;
		this.target = target;
		this.accuracy = accuracy;
		this.source = source;
	}
	
	
	@Override
	public boolean perform(Game g) {
		int appliedDamage = w.getPower();
		if(w.isMeleeWeapon()){
			appliedDamage += source.getStrength();
		}
		if(hitSuccess()){
			target.applyDamage(appliedDamage);
			return true;
		}
		return false;
	}

	private boolean hitSuccess(){
		return Math.random() < accuracy;
	}

}
