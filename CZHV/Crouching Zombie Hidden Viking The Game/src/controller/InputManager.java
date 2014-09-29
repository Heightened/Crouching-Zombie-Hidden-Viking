package controller;

import model.Game;

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
