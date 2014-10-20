package model.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.AIController;
import model.item.Item;
import model.item.Weapon;
import model.map.Cell;
import pathfinding.Node;
import pathfinding.PathFinder;
import pathfinding.distanceHeuristics.ApproxEuclid;
import simulator.tempFlocking.Vehicle;

public class GameCharacter extends Vehicle{
	public static final float DEFAULT_MELEE_RANGE = 2;
	
	private Inventory bag;
	
	private int maxHp = 0;
	private int currentHp = 0;
	private int strength = 0;
	private float maxSpeed = 0;
	private float speedX = 0, speedY = 0;
	private float x=0.0f,y=0.0f; // in-cell position 0<=x<1 and 0<=y<1
	
	private Map<Skill, Boolean> skills = new HashMap<>();
	private boolean infected;
	
	private Cell cell = null;
	private PathFinder pathFinder;
	private List<Node> path;
	private int pathPointer;
	private float accuracy = 1;
	private boolean selected;
	private boolean isMoving = false;
	private long lastHit;
	private long delay = 1000;
	
	public GameCharacter(){
		this(100,16,16,2,false);
	}
	
	public GameCharacter(int maxHp, int strength, int maxSpeed, int inventory_size, boolean infected){
		setMaxHp(maxHp);
		setStrength(strength);
		setMaxSpeed(maxSpeed);
		setBag(new Inventory(inventory_size));
		setInfected(infected);
		currentHp = maxHp;
		
		this.skills.put(Skill.OPEN_DOOR, true);
	}
	
	// only for simulator
	public void move(float dtime)
	{
		//System.out.println("speed= ("+speedX+","+speedY+")");
		if(this.isDead())
		{
			System.out.println("Cannot move because I'm dead!");
			return;
		}
		
		float newX = this.x+dtime*this.speedX;
		float newY = this.y+dtime*this.speedY;
		
		if(newX < -.5 || newX >= 0.5 || newY < -.5 || newY >= 0.5)
		{
			this.teleportTo(newX+this.cell.getX(), newY+this.cell.getY());
		}
		else
		{
			this.x = newX;
			this.y = newY;
		}
		updatePathProgress();
	}
	
	private void updatePathProgress()
	{
		if(path != null)
		{
			if(this.isAtTarget())
			{
				Cell nextCell = this.pathFinder.getMap().getCell(path.get(pathPointer).getX(), path.get(pathPointer).getY());
				if(nextCell.isPassible())
				{
					this.setFlockingTargetNode(path.get(++pathPointer));
					this.setFlockingTargetRadius(nextCell.getSpaceRadius());
					if (pathPointer >= path.size() - 1)
					{
						this.setFlockingTargetRadius(0.5f);
						stopMovement();
					}
				} 
				else
				{
					stopMovement();
				}
			}
		}
	}
	
	// only for controller
	public void moveTo(float x, float y)
	{
		assert this.cell != null;
		
		List<Node> nodes = this.pathFinder.calculatePath(
				this.cell.getX(), this.cell.getY(),
				(int)(x+0.5f), (int)(y+0.5f)
			);

		followPath(nodes);
	}
	
	public void followPath(List<Node> nodes)
	{
		if (nodes != null) {
			/*if (this.path != null) {
				for(Node n : this.path)
				{
					this.cell.getMap().getCell(n.getX(), n.getY()).getItemHolder().removeItem();
				}
			}
			for(Node n : nodes)
			{
				//System.out.println("("+n.getX()+","+n.getY()+")");
				this.cell.getMap().getCell(n.getX(), n.getY()).getItemHolder().setItem(new Item());
			}*/
			this.pathPointer = 0;
			this.path = nodes;
			this.setFlockingTargetNode(path.get(pathPointer));
			updatePathProgress();
			isMoving = true;
		}
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean sel){
		this.selected = sel;
	}
	
	public void teleportTo(float x, float y)
	{
		assert this.cell != null;
		
		int xi = (int)Math.round(x);
		int yi = (int)Math.round(y);
		model.map.Cell oldCell = this.cell;
		model.map.Cell newCell;
		if(this.cell.getMap().isInGrid(xi, yi))
			newCell = this.cell.getMap().getCell(xi, yi);
		else
			newCell = oldCell;
		newCell.getCharacterHolder().getItem().add(this);
		this.x = x-xi;
		this.y = y-yi;
		
		this.cell = newCell;
		oldCell.getCharacterHolder().getItem().remove(this);
		
		newCell.characterMoved(this, null);
		oldCell.characterMoved(this, null);
		
	}
	
	public void teleportTo(model.map.Cell cell)
	{

		model.map.Cell oldCell = this.cell;
		model.map.Cell newCell = cell;
		
		cell.getCharacterHolder().getItem().add(this);
		
		if(this.cell != null)
			this.cell.getCharacterHolder().getItem().remove(this);
		
		this.cell = cell;
		
		newCell.characterMoved(this, null);
		if(oldCell != null)
			oldCell.characterMoved(this, null);
	}
	
	public boolean sparkles()
	{
		return true;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public float getAbsX()
	{
		// TODO optimize
		return this.x + this.cell.getX();
	}
	
	public float getAbsY()
	{
		// TODO optimize
		return this.y + this.cell.getY();
	}
	
	public float distanceTo(GameCharacter c)
	{
		return this.distanceTo(c.getAbsX(), c.getAbsY());
	}
	
	public float distanceTo(float x, float y)
	{
		return (float)Math.sqrt(Math.pow(x-this.getAbsX(), 2) + Math.pow(y-this.getAbsY(), 2));
	}
	
	public void setPathFinder(PathFinder pathFinder)
	{
		this.pathFinder = pathFinder;
	}
	
	public PathFinder getPathFinder(){
		return this.pathFinder;
	}
	
	public void stopMovement(){
		isMoving = false;
		path = null;
		pathPointer = 0;
	}
	

	public boolean isMoving() {
		return isMoving;
	}

	public boolean hit() {
		boolean hit = this.lastHit+this.delay < System.currentTimeMillis();
		if(hit)
			this.lastHit = System.currentTimeMillis();
		
		return hit;
	}
	
	public Inventory getBag() {
		return bag;
	}

	public float getAccuracy() {
		return accuracy;
	}
	
	public float getBestRange(){
		float range = DEFAULT_MELEE_RANGE;
		Collection<Weapon> weapons = getWeapons();
		for(Weapon w: weapons){
			if(w.getRange()> range){
				range = w.getRange();
			}
		}
		return range;
	}
	
	public Weapon getBestWeapon(float range){
		Weapon best = null;
		if(getWeapons().isEmpty()){
			return best;
		}
		Collection<Weapon> weapons = getWeapons();
		for(Weapon w: weapons){
			if(w.getRange()<range){
				if(best == null){
					best = w;
				} else if (w.getPower()> best.getPower()){
					best = w;
				}
			}
		}
		return best;
	}
	
	public Collection<Weapon> getWeapons() {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		ItemSlot[] inventory = getBag().getInventory();
		for (int i = 0; i < inventory.length; i++) {
			Item item = inventory[i].getItem();
			if(item instanceof Weapon){
				weapons.add((Weapon)item);
			}
		}
		return weapons;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public void setBag(Inventory bag) {
		this.bag = bag;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int hp) {
		this.maxHp = hp;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}
	
	public float getMaxSpeed()
	{
		if(this.isInfected())
			return this.getCell().getSpeedMultiplyer()*this.maxSpeed;
		else
			return this.maxSpeed;
	}
	
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float[] getSpeed() {
		return new float[] {this.speedX, this.speedY};
	}

	public void setSpeed(float speedX, float speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	public float getDirection() {
		return (float) Math.atan2(this.speedX,this.speedY);
	}

	public int getCurrentHp() {
		if(currentHp<0){
			return 0;
		}
		return currentHp;
	}

	public List<Node> getPath(){
		return path;
	}

	public void applyDamage(int Damage) {
		currentHp = currentHp-Damage;		
		if(isDead()){
			if(cell!=null){
				cell.getMap().remove(this);
			}
		}
	}
	
	public void heal(int hp){
		currentHp = currentHp+hp;
	}
	
	public boolean isDead(){
		return getCurrentHp()<=0;
	}
	
	public Cell getCell()
	{
		return this.cell;
	}

	public boolean hasSkill(Skill skill)
	{
		return this.skills.containsValue(skill) && this.skills.get(skill);
	}
	
	public enum Skill
	{
		OPEN_DOOR;
	}

	public boolean isInfected() {
		return infected;
	}

	public void setInfected(boolean infected) {
		this.infected = infected;
	}

	// ---vvv--- DEBUG CODE ---vvv---
	private Collection<AIController> followers;

	public void setFollowers(Collection<AIController> followers)
	{
		this.followers = followers;
	}
	public Collection<GameCharacter> getFollowers()
	{
		if(this.followers == null)
			return new LinkedList<>();
		
		Collection<GameCharacter> f = new LinkedList<>();
		synchronized(this.followers)
		{
			for(AIController aic : this.followers)
				f.add(aic.getCharacter());
		}
			
		return f;
	}
}
