package controller.actions;

import java.util.Collection;
import java.util.List;

import model.Game;
import model.character.GameCharacter;
import pathfinding.Node;
import pathfinding.PathFinder;

public class GroupMove implements Action{
	private Collection<GameCharacter> characters;
	private float x,y;
	
	public GroupMove(Collection<GameCharacter> characters, float x, float y){
		this.characters = characters;
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean perform(Game g) {
		float avgX = 0, avgY = 0;
		for(GameCharacter gc : characters) {
			avgX += gc.getX();
			avgY += gc.getY();
		}
		avgX = avgX / characters.size();
		avgY = avgY / characters.size();
		
		
		
		return false;
	}
}
