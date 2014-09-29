package view.renderer3D.core;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.tempFlocking.Vehicle;

public class DEMOselecter {
	ArrayList<Vehicle> objList;
	public DEMOselecter(ArrayList<Vehicle> objList){
		this.objList = objList;
		startpos = new Vector2f();
		endpos = new Vector2f();
	}
	
	boolean startclick = false;
	Vector2f startpos;
	Vector2f endpos;
	public void update(Matrix4f mvp){
		if (!startclick && Mouse.isButtonDown(0)){
			startclick = true;
			startpos.x = Mouse.getX();
			startpos.y = Mouse.getY();
		}
		
		if (startclick && !Mouse.isButtonDown(0)){
			startclick = false;
			endpos.x = Mouse.getX();
			endpos.y = Mouse.getY();

			//swap coordinates for squares which dont start top-left
			Vector2f startposM = new Vector2f();
			Vector2f endposM = new Vector2f();
			startposM.x = (float)Math.min(endpos.x, startpos.x);
			startposM.y = (float)Math.max(endpos.y, startpos.y);
			
			endposM.x = (float)Math.max(endpos.x, startpos.x);
			endposM.y = (float)Math.min(endpos.y, startpos.y);
			
			for (Dummy3DObj obj : objList){
				obj.calcScreenSpace(mvp);
				obj.selectRect(startposM, endposM);
			}
		}
	}
	
	public void draw(ShaderObject quadShader, VBO quadVBO, Vector4f color){
		if (startclick){
			Vector2f startnorm = normalize(startpos);
			Vector2f endnorm = normalize(new Vector2f(Mouse.getX(), Mouse.getY()));
	        quadShader.putUnifFloat4("color", color);
	        quadShader.putUnifFloat4("quadSize", new Vector4f(startnorm.x, startnorm.y,endnorm.x - startnorm.x, endnorm.y - startnorm.y));
	        quadVBO.bind();
	        quadVBO.prepareForDraw(quadShader);
	        quadVBO.draw();
	        quadVBO.unbind();
		}
	}
	
	public Vector2f getNormalizedMouse(){
		Vector2f ret = normalize(new Vector2f(Mouse.getX(), Mouse.getY()));
		ret.y = - ret.y;
		return ret;
	}
	
	public Vector2f normalize(Vector2f in){
		return new Vector2f((in.x/Renderer3D.screenSize.width)*2-1, (in.y/Renderer3D.screenSize.height)*2-1);
	}
	
	public Vector2f normalize(float x, float y){
		return new Vector2f((x/Renderer3D.screenSize.width)*2-1, (y/Renderer3D.screenSize.height)*2-1);
	}
}
