package model.map;

public interface ChangeListener<T>
{
	public void setActive(T changed);
	
	public void setInactive(T changed);
}
