package simulator;

import java.util.Collection;

import model.Game;
import model.map.Cell;
import model.map.Map;

public class Simulator extends Thread{
	private boolean running;
	private Game game;
	private Map map;
	
	public Simulator(Game game){
		this.game = game;
		this.map = game.getMap();
	}

	public void quit(){
		running = false;
	}
	
	@Override
	public void run(){
		running = true;
		while(running){
			//TODO: push updates to game
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
			Collection<Cell> activeCells = map.getActiveCells();
			for (Cell cell : activeCells){

			}
		}
	}
}
