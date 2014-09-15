/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Resources.Terrain.Byte.Collision.Astar;

import Graphics.Resources.Resources;
import Graphics.Resources.Terrain.Byte.ByteMap;
import Graphics.Resources.Terrain.Byte.Nodes.LocationInNode;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Vouwfietsman
 */
public class Node implements Comparable{
    float path_length = 0;
    float heuristicscore = 0;
    
    boolean solid = false;
    
    int x = 0;
    int y = 0;
    
    public SortedList neighbours;
    Node camefrom;
    
    
    public Node(int x, int y){
        this.x = x;
        this.y = y;
        heuristicscore = 100000;
        path_length = 100000;
        neighbours = new SortedList();
    }
    
    public Node(Node n){
        this.x = n.x;
        this.y = n.y;
    }
    
    public void setSolid(){
        this.solid = true;
    }
    
    public void setNoSolid(){
        this.solid = false;
    }
    
    public boolean isSolid(){
        return solid;
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
            for (int i = -1; i < 2; i++){
                for (int j = -1; j < 2; j++){
                    if (!hasNeighbour(x + i, y + j)){
                        Node n = Astar.getNode(x + i, y + j);
                        neighbours.add( n);
                        n.neighbours.add(this);
                    }
                }
            }
        }
        return neighbours.getList();
    }
    
    private boolean hasNeighbour(int x, int y){
        for (Node n : neighbours.list){
            if (n.x == x && n.y == y){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Object t) {
        Node n = (Node)t;
        if (getScore() == n.getScore()){
            return 0;
        }
        if (getScore() > n.getScore()){
            return 1;
        }else{
            return -1;
        }
    }
    
    Vector3f position;
    public Vector3f getPosition(){
        if (position == null){
            position = new Vector3f(ByteMap.offsetx + x*20, ByteMap.getHeight(x, y), ByteMap.offsetz+y*20);
        }
        return position;
        
    }
    
    LocationInNode loc;
    public LocationInNode getLoc(){
        if (loc == null){
            loc = LocationInNode.createLOC(x*20, y*20).get(0);
        }
        return loc;
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
        return x +" " + y + " " + path_length +" " +heuristicscore;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
}
