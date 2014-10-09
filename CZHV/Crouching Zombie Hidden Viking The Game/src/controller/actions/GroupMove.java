package controller.actions;

import java.util.ArrayList;
import java.util.Collection;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;
import pathfinding.PathFindingMap;

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
		int avgX = 0, avgY = 0;
		ArrayList<PathFindingMap> maps = new ArrayList<PathFindingMap>();
		for(GameCharacter gc : characters) {
			Cell cell = gc.getCell();
			avgX += cell.getX();
			avgY += cell.getY();
			maps.add(gc.getPathFinder().makeMap(cell.getX(), cell.getY()));
		}
		//this might truncate but who cares
		avgX = avgX / characters.size();
		avgY = avgY / characters.size();
		
		PathFindingMap merged = PathFindingMap.mergeMap(maps);
		//TODO: implement iterative character removal if average not in pathfindingmap
		
		for(GameCharacter gc : characters){
			
		}
		
		
		return false;
	}
}
