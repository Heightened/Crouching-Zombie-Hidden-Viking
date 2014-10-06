package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;

import model.character.GameCharacter;

public class ChunkedMap implements ChangeListener<Cell>
{
	public final Map map;
	private final int chunkWidth;
	private final int chunkHeight;
	private final int numChunks;
	
	private final List<Chunk> chunks = new LinkedList<>();

	public ChunkedMap(Map map, int chunkSize, int numChunks)
	{
		this(map, chunkSize, chunkSize, numChunks);
	}
	
	public ChunkedMap(Map map, int chunkWidth, int chunkHeight, int numChunks)
	{
		this.map = map;
		this.map.addListener(this);
		this.chunkHeight = chunkHeight;
		this.chunkWidth  = chunkWidth;
		this.numChunks   = numChunks;
	}
	
	// returns all active cells inside the chunk that contains the cell at (x,y)
	public Collection<model.map.Cell> getActiveCells(int x, int y)
	{
		return this.getChunk(x, y).getActiveCells();
	}

	// returns all characters inside the chunk that contains the cell at (x,y)
	public Collection<model.character.GameCharacter> getCharacters(int x, int y)
	{
		return this.getChunk(x, y).getCharacters();
	}
	
	public ArrayList<model.map.Cell> getActiveCells()
	{
		ArrayList<model.map.Cell> allCells = new ArrayList<>();
		
		for(Chunk c : this.chunks)
			allCells.addAll(c.getActiveCells());
		
		return allCells;
	}

	public ArrayList<model.character.GameCharacter> getCharacters()
	{
		ArrayList<model.character.GameCharacter> allChars = new ArrayList<>();
		
		for(Chunk c : this.chunks)
			allChars.addAll(c.getCharacters());
		
		return allChars;
	}

	@Override
	public void setActive(Cell changed)
	{
		if(this.isLoaded(changed.getX(), changed.getY()))
			this.getChunk(changed.getX(), changed.getY()).setActive(changed);
	}

	@Override
	public void setInactive(Cell changed)
	{
		if(this.isLoaded(changed.getX(), changed.getY()))
			this.getChunk(changed.getX(), changed.getY()).setInactive(changed);
	}
	
	@Override
	public void characterMoved(GameCharacter character)
	{
		int x = character.getCell().getX();
		int y = character.getCell().getY();
		
		if(this.isLoaded(x, y))
			this.getChunk(x, y).characterMoved(character);
	}
	
	private boolean isLoaded(int x, int y)
	{
		for(Chunk chunk : this.chunks)
			if(chunk.is(x/this.chunkWidth, y/this.chunkHeight))
				return true;
		
		return false;
	}
	
	private Chunk load(int x, int y)
	{
		while(this.chunks.size() >= this.numChunks)
			this.unload();
		
		// insert at start
		this.chunks.add(0, new Chunk(
				x/this.chunkWidth,
				y/this.chunkHeight,
				this.map.getActiveCells(x, y, x+this.chunkWidth, y+this.chunkHeight)
			));
		
		return this.chunks.get(0);
	}
	
	private void unload()
	{
		this.chunks.remove(this.chunks.size()-1);
	}
	
	private Chunk getChunk(int x, int y)
	{
		Chunk chunk = null;
		
		if(!this.isLoaded(x, y))
			chunk = this.load(x,y);
		else
			for(Chunk c : this.chunks)
				if(c.is(x/this.chunkWidth, y/this.chunkHeight))
					chunk = c;
		
		if(chunk == null)
			throw new RuntimeException("This cannot happen");
		
		this.chunks.remove(chunk);
		this.chunks.add(0,chunk);
		
		return chunk;
	}
}
