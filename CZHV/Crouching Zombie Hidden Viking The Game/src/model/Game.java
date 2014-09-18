package model;

import controller.Action;
import controller.ControlListener;
import model.map.Map;

public class Game implements ControlListener {
	Map map;
	Character[] characters;
	
	public Game() {
		
	}

	@Override
	public void input(String key) {
		//intentionally left blank
	}

	@Override
	public void doAction(Action a) {
		switch(a.getActionType()) {
			case move:
				
				break;
			case shoot:
				
				break;
			case pickup:
				
				break;
			case drop:
				
				break;
			default: break;
		}
	}	
}
