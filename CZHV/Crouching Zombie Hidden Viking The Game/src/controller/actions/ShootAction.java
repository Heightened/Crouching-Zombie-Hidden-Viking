package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.item.Weapon;

public class ShootAction implements Action {
	private Weapon w;
	private GameCharacter target;
	private GameCharacter source;
	
	public ShootAction(GameCharacter source, GameCharacter target){
		this(source.getBestWeapon(source.distanceTo(target)), source, target);
	}
	
	public ShootAction(Weapon w, GameCharacter source, GameCharacter target){
		this.w = w;
		this.target = target;
		this.source = source;
	}
	
	public boolean inRange(float radius){
		return source.distanceTo(target)<radius;
	}
	
	@Override
	public boolean perform(Game g) {
		int appliedDamage = source.getStrength();
			if(w!=null){
				if(!inRange(w.getRange())){
					return false;
				}
				appliedDamage += w.getPower();
				if(!w.isMeleeWeapon()){
					appliedDamage -= source.getStrength();
				}
				if(source.hit() && hitSuccess(Math.min(w.getAccuracy(), source.getAccuracy()))){
					target.applyDamage(appliedDamage);
					return true;
				}
			} else {
				if(!inRange(GameCharacter.DEFAULT_MELEE_RANGE)){
					return false;
				}
				if(source.hit() && hitSuccess(source.getAccuracy())){
					target.applyDamage(appliedDamage);
					return true;
				}
			}
		
		return false;
	}

	private boolean hitSuccess(float accuracy){
		return Math.random() < accuracy;
	}

}
