package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import view.renderer3D.core.RendererInfoInterface;
import controller.actions.GroupMoveAction;

public class InputManager extends ConcreteController{
	
	private RendererInfoInterface renderer;
	public InputManager(Game game, RendererInfoInterface renderer){
		super(game);
		this.renderer = renderer;
		try {
			Keyboard.create();
			Mouse.create();
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
	private List<GameCharacter> selected;
	public void pollInput(){
		while(Mouse.next() || Keyboard.next()){
			//read mouse and keyboard events
			//TODO mouse keyboard combinations
			
			if(Mouse.getEventButton() == 0){
				if(Mouse.getEventButtonState()){
					startClick = new Point(Mouse.getX(), Mouse.getY());
				}else{
					if (startClick != null){
						endClick = new Point(Mouse.getX(), Mouse.getY());
						double distance = Math.abs(endClick.x - startClick.x) + Math.abs(endClick.y - startClick.y);
						//distance determines whether we select with selectbox or a single point
						if (distance > 10){
							Collection<Cell> selectedCells = renderer.squareSelect(startClick, endClick);
							for (Cell cell : selectedCells){
								selected = cell.getCharacterHolder().getItem();
								for (GameCharacter c : selected){
									c.setSelected(true);
								}
							}
						}else{
							Object obj = renderer.click(startClick.x, startClick.y);
							if (obj != null){
								if (obj instanceof Cell){
									selected = ((Cell)obj).getCharacterHolder().getItem();
									for (GameCharacter c : selected){
										c.setSelected(true);
									}
								}	
							}
						}
						startClick = null;
					}
				}
			}
			//right mouse button
			if(Mouse.getEventButton() == 1){
				if(Mouse.getEventButtonState()){
					//TODO clicked empty tile while characters selected: move to
					//TODO clicked zombie while characters selected: attack
					//TODO click on viking while selected: open inventory
					//TODO clicked/drag from any tile while nothing selected, selection mode
				}else{
					startClick = new Point(Mouse.getX(), Mouse.getY());
					Object obj = renderer.click(startClick.x, startClick.y);
					if (obj != null){
						if (obj instanceof Vector2f){
							ArrayList<GameCharacter> selectedCharacters = new ArrayList<GameCharacter>();
							Collection<Cell> cells = getGame().getMap().getActiveCells();
							for(Cell c: cells){
								List<GameCharacter> temp = c.getCharacterHolder().getItem();
								for(int i= 0 ; i<temp.size(); i++){
									if(temp.get(i).isSelected()){
										selectedCharacters.add(temp.get(i));
									}
								}
							}
							GroupMoveAction m = new GroupMoveAction(selectedCharacters, ((Vector2f) obj).getX(), ((Vector2f) obj).getY());
							getGame().getActionBuffer().add(m);
						}	
					}
				}
			}
		}
	}
}
