package model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import model.character.GameCharacter;
import model.map.ChunkedMap;
import model.map.Map;
import controller.actions.Action;

public class Game {
	private Map map;
	private ChunkedMap flockingMap;
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
		flockingMap = new ChunkedMap(map, 2, 200);
	}	
	
	public void update(){
		controlBuffer.drainTo(actionBuffer);
		while(!actionBuffer.isEmpty()){
			actionBuffer.remove().perform(this);
		}
	}
	
	public void tick(float dtime)
	{
		if(dtime == 0.0)
			return;
		
		System.out.println("TICK! (dtime="+dtime+")");
		
		// update characters
		ArrayList<GameCharacter> chars = (ArrayList<GameCharacter>)flockingMap.getCharacters();
		for(GameCharacter c : chars)
		{
			c.move(dtime);
		}
		
		//update moar!
	}
	
	public ChunkedMap getFlockingMap(){
		return flockingMap;
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
