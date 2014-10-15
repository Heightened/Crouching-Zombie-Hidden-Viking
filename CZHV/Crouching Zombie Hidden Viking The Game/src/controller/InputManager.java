package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import model.Game;
import model.character.GameCharacter;
import model.character.ItemSlot;
import model.item.Item;
import model.item.Weapon;
import model.map.Cell;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import view.renderer3D.core.RendererInfoInterface;
import controller.actions.GroupMoveAction;
import controller.actions.PickupAction;
import controller.actions.ShootAction;

public class InputManager extends ConcreteController{
	
	private RendererInfoInterface renderer;
	private ArrayList<GameCharacter> selectedCharacters;
	private ActionThreadFactory af= new ActionThreadFactory();
	
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
	private boolean attackEnabled;
	
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
					startClick = new Point(Mouse.getX(), Mouse.getY());
				}else{
					if(startClick!=null){
						Object obj = renderer.click(startClick.x, startClick.y);
						if (obj != null){
							if (obj instanceof Vector2f){
								doGroupMoveAction((Vector2f)obj);
							}	
							if (obj instanceof Cell){
								if(attackEnabled){
									doAttack((Cell)obj);
								}
								doPickupItem((Cell)obj);
							}
						}
					}
				}
			}			
			if(Keyboard.getEventKey() == Keyboard.KEY_A){
				if(!Keyboard.getEventKeyState()){
					attackEnabled = !attackEnabled;
					if(attackEnabled){
						doAttack(null);// 
					} else {
						stopThreads(attack);
					}
				}
			}
		}
	}

	ActionThread attack = af.attackThread();
	private void doAttack(Cell cell) {
		stopThreads(attack); //halt the previous attack command
		attack = af.attackThread();
		attack.start();
		//if a target cell was specified we target it with the selected characters
		if(cell != null){
			List<GameCharacter> characters = cell.getCharacterHolder().getItem();
			for(GameCharacter c: characters){
				if(c.isInfected()){				
					doGroupMoveAction(cellToVector2f(c.getCell()));
					//only one attack thread may spawn
					attack.setTarget(c);
				}
			}
		} else {
			//if no target specified everything is a target and all controlled characters become the source
			attack.setTargets(getGame().getUndead());
		}
	}
	
	private void stopThreads(ActionThread at) {
		if(at.isAlive()){				
			try {
				at.cancel();
				at.join();
			} catch (InterruptedException e) {
				// nothing to do here
			}
		}
	}
	
	ActionThread pickUp = af.pickUpItemThread();
	private void doPickupItem(Cell c) {
		Item i = c.getItemHolder().getItem();
		doGroupMoveAction(cellToVector2f(c));
		//only one pickup thread may spawn
		stopThreads(pickUp);
		pickUp = af.pickUpItemThread();
		pickUp.start();
		//TODO stuff here
	}
	
	private Vector2f cellToVector2f(Cell c){
		return new Vector2f(c.getX(), c.getY());
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
	

	
	class ActionThread extends Thread{
		protected boolean running;
		protected LinkedBlockingQueue<GameCharacter> targets = new LinkedBlockingQueue<GameCharacter>();
		protected GameCharacter focusedTarget;
		private Item i;
		
		public void cancel(){
			running = false;
		}

		public boolean atDestination(GameCharacter c){
			return c.isAtTarget() && c.getPath() == null;		
		}
		
		public boolean nearby(GameCharacter c, GameCharacter c2, int radius){
			return c.distanceTo(c2)<radius;
		}
		
		//puts the target at the head of the list
		public void setTarget(GameCharacter target){
			focusedTarget = target;
		}
		
		public void removeTarget(GameCharacter target){
			targets.remove(target);
		}
		
		public void setTargets(ArrayList<GameCharacter> targets) {
			this.targets.addAll(targets);
		}
	}
	
	class ActionThreadFactory {
		public ActionThread attackThread(){
			return new ActionThread(){		
				@Override
				public void run(){
					while(running){
						if(focusedTarget != null){
							for (GameCharacter c : getGame().getControlledCharacters()) {
								ItemSlot[] inventory = c.getBag().getInventory();
								Weapon v = getWeapon(inventory);
								if (nearby(c, focusedTarget, v.getRange())) {
									getGame().getActionBuffer().add(new ShootAction(v, c, focusedTarget));
									if (focusedTarget.isDead()) {
										focusedTarget = null;
									}
								}
								
							}
						} else {
							int numChars = getGame().getControlledCharacters().size();
							GameCharacter[] lockedTargets = new GameCharacter[numChars];
							for(int i = 0; i<getGame().getControlledCharacters().size(); i++) {
								Iterator<GameCharacter> iter = targets.iterator();
								GameCharacter gc = getGame().getControlledCharacters().get(i);
								ItemSlot[] inventory = gc.getBag().getInventory();
								Weapon v = getWeapon(inventory);
								while(lockedTargets[i] == null && iter.hasNext()){
									GameCharacter enemy = iter.next();
									if(nearby(gc, enemy, v.getRange())){
										lockedTargets[i] = enemy;
									}
								} 
								if(lockedTargets[i]!=null){
									if(nearby(gc, lockedTargets[i], v.getRange())){
										getGame().getActionBuffer().add(new ShootAction(v,gc,lockedTargets[i]));
									} else {
										lockedTargets[i] = null;
									}
									if(lockedTargets[i].isDead()){
										lockedTargets[i] = null;
									}
								}
							}
						}
					}
					
				}
				
				private Weapon getWeapon(ItemSlot[] inventory) {
					for (int i = 0; i < inventory.length; i++) {
						Item item = inventory[i].getItem();
						if(item instanceof Weapon){
							return (Weapon) item;
						}
					}
					return null;
				}
			};
		}
		
		public ActionThread pickUpItemThread(){
			return new ActionThread(){
				@Override
				public void run(){
					while(running){
						for(GameCharacter gc: selectedCharacters){
							if(getGame().getControlledCharacters().contains(gc) && atDestination(gc)){
								getGame().getActionBuffer().add(new PickupAction(gc));
							}
						}
					}
				}
			};
		}
	}
}
