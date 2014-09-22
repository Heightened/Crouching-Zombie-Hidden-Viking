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
    private Matrix4f viewMat;
    public float pitch = 0;
    public float yaw = 0;
    
    public Vector4f spotDirection;
    
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
    
    public FloatBuffer getViewMatrix(){
    	return viewMatrix;
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
