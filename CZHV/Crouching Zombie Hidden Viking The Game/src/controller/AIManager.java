package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import controller.ai.SimpleAIController;
import model.Game;
import model.character.GameCharacter;
import model.map.MapChangeListener;

public class AIManager implements MapChangeListener {
	private static int POOL_SIZE = 10; //maybe read this in from a file?
	private ManagerThread[] threadPool;
	private LinkedBlockingQueue<AIController> activeControllers;
	private ArrayList<AIController> inactiveControllers;
	private HashMap<GameCharacter, AIController> controlBinding;
	private Game game;
	
	private long time = System.currentTimeMillis();
	
	public AIManager(Game game) {
		this.game = game;
		activeControllers = new LinkedBlockingQueue<AIController>();
		inactiveControllers = new ArrayList<AIController>();
		controlBinding = new HashMap<GameCharacter, AIController>();
		
		this.game.getFlockingMap().addListener(this);
		
		System.out.println("Adding "+(this.game.getFlockingMap().getCharacters().size())+" zombie controllers");
		for(GameCharacter c : this.game.getFlockingMap().getCharacters())
			this.setActive(c);
		
		threadPool = new ManagerThread[POOL_SIZE];
		for(int i = 0; i<threadPool.length; i++){
			threadPool[i] = new ManagerThread();
			threadPool[i].start();
		}
	}
	
	public void setActive(AIController control){
		if(inactiveControllers.contains(control)){
			inactiveControllers.remove(control);
		}
		activeControllers.add(control);
	}
	
	public void setInactive(AIController control){
		if(activeControllers.contains(control)){
			activeControllers.remove(control);
		}
		inactiveControllers.add(control);
	}
	
	@Override
	public void setActive(GameCharacter character) {
		if(game.isAIControlled(character))
		{
			addControlBinding(character);
			setActive(controlBinding.get(character));
		}
	}

	@Override
	public void setInactive(GameCharacter character) {
		if(game.isAIControlled(character))
		{
			if(character.isDead())
			{
				if(controlBinding.containsKey(character))
					activeControllers.remove(controlBinding.get(character));
				
				removeControlBinding(character);
			}
			else
			{
				addControlBinding(character);
				setInactive(controlBinding.get(character));
			}
		}
	}

	private void addControlBinding(GameCharacter character) {
		if(!controlBinding.containsKey(character)){
			//TODO: factory for AIController
			System.out.println("Added AIController");
			controlBinding.put(character,  new SimpleAIController(game, character, this.controlBinding));
		}
	}
	
	private void removeControlBinding(GameCharacter character)
	{
		if(controlBinding.containsKey(character))
			controlBinding.remove(character);
	}
	
	public void stopManager(){
		for(int i = 0; i<threadPool.length; i++){
			threadPool[i].quit();
		}
	}
	
	class ManagerThread extends Thread{
		private boolean running;
		private int count = 0;
		
		public void quit(){
			running = false;
		}
		
		@Override
		public void run(){
			running = true;
			while(running){
				if(System.currentTimeMillis() - time > 1000)
				{
					time = System.currentTimeMillis();
					
					System.out.println("Handled "+count+" AI ticks in 1 seconds. Current Queue size: "+activeControllers.size());
					
					count= 0;
				}
				try {
					AIController temp = activeControllers.poll();
					if(temp != null){
						temp.update();
						count++;
						if(temp.isAlive())
							activeControllers.put(temp);
					}
					try{
						//sleep for one millisecond
						Thread.sleep(1);
					}catch(Exception e){
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

