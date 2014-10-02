package czhv;

import model.Game;
import simulator.Simulator;
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
		System.out.println("henk4");
		renderer = new Renderer3D(game);
		System.out.println("henk3");
		aiManager = new AI_Manager(game);
		System.out.println("henk2");
		simulator = new Simulator(game);
		System.out.println("henk1");
		//simulator.start();
	}
	
	private final static void loop(){
		while(!exit){
			game.update();
			renderer.update();
		}
		simulator.quit();
	}
	
	public final static void exit(){
		exit = true;
	}
}
