package view.renderer3D;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.ShaderObject;

public class Animation {
	AnimationFrame[] frames;
	
	private String getFramePath(String path, int frame) {
		char[] digits = new char[6];
		char[] frameNumber = Integer.toString(frame).toCharArray();
		
		for (int i = 0; i < 6; i++) {
			int offset = digits.length - frameNumber.length;
			
			if (i >= offset) {
				digits[i] = frameNumber[i - offset];
			} else {
				digits[i] = '0';
			}
		}
		
		return path + "_" + digits.toString() + ".obj";			 
	}
	
	public Animation(int size, String path) {
		frames = new AnimationFrame[120];
		
		for (int i = 0; i < size; i++) {
			frames[i] = new AnimationFrame(getFramePath(path, i));
		}
	}
	
	public Animation(int size, String path, int[][] trackers) {
		frames = new AnimationFrame[120];
		
		for (int i = 0; i < size; i++) {
			frames[i] = new AnimationFrame(getFramePath(path, i), trackers);
		}
	}
	
	private int getFrame(float t) {
		return (int) (frames.length * t);
	}
	
	int frame;
	
	public void setTime(float t) {
		frame = getFrame(t);
	}
	
	public void draw(ShaderObject shader) {
		frames[frame].draw(shader);
	}
	
	public void draw(ShaderObject shader, float t) {
		setTime(t);
		frames[frame].draw(shader);
	}
	
	public Vector3f getTrackerPosition(int tracker) {
		return frames[frame].getTrackerPosition(tracker);
	}
	
	public Vector3f getTrackerNormal(int tracker) {
		return frames[frame].getTrackerNormal(tracker);
	}
}
