/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core.lighting;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.MatrixCZHV;
import view.renderer3D.core.Renderer3D;
import view.renderer3D.core.shadows.Shadow;

/**
 *
 * @author Vouwfietsman
 */
public class Light {
    Vector3f position;
    Vector3f color;
    Vector3f specColor;
    float radius;
    float cutoffangle;

    private float[] floats;
    private FloatBuffer buffer;
    
    boolean[] dirty;
    
    private FloatBuffer viewMatrix;
    private FloatBuffer projectionMatrix;
    private Matrix4f viewMat;
    public float pitch = 0;
    public float yaw = 0;
    
    public Vector4f spotDirection;
    private Shadow shadow;
    
    public Light(Vector3f position, Vector3f color, Vector3f specColor, float radius, float cutoffangle, Vector4f spotdirection) {
        this.position = position;
        this.color = color;
        this.spotDirection = spotdirection;
        this.specColor = specColor;
        this.radius = radius;
        this.cutoffangle = cutoffangle;
        floats = new float[16];
        buffer = BufferUtils.createFloatBuffer(16);
        dirty = new boolean[2];
        dirty[0] = true;
        dirty[1] = true;
        calcProjectionMatrix();
    }
    
    public Light calcViewMatrix(){
    	if (viewMatrix == null){
    		viewMatrix = BufferUtils.createFloatBuffer(16);
    		viewMat = new Matrix4f();
    	}
    	viewMat.setIdentity();
    	yaw = -(float)Math.atan2(spotDirection.x, spotDirection.z)+(float)Math.PI;
    	float length = (float)Math.sqrt(spotDirection.x*spotDirection.x + spotDirection.z*spotDirection.z);
    	pitch = -(float)Math.atan2(spotDirection.y, length);
		Matrix4f.rotate(pitch, new Vector3f(1,0,0), viewMat, viewMat);
		Matrix4f.rotate(yaw, new Vector3f(0,1,0), viewMat, viewMat);
		Vector3f posneg = new Vector3f(-position.x, -position.y, -position.z);
		
		Matrix4f.translate(posneg, viewMat, viewMat);
    	MatrixCZHV.MatrixToBuffer(viewMat, viewMatrix);
    	return this;
    }
    
    public void setShadow(Shadow s){
    	this.shadow = s;
    }
    
    public Shadow getShadow(){
    	return shadow;
    }
    
    public final void calcProjectionMatrix(){
    	float fieldOfView = 90f;
		float aspectRatio = 1;//Renderer3D.screenSize.width/(float)Renderer3D.screenSize.height;//affected lightmap is square, so x/y = 1
		float near_plane = 0.05f;
		float far_plane = 300;

        float y_scale = 1/(float)Math.tan(Math.toRadians(fieldOfView / 2f));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far_plane - near_plane;
		
		Matrix4f projMat = new Matrix4f();
		
		projMat.m00 = x_scale;
		projMat.m11 = y_scale;
		projMat.m22 = -((far_plane + near_plane) / frustum_length);
		projMat.m23 = -1;
		projMat.m32 = -((2 * near_plane * far_plane) / frustum_length);
		projMat.m33 = 0;    
		
		projectionMatrix = BufferUtils.createFloatBuffer(16);
		
		projMat.store(projectionMatrix);
		projectionMatrix.flip();
    }
    
    public FloatBuffer getViewMatrix(){
    	return viewMatrix;
    }
    
    public FloatBuffer getProjectionMatrix(){
    	return projectionMatrix;
    }
    
    public void setDirty(){
        dirty[0] = true;
        dirty[1] = true;
    }
    
    public float[] toFloat(){
        floats[0] = position.x;
        floats[1] = position.y;
        floats[2] = position.z;
        floats[3] = position.z;//worldpos.w is waardeloos
        
        floats[4] = color.x;
        floats[5] = color.y;
        floats[6] = color.z;
        floats[7] = cutoffangle;
        
        floats[8] = specColor.x;
        floats[9] = specColor.y;
        floats[10] = specColor.z;
        floats[11] = specColor.z;//speccolor.a is waardeloos
        
        floats[12] = radius;
        floats[13] = spotDirection.x;
        floats[14] = spotDirection.y;
        floats[15] = spotDirection.z;
        return floats;
    }
    
    public FloatBuffer toBuffer(){
        buffer.clear();
        buffer.put(toFloat());
        buffer.flip();
        return buffer;
    }
}
