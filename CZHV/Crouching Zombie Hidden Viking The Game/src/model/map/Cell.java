package model.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import model.Container;
import model.character.GameCharacter;
import model.item.Item;
import model.map.decor.Decor;

public class Cell {
	
	private boolean isPassible = true; // should be taken over by decor later;
	private Container<Item> itemHolder = new Container<>();
	private Container<ArrayList<GameCharacter>> characterHolder = new Container<>();
	private Container<Decor> decorHolder = new Container<>();
	private Map map;
	private int x,y;

	public Cell(Map map, int x, int y)
	{
		this.map = map;
		this.x   = x;
		this.y   = y;
		characterHolder.setItem(new ArrayList<GameCharacter>());
	}
	
	// should be taken over by decor later
	public void setPassible(boolean isPassible)
	{
		this.isPassible = isPassible;
	}
	
	// returns false only if permanently impassible
	public boolean isPassible()
	{
		return this.isPassible && (this.decorHolder.isEmpty() || this.decorHolder.getItem().isPassible());
	}
	
	// returns false even if temporarily impassible
	public boolean isFree(GameCharacter c)
	{
		return this.isPassible()
				&& (this.characterHolder.isEmpty() || this.characterHolder.getItem().contains(c))
				&& (this.decorHolder.isEmpty() || this.decorHolder.getItem().isPassible(c));
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public Map getMap()
	{
		return this.map;
	}
	
	public Collection<Cell> getNeighbours()
	{
		return new HashSet<>();
	}
	
	public Container<Item> getItemHolder()
	{
		return this.itemHolder;
	}
	
	public Container<ArrayList<GameCharacter>> getCharacterHolder()
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
