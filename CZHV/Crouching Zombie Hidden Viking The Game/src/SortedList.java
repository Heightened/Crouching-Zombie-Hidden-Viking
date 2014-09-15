/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.Resources.Terrain.Byte.Collision.Astar;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Vouwfietsman
 */
public class SortedList {
    ArrayList<Node> list;
    public SortedList(){
        list = new ArrayList<Node>();
    }
    
    public void add(Node n){
        list.add(n);
        sort();
    }
    
    public void remove(Node n){
        list.remove(n);
    }
    
    public boolean contains(Node n){
        return list.contains(n);
    }
    
    public Node getFirst(){
        return list.get(0);
    }
    
    public void sort(){
        Collections.sort(list);
    }
    
    public boolean isEmpty(){
        return list.isEmpty();
    }
    
    public ArrayList<Node> getList(){
        return list;
    }
}
