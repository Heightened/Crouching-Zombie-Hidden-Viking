package view.renderer3D.particles;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.ShaderObject;

public class ParticleTest {
	Emitter test;
	
	public ParticleTest() {
		//test = new DirectionalEmitter(10000, 10, 50, 50, new Drawable3D[] {new TriangleParticle(0.01f)}, new Vector3f(1,0.1f,1), new Vector3f(0,0.001f,0), new Vector3f(0,0.00001f,0), 0.8f, 0.05f);
		float[] anchorArray = new float[] {
			0.0f, 1, 1, 1, 1, 
			0.5f, 1, 1, 0, 1, 
			1.0f, 1, 0, 0, 1/*,
			1.0f, 0.2f, 0.2f, 0.2f, 1*/
		};
		Gradient gradient = new Gradient(anchorArray);
		test = new ColoredDirectionalEmitter(2200, 20, 10, 100, new Drawable3D[] {new TriangleParticle(0.01f)}, new Vector3f(1,0.1f,1), new Vector3f(0,0.001f,0), new Vector3f(0,0.00002f,0), 0.8f, 0.05f, gradient);
		//test = new DirectionalEmitter(10000, 10, 50, 50, new Drawable3D[] {new TriangleParticle(0.01f)}, new Vector3f(1,0.1f,1), new Vector3f(0,0.001f,0), new Vector3f(0,0.00001f,0), 0.8f, 0.05f);
	}
	
	public void update(ShaderObject shader) {
		test.emit();
		test.updateDraw(shader);
	}
}
