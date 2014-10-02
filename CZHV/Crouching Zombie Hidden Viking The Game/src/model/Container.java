package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import model.map.ChangeListener;
	
public class Container<I> {
	private I item = null;
	private Collection<ChangeListener<Container<? extends Object>>> listeners = new LinkedList<>();
	
	public Container()
	{
		this(null);
	}
	
	public Container(I item)
	{
		this.setItem(item);
	}
	
	public void addListener(ChangeListener<Container<? extends Object>> l)
	{
		this.listeners.add(l);
	}
	
	public I getItem()
	{
		return this.item;
	}
	
	public void setItem(I item)
	{
		if(this.isEmpty())
			for(ChangeListener<Container<? extends Object>> l : this.listeners)
				l.setActive(this);
		
		this.item = item;
	}
	
	public void removeItem()
	{
		this.item = null;
	}
	
	public boolean isEmpty()
	{
		if (this.item instanceof ArrayList<?> ){
			return ((ArrayList<?>)this.item).isEmpty();
		}
		return this.item == null;
	}
}
