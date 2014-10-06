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
		
		for(Cell c : this.cells)
		{
			if(!c.getCharacterHolder().isEmpty())
			{
				this.chars.addAll(c.getCharacterHolder().getItem());
			}
		}
	}
	
	public Chunk(int lx, int ly)
	{
		this(lx, ly, new ArrayList<Cell>());
	}
	
	//returns true if this is the chunk at chunkposition (lx, ly)
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
		return this.chars;
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
	public void characterMoved(GameCharacter character)
	{
		if(this.cells.contains(character.getCell()) && !this.chars.contains(character))
			this.chars.add(character);
		else if(!this.cells.contains(character.getCell()) && this.chars.contains(character))
			this.chars.remove(character);
	}
}
