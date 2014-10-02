package simulator.tempFlocking;

import java.util.ArrayList;

import model.character.GameCharacter;
import model.map.ChunkedMap;

import org.lwjgl.util.vector.Vector2f;


public class FlockingManager {
	Grid grid;

	ArrayList<GameCharacter> vlist;	
	public static final Vector2f screenSize = new Vector2f(2,2);
	public static final float GRID_CELL_SIZE = 0.15f;
	public FlockingManager(){
		grid = new Grid((int)(2/GRID_CELL_SIZE+2),(int)(2/GRID_CELL_SIZE+2));
	}
	
	public void setVehicleList(ArrayList<GameCharacter> vehicles){
		this.vlist = vehicles;
	}

	public void loop(ChunkedMap flockingMap){
		for (GameCharacter v : vlist){
			v.update(this, grid);
			v.update(this, grid);
		}
	}
}
