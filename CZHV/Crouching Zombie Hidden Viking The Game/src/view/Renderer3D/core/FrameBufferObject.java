/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @author Vouwfietsman
 */
public class FrameBufferObject {
    private int width;
    private int height;
    private int bufferID;
    private String name;
    private List<Integer> attachments;
    private boolean bound = false;
    private boolean[] colormask;
    
    public FrameBufferObject(String name, int width, int height){
        this.width = width;
        this.height = height;
        this.name = name;
        attachments = new ArrayList<>();
    	colormask = new boolean[4];
    	colormask[0] = true;
    	colormask[1] = true;
    	colormask[2] = true;
    	colormask[3] = true;
    }
    
    public void setColorMask(boolean r, boolean g, boolean b, boolean a){
    	colormask[0] = r;
    	colormask[1] = g;
    	colormask[2] = b;
    	colormask[3] = a;
    }
    
    public void setup(){
        bufferID = glGenFramebuffers();
        bind();
    }
    
    public void addAttachment(int attachment){
        classInv();
        attachments.add( attachment);
    }
    
    public void done(){
        classInv();
        if( !attachments.isEmpty()){
            IntBuffer DrawBufferTable = BufferUtils.createIntBuffer(attachments.size());
            for (int i = 0; i < attachments.size(); i++){
                DrawBufferTable.put( attachments.get(i));
            }
            DrawBufferTable.rewind();
            
            glDrawBuffers(DrawBufferTable);
            
            DrawBufferTable = null;//free table object
        }else{
            glDrawBuffer(GL_NONE);
        }
        checkOK();
        unBind();
    }
    
    public void bind(){
        if (bound){
            System.err.println(this + " ALREADY BOUND");
            TOOLBOX.printStackTraceFromHere();
            System.exit(0);
        }
        glColorMask(colormask[0], colormask[1], colormask[2], colormask[3]);
        bound = true;
        glBindFramebuffer( GL_FRAMEBUFFER, bufferID);
    }
    
    public void unBind(){
        bound = false;
        glColorMask(true,true,true,true);
        glBindFramebuffer( GL_FRAMEBUFFER, 0);
    }
    
    public void addTexture(TextureObject t, int attachment){
        classInv();
        if (attachment != GL_DEPTH_ATTACHMENT){
            attachments.add(attachment);
        }
        glFramebufferTexture2D(GL_FRAMEBUFFER, attachment,GL_TEXTURE_2D, t.getTextureID(), 0);
        TOOLBOX.checkGLERROR();
    }
    
    private void checkOK(){
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            System.err.println(this + " FRAMEBUFFER NOT COMPLETE");
            TOOLBOX.printStackTraceFromHere();
            System.exit(0);
        }
    }
    
    private void classInv(){
        if (!bound){
            System.err.println("OPERATING ON " + this + " WHILE NOT BOUND");
            TOOLBOX.printStackTraceFromHere();
            System.exit(0);
        }
    }
    
    @Override
    public String toString(){
        return name + " " + bufferID + " " + width + " " + height;
    }
}
