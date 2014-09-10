package model;
	
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
		return this.item == null;
	}
}
