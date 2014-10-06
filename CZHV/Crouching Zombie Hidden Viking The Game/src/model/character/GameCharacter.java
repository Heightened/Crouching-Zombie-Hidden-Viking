package model.character;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.item.Item;
import model.map.Cell;
import pathfinding.Node;
import pathfinding.PathFinder;
import simulator.tempFlocking.Vehicle;

public class GameCharacter extends Vehicle{
	
	private Inventory bag;
	
	private int maxHp = 0;
	private int currentHp = 0;
	private int strength = 0;
	private float maxSpeed = 0;
	private float speedX = 0, speedY = 0;
	private float x=0.5f,y=0.5f; // in-cell position 0<=x<1 and 0<=y<1
	
	private Map<Skill, Boolean> skills = new HashMap<>();
	private boolean infected;
	
	private Cell cell = null;
	private PathFinder pathFinder;
	
	private boolean selected;
	
	public GameCharacter(){
		this(100,16,16,2,false);
	}
	
	public GameCharacter(int maxHp, int strength, int maxSpeed, int inventory_size, boolean infected){
		super();
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
		// if moved outside of cell, us teleportTo
		// else just update in-cell position
	}
	
	// only for controller
	public void moveTo(float x, float y)
	{
		assert this.cell != null;
		
		Collection<Node> nodes = this.pathFinder.calculatePath(
				this.cell.getX(), this.cell.getY(),
				(int)x, (int)y
			);

		if (nodes != null) {
			for(Node n : nodes)
			{
				System.out.println("("+n.getX()+","+n.getY()+")");
				this.cell.getMap().getCell(n.getX(), n.getY()).getItemHolder().setItem(new Item());
			}
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
		
		int xi = (int)x;
		int yi = (int)y;
		model.map.Cell oldCell = this.cell;
		model.map.Cell newCell = this.cell.getMap().getCell(xi, yi);
		
		newCell.getCharacterHolder().getItem().add(this);
		this.x = x-xi;
		this.y = y-yi;
		
		this.cell = newCell;
		oldCell.getCharacterHolder().removeItem();
		
		newCell.characterMoved(this);
		oldCell.characterMoved(this);
		
	}
	
	public void teleportTo(model.map.Cell cell)
	{
		cell.getCharacterHolder().getItem().add(this);
		
		if(this.cell != null)
			this.cell.getCharacterHolder().removeItem();
		
		this.cell = cell;
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
	
	public void setPathFinder(PathFinder pathFinder)
	{
		this.pathFinder = pathFinder;
	}
	
	public Inventory getBag() {
		return bag;
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
	
	public float getMaxSpeed() {
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

	public void applyDamage(int Damage) {
		currentHp = currentHp-Damage;
		if(isDead()){
			if(cell!=null){
				cell.getCharacterHolder().removeItem();
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
}
