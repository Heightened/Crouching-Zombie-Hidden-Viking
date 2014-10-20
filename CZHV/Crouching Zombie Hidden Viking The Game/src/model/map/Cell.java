package model.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import pathfinding.Node;

import model.Container;
import model.character.GameCharacter;
import model.item.Item;
import model.map.decor.Decor;

public class Cell implements ChangeListener<Container<? extends Object>>
{
	private boolean isPassible = true; // should be taken over by decor later;
	private Container<Item> itemHolder = new Container<>();
	private Container<List<GameCharacter>> characterHolder = new Container<>();
	private Container<Decor> decorHolder = new Container<>();
	private Map map;
	private int x,y;
	private float spaceRadius;
	private float lightIntensity = 0;
	
	public Cell(Map map, int x, int y, float defaultRadius)
	{
		this.map = map;
		this.x   = x;
		this.y   = y;
		this.itemHolder.addListener(this);
		this.characterHolder.addListener(this);
		this.decorHolder.addListener(this);
		this.characterHolder.setItem(new ArrayList<GameCharacter>());
		spaceRadius = defaultRadius;
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
	
	public Container<Item> getItemHolder()
	{
		return this.itemHolder;
	}
	
	public Container<List<GameCharacter>> getCharacterHolder()
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

	@Override
	public void setActive(Container<? extends Object> container)
	{
		this.map.setActive(this);
	}

	@Override
	public void setInactive(Container<? extends Object> container)
	{
		this.map.setInactive(this);
	}
	
	@Override
	public void characterMoved(GameCharacter c, Cell cell)
	{
		if(c.sparkles())
		{
			int m = this.getCharacterHolder().getItem().contains(c)? 1 : -1;
			this.updateIntensities(m);
		}
		
		this.map.characterMoved(c, this);
	}
	
	public void updateIntensities(int m)
	{
		Collection<Cell> cells = this.getMap().getNearbyCells(this.getX(), this.getY(), 18);
		
		for(Cell ic : cells)
			ic.addLightIntensity((float) (m*2f/Math.pow(1f+Cell.distance(ic, this), 2)));
	}

	public float getSpaceRadius()
	{
		return this.spaceRadius;
	}
	
	protected void setSpaceRadius(float r)
	{
		this.spaceRadius = r;
	}
    
    public static float distance(Cell c1, Cell c2){
        return (float)Math.sqrt((Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2)));
    }

	public float getLightIntensity()
	{
		return this.lightIntensity;
	}

	public void setLightIntensity(float intensity)
	{
		this.lightIntensity = intensity;
	}

	public void addLightIntensity(float intensity)
	{
		this.lightIntensity += intensity;
	}

	public float getSpeedModifier()
	{
		return 1f/(1f+this.lightIntensity);
	}
}
