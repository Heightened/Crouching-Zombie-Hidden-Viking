package pathfinding;

import model.map.Map;
import model.character.Character;

public abstract class PathFinder
{
	
	private int radius;
	private Map map;
	private Character character;
	
	public PathFinder(Map map, int radius, Character character)
	{
		this.map       = map;
		this.radius    = radius;
		this.character = character;
	}
	
	protected PathFindingMap getMap(int x, int y)
	{
		return new PathFindingMap(this.map.getNearbyCells(x, y, this.radius), this.character);
	}
	
	// returns a path from (x1,y1) to (x2,y2)
	public abstract Path calculatePath(int x1, int y1, int x2, int y2);
	
	// returns a path from (x1,y1) in the general direction of direction [rad]
	public abstract Path calculatePath(int x1, int y1,  float direction);
	
	// --------------------------------------
	// knowledge exchange between pathfinders
	
	// returns the data collected by this pathfinder.
	public abstract PathFindingData getData();
	
	// adds new data to make the pathfinder smarter.
	public abstract void addData(PathFindingData data);
}
