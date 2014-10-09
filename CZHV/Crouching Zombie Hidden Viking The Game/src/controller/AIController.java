package controller;

import model.Game;
import model.character.GameCharacter;

public abstract class AIController implements Controller {
	private Game game;
	private GameCharacter gameChar;
	
	public AIController(Game game, GameCharacter gameChar) {
		this.game = game;
		this.gameChar = gameChar;
	}
	
	public abstract void update();

	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	public GameCharacter getCharacter() {
		return this.gameChar;
	}

	public void setCharacter(GameCharacter character) {
		this.gameChar = character;
	}

	public abstract int getFollowerCount();

	public abstract float getSatisfactionLevel();

	public abstract float getSatisfaction();

	public abstract boolean isFollower(AIController c);
}
