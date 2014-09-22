/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core.shadows;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import view.renderer3D.core.FrameBufferObject;
import view.renderer3D.core.TextureObject;
import view.renderer3D.core.lighting.Light;

/**
 *
 * @author Vouwfietsman
 */
public class ShadowManager {
    private TextureObject shadowDepthTexture;
    private FrameBufferObject shadowFBO;
    private static final int shadowMapResolution = 2048;
    private static final int shadowPartResolution = 512;
    
    private int numParts = -1;
    private Shadow[][] shadowList;
    private FloatBuffer biasMatrix;
    
    public ShadowManager(){
        numParts = (int)(shadowMapResolution/shadowPartResolution);
        shadowList = new Shadow[numParts][numParts];
        biasMatrix = BufferUtils.createFloatBuffer(16);
        float[] biasarray = {0.5f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f};//goed, niet veranderen
        biasMatrix.put(biasarray);
    }
    
    public void init(){
        shadowFBO = new FrameBufferObject("shadow fbo", shadowMapResolution, shadowMapResolution);
        shadowFBO.bind();
        
        // Depth texture
        shadowDepthTexture = new TextureObject("shadow depth texture", shadowMapResolution, shadowMapResolution, GL30.GL_DEPTH_COMPONENT32F, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT);
        shadowDepthTexture.setup();
        shadowDepthTexture.setMINMAG(GL_LINEAR);
        shadowDepthTexture.setWRAPST(GL_CLAMP);
        shadowDepthTexture.unbind();
        

        shadowFBO.setup();
        shadowFBO.setColorMask(false, false, false, false);
        shadowFBO.addTexture(shadowDepthTexture, GL_DEPTH_ATTACHMENT);
        shadowFBO.done();
    }
    
    public void removeShadow(int x, int y){
        shadowList[x][y] = null;
    }
    
    public void removeShadow(Shadow s){
        for (int i = 0; i < shadowList.length; i++){
            for (int j = 0; j < shadowList[0].length; j++){
                if (shadowList[i][j] != null && shadowList[i][j].equals(s)){
                    shadowList[i][j] = null;
                    return;
                }
            }
        }
    }
    
    public Shadow getShadow(Light l){
        for (int i = 0; i < shadowList.length; i++){
            for (int j = 0; j < shadowList[0].length; j++){
                if (shadowList[i][j] == null){
                    shadowList[i][j] = new Shadow(l, i, j);
                    return shadowList[i][j];
                }
            }
        }
        return null;//full, node is not accepted and wont have shadow
    }
    
    public void update(){
        int count = 0;
        int loaded = 0;
        for (int i = 0; i < shadowList.length; i++){
            for (int j = 0; j < shadowList[0].length; j++){
                if (shadowList[i][j] != null){
                    count++;
                    if (!shadowList[i][j].isDirty()){
                        //load the shadow   
                        updateShadow(shadowList[i][j]);
                        return;//load only one for performance reasons
                    }else{
                        loaded++;
                    }
                }
            }
        }
    }
    
    private void updateShadow(Shadow s){
        glViewport(s.getX(), s.getY(), shadowPartResolution, shadowPartResolution);//map vertices to new window
    //    glEnable(GL_SCISSOR_TEST);
        //scissor call likely unnecessary
     //  glScissor(s.getX(), s.getY(), shadowPartResolution, shadowPartResolution);//allow pixel writes only in this window
        
		shadowFBO.bind();
		glClear (GL_DEPTH_BUFFER_BIT);//may be unnecessary
		
		//render scene with view matrix for this shadow, disabling all shaders
		s.getLight().getViewMatrix();
        
       // glEnable(GL_CULL_FACE);
       // glCullFace(GL_BACK);//if back then all is white
     
		shadowFBO.unBind();
		s.setDirty(false);
    }

    public int getNumParts() {
        return numParts;
    }

    public TextureObject getShadowDepthTexture() {
        return shadowDepthTexture;
    }

    public Shadow[][] getShadowList() {
        return shadowList;
    }

    public static int getShadowMapResolution() {
        return shadowMapResolution;
    }


    public static int getShadowPartResolution() {
        return shadowPartResolution;
    }
    
    
}
