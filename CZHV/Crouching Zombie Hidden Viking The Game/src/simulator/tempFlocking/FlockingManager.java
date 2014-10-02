package simulator.tempFlocking;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Vector2f;


public class FlockingManager {
	Grid grid;

	ArrayList<Vehicle> vlist;	
	public static final Vector2f screenSize = new Vector2f(2,2);
	public static final float GRID_CELL_SIZE = 0.15f;
	public FlockingManager(ArrayList<Vehicle> vlist){
		grid = new Grid((int)(2/GRID_CELL_SIZE+2),(int)(2/GRID_CELL_SIZE+2));
		this.vlist = vlist;
	}

	public void loop(){
		for (Vehicle v : vlist){
			v.update(this, grid);
			v.update(this, grid);
		}
		grid.reset();
		for (Vehicle v : vlist){
			grid.addVehicle(v);
		}
	}
}
