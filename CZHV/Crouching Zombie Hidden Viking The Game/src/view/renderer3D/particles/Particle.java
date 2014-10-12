package view.renderer3D.particles;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.MatrixCZHV;
import view.renderer3D.core.ShaderObject;

public class Particle {
	Matrix4f modelMatrix, translationMatrix, transformationMatrix;
	Matrix4f transformationSpeed;
	Vector3f translationSpeed, force;
	Drawable3D type;
	int lifespan, timeAlive;
	
	FloatBuffer bufferedModelMatrix;
	
	public Particle(Vector3f initialLocation, Vector3f translationSpeed, Matrix4f transformationSpeed, Vector3f force, Drawable3D type, int lifespan) {
		this.modelMatrix = new Matrix4f();
		this.translationMatrix = new Matrix4f();
		Matrix4f.translate(initialLocation, this.translationMatrix, this.translationMatrix);
		this.transformationMatrix = new Matrix4f();
		this.translationSpeed = new Vector3f(translationSpeed);
		this.transformationSpeed = transformationSpeed;
		this.force = force;
		this.type = type;
		this.lifespan = lifespan;
		
		bufferedModelMatrix = BufferUtils.createFloatBuffer(16);
	}
	
	public boolean isAlive() {
		return (timeAlive < lifespan);
	}
	
	Matrix4f identity4f = new Matrix4f();
	
	public void update() {
		//Accelerate in force direction
		//Matrix4f.translate(force, translationSpeed, translationSpeed);
		Vector3f.add(force, translationSpeed, translationSpeed);
		//Apply translation
		//Matrix4f.mul(translationSpeed, translationMatrix, translationMatrix);
		Matrix4f.translate(translationSpeed, translationMatrix, translationMatrix);
		//Apply rotation
		Matrix4f.mul(transformationSpeed, transformationMatrix, transformationMatrix);
		//Calculate modelMatrix
		Matrix4f.mul(translationMatrix, transformationMatrix, modelMatrix);
		
		MatrixCZHV.MatrixToBuffer(modelMatrix, bufferedModelMatrix);

		timeAlive++;
	}
	
	public void draw(ShaderObject shader) {
		shader.putMat4("modelMatrix", bufferedModelMatrix);
//		float color = 1 - (timeAlive / (float) lifespan);
//		shader.putUnifFloat4("color", new Vector4f(color + 0.1f,color / 3f,0,1));
		type.draw(shader);
	}
}