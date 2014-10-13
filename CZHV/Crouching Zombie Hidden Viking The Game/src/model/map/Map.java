package model.map;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import pathfinding.Astar;
import util.Rand;
import model.character.GameCharacter;
import model.item.Item;


public class Map implements ChangeListener<Cell>
{
	protected Cell[][] grid;
	private Collection<ChangeListener<Cell>> listeners = new LinkedList<>();
	
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
		int n = Rand.randInt(size/60, size/30);
		
		for(int i=0; i<n; i++)
		{
			int l = Rand.randInt(10, 20);
			
			int x = Rand.randInt(0, this.grid.length);
			int y = Rand.randInt(0, this.grid[0].length);
			
			for(int j=0; j<l; j++)
			{
				if(this.isInGrid(x,y))
					this.getCell(x,y).setPassible(false);
				
				switch(Rand.randInt(0, 3))
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
		
		int nZombies = 30; //this.randInt(size/600, size/300);
		
		for(int i=0; i<nZombies; i++)
		{
			int x,y;
			GameCharacter c = new GameCharacter(128,16,16,2,true);
			c.setPathFinder(new Astar(this,100,c));
			
			do
			{
				x = Rand.randInt(0, this.getWidth() - 1);
				y = Rand.randInt(0, this.getHeight() - 1);
			}
			while(!this.getCell(x, y).isFree(c));
			
			c.teleportTo(this.getCell(x,y));
			
			//if(this.randInt(0,5)==2)
			{
				//c.moveTo(this.randInt(0,this.getWidth()-1)+0.5f, this.randInt(0, this.getHeight()-1)+0.5f);
			}
		}
		
		//*
		int nVikings = 10; //Rand.randInt(size/600, size/300);
		
		for(int i=0; i<nVikings; i++)
		{
			int x,y;
			GameCharacter c = new GameCharacter(128,16,16,2,false);
			c.setPathFinder(new Astar(this,100,c));
			
			do
			{
				x = Rand.randInt(0, this.getWidth() - 1);
				y = Rand.randInt(0, this.getHeight() - 1);
			}
			while(!this.getCell(x, y).isFree(c));
			
			c.teleportTo(this.getCell(x,y));
			
			//if(this.randInt(0,5)==2)
			{
				//c.moveTo(this.randInt(0,this.getWidth()-1)+0.5f, this.randInt(0, this.getHeight()-1)+0.5f);
			}
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
	
	//DEPRICATED
	public Collection<Cell> getActiveCells()
	{
		Collection<Cell> activeCells = new HashSet<>();
		
		for(int x=0; x<this.grid.length; x++)
			for(int y=0; y<this.grid[x].length; y++)
				if(this.getCell(x,y).isActive())
					activeCells.add(this.getCell(x,y));
		
		return activeCells;
	}
	
	// returns all active cells such that
	// x1 <= c.getX() < x2 OR x2 <= c.getX() < x1
	// AND
	// y1 <= c.getY() < y2 OR y2 <= c.getY() < y1
	public Collection<Cell> getActiveCells(int x1, int y1, int x2, int y2)
	{
		assert this.isInGrid(x1, y1);
		assert this.isInGrid(x2, y2);
		
		
		
		Collection<Cell> activeCells = new HashSet<>();
		
		for(int x=Math.min(x1, x2); x<Math.max(x1, x2); x++)
			for(int y=Math.min(y1, y2); y<Math.max(y1, y2); y++)
				if(isInGrid(x, y) && this.getCell(x,y).isActive())
					activeCells.add(this.getCell(x,y));
		
		return activeCells;
	}
	
	public void addListener(ChangeListener<Cell> l)
	{
		this.listeners.add(l);
	}

	@Override
	public void setActive(Cell changed)
	{
		for(ChangeListener<Cell> l : this.listeners)
			l.setActive(changed);
	}

	@Override
	public void setInactive(Cell changed)
	{
		for(ChangeListener<Cell> l : this.listeners)
			l.setInactive(changed);
	}
	
	@Override
	public void characterMoved(GameCharacter character, Cell cell)
	{
		for(ChangeListener<Cell> l : this.listeners)
			l.characterMoved(character, cell);
	}
}
