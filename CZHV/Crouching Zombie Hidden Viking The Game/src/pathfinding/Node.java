package pathfinding;

import java.util.ArrayList;
import util.SortedList;

/**
 *
 * @author Vouwfietsman
 */
public class Node implements Comparable<Node>
{
	float path_length = 0;
    float heuristicscore = 0;
    
    PathFindingMap.CellType type;
    
    int x = 0;
    int y = 0;
    
    private PathFindingMap map;
    
    public SortedList<Node> neighbours;
    Node camefrom;
    
    
    public Node(int x, int y, PathFindingMap.CellType type, PathFindingMap map){
        this.x = x;
        this.y = y;
        this.map = map;
        this.type = type;
        heuristicscore = 100000;
        path_length = 100000;
        neighbours = new SortedList<>();
    }
    
    public Node(Node n, PathFindingMap map){
        this.x = n.x;
        this.y = n.y;
        this.type = n.type;
        this.map = map;
    }
    
    protected PathFindingMap getMap()
    {
    	return this.map;
    }
    
    public boolean isSolid(){
        return this.type == PathFindingMap.CellType.IMPASSIBLE;
    }
    
    public void addNeighbour(Node n){
        neighbours.add(n);
    }
    
    public float getScore(){
        return path_length + heuristicscore;
    }
    
    public void calcHeuristic(Node goal){
        heuristicscore = distance(this, goal);
    }
    
    public static float distance(Node n1, Node n2){
        return (float)Math.sqrt((Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2)));
    }
    
    boolean asked = false;
    public ArrayList<Node> getNeighbours(){
        //TODO: CHECK COLLISION
        if (!asked){
            asked = true;
            int minI = -1;
            int minJ = -1;
            int maxI = 1;
            int maxJ = 1;
            if(this.x <= 1) minI = 0;
            if(this.y <= 1) minJ = 0;
            if(this.x >= 28) maxI = 0;
            if(this.y >= 28) maxJ = 0;
            for (int i = minI; i <= maxI; i++){
                for (int j = minJ; j <= maxJ; j++){
                    if (!(i==0 && j==0) && !hasNeighbour(x + i, y + j)){
                        Node n = this.getMap().getNode(x + i, y + j);
                        neighbours.add( n);
                        n.neighbours.add(this);
                    }
                }
            }
        }
        return neighbours.getList();
    }
    
    private boolean hasNeighbour(int x, int y){
        for (Node n : neighbours.getList()){
            if (n.x == x && n.y == y){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Node n) {
        if (getScore() == n.getScore()){
            return 0;
        }
        if (getScore() > n.getScore()){
            return 1;
        }else{
            return -1;
        }
    }
    
    @Override
    public boolean equals(Object o){
        Node n = (Node)o;
        if (n.x == x && n.y == y){
            return true;
        }
        return false;
    }
    
    @Override
    public String toString(){
        return "("+x+", "+y+") cost:" + path_length +", score" +heuristicscore;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
