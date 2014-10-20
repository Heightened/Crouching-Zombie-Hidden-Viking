package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

import model.character.GameCharacter;
import model.map.Cell;
import model.map.ChunkedMap;
import model.map.Map;
import controller.actions.Action;

public class Game {
	public static final boolean AI_DRAW_HIERARCHY = true;
	private Map map;
	private ChunkedMap flockingMap;
	private ChunkedMap viewMap;
	private LinkedBlockingQueue<Action> controlBuffer;
	private LinkedBlockingQueue<Action> actionBuffer;
	private ArrayList<GameCharacter> controlledCharacters;

	public Game() {
		//TODO: fill in stuff
		controlBuffer = new LinkedBlockingQueue<Action>();
		actionBuffer = new LinkedBlockingQueue<Action>();
		createMap();
		controlledCharacters = map.getControlledCharacters();
	}

	private void createMap() {
		//TODO do map creation
		map = new Map(100,100);
		map.populate();
		flockingMap = new ChunkedMap(map, 2, 2500);
		viewMap = new ChunkedMap(map, 10, 100);
		//TODO add characters to map
	}	
	
	public void update(){
		controlBuffer.drainTo(actionBuffer);
		while(!actionBuffer.isEmpty()){
			actionBuffer.remove().perform(this);
			controlledCharacters = map.getControlledCharacters();
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
	
	public ChunkedMap getViewMap(){
		return viewMap;
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

	public boolean isAIControlled(GameCharacter character) {
		return character.isInfected() && !this.controlledCharacters.contains(character);
	}
	
	public ArrayList<GameCharacter> getUndead(){
		ArrayList<GameCharacter> undead = new ArrayList<GameCharacter>();
		Collection<Cell> all = map.getActiveCells();
		for(Cell c: all){
			for(GameCharacter gc: c.getCharacterHolder().getItem()){
				if(gc.isInfected()){
					undead.add(gc);
				}
			}
		}
		return undead;
	}
}
