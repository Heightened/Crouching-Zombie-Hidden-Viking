package view.renderer3D.core;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.Animation;
import view.renderer3D.Model;

public class Dummy3DObj {
	//private static Model mesh;
	private static Animation mesh;
	private Vector4f position;
	private Matrix4f modelMat;
	private Vector3f rotation;
	private FloatBuffer modelMatrix;
	private Vector4f screenPos;
	private float scale = 0.035f;
	private boolean selected = false;
	
	public Dummy3DObj(Vector4f position, Vector3f rotation){
		modelMatrix = BufferUtils.createFloatBuffer(16);
		this.rotation = rotation;
		modelMat = new Matrix4f();
		screenPos = new Vector4f();
		this.position = position;
		if (mesh == null){
			//mesh = new Model("tricube.obj");
			mesh = new Animation(120, "Animation/Cube/cube");
		}
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setPosition(float x, float y, float z){
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public void calcScreenSpace(Matrix4f mvp){
		Matrix4f.transform(mvp, position, screenPos);
		screenPos.x /= screenPos.w;//divide by W for perspective (normally the GC does this for you)
		screenPos.y /= screenPos.w;
		//translate to absolute window coordinates to comply with LWJGL.Mouse
		screenPos.x = (screenPos.x + 1)/2*Renderer3D.screenSize.width;
		screenPos.y = (screenPos.y + 1)/2*Renderer3D.screenSize.height;
	}
	
	final float radius = 30f;
	public void select(Vector2f mousePos){
		Vector2f temp = new Vector2f( mousePos.x - screenPos.x,mousePos.y - screenPos.y);
		if (temp.length() < radius){
			selected = true;
		}else{
			selected = false;
		}
	}
	
	public void selectRect(Vector2f startpos, Vector2f endpos){
		if (screenPos.x > startpos.x && screenPos.y < startpos.y && screenPos.x < endpos.x && screenPos.y > endpos.y){
			selected = true;
		}else{
			selected = false;
		}
	}
	
	public void calcModelMatrix(){
		MatrixCZHV.getModelMatrix(new Vector3f(position.x, position.y, position.z), new Vector3f(scale, scale, scale), rotation, modelMat);
		MatrixCZHV.MatrixToBuffer(modelMat, modelMatrix);
	}
	
	float animationTime = 0;
	
	public void update() {
		animationTime += 0.005f ;
		animationTime %= 1.0f;
		mesh.setTime(animationTime);
	}
	
	public void draw(ShaderObject shader){
		calcModelMatrix();
		shader.putMat4("modelMatrix", modelMatrix);
		
		mesh.draw(shader);
		/*
        mesh.bind();
        mesh.prepareForDraw(shader);
        mesh.draw();
        mesh.unbind();*/
	}
	
	public void putVertex(FloatBuffer buffer, float x, float y, float z){
		buffer.put(x).put(y).put(z);
		buffer.put(0).put(1).put(0);
		buffer.put(x/scale).put(z/scale);
	}
}
