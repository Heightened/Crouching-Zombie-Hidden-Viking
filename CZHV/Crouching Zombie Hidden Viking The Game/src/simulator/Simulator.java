package simulator;

import model.Game;

public class Simulator extends Thread{
	private boolean running;
	private Game game;
	
	public Simulator(Game game){
		this.game = game;
	}

	public void quit(){
		running = false;
	}
	
	@Override
	public void run(){
		running = true;
		while(running){
			//TODO: push updates to game
			
		}
	}
}
