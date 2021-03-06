package view.renderer3D.core;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.resources.Animation;
import view.renderer3D.core.resources.Resource;

public class Dummy3DObj {
	private static Animation mesh;
	protected Vector4f position;
	private Matrix4f modelMat;
	protected Vector3f rotation;
	private FloatBuffer modelMatrix;
	private Vector4f screenPos;
	private float scale = 0.035f;
	private boolean selected = false;
	
	public Dummy3DObj(){
		modelMatrix = BufferUtils.createFloatBuffer(16);
		this.rotation = new Vector3f();
		modelMat = new Matrix4f();
		screenPos = new Vector4f();
		this.position = new Vector4f(Renderer3D.cellSize*0, 0, Renderer3D.cellSize*0,1);
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setPosition(float x, float y, float z){
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	public Vector4f getPosition(){
		return position;
	}
	
	public Vector3f getRotation(){
		return rotation;
	}

	
	public void setRotation(float x, float y, float z){
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}
	
	public Vector4f calcScreenSpace(Matrix4f mvp){
		Matrix4f.transform(mvp, position, screenPos);
		screenPos.x /= screenPos.w;//divide by W for perspective (normally the GC does this for you)
		screenPos.y /= screenPos.w;
		//translate to absolute window coordinates to comply with LWJGL.Mouse
		screenPos.x = (screenPos.x + 1)/2*Renderer3D.screenSize.width;
		screenPos.y = (screenPos.y + 1)/2*Renderer3D.screenSize.height;
		return screenPos;
	}
	
	public Vector4f calcScreenSpace(float height, Matrix4f mvp){
		Matrix4f.transform(mvp, new Vector4f(position.x, position.y+height, position.z, 1), screenPos);
		screenPos.x /= screenPos.w;//divide by W for perspective (normally the GC does this for you)
		screenPos.y /= screenPos.w;
		//translate to absolute window coordinates to comply with LWJGL.Mouse
		return screenPos;
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
		if (mesh == null){
			//mesh = new Animation(120, "Animation/Cube/cube");
		}
		animationTime += 0.005f ;
		animationTime %= 1.0f;
		//mesh.setTime(animationTime);
	}
	
	private float scalex = scale;
	private float scaley = scale*7;
	private float scalez = scale;
	
	public ArrayList<Vector3f> drawBoundingBox(){
		ArrayList<Vector3f> bb = new ArrayList<>();
		//top
		bb.add(new Vector3f(position.x - scalex,position.y + scaley,position.z - scalez));
		bb.add(new Vector3f(position.x + scalex,position.y + scaley,position.z - scalez));

		bb.add(new Vector3f(position.x - scalex,position.y + scaley,position.z - scalez));
		bb.add(new Vector3f(position.x - scalex,position.y + scaley,position.z + scalez));

		bb.add(new Vector3f(position.x + scalex,position.y + scaley,position.z + scalez));
		bb.add(new Vector3f(position.x + scalex,position.y + scaley,position.z - scalez));

		bb.add(new Vector3f(position.x + scalex,position.y + scaley,position.z + scalez));
		bb.add(new Vector3f(position.x - scalex,position.y + scaley,position.z + scalez));
		
		//bottom
		bb.add(new Vector3f(position.x - scalex,position.y + scaley,position.z - scalez));
		bb.add(new Vector3f(position.x - scalex,position.y,position.z - scalez));

		bb.add(new Vector3f(position.x - scalex,position.y + scaley,position.z + scalez));
		bb.add(new Vector3f(position.x - scalex,position.y,position.z + scalez));

		bb.add(new Vector3f(position.x + scalex,position.y + scaley,position.z - scalez));
		bb.add(new Vector3f(position.x + scalex,position.y,position.z - scalez));

		bb.add(new Vector3f(position.x + scalex,position.y + scaley,position.z + scalez));
		bb.add(new Vector3f(position.x + scalex,position.y,position.z + scalez));
		
		return bb;
	}
	
	public boolean checkAABB(Line3D ray){
		Vector3f top = ray.collideXZPlane(this.position.y-scaley);
		Vector3f bottom = ray.collideXZPlane(this.position.y+scaley);
		Vector3f front = ray.collideYZPlane(this.position.x-scalex);
		Vector3f back = ray.collideYZPlane(this.position.x+scalex);
		Vector3f left = ray.collideXYPlane(this.position.z-scalez);
		Vector3f right = ray.collideXYPlane(this.position.z+scalez);
		if (top != null && inBetween(top.x, this.position.x, scalex) && inBetween(top.z, this.position.z, scalez)) return true;
		if (bottom != null && inBetween(bottom.x, this.position.x, scalex) && inBetween(bottom.z, this.position.z, scalez)) return true;

		if (front != null && inBetween(front.y, this.position.y, scaley) && inBetween(front.z, this.position.z, scalez)) return true;
		if (back != null && inBetween(back.y, this.position.y, scaley) && inBetween(back.z, this.position.z, scalez)) return true;

		if (left != null && inBetween(left.x, this.position.x, scalex) && inBetween(left.y, this.position.y, scaley)) return true;
		if (right != null && inBetween(right.x, this.position.x, scalex) && inBetween(right.y, this.position.y, scaley)) return true;
		
		return false;
	}
	
	public boolean inBetween(float source, float pos, float scale){
		if (source >= pos - scale && source <= pos + scale){
			return true;
		}
		return false;
	}
	
	public void draw(ShaderObject shader){
		calcModelMatrix();
		shader.putMat4("modelMatrix", modelMatrix);
		
		shader.bindTexture("texture", Resource.vikingTexture);
		Resource.viking.draw(shader);
		//mesh.draw(shader);
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
