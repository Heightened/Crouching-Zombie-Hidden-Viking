package controller;

import model.Game;

public class Controller {
	private Game game;
	
	public Controller(Game game) {
		this.setGame(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
