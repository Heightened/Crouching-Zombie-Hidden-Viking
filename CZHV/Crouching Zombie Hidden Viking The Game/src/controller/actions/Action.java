package controller.actions;

import model.Game;

public interface Action {	
	public boolean perform(Game g);
	public String type();
}
