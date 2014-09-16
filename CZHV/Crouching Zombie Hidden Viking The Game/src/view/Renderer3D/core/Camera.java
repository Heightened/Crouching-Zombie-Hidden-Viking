package view.renderer3D.core;

import java.nio.FloatBuffer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private float pitch = 242;
	private float yaw = 90;
	private FloatBuffer viewMatrix;
	private Matrix4f viewMat;
	
	public Camera(FloatBuffer viewMatrix){
		this.viewMatrix = viewMatrix;
		viewMat = new Matrix4f();
	}
	
	int curAngle = 0;
	float sensitivity = 1/100f;
	public void lookThrough(){
		viewMat.setIdentity();
        curAngle++;
        
        float dx = Mouse.getDX();
        float dy = Mouse.getDY();
        
        if (Mouse.isButtonDown(0)){
	        yaw += dx*sensitivity;
	        pitch += dy*sensitivity;
        }
		Matrix4f.rotate(pitch, new Vector3f(1,0,0), viewMat, viewMat);
		Matrix4f.rotate(yaw, new Vector3f(0,1,0), viewMat, viewMat);
		
		
		viewMatrix.rewind();
		viewMat.store(viewMatrix);
		viewMatrix.flip();
	}
}
