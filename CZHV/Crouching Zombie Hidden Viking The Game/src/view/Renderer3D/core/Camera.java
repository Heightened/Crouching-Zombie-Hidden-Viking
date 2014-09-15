package view.renderer3D.core;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Camera {
	private float pitch = 242;
	private float yaw = 90;
	public Camera(){
		
	}
	
	int curAngle = 0;
	float sensitivity = 1/5f;
	public void lookThrough(){
        GL11.glLoadIdentity();
        curAngle++;
        
        float dx = Mouse.getDX();
        float dy = Mouse.getDY();
        
        if (Mouse.isButtonDown(0)){
	        yaw += dx*sensitivity;
	        pitch += dy*sensitivity;
        }
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);

        GL11.glTranslatef(0,0,0);
	}
}
