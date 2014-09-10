package model.map;

import java.util.Collection;
import java.util.HashSet;

import model.Container;
import model.Item;
import model.character.Character;

public class Cell {
	
	private boolean isPassible = true;
	private Container<Item> itemHolder = new Container<>();
	private Container<Character> characterHolder = new Container<>();
	private Container<Decor> decorHolder = new Container<>();
	private Map map;
	private int x,y;

	public Cell(Map map, int x, int y)
	{
		this.map = map;
		this.x   = x;
		this.y   = y;
	}
	
	public void setPassible(boolean isPassible)
	{
		this.isPassible = isPassible;
	}
	
	public boolean isPassible()
	{
		return this.isPassible && this.characterHolder.isEmpty();
	}
	
	public Collection<Cell> getNeighbours()
	{
		return new HashSet<>();
	}
	
	public Container<Item> getItemHolder()
	{
		return this.itemHolder;
	}
	
	public Container<Character> getCharacterHolder()
	{
		return this.characterHolder;
	}
	
	public Container<Decor> getDecorHolder()
	{
		return this.decorHolder;
	}
	
	public boolean isActive() {
		return !this.itemHolder.isEmpty() || !this.characterHolder.isEmpty();
	}
}
