package controller.actions;

import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;
import pathfinding.Astar;
import pathfinding.Node;
import pathfinding.PathFindingMap;

public class GroupMoveAction implements Action{
	private List<GameCharacter> characters;
	private int x,y;
	private int avgX = 0, avgY = 0;
	
	public GroupMoveAction(List<GameCharacter> characters, float x, float y){
		this.characters = characters;
		this.x = (int)(x+0.5);
		this.y = (int)(y+0.5);
	}

	@Override
	public boolean perform(Game g) {
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
		
		Node startNode = merged.getNode(avgX, avgY);
		Node goalNode = merged.getNode(x, y);
		
		Astar pathfinder = (Astar) characters.get(0).getPathFinder();
		List<Node> path = pathfinder.calculatePath(startNode, goalNode);
		
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
