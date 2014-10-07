package simulator.tempFlocking;

import java.util.ArrayList;

import model.character.GameCharacter;
import model.map.ChunkedMap;

import org.lwjgl.util.vector.Vector2f;

import view.renderer3D.core.Renderer3D;


public class FlockingManager {
	ArrayList<GameCharacter> vlist;	
	public static final Vector2f screenSize = new Vector2f(2,2);
	public static final float GRID_CELL_SIZE = 0.15f;
	public FlockingManager(){
		
	}
	
	public void setVehicleList(ArrayList<GameCharacter> vehicles){
		this.vlist = vehicles;
		if (vlist.isEmpty()){
			System.out.println("EMPTY1");
		}
	}

	public void loop(ChunkedMap flockingMap){
		for (GameCharacter v : vlist){
			int gridx = (int)(v.getAbsX());
			int gridy = (int)(v.getAbsY());
			v.update(flockingMap, gridx, gridy);
			v.update(flockingMap, gridx, gridy);
			v.setSpeed(v.getVelocity().x,v.getVelocity().y);
		}
	}
}
