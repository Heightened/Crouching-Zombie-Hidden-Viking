package controller.actions;

import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;
import pathfinding.Astar;
import pathfinding.Node;
import pathfinding.PathFindingMap;
import pathfinding.PathFindingMap.CellType;

public class GroupMoveAction implements Action{
	private List<GameCharacter> characters;
	private int avgX = 0, avgY = 0;
	List<Node> path;
	
	public GroupMoveAction(List<GameCharacter> characters, float x, float y) throws Exception{
		if(characters.isEmpty()){
			throw new Exception("no characters selected for group move");
		}
		this.characters = characters;
		int ix = (int)(x+0.5);
		int iy = (int)(y+0.5);
		
		//get the individual characters vision range and calculate group centre
		ArrayList<PathFindingMap> maps = getPathFindingMaps();		
		//calculate the vision range of the group
		PathFindingMap merged = PathFindingMap.mergeMap(maps);
		while(!isInMap(avgX, avgY, merged)){
			removeFurthestCharacter(avgX, avgY);
			maps = getPathFindingMaps();
			//re-calculate the vision range of the group
			merged = PathFindingMap.mergeMap(maps);
		}
		
		Astar pathfinder = (Astar) characters.get(0).getPathFinder();
		path = pathfinder.calculatePath(avgX, avgY, ix, iy);
	}

	@Override
	public boolean perform(Game g) {		
		for(GameCharacter gc: characters){
			//TODO use smarter follow path
			gc.followPath(path);
		}
		return true;
	}
	
	private void removeFurthestCharacter(int X, int Y){
		int index = 0;
		int dif = 0;
		for(int i = 0; i<characters.size(); i++){
			Cell c = characters.get(i).getCell();
			int tempDif = Math.abs(X-c.getX() + Y-c.getY());
			if(tempDif>dif){
				dif = tempDif;
				index = i;
			}
		}
		characters.remove(index);
	}

	private ArrayList<PathFindingMap> getPathFindingMaps() {
		avgX = 0;
		avgY = 0;
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
		return maps;
	}
	
	private boolean isInMap(int X, int Y, PathFindingMap map){
		return map.getGrid().containsKey(X) && map.getGrid().get(X).containsKey(Y);
	}
}
