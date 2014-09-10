package model.map;

import java.util.Collection;
import java.util.HashSet;


public class Map
{
	protected Cell[][] grid;
	
	public Map(int width, int height)
	{
		this.grid = new Cell[width][height];
		
		for(int x=0; x<width; x++)
			for(int y=0; x<height; y++)
				this.grid[x][y] = new Cell(this, x, y);
	}
	
	public void populate()
	{
		
	}
	
	public Cell getCell(int x, int y)
	{
		return this.grid[x][y];
	}
	
	public Collection<Cell> getActiveCells()
	{
		// TODO:
		// - optimise by caching this
		// - update cached version after changes, instead of recreating a new cached version
		// - cache in blocks
		// - return only blocks that overlap with given area (add parameters like x,y,x2,y2)
		
		Collection<Cell> activeCells = new HashSet<>();
		
		for(int x=0; x<this.grid.length; x++)
			for(int y=0; x<this.grid[x].length; y++)
				if(!this.getCell(x,y).isActive())
					activeCells.add(this.getCell(x,y));
		
		return activeCells;
	}
}
