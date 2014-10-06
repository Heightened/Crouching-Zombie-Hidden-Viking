package simulator;

import java.util.ArrayList;
import java.util.Collection;

import controller.actions.Tick;
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
	
	long time = System.currentTimeMillis();
	@Override
	public void run(){
		running = true;
		while(running){
			float dtime = (System.currentTimeMillis() - time)/1000f;
			time = System.currentTimeMillis();
			System.out.println("t: "+time);
			//TODO: push updates to game
			//time = System.currentTimeMillis() - time;
			//if (time > 1000){
			//	time = 0;
			//}
			//float dtime = time/16;
			ArrayList<GameCharacter> chars = (ArrayList<GameCharacter>)flockingMap.getCharacters();
			flockingManager.setVehicleList(chars);
			flockingManager.loop(flockingMap);
			this.game.getActionBuffer().add(new Tick(dtime));
			//time = System.currentTimeMillis();
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
