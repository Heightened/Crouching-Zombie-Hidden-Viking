package controller.actions;

import model.Game;

public class PickupAction implements Action{
	Game g;
	
	public PickupAction(Game g){
		this.g = g;
	}
	
	@Override
	public boolean perform() {
		//TODO g.pickupitem
		return false;
	}

}
