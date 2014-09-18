package controller;

import model.Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputManager extends Controller{
	
	public InputManager(Game game){
		super(game);
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
	
	public void pollInput(){
		while(Mouse.next()){
			if(Mouse.getEventButtonState()){
				//left mouse button
				if(Mouse.getEventButton() == 0){
					//TODO: do stuff here
				}
				//right mouse button
				if(Mouse.getEventButton() == 1){
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
