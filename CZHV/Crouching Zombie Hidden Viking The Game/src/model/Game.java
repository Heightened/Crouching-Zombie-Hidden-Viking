package model;

import java.util.concurrent.LinkedBlockingQueue;

import model.map.Map;
import controller.actions.Action;

public class Game {
	private Map map;
	private LinkedBlockingQueue<Action> controlBuffer;
	private LinkedBlockingQueue<Action> actionBuffer;
	
	public Game() {
		//TODO: fill in stuff
		createMap();
		controlBuffer = new LinkedBlockingQueue<Action>();
		actionBuffer = new LinkedBlockingQueue<Action>();
		
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
