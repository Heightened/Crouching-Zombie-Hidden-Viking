package controller.actions;

import model.Game;

public class TickAction implements Action {
	private float dtime;
	
	public TickAction(float dtime)
	{
		this.dtime = dtime;
	}
	
	@Override
	public boolean perform(Game g) {
		g.tick(dtime);
		
		return true;
	}
}
