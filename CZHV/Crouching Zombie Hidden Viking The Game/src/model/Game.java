package model;

import java.util.concurrent.LinkedBlockingQueue;

import model.map.Map;
import simulator.Simulator;
import controller.actions.Action;

public class Game {
	private Map map;
	private Simulator sim;
	private Boolean running;
	private LinkedBlockingQueue<Action> controlBuffer;
	private LinkedBlockingQueue<Action> actionBuffer;
	
	public Game() {
		//TODO: fill in stuff
		createMap();
		controlBuffer = new LinkedBlockingQueue<Action>();
		actionBuffer = new LinkedBlockingQueue<Action>();
		
		startNewSimulator();
		
		new Thread(){
			@Override
			public void run(){
				running = true;
				while(running){
					update();
				}
			}
		}.start();
	}

	private void createMap() {
		//TODO do map creation
		map = new Map(30,30);
	}	
	
	public void update(){
		controlBuffer.drainTo(actionBuffer);
		while(!actionBuffer.isEmpty()){
			actionBuffer.remove().perform(this);
		}
	}
	
	public void quit(){
		stopSimulator();
		running = false;
	}
	
	public void startNewSimulator(){
		//no duplicate simulators
		if(sim != null){
			sim.quit();
		}
		sim = new Simulator(this);
		sim.start();
	}

	public void stopSimulator(){
		if(sim != null){
			sim.quit();
		}
	}
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public LinkedBlockingQueue<Action> getActionBuffer() {
		return actionBuffer;
	}

	public void setActionBuffer(LinkedBlockingQueue<Action> actionBuffer) {
		this.actionBuffer = actionBuffer;
	}
}
