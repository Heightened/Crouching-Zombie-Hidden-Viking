package model.map;

import java.util.Collection;

import model.Container;
import model.character.Item;
import model.character.Character;

public class Cell {
	
	private Container<Item> itemHolder;
	private Container<Character> characterHolder;
	private Map map;
	private int x,y;

	public Cell(Map map, int x, int y)
	{
		this.map = map;
		this.x   = x;
		this.y   = y;
	}
	
	public Collection<Cell> getNeighbours()
	{
		return null;
	}
	
	public Container<Item> getItemHolder()
	{
		return this.itemHolder;
	}
	
	public Container<Character> getCharacterHolder()
	{
		return this.characterHolder;
	}
	
	public boolean isActive() {
		return !this.itemHolder.isEmpty() || !this.characterHolder.isEmpty();
	}
}
