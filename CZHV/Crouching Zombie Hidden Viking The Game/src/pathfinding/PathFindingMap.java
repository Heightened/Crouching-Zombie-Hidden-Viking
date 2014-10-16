package pathfinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.character.GameCharacter;
import model.map.Cell;

public class PathFindingMap
{
	protected Map<Integer, Map<Integer, CellCapsule>> grid;
	protected GameCharacter character;
	protected int width;
	protected int height;
	
	public PathFindingMap(Collection<Cell> cells, GameCharacter character, int width, int height)
	{
		this.grid = new HashMap<Integer, Map<Integer, CellCapsule>>();
		this.character = character;
		this.width = width;
		this.height = height;
		
		for(Cell c : cells)
		{
			this.addCell(c);
		}
	}
	
	/**
	 * This will merge a list of pathfinding maps into the map at index 0 
	 * @param maps
	 * @return merged map
	 */
	public static PathFindingMap mergeMap(ArrayList<PathFindingMap> maps){
		for(PathFindingMap map: maps){
			for(Integer key1: map.grid.keySet()){
				if(!maps.get(0).grid.containsKey(key1)){
					maps.get(0).grid.put(key1, new HashMap<Integer, CellCapsule>());
				}
				for(Integer key2: map.grid.get(key1).keySet()){
					if(map.grid.get(key1).get(key2).celltype != PathFindingMap.CellType.UNKNOWN){
						maps.get(0).grid.get(key1).put(key2, map.grid.get(key1).get(key2));
					}
				}
			}
		}
		return maps.get(0);
	}
	
	public void addKnowledge(PathFindingMap map){
		for(Integer key1: map.grid.keySet()){
			if(!this.grid.containsKey(key1)){
				this.grid.put(key1, new HashMap<Integer, CellCapsule>());
			}
			for(Integer key2: map.grid.get(key1).keySet()){
				if(map.grid.get(key1).get(key2).celltype != PathFindingMap.CellType.UNKNOWN){
					this.grid.get(key1).put(key2, map.grid.get(key1).get(key2));
				}
			}
		}
	}
	
	public Map<Integer, Map<Integer, CellCapsule>> getGrid() {
		return grid;
	}
	
	public void addCell(Cell c)
	{
		if(!this.grid.containsKey(c.getX()))
			this.grid.put(c.getX(), new HashMap<Integer, CellCapsule>());
		
		CellType value;
		if(c.isPassible())
			value = CellType.PASSIBLE;
		else
			value = CellType.IMPASSIBLE;

		this.grid.get(c.getX()).put(c.getY(), new CellCapsule(value));
	}
	
	public void addAll(Collection<Cell> cells)
	{
		for(Cell c : cells)
		{
			this.addCell(c);
		}
	}
	
	public boolean isValidPosition(int x, int y)
	{
		return (x >= 0 && y >=0 && x < width && y < height);
	}
	
	public Node getNode(int x, int y)
	{
		if(!this.grid.containsKey(x))
			this.grid.put(x, new HashMap<Integer, CellCapsule>());

		if(!this.grid.get(x).containsKey(y))
			this.grid.get(x).put(y, new CellCapsule(CellType.UNKNOWN));
		
		if(this.grid.get(x).get(y).getNode() == null)
			this.grid.get(x).get(y).newNode(x,y,this);

		return this.grid.get(x).get(y).node;
	}
	
	public class CellCapsule
	{
		private Node node;
		public CellType celltype;
		
		public CellCapsule(CellType ct) {
			this.celltype = ct;
		}
		
		public void newNode(int x, int y, PathFindingMap map)
		{
			this.node = new Node(x,y,this.celltype,map);
		}
		
		public Node getNode()
		{
			return this.node;
		}
	}
	
	public enum CellType
	{
		PASSIBLE,
		IMPASSIBLE,
		UNKNOWN;
	}
}
