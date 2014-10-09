package view.renderer3D.core;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Line3D {
	public Vector4f start;
	public Vector4f dir;
	
	public Line3D(Vector4f start, Vector4f dir){
		this.start = start;
		this.dir = dir;
	}
	
	public Vector3f collideXZPlane(float y){
		Vector3f colPoint = new Vector3f();
		
		float difY = start.y - y;
		float steps = difY/dir.y;
		
		if (steps < 0){
			return null;
		}
		
		colPoint.x = start.x + steps*dir.x;
		colPoint.y = start.y + steps*dir.y;//this should be y
		colPoint.z = start.z + steps*dir.z;
		
		return colPoint;
	}
	
	public Vector3f collideYZPlane(float x){
		Vector3f colPoint = new Vector3f();
		
		float difX = start.x - x;
		float steps = difX/dir.x;
		
		if (steps < 0){
			return null;
		}
		
		colPoint.x = start.x + steps*dir.x;
		colPoint.y = start.y + steps*dir.y;//this should be y
		colPoint.z = start.z + steps*dir.z;
		
		return colPoint;
	}
	
	public Vector3f collideXYPlane(float z){
		Vector3f colPoint = new Vector3f();
		
		float difZ = start.z - z;
		float steps = difZ/dir.z;
		
		if (steps < 0){
			return null;
		}
		
		colPoint.x = start.x + steps*dir.x;
		colPoint.y = start.y + steps*dir.y;//this should be y
		colPoint.z = start.z + steps*dir.z;
		
		return colPoint;
	}
	
	@Override
	public String toString(){
		return "s " + start.x + " " + start.y + " " + start.z + " dir " + dir.x + " " + dir.y + " " + dir.z;
	}
}
