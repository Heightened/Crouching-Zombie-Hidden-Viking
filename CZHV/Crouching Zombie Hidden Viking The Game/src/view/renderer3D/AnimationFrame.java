package view.renderer3D;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class AnimationFrame extends Model {
	private class VertexTracker {
		int[] attributeLocations;
		
		Vector3f position;
		Vector3f normal;
		Vector2f uvcoord;
		
		public VertexTracker(int[] tracker) {
			attributeLocations = tracker;
		}
		
		public void setTracker(float[] position, float[] normal, float[] uvcoord) {
			this.position.set(position[0], position[1], position[2]);
			this.normal.set(normal[0], normal[1], normal[2]);
			this.uvcoord.set(uvcoord[0], uvcoord[1]);
		}
		
		public VertexTracker(float[] position, float[] normal, float[] uvcoord) {
			setTracker(position, normal, uvcoord);
		}
	}
	
	VertexTracker[] vertexTrackers;
	
	public AnimationFrame(String objPath) {
		super(objPath);
		
		vertexTrackers = null;
	}
	
	public AnimationFrame(String objPath, int[][] trackers) {
		super(objPath);
		
		vertexTrackers = new VertexTracker[trackers.length];
		
		for (int i = 0; i < trackers.length; i++) {
			vertexTrackers[i] = new VertexTracker(trackers[i]);
		}
	}
	
	@Override 
	protected void finalizeVertexData() {
		if (vertexTrackers != null) for (VertexTracker vt : vertexTrackers) {
			vt.setTracker(
					distinctVertices.get(vt.attributeLocations[0]), 
					distinctNormals.get(vt.attributeLocations[1]),
					distinctTextureCoords.get(vt.attributeLocations[2]));
		}
		
		distinctVertices = null;
		distinctTextureCoords = null;
		distinctNormals = null;
	}
	
	public Vector3f getTrackerPosition(int i) {
		return vertexTrackers[i].position;
	}
	
	public Vector3f getTrackerNormal(int i) {
		return vertexTrackers[i].normal;
	}
	
	public Vector2f getTrackerTextureCoord(int i) { //This will likely remain unused
		return vertexTrackers[i].uvcoord;
	}
}
