package pathfinding;

import java.util.ArrayList;
import java.util.List;

import model.character.GameCharacter;
import model.map.Map;
import util.SortedList;

public class Astar extends PathFinder
{
	private PathFindingMap currentMap;
	final float unknownCellWeight = 1.2f;
	final float impassibleWeight = 3f;
	
	public Astar(Map map, int radius, GameCharacter character)
	{
		super(map, radius, character);
	}
	
	public List<Node> calculatePath(int x1, int y1, int x2, int y2)
	{
		this.currentMap = this.makeMap(x1, y1);
		return this.calculatePath(this.currentMap.getNode(x1, y1), this.currentMap.getNode(x2, y2));
	}
	
	public List<Node> calculatePath(Node start, Node goal)
	{
		if(start == goal || goal == null || start == null)
		{
			return null;
		}
		
		//System.out.println("START PATHFINDING");
		SortedList<Node> closedset = new SortedList<>();
		SortedList<Node> openset = new SortedList<>();
		
		if (goal.isSolid())
		{
			return null;
		}
		if (start.isSolid())
		{
			if(!this.getCharacter().isInfected())
			{
				System.out.println("regel 49");
				System.out.println("position of character: ("+getCharacter().getAbsX()+", "+getCharacter().getAbsY()+")");
				for(Node n : start.getNeighbours())
					System.out.println("("+n.getX()+", "+n.getY()+")");
			}
			return null;
		}
		openset.add(start);
		start.path_length = 0;
		start.calcHeuristic(goal);
		
		long time = System.currentTimeMillis();
		int counter = 0;//counts number of nodes processed
		while (!openset.isEmpty()){
			counter++;
			//if (counter > 100){
				//visited too much nodes, destination unreachable or too far away
				//bail
				//System.out.println("PATH NOT FOUND/TOO FAR AWAY");
				//break;
			//}
			Node current = openset.getFirst();
			//System.out.println("it current:" + current.x + " " + current.y);
			
			if (current.equals(goal)){
				if(!this.getCharacter().isInfected())
					System.out.println("FOUND PATH IN: " + (System.currentTimeMillis() - time) + "ms");
				return reconstructPath( goal, start);
			}
			
			openset.remove(current);
			closedset.add(current);
			for (Node n : current.getNeighbours()){
				//System.out.println(n.x + " " + n.y);
				//if (n.isSolid()){System.out.println("SOLID");}
				if(n.isSolid() || currentMap.getNode(current.getX(), n.getY()).isSolid() ||
						currentMap.getNode(n.getX(), current.getY()).isSolid()) continue;//non-passable terrain, ignore
				
				//System.out.println("Neighbour:" + n.x + " " + n.y);
				float weight = 1;
				if(n.type == PathFindingMap.CellType.UNKNOWN) weight *= unknownCellWeight;
				if(!this.map.getCell(n.getX(), n.getY()).isFree(null)) weight *= impassibleWeight;
				
				float tentative_g_score = current.path_length + weight * Node.distance(n, current);
				float tentative_f_score = tentative_g_score + Node.distance(n, goal);
				
				//System.out.println("fscore " + tentative_f_score +" neighbourscore = " + n.getScore());
				if (closedset.contains(n) || tentative_f_score >= n.getScore() ){
					continue;
				}
				
				if (!openset.contains(n) || tentative_f_score < n.getScore() ){
					n.camefrom = current;
					n.path_length = tentative_g_score;
					n.heuristicscore = Node.distance(n, goal);
					if (!openset.contains(n)){
						openset.add(n);
						//System.out.println("add " + n.x + " " + n.y);
					}
				}
			}
		}
		//System.out.println("NO PATH");
		if(!this.getCharacter().isInfected())
			System.out.println("NO PATH FOUND");
		
		return null;
	}
	
	public ArrayList<Node> reconstructPath( Node current, Node start){
		//System.out.println(current);
		if (!current.equals(start)){
			ArrayList<Node> p = reconstructPath( current.camefrom, start);
			p.add(current); //new Node(current, this));
			return p;
		}
		else{
			ArrayList<Node> ret = new ArrayList<>();
			ret.add(start); //new Node(start, this));
			return ret;
		}
	}
	
	private Node getGoal(Node start, float direction)
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Path calculatePath(int x1, int y1, float direction) {
		Node start = this.currentMap.getNode(x1, y1);
		this.calculatePath(start, this.getGoal(start,direction));
		return null;
	}

	@Override
	public PathFindingData getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addData(PathFindingData data) {
		// TODO Auto-generated method stub
		
	}
}
