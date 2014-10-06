package controller.actions;

import model.Game;

public class Tick implements Action {
	private float dtime;
	
	public Tick(float dtime)
	{
		this.dtime = dtime;
	}
	
	@Override
	public boolean perform(Game g) {
		g.tick(dtime);
		
		return true;
	}
}
