package model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import model.character.GameCharacter;
import model.map.ChunkedMap;
import model.map.Map;
import controller.actions.Action;

public class Game {
	public static final boolean AI_DRAW_HIERARCHY = true;
	private Map map;
	private ChunkedMap flockingMap;
	private LinkedBlockingQueue<Action> controlBuffer;
	private LinkedBlockingQueue<Action> actionBuffer;
	private ArrayList<GameCharacter> controlledCharacters;
	private ArrayList<GameCharacter> aiCharacters;

	public Game() {
		//TODO: fill in stuff
		controlBuffer = new LinkedBlockingQueue<Action>();
		actionBuffer = new LinkedBlockingQueue<Action>();
		aiCharacters = new ArrayList<GameCharacter>();
		createMap();
		controlledCharacters = map.getControlledCharacters();
	}

	private void createMap() {
		//TODO do map creation
		map = new Map(30,30);
		map.populate();
		flockingMap = new ChunkedMap(map, 2, 225);
		//TODO add characters to map
	}	
	
	public void update(){
		controlBuffer.drainTo(actionBuffer);
		while(!actionBuffer.isEmpty()){
			actionBuffer.remove().perform(this);
		}
	}
	
	int tickCount = 0;
	public void tick(float dtime)
	{
		tickCount++;
		
		if(dtime == 0.0)
			return;
		
		if(tickCount%100 == 0)
			System.out.println("100 ticks");
		
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
	
	public ArrayList<GameCharacter> getControlledCharacters() {
		return controlledCharacters;
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

	public ArrayList<GameCharacter> getAiCharacters() {
		return aiCharacters;
	}
}
