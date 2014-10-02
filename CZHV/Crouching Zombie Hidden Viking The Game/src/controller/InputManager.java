package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import model.Game;
import model.character.GameCharacter;
import model.map.Cell;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import view.renderer3D.core.RendererInfoInterface;

public class InputManager extends Controller{
	
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
	
	
	public Point startClick;
	public Point endClick;
	public void pollInput(){
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				if(Mouse.getEventButtonState()){
					startClick = new Point();
					startClick.x = Mouse.getX();
					startClick.y = Mouse.getY();
				}else{
					if (startClick != null){
						endClick = new Point(Mouse.getX(), Mouse.getY());
						double distance = Math.abs(endClick.x - startClick.x) + Math.abs(endClick.y - startClick.y);
						//distance determines whether we select with selectbox or a single point
						if (distance > 10){
							Collection<Cell> selectedCells = renderer.squareSelect(startClick, endClick);
							System.out.println("Selected cells:");
							for (Cell cell : selectedCells){
								ArrayList<GameCharacter> chars = cell.getCharacterHolder().getItem();
								for (GameCharacter c : chars){
									c.setSelected(true);
								}
							}
						}else{
							Object selected = renderer.click(startClick.x, startClick.y);
							if (selected != null){
								if (selected instanceof Cell){
									ArrayList<GameCharacter> chars = ((Cell)selected).getCharacterHolder().getItem();
									for (GameCharacter c : chars){
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
					//TODO: do stuff here
				}
			}
		}
		
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				//TODO: keyconfig
			}
		}
	}
}
