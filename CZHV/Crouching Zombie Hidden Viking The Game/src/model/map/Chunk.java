package model.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import model.character.GameCharacter;

public class Chunk implements ChangeListener<Cell>
{
	private Collection<Cell> cells;
	private Collection<GameCharacter> chars = new ArrayList<>();
	private int lx,ly;
	private int width,height;
	private List<MapChangeListener> listeners = new LinkedList<>();
	
	public Chunk(int lx, int ly, int width, int height, Collection<Cell> cells)
	{
		this.lx = lx;
		this.ly = ly;
		this.width = width;
		this.height= height;
		this.cells = cells;
		
		for(Cell c : this.cells)
			if(!c.getCharacterHolder().isEmpty())
				this.chars.addAll(c.getCharacterHolder().getItem());
		
		for(MapChangeListener l : this.listeners)
			for(GameCharacter c : this.chars)
				l.setActive(c);
	}
	
	public Chunk(int lx, int ly, int width, int height)
	{
		this(lx, ly, height, width, new ArrayList<Cell>());
	}
	
	//returns true if this is the chunk at chunkposition (lx, ly)
	public boolean is(int lx, int ly)
	{
		return this.lx == lx && this.ly == ly;
	}

	public boolean contains(Cell cell)
	{
		return this.contains(cell.getX(), cell.getY());
	}
	
	public boolean contains(int x, int y)
	{
		return x/this.width == this.lx && y/this.height == this.ly;
	}
	
	public Collection<model.map.Cell> getActiveCells()
	{
		return this.cells;
	}
	
	public Collection<model.character.GameCharacter> getCharacters()
	{
		return this.chars;
	}
	
	public void addListener(MapChangeListener l)
	{
		this.listeners.add(l);
	}
	
	public void deactivate()
	{
		for(MapChangeListener l : this.listeners)
			for(GameCharacter c : this.chars)
				l.setInactive(c);
	}

	@Override
	public void setActive(Cell changed)
	{
		this.cells.add(changed);
	}

	@Override
	public void setInactive(Cell changed)
	{
		this.cells.remove(changed);
	}
	
	@Override
	public void characterMoved(GameCharacter character, Cell cell)
	{
		if(character == null)
			(new Exception("Character cannot be null")).printStackTrace();
		
		if(this.contains(character.getCell()) && !this.chars.contains(character))
		{
			this.chars.add(character);
			for(MapChangeListener l : this.listeners)
				l.setActive(character);
		}
		else if(!this.contains(character.getCell()) && this.chars.contains(character))
		{
			this.chars.remove(character);
			for(MapChangeListener l : this.listeners)
				l.setInactive(character);
		}
	}
}
