package controller.actions;

import model.Game;
import model.character.GameCharacter;


public class MoveAction implements Action{
	private GameCharacter c;
	private float x,y;
	
	public MoveAction(GameCharacter c, float x, float y){
		this.c = c;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean perform(Game g) {
		c.moveTo(x, y);
		return true;
	}
}
