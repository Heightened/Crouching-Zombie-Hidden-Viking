package controller;

import model.Game;

public class ConcreteController implements Controller {
	private Game game;
	
	public ConcreteController(Game game) {
		this.setGame(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
