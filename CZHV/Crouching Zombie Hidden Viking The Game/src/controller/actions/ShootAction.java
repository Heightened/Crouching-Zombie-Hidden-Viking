package controller.actions;

import model.Game;
import model.character.GameCharacter;
import model.item.Weapon;

public class ShootAction implements Action {
	private Weapon w;
	private GameCharacter target;
	private GameCharacter source;
	
	public ShootAction(GameCharacter source, GameCharacter target){
		this.target = target;
		this.source = source;
	}
	
	public ShootAction(Weapon w, GameCharacter source, GameCharacter target){
		this.w = w;
		this.target = target;
		this.source = source;
	}
	
	
	@Override
	public boolean perform(Game g) {
		int appliedDamage = source.getStrength();
		if(w!=null){
			appliedDamage += w.getPower();
			if(!w.isMeleeWeapon()){
				appliedDamage -= source.getStrength();
			}
			if(hitSuccess(Math.min(w.getAccuracy(), source.getAccuracy()))){
				System.out.println("HIT DAMAGE  WHOA: "+appliedDamage);
				target.applyDamage(appliedDamage);
				return true;
			}
		} else {
			if(hitSuccess(source.getAccuracy())){
				target.applyDamage(appliedDamage);
				return true;
			}
		}
		
		return false;
	}

	private boolean hitSuccess(float accuracy){
		return Math.random() < accuracy;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "Shoot Action";
	}

}
