/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core.lighting;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

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
    Vector3f spotdirection;

    private float[] floats;
    private FloatBuffer buffer;
    
    boolean[] dirty;
    
    public Light(Vector3f position, Vector3f color, Vector3f specColor, float radius, float cutoffangle, Vector3f spotdirection) {
        this.position = position;
        this.color = color;
        this.specColor = specColor;
        this.radius = radius;
        this.cutoffangle = cutoffangle;
        this.spotdirection = spotdirection;
        floats = new float[16];
        buffer = BufferUtils.createFloatBuffer(16);
        dirty = new boolean[2];
        dirty[0] = true;
        dirty[1] = true;
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
        floats[13] = spotdirection.x;
        floats[14] = spotdirection.y;
        floats[15] = spotdirection.z;
        return floats;
    }
    
    public FloatBuffer toBuffer(){
        buffer.clear();
        buffer.put(toFloat());
        buffer.flip();
        return buffer;
    }
}
