package simulator;

import java.util.ArrayList;
import java.util.Collection;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;
import model.map.ChunkedMap;
import model.map.Map;
import simulator.tempFlocking.FlockingManager;

public class Simulator extends Thread{
	private boolean running;
	private Game game;
	private ChunkedMap flockingMap;
	private Map map;
	private FlockingManager flockingManager;
	
	public Simulator(Game game){
		this.game = game;
		this.flockingMap = game.getFlockingMap();
		this.map = game.getMap();
		flockingManager = new FlockingManager();
		System.out.println("START");
	}

	public void quit(){
		System.out.println("QUIT");
		running = false;
	}
	
	@Override
	public void run(){
		System.out.println("EMPTY");
		running = true;
		while(running){
			//TODO: push updates to game
			System.out.println("EMPTY");
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
			ArrayList<GameCharacter> chars = (ArrayList<GameCharacter>)flockingMap.getCharacters();
			flockingManager.setVehicleList(chars);
			flockingManager.loop(flockingMap);
		}
	}
}
