package controller;

import controller.actions.Action;
import model.Game;

public class Simulator implements ControlListener{
	private Game game;
	private boolean paused = false;
	
	public Simulator(Game game){
		this.setGame(game);
	}

	@Override
	public void doAction(Action a) {
		if(!isPaused()){
			a.perform();
		}
	}
	
	//getset-------------------------------------------------

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean isPaused() {
		return paused;
	}

	public void pauseGame(boolean paused){
		this.paused = paused;
	}
}
