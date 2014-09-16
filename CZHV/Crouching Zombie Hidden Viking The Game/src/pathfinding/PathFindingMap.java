package pathfinding;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import model.map.Cell;
import model.character.Character;

public class PathFindingMap
{
	protected Map<Integer, Map<Integer, CellType>> grid;
	protected Character character;
	
	public PathFindingMap(Collection<Cell> cells, Character character)
	{
		this.grid = new HashMap<Integer, Map<Integer, CellType>>();
		this.character = character;
		
		for(Cell c : cells)
		{
			this.addCell(c);
		}
	}
	
	public void addCell(Cell c)
	{
		if(!this.grid.containsValue(c.getX()))
			this.grid.put(c.getX(), new HashMap<Integer, CellType>());
		
		CellType value;
		if(c.isFree(this.character))
			value = CellType.PASSIBLE;
		else
			value = CellType.IMPASSIBLE;
		
		this.grid.get(c.getX()).put(c.getY(), value);
	}
	
	public void addAll(Collection<Cell> cells)
	{
		for(Cell c : cells)
		{
			this.addCell(c);
		}
	}
	
	public CellType getCellType(int x, int y)
	{
		if(this.grid.containsKey(x) && this.grid.get(x).containsKey(y))
			return this.grid.get(x).get(y);
		else
			return CellType.UNKNOWN;
	}
	
	public enum CellType
	{
		PASSIBLE,
		IMPASSIBLE,
		UNKNOWN;
	}
}
