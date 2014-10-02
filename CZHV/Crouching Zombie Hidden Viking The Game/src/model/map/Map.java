package model.map;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import pathfinding.Astar;
import model.character.GameCharacter;
import model.item.Item;


public class Map
{
	protected Cell[][] grid;
	
	public Map(int width, int height)
	{
		this.grid = new Cell[width][height];
		
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++)
				this.grid[x][y] = new Cell(this, x, y);
	}
	
	public void populate()
	{
		int size = this.grid.length * this.grid[0].length;
		int n = this.randInt(size/60, size/30);
		
		for(int i=0; i<n; i++)
		{
			int l = this.randInt(10, 20);
			
			int x = this.randInt(0, this.grid.length);
			int y = this.randInt(0, this.grid[0].length);
			
			for(int j=0; j<l; j++)
			{
				if(this.isInGrid(x,y))
					this.getCell(x,y).setPassible(false);
				
				switch(this.randInt(0, 3))
				{
					case 0:
						x--;
						break;
					case 1:
						x++;
						break;
					case 2:
						y--;
						break;
					case 3:
						y++;
						break;
				}
			}
		}
		
		int nZombies = 5; //this.randInt(size/600, size/300);
		
		for(int i=0; i<nZombies; i++)
		{
			int x,y;
			GameCharacter c = new GameCharacter(128,16,16,2,true);
			c.setPathFinder(new Astar(this,100,c));
			
			do
			{
				x = this.randInt(0, this.getWidth() - 1);
				y = this.randInt(0, this.getHeight() - 1);
			}
			while(!this.getCell(x, y).isFree(c));
			
			c.teleportTo(this.getCell(x,y));
			
			//if(this.randInt(0,5)==2)
			{
				c.moveTo(this.randInt(0,this.getWidth()-1)+0.5f, this.randInt(0, this.getHeight()-1)+0.5f);
			}
		}
		
		/*
		int nItems = this.randInt(size/600, size/300);
		
		for(int i=0; i<nItems; i++)
		{
			int x,y;
			
			do
			{
				x = this.randInt(0, this.getWidth() - 1);
				y = this.randInt(0, this.getHeight() - 1);
			}
			while(!this.getCell(x, y).isPassible());
			
			this.getCell(x, y).getItemHolder().setItem(new Item());
		}
		//*/
	}
	
	public boolean isInGrid(int x, int y)
	{
		return x>=0 && y>=0 && x<this.getWidth() && y<this.getHeight();
	}

	public int getWidth()
	{
		return this.grid.length;
	}

	public int getHeight()
	{
		return this.grid[0].length;
	}

	public Cell getCell(int x, int y)
	{
		if(this.isInGrid(x, y))
			return this.grid[x][y];
		else
			throw new IllegalArgumentException("Cannot return cell outside of grid");
	}
	
	public Collection<Cell> getNearbyCells(int x, int y, int r)
	{
		Collection<Cell> nearbyCells = new HashSet<>();
		
		for(int xi=x-r; xi<=x+r; xi++)
			for(int yi=y-r; yi<=y+r; yi++)
				if(Math.pow(xi-x,0) + Math.pow(yi-y,2) < r*r && this.isInGrid(xi, yi))
					nearbyCells.add(this.getCell(xi,yi));
		
		return nearbyCells;
	}
	
	public Collection<Cell> getImpassibleCells()
	{
		// TODO:
		// - optimise by caching this
		// - cache in blocks
		// - return only blocks that overlap with given area (add parameters like x,y,x2,y2)
		
		Collection<Cell> impassibleCells = new HashSet<>();
		
		for(int x=0; x<this.grid.length; x++)
			for(int y=0; y<this.grid[x].length; y++)
				if(!this.getCell(x,y).isPassible())
					impassibleCells.add(this.getCell(x,y));
		
		return impassibleCells;
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
			for(int y=0; y<this.grid[x].length; y++)
				if(this.getCell(x,y).isActive())
					activeCells.add(this.getCell(x,y));
		
		return activeCells;
	}
	
	//This should in in some util class
	private int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
