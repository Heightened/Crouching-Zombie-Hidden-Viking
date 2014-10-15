package controller.actions;

import model.Game;
import model.character.GameCharacter;

public class StopMovingAction implements Action{
	private GameCharacter c;
	
	public StopMovingAction(GameCharacter c){
		this.c = c;
	}
	
	
	@Override
	public boolean perform(Game g) {
		c.stopMovement();
		return false;
	}
	
}
