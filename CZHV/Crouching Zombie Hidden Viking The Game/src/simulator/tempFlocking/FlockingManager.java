package simulator.tempFlocking;

import java.util.ArrayList;

import model.character.GameCharacter;
import model.map.ChunkedMap;

import org.lwjgl.util.vector.Vector2f;


public class FlockingManager {
	ArrayList<GameCharacter> vlist;	
	public static final Vector2f screenSize = new Vector2f(2,2);
	public static final float GRID_CELL_SIZE = 0.15f;
	public FlockingManager(){
		
	}
	
	public void setVehicleList(ArrayList<GameCharacter> vehicles){
		this.vlist = vehicles;
		if (vlist.isEmpty()){
			System.out.println("EMPTY");
		}
	}

	public void loop(ChunkedMap flockingMap){
		System.out.println("EMPTY");
		for (GameCharacter v : vlist){
			
			v.setPosition(v.getAbsX(), 0, v.getAbsY());
		}
		for (GameCharacter v : vlist){
			int gridx = (int)(v.getAbsX()%1);
			int gridy = (int)(v.getAbsY()%1);
			v.update(flockingMap, gridx, gridy);
			v.update(flockingMap, gridx, gridy);
			v.setSpeed(v.getVelocity().x, v.getVelocity().y);
		}
	}
}
