package czhv;

import model.Game;
import simulator.Simulator;
import view.renderer3D.core.Renderer3D;
import controller.AIManager;
import controller.InputManager;

/**
 * 
 * @author Vouwfietsman
 *
 */
public class mainClass {
	private static Game game;
	private static Renderer3D renderer;
	private static AIManager aiController;
	private static InputManager inputManager;
	private static Simulator simulator;
	
	private static boolean exit = false;
	
	
	public static void main(String[] args){
		System.out.println("henk6");
		init();
		System.out.println("henk5");
		loop();
	}
	
	private final static void init(){
		game = new Game();
		renderer = new Renderer3D(game);
		inputManager = new InputManager(game, renderer);
		aiController = new AIManager(game);
		simulator = new Simulator(game);
		simulator.start();
	}
	
	private final static void loop(){
		long time = System.currentTimeMillis();
		while(!exit){
			game.update();
			if(System.currentTimeMillis() - time > 13) //limit to 77 fps
			{
				time = System.currentTimeMillis();
				renderer.update();
			}
			
			inputManager.pollInput();
		}
		simulator.quit();
		aiController.stopManager();
	}
	
	public final static void exit(){
		exit = true;
	}
}
