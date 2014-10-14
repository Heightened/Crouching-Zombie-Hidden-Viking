package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Game;
import model.character.GameCharacter;
import model.item.Item;
import model.map.Cell;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import view.renderer3D.core.RendererInfoInterface;
import controller.actions.GroupMoveAction;

public class InputManager extends ConcreteController{
	
	private RendererInfoInterface renderer;
	private ArrayList<GameCharacter> selectedCharacters;
	
	public InputManager(Game game, RendererInfoInterface renderer){
		super(game);
		this.renderer = renderer;
		try {
			Keyboard.create();
			Mouse.create();
			selectedCharacters = new ArrayList<GameCharacter>();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void stopManager(){
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	
	private Point startClick;
	private Point endClick;
	
	public void pollInput(){
		while(Mouse.next() || Keyboard.next()){
			//if left mouse button was pressed
			if(Mouse.getEventButton() == 0){
				//select cells
				Collection<Cell> selected = selector();
				if(selected != null){
					for(Cell c: selected){
						//get the characters
						List<GameCharacter> characters = c.getCharacterHolder().getItem();
						//if characters were selected add them to the selection
						for(GameCharacter character: characters){
							if(!character.isSelected()){
								character.setSelected(true);
								selectedCharacters.add(character);
							}
						}
					}
				} else {
					//if no characters were selected deselect all characters
					for(GameCharacter character: selectedCharacters){
						character.setSelected(false);
					}
					selectedCharacters.clear();
				}
			}
			//right mouse button
			if(Mouse.getEventButton() == 1){				
				if(Mouse.getEventButtonState()) {
					//TODO clicked zombie while characters selected: attack
					//TODO click on viking while selected: open inventory
					//TODO clicked/drag from any tile while nothing selected, selection mode
					
				}else{
					startClick = new Point(Mouse.getX(), Mouse.getY());
					Object obj = renderer.click(startClick.x, startClick.y);
					if (obj != null){
						if (obj instanceof Vector2f){
							doGroupMoveAction((Vector2f)obj);
						}	
						if (obj instanceof Cell){
							doPickupItem((Cell)obj);
						}
					}
				}
			}
		}
	}

	private void doPickupItem(Cell c) {
		Item i = c.getItemHolder().getItem();
		Vector2f vec = new Vector2f(c.getX(), c.getY());
		doGroupMoveAction(vec);
		new Thread(){
			
			@Override
			public void run(){
				
			}
		}.start();
	}

	private void doGroupMoveAction(Vector2f vec) {
		ArrayList<GameCharacter> controllable = getControllableCharacters();
		try {
			GroupMoveAction m = new GroupMoveAction(controllable,  vec.getX(), vec.getY());
			getGame().getActionBuffer().add(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<GameCharacter> getControllableCharacters() {
		ArrayList<GameCharacter> controllable = new ArrayList<GameCharacter>();
		for(GameCharacter c: selectedCharacters){
			if(getGame().getControlledCharacters().contains(c)){
				controllable.add(c);
			}
		}
		return controllable;
	}
	
	private Collection<Cell> selector(){
		Collection<Cell> selection;
		if(Mouse.getEventButtonState()){
			startClick = new Point(Mouse.getX(), Mouse.getY());
		} else {
			if (startClick != null){
				endClick = new Point(Mouse.getX(), Mouse.getY());
				double distance = Math.abs(endClick.x - startClick.x) + Math.abs(endClick.y - startClick.y);
				//distance determines whether we select with selectbox or a single point
				if (distance > 10){
					return renderer.squareSelect(startClick, endClick);
				} else {
					Object obj = renderer.click(startClick.x, startClick.y);
					if (obj != null){
						if (obj instanceof Cell){
							selection = new ArrayList<Cell>();
							selection.add((Cell)obj);
							return selection;
						}
					}
				}
			} 
		}
		return null;
	}
}
