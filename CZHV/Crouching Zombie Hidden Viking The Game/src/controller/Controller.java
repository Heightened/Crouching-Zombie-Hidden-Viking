package controller;

import model.Game;

public class Controller {
	private Game game;
	private ControlListener controlListener;
	
	public Controller(Game game) {
		this.setGame(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public ControlListener getControlListener() {
		return controlListener;
	}

	public void setControlListener(ControlListener controlListener) {
		this.controlListener = controlListener;
	}	
	

}
