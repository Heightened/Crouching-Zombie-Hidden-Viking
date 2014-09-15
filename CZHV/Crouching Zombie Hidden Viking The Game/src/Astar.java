/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Resources.Terrain.Byte.Collision.Astar;

import Graphics.Resources.Resources;
import Graphics.lwjgl.core.Interface.EventPrint;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Vouwfietsman
 * 
 * pseudo from wiki
 * http://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class Astar {
    public static ArrayList<Node> invoke(Node start, Node goal){
        knownNodes.clear();
        knownNodes.add(start);
        knownNodes.add(goal);
        SortedList closedset = new SortedList();
        SortedList openset = new SortedList();
        
        EventPrint.println("PATH TO " + goal);
        
        if (goal.isSolid()){
            return null;
        }
        if (start.isSolid()){
            return null;
        }
        openset.add(start);
        start.path_length = 0;
        start.calcHeuristic(goal);
        
        long time = System.currentTimeMillis();
        int counter = 0;//counts number of nodes processed
        while (!openset.isEmpty()){
            counter++;
            if (counter > 100){
                //visited too much nodes, destination unreachable or too far away
                //bail
                EventPrint.println("PATH NOT FOUND/TOO FAR AWAY");
                break;
            }
            Node current = openset.getFirst();
            //System.out.println("it current:" + current.x + " " + current.y);
            
            if (current.equals(goal)){
                EventPrint.println("FOUND PATH IN: " + (System.currentTimeMillis() - time) + "ms");
                return reconstructPath( goal, start);
            }
            
            openset.remove(current);
            closedset.add(current);
            for (Node n : current.getNeighbours()){
                if(n.isSolid()) continue;//non-passable terrain, ignore
                
                //System.out.println("Neighbour:" + n.x + " " + n.y);
                float tentative_g_score = current.path_length + Node.distance(n, current);
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
        EventPrint.println("NO PATH FOUND");
        return null;
    }
    
    public static ArrayList<Node> reconstructPath( Node current, Node start){
        //System.out.println(current);
        if (!current.equals(start)){
             ArrayList<Node> p = reconstructPath( current.camefrom, start);
             p.add(new Node(current));
             return p;
        }
        else{
            ArrayList<Node> ret = new ArrayList<>();
            ret.add(new Node(start));
            return ret;
        }
    }
    
    
    public static void main(String[] args){
        Node start = new Node(2,0);
        Node goal = new Node(4,10);
        printList(invoke( start, goal));
    }
    
    public static void printList(ArrayList<Node> list){
        System.out.println("PATH:");
        for (Node n : list){
            System.out.println(n.x + " " + n.y);
        }
    }
    
    static ArrayList<Node> knownNodes = new ArrayList<>();
    public static Node getNode(int x, int y){
        for (Node n : knownNodes){
            if (n.x == x && n.y == y){
                return n;
            }
        }
        Node n = new Node(x, y);
        knownNodes.add(n);
        return n;
    }
    
    public static void drawPath(ArrayList<Node> list){
       glDisable(GL_DEPTH_TEST);
        glLineWidth(5f);
        glPushMatrix();
            glBegin(GL_LINES);
            for (int i = 0; i < list.size()-1; i++){
                submitVertex(list.get(i));
                submitVertex(list.get(i+1));
            }
            glEnd();
        glPopMatrix();
        glLineWidth(1f);
        glEnable(GL_DEPTH_TEST);
    }
    
    public static void submitVertex(Node n){
        GL11.glColor3f(1,0,0);
        GL11.glVertex3f(n.getPosition().x, n.getPosition().y+10, n.getPosition().z);
    }
}
