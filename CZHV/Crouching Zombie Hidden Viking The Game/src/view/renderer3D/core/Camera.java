package view.renderer3D.core;

import java.nio.FloatBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private float pitch = 0.95f;
	private float yaw = 0;
	private FloatBuffer viewMatrix;
	private Matrix4f viewMat;
	private Vector3f position;
	
	public Camera(FloatBuffer viewMatrix, Matrix4f viewmat){
		this.viewMatrix = viewMatrix;
		viewMat = viewmat;
		position = new Vector3f(-1,-1,-2);
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
	int curAngle = 0;
	float sensitivity = 1/100f;
	public void lookThrough(){
		viewMat.setIdentity();
        curAngle++;
        
        float dx = Mouse.getDX();
        float dy = Mouse.getDY();
        
        if (Mouse.isButtonDown(0)){
	       // yaw += dx*sensitivity;
	       // pitch -= dy*sensitivity;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
        	position.x += 0.05;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
        	position.x -= 0.05;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
        	position.z += 0.05;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
        	position.z -= 0.05;
        }
        
       //System.out.println(pitch + " " + yaw);
        
		Matrix4f.rotate(pitch, new Vector3f(1,0,0), viewMat, viewMat);
		Matrix4f.rotate(yaw, new Vector3f(0,1,0), viewMat, viewMat);
		
		Matrix4f.translate(position, viewMat, viewMat);
		
		
		viewMatrix.rewind();
		viewMat.store(viewMatrix);
		viewMatrix.flip();
	}
}
