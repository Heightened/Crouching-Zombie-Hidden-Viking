package model.map;

import java.util.ArrayList;
import java.util.Collection;

import model.character.GameCharacter;

public class Chunk implements ChangeListener<Cell>
{
	private Collection<Cell> cells;
	private Collection<GameCharacter> chars = new ArrayList<>();
	private int lx,ly;
	
	public Chunk(int lx, int ly, Collection<Cell> cells)
	{
		this.lx = lx;
		this.ly = ly;
		this.cells = cells;
	}
	
	public Chunk(int lx, int ly)
	{
		this(lx, ly, new ArrayList<Cell>());
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
		//TODO optimize
		Collection<model.character.GameCharacter> characters = new ArrayList<>();
		
		
		for(Cell c : this.cells)
		{
			if(!c.getCharacterHolder().isEmpty())
			{
				characters.addAll(c.getCharacterHolder().getItem());
			}
		}

		return characters;
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
