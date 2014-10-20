package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import model.Game;
import model.character.GameCharacter;
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
import controller.actions.StopMovingAction;

public class InputManager extends ConcreteController{
	
	private RendererInfoInterface renderer;
	private ArrayList<GameCharacter> selectedCharacters;
	private ActionThreadFactory af= new ActionThreadFactory();
	ActionThread attack = af.attackThread();
	ActionThread pickUp = af.pickUpItemThread();
	
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
		stopThreads(pickUp);
		stopThreads(attack);
		Keyboard.destroy();
		Mouse.destroy();		
	}
	
	
	private Point startClick;
	private Point endClick;
	
	public void pollInput(){
		while(Mouse.next() || Keyboard.next()){
			//if left mouse button was pressed
			if(Mouse.getEventButton() == 0){
				
				if(Mouse.getEventButtonState()){
					startClick = new Point(Mouse.getX(), Mouse.getY());
				} else {
					Collection<Cell> selected = selector();
					if (startClick != null) {
						// select cells
						ArrayList<GameCharacter> temp = new ArrayList<GameCharacter>();
						for(GameCharacter gc: selectedCharacters){
							if(!gc.isDead()){
								temp.add(gc);
							}
						}
						if (selected != null) {
							for (GameCharacter gc : selectedCharacters) {
								gc.setSelected(false);
							}
							selectedCharacters.clear();
							for (Cell c : selected) {
								// get the characters
								List<GameCharacter> characters = c
										.getCharacterHolder().getItem();
								// if characters were selected add them to the
								// selection
								if (Keyboard.getEventKey() == Keyboard.KEY_LSHIFT
										|| Keyboard.getEventKey() == Keyboard.KEY_RSHIFT) {
									if (Keyboard.getEventKeyState()) {
										for (GameCharacter character : characters) {

											temp.add(character);
											selectedCharacters = temp;
										}
										for (GameCharacter character : selectedCharacters) {
											character.setSelected(true);
										}
									}
								} else {

									for (GameCharacter character : characters) {
										if (!character.isSelected()) {
											character.setSelected(true);
											selectedCharacters.add(character);
										}
									}
								}
							}
						} else {
							// if no characters were selected deselect all
							// characters
							for (GameCharacter character : selectedCharacters) {
								character.setSelected(false);
							}
							selectedCharacters.clear();
						}
					}
					startClick = null;
				}
			} 
			//right mouse button
			if(Mouse.getEventButton() == 1){				
				if(Mouse.getEventButtonState()) {
					startClick = new Point(Mouse.getX(), Mouse.getY());
				} else {
					if(startClick!=null){
						Object obj = renderer.click(startClick.x, startClick.y);
						startClick = null;
						if (obj != null) {
							if (Keyboard.getEventKey() == Keyboard.KEY_A) {
								// if mouse clicked and A pressed
								if (Keyboard.getEventKeyState()) {
									if (obj instanceof Vector2f) {
										doAttack(null);
									}
									if (obj instanceof Cell) {
										doAttack((Cell) obj);
									}
								}
							} else {
								if (obj instanceof Vector2f) {
									// stopThreads(attack);
									stopAttack();
									boolean Switch = false;
									;
									Collection<Cell> cells = getGame().getMap()
											.getActiveCells();
									for (Cell c : cells) {
										if (c.getX() == (int) (((Vector2f) obj).x + 0.5f)
												&& c.getY() == (int) (((Vector2f) obj).y + 0.5f)) {
											if (!c.getItemHolder().isEmpty()) {
												doPickupItem((Vector2f) obj);
												Switch = true;
											}
										}
									}
									if (!Switch) {
										doGroupMoveAction((Vector2f) obj,
												getControllableCharacters());
									}
								}
								if (obj instanceof Cell) {
									if (!((Cell) obj).getItemHolder().isEmpty()) {
										doPickupItem(cellToVector2f((Cell) obj));
									}
								}
							}
						}
					}
				}
			}			

			if(Keyboard.getEventKey() == Keyboard.KEY_S){
				boolean startPress = false;
				if(Keyboard.getEventKeyState()){
					startPress = true;
				} else {
					if(startPress){
						startPress = false;
						for(GameCharacter g: selectedCharacters){
							if(getGame().getControlledCharacters().contains(g)){
								getGame().getActionBuffer().add(new StopMovingAction(g));
							}
						}
					}
				}
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_L){
				boolean startPress = false;
				if(Keyboard.getEventKeyState()){
					startPress = true;
				} else {
					if(startPress){
						startPress = false;
						for(GameCharacter g: selectedCharacters){
							if(getGame().getControlledCharacters().contains(g)){
								g.toggleSparkle();
							}
						}
					}
				}
			}
		}
	}
	
	private Collection<Cell> selector(){
		Collection<Cell> selection;
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
		return null;
	}

	private void doAttack(Cell cell) {
		if(!attack.isAlive()){
			attack = af.attackThread();
			attack.start();
		}
		//if a target cell was specified we target it with the selected characters
		if(cell != null){
			List<GameCharacter> characters = cell.getCharacterHolder().getItem();
			for(GameCharacter c: characters){
				if(c.isInfected()){				
					attack.setTarget(c);
				}
			}
		} else {
			attack.setTargets(getGame().getUndead());
		}
		attack.addAttackers(getControllableCharacters());
	}
	
	private void stopAttack(){
		attack.removeAttackers(getControllableCharacters());
	}
	
	private void stopThreads(ActionThread at) {
		if(at.isAlive()){				
			try {
				at.cancel();
				at.join(0);
			} catch (InterruptedException e) {
				// nothing to do here
			}
		}
	}
	
	private void doPickupItem(Vector2f obj) {
		stopThreads(pickUp);
		pickUp = af.pickUpItemThread();		
		doGroupMoveAction(obj, getControllableCharacters());
		pickUp.start();
	}
	
	private Vector2f cellToVector2f(Cell c){
		return new Vector2f(c.getX(), c.getY());
	}
	
	private void doGroupMoveAction(Vector2f vec, ArrayList<GameCharacter> characters) {
		ArrayList<GameCharacter> controllable = characters;
		try {
			GroupMoveAction m = new GroupMoveAction(controllable,  vec.getX(), vec.getY());
			getGame().getActionBuffer().add(m);
		} catch (Exception e) {
			//Nothing has to be done
		}
	}

	protected ArrayList<GameCharacter> getControllableCharacters() {
		ArrayList<GameCharacter> controllable = new ArrayList<GameCharacter>();
		for(GameCharacter c: selectedCharacters){
			if(getGame().getControlledCharacters().contains(c)){
				controllable.add(c);
			}
		}
		return controllable;
	}
		
	class ActionThread extends Thread {
		protected boolean running = true;
		protected LinkedBlockingQueue<GameCharacter> targets = new LinkedBlockingQueue<GameCharacter>();
		protected LinkedBlockingQueue<GameCharacter> attacker = new LinkedBlockingQueue<GameCharacter>();
		protected GameCharacter focusedTarget;
		
		public void cancel(){
			running = false;
		}

		public boolean atDestination(GameCharacter c){
			return c.isAtTarget() && c.getPath() == null;		
		}
		
		public boolean nearby(GameCharacter c, GameCharacter c2, float radius){
			return c.distanceTo(c2)<radius;
		}
		
		public void addAttackers(ArrayList<GameCharacter> attackers){
			for(GameCharacter gc: attackers){
				if(!this.attacker.contains(gc)){
					this.attacker.add(gc);
				}
			}
		}
		
		public void removeAttackers(ArrayList<GameCharacter> attackers){
			for(GameCharacter gc: attackers){
				if(this.attacker.contains(gc)){
					this.attacker.remove(gc);
				}
			}
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
						int numChars = attacker.size();
						GameCharacter[] lockedTargets = new GameCharacter[numChars];
						ArrayList<GameCharacter> characters = new ArrayList<GameCharacter>();
						characters.addAll(attacker);
						for(int i = 0; i<characters.size(); i++) {
// This block determines the targets -------------------------------------------------------------------
							if(focusedTarget!=null){
								if(focusedTarget.isDead()){
									focusedTarget = null;
								}
								if(attacker.contains(characters.get(i))){
									if(lockedTargets.length != 0 && lockedTargets[i] == null){
										lockedTargets[i] = focusedTarget;
									}
								}
							}
							Iterator<GameCharacter> iter = targets.iterator();
							GameCharacter gc = characters.get(i);
							Weapon w = gc.getBestWeapon(gc.getBestRange());
							float range = GameCharacter.DEFAULT_MELEE_RANGE;
							if(w != null){
								range = w.getRange();
							}
							while(lockedTargets.length != 0 && lockedTargets[i] == null && iter.hasNext()){
								GameCharacter enemy = iter.next();
								if(!enemy.isDead()){									
									if(nearby(gc, enemy, range)){
										lockedTargets[i] = enemy;
									}
								} else {
									iter.remove();
								}
								
							} 
// end target determination -----------------------------------------------------------------------------
							if(lockedTargets.length != 0 && lockedTargets[i]!=null){
								if(lockedTargets[i].isDead()){
									lockedTargets[i] = null;
								} else if(nearby(gc, lockedTargets[i], range)){
									getGame().getActionBuffer().add(new StopMovingAction(gc));
									getGame().getActionBuffer().add(new ShootAction(w,gc,lockedTargets[i]));
								} else if(!nearby(gc,lockedTargets[i], range)){
									doGroupMoveAction(cellToVector2f(focusedTarget.getCell()), characters);
								} else {
									lockedTargets[i] = null;
								}
							}
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}		
				}
			};
		}
		
		public ActionThread pickUpItemThread(){
			return new ActionThread(){
				@Override
				public void run(){
					ArrayList<GameCharacter> temp = selectedCharacters;
					boolean areMoving = false;
					while(running){
						if(!areMoving){
							boolean temp2 = true;
							for(GameCharacter gc: temp){
								temp2 = temp2 && gc.isMoving();
							}
							areMoving = temp2;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
						} else {
							for(GameCharacter gc: temp){
								if(getGame().getControlledCharacters().contains(gc) && atDestination(gc)){
									getGame().getActionBuffer().add(new PickupAction(gc));
									cancel();
								}
							}
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
						}						
					}
				}
			};
		}
	}
}
