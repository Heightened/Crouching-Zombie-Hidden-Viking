package controller.actions;

import java.util.List;

import pathfinding.Node;
import model.Game;
import model.character.GameCharacter;


public class MoveAction implements Action{
	private GameCharacter c;
	private List<Node> path;
	
	public MoveAction(GameCharacter c, float x, float y){
		this.c = c;
		
		this.path = c.getPathFinder().calculatePath(
				c.getCell().getX(),
				c.getCell().getY(),
				(int)Math.round(x),
				(int)Math.round(y)
			);
	}

	@Override
	public boolean perform(Game g) {
		c.followPath(this.path);
		return true;
	}
}
