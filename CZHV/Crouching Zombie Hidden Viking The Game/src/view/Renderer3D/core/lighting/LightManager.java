/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core.lighting;

import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Renderer3D;
import view.renderer3D.core.ShaderObject;

/**
 *
 * @author Vouwfietsman
 */
public class LightManager {
    private ArrayList<Light> lights = new ArrayList<>();
    private int[] lightBuffer;
    private int[] indexBuffer;
    
    private FloatBuffer indexFloats;
    private FloatBuffer lightFloats;
    private Vector4f[][] indexVectors;
    private Vector4f[][] indexStrength;
    
    public static final int lightBindingPoint = 1;
    public static final int indexBindingPoint = 2;
    
    static final int MAX_LIGHTS = 128;
    static final int STRUCT_SIZE_F = 4*4;
    static final int STRUCT_SIZE_B = 4*STRUCT_SIZE_F;
    static final int GRID_SIZE = 16;
    static final float GRID_CELL_SIZE = 0.25f;
    
    private static int flipflop = 0;
    private static boolean inits = false;
    public LightManager(){
        inits = true;
        Light nullLight = new Light(new Vector3f(0,0,0), new Vector3f(0,0,0),new Vector3f(0,0,0),0,1,new Vector3f(0,0,0));
        lights.add(nullLight);// "empty" light, invisible padding light
        lights.add(new Light(new Vector3f(0,0.5f,0), new Vector3f(1,1,0),new Vector3f(1,0,0),1f,.5f,new Vector3f(0,-1,0)));
        lights.add(new Light(new Vector3f(1,0.5f,0), new Vector3f(0,1,1),new Vector3f(1,0,0),1f,.5f,new Vector3f(0,-1,0)));
        lights.add(new Light(new Vector3f(0,0.5f,1), new Vector3f(0,1,0),new Vector3f(1,0,0),1f,.5f,new Vector3f(0,-1,0)));
        lights.add(new Light(new Vector3f(1,0.5f,1), new Vector3f(1,0,1),new Vector3f(1,0,0),1f,.5f,new Vector3f(0,-1,0)));
        
        lightBuffer = new int[2];
        lightBuffer[0] = ARBVertexBufferObject.glGenBuffersARB();
        lightBuffer[1] = ARBVertexBufferObject.glGenBuffersARB();
        ARBVertexBufferObject.glBindBufferARB(GL_UNIFORM_BUFFER, lightBuffer[0]);
        ARBVertexBufferObject.glBufferDataARB(GL_UNIFORM_BUFFER, MAX_LIGHTS*STRUCT_SIZE_B, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);
        ARBVertexBufferObject.glBindBufferARB(GL_UNIFORM_BUFFER, lightBuffer[1]);
        ARBVertexBufferObject.glBufferDataARB(GL_UNIFORM_BUFFER, MAX_LIGHTS*STRUCT_SIZE_B, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);
        
        indexBuffer = new int[2];
        indexBuffer[0] = ARBVertexBufferObject.glGenBuffersARB();
        indexBuffer[1] = ARBVertexBufferObject.glGenBuffersARB();
        ARBVertexBufferObject.glBindBufferARB(GL_UNIFORM_BUFFER, indexBuffer[0]);
        ARBVertexBufferObject.glBufferDataARB(GL_UNIFORM_BUFFER, GRID_SIZE*GRID_SIZE*4*4, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);
        ARBVertexBufferObject.glBindBufferARB(GL_UNIFORM_BUFFER, indexBuffer[1]);
        ARBVertexBufferObject.glBufferDataARB(GL_UNIFORM_BUFFER, GRID_SIZE*GRID_SIZE*4*4, ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB);
        
        indexFloats = BufferUtils.createFloatBuffer(GRID_SIZE*GRID_SIZE*4);
        lightFloats = BufferUtils.createFloatBuffer(MAX_LIGHTS*STRUCT_SIZE_F);
        indexVectors = new Vector4f[GRID_SIZE][GRID_SIZE];
        indexStrength = new Vector4f[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < indexVectors.length; i++){
            for (int j = 0; j < indexVectors[0].length; j++){
                indexVectors[i][j] = new Vector4f();
                indexStrength[i][j] = new Vector4f(1000000,1000000,1000000,1000000);
            }
        }
    }
    
    private boolean bound = false;
    public void bind(ShaderObject shader){
        if (bound){
            return;
        }
        bound = true;
        glBindBufferBase(GL_UNIFORM_BUFFER, shader.getUnifBlockBindingPoint("lightblock"), getLightBuffer());
        glBindBufferBase(GL_UNIFORM_BUFFER, shader.getUnifBlockBindingPoint("indexblock"), getIndexBuffer());
    }
    
    public void unbind(){
    	bound = false;
        glBindBufferBase(GL_UNIFORM_BUFFER, lightBindingPoint, 0);
        glBindBufferBase(GL_UNIFORM_BUFFER, indexBindingPoint, 0);
    }
    
    public int getLightBuffer(){
        return lightBuffer[flipflop];
    }
    public int getIndexBuffer(){
        return indexBuffer[flipflop];
    }
    
    public void update(){ 
        animate();//animate the lights
        writeLightsToBuffer();//write the lights to the graphics card
        distribute();//update the grid containing the lights
        writeIndex();//write index to graphics card
        //flip the buffers
        if (flipflop == 0){
            flipflop = 1;
        }else{
            flipflop = 0;
        }
        //light is updated to graphics card, so draw any lit geometry
    }
    
    private void animate(){
        //stay idle for now
        for (int i = 1; i < lights.size(); i++){
            lights.get(i).setDirty();
           // lights.get(i).position.y = ((float)Math.sin(Renderer3D.currentTime*20+i)*0.5f);
           lights.get(i).spotdirection.z = ((float)Math.sin(Renderer3D.currentTime*20+i)*1f);
           lights.get(i).spotdirection.x = ((float)Math.cos(Renderer3D.currentTime*20+i)*1f);
           // lights.get(i).cutoffangle = 0.8f;
            //lights.get(i).radius = (float)Math.sin(Renderer3D.currentTime*10)*1+1;
        }
    }
    
    private void distribute(){
        long time = System.currentTimeMillis();
        //reset all lights
        for (int i = 0; i < indexVectors.length; i++){
            for (int j = 0; j < indexVectors[0].length; j++){
                indexVectors[i][j].set(0,0,0,0);
                indexStrength[i][j].set(1000000,1000000,1000000,1000000);
            }
        }
        for (int i = 1; i < lights.size(); i++){
            Light l = lights.get(i);
            Vector3f pos = l.position;
            int left = (int)((l.position.x-l.radius)/GRID_CELL_SIZE);
            int right = (int)((l.position.x+l.radius)/GRID_CELL_SIZE);
            int top = (int)((l.position.z+l.radius)/GRID_CELL_SIZE);
            int bottom = (int)((l.position.z-l.radius)/GRID_CELL_SIZE);
          //  System.out.println(left + " " + right);
           // System.out.println(bottom + " " + top);
           // System.out.println(pos.x + " " + pos.y + " " + pos.z);
           // System.out.println(l.radius);
           // System.exit(0);
            for (int x = left-1; x < right+1; x++){
                for (int z = bottom-1; z < top+1; z++){
                    float distance = (float)Math.pow((x+0.5)*GRID_CELL_SIZE-pos.x,2) + (float)Math.pow((z+0.5)*GRID_CELL_SIZE-pos.z,2);
                    influenceCell(i, x, z, distance);
                }
            }
        }
        for (int x = 0; x < GRID_SIZE; x++){
            for (int z = 0; z < GRID_SIZE; z++){
            	//System.out.println(indexVectors[x][z].x + " " + indexVectors[x][z].y + " " + indexVectors[x][z].z + " " + indexVectors[x][z].w );
            }
        }
    }
    
    //this is a horrible piece of code, change from vectors to arrays please
    private void influenceCell(int lightIndex, int x, int y, float distance){
        if (x < 0 || y < 0 || x >= GRID_SIZE || y >= GRID_SIZE){
            return;
        }
        if (indexStrength[x][y].w > distance){
            if (indexStrength[x][y].z > distance){
                if (indexStrength[x][y].y > distance){
                    if (indexStrength[x][y].x > distance){
                        indexVectors[x][y].w = indexVectors[x][y].z;
                        indexStrength[x][y].w = indexStrength[x][y].z;
                        indexVectors[x][y].z = indexVectors[x][y].y;
                        indexStrength[x][y].z = indexStrength[x][y].y;
                        indexVectors[x][y].y = indexVectors[x][y].x;
                        indexStrength[x][y].y = indexStrength[x][y].x;
                        indexVectors[x][y].x = lightIndex;
                        indexStrength[x][y].x = distance;
                    }else{
                        indexVectors[x][y].w = indexVectors[x][y].z;
                        indexStrength[x][y].w = indexStrength[x][y].z;
                        indexVectors[x][y].z = indexVectors[x][y].y;
                        indexStrength[x][y].z = indexStrength[x][y].y;
                        indexVectors[x][y].y = lightIndex;
                        indexStrength[x][y].y = distance;
                    }
                }else{
                    indexVectors[x][y].w = indexVectors[x][y].z;
                    indexStrength[x][y].w = indexStrength[x][y].z;
                    indexVectors[x][y].z = lightIndex;
                    indexStrength[x][y].z = distance;
                }
            }else{
                indexVectors[x][y].w = lightIndex;
                indexStrength[x][y].w = distance;
            }
        }else{
            //light too weak, ignore it
        }
    }
    
    private void writeIndex(){
        glBindBufferBase(GL_UNIFORM_BUFFER, indexBindingPoint, 0);//unbind for edit
        indexFloats.clear();
        for (int j = 0; j < indexVectors[0].length; j++){
            for (int i = 0; i < indexVectors.length; i++){
                indexFloats.put(indexVectors[i][j].x).put(indexVectors[i][j].y).put(indexVectors[i][j].z).put(indexVectors[i][j].w);
            }
        }
        indexFloats.flip();
        ARBVertexBufferObject.glBindBufferARB(GL_UNIFORM_BUFFER, indexBuffer[flipflop]);
        ARBVertexBufferObject.glBufferSubDataARB(GL_UNIFORM_BUFFER, 0, indexFloats);
    }
    
    private void writeLightsToBuffer(){
        lightFloats.clear();
        for (int i = 0; i < lights.size(); i++){
            lightFloats.put(lights.get(i).toFloat());
        }
        lightFloats.flip();
        glBindBufferBase(GL_UNIFORM_BUFFER, lightBindingPoint, 0);//unbind for edit
        ARBVertexBufferObject.glBindBufferARB(GL_UNIFORM_BUFFER, lightBuffer[flipflop]);
        ARBVertexBufferObject.glBufferSubDataARB(GL_UNIFORM_BUFFER, 0, lightFloats);
    }
}
