package model.map;

import java.util.Collection;
import java.util.HashSet;

import model.character.GameCharacter;

public class Chunk implements ChangeListener<Cell>
{
	private Collection<Cell> cells;
	private Collection<GameCharacter> chars = new HashSet<>();
	private int lx,ly;
	
	public Chunk(int lx, int ly, Collection<Cell> cells)
	{
		this.lx = lx;
		this.ly = ly;
		this.cells = cells;
	}
	
	public Chunk(int lx, int ly)
	{
		this(lx, ly, new HashSet<Cell>());
	}
	
	public boolean is(int lx, int ly)
	{
		return this.lx == lx && this.ly == ly;
	}
	
	public Collection<model.map.Cell> getActiveCells()
	{
		return this.cells;
	}
	
	public Collection<model.character.GameCharacter> getCharacters()
	{
		throw new UnsupportedOperationException("Dit werkt nog niet");
		//return this.chars;
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
}
