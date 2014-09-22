/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core.shadows;

import view.renderer3D.core.lighting.Light;


/**
 *
 * @author Vouwfietsman
 */
public class Shadow {
    private boolean isDirty = true;
    private int x;
    private int y;
    private Light source;
    private float shadowFactor;//produces smooth transition when shadows are loaded
    
    public Shadow(Light l, int x, int y){
    	this.source = l;
        this.x = x;
        this.y = y;
        shadowFactor = 1;
    }

    public float getShadowFactor(){
        if (shadowFactor > 0){
            shadowFactor -= 0.01;
        }
        return shadowFactor;
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
    
    public int getX(){
        return x * ShadowManager.getShadowPartResolution();
    }
    
    public int getY(){
        return y * ShadowManager.getShadowPartResolution();
    }
    
    public int getIX(){
        return x;
    }
    
    public int getIY(){
        return y;
    }
    
    public int getFarX(){
        return (x + 1) * ShadowManager.getShadowPartResolution();
    }
    public int getFarY(){
        return (y + 1) * ShadowManager.getShadowPartResolution();
    }
    
    
    public boolean isDirty(){
        return isDirty;
    }

    public Light getLight(){
    	return source;
    }
}
