package pathfinding;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import model.map.Cell;
import model.character.Character;

public class PathFindingMap
{
	protected Map<Integer, Map<Integer, CellCapsule>> grid;
	protected Character character;
	
	public PathFindingMap(Collection<Cell> cells, Character character)
	{
		this.grid = new HashMap<Integer, Map<Integer, CellCapsule>>();
		this.character = character;
		
		for(Cell c : cells)
		{
			this.addCell(c);
		}
	}
	
	public void addCell(Cell c)
	{
		if(!this.grid.containsValue(c.getX()))
			this.grid.put(c.getX(), new HashMap<Integer, CellCapsule>());
		
		CellType value;
		if(c.isFree(this.character))
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
