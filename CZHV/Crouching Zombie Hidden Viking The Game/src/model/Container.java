package model;

import java.util.ArrayList;
	
public class Container<I> {
	private I item = null;
	
	public Container()
	{
		this(null);
	}
	
	public Container(I item)
	{
		this.setItem(item);
	}
	
	public I getItem()
	{
		return this.item;
	}
	
	public void setItem(I item)
	{
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
