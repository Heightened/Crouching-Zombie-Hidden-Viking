package czhv;

import model.Game;
import view.renderer3D.core.Renderer3D;
import controller.AI_Manager;

/**
 * 
 * @author Vouwfietsman
 *
 */
public class mainClass {
	private static Game game;
	private static Renderer3D renderer;
	private static AI_Manager aiManager;
	
	private static boolean exit = false;
	
	
	public static void main(String[] args){
		init();
		loop();
	}
	
	private final static void init(){
		game = new Game();
		renderer = new Renderer3D(game);
		aiManager = new AI_Manager(game);
	}
	
	private final static void loop(){
		while(!exit){
			renderer.update();
		}
	}
	
	public final static void exit(){
		exit = true;
	}
}
