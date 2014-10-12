package view.renderer3D.particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.ShaderObject;

public class ColoredParticle extends Particle {
	Gradient gradient;
	
	public ColoredParticle(Vector3f initialLocation, Vector3f translationSpeed, Matrix4f transformationSpeed, Vector3f force, Drawable3D type, int lifespan, Gradient gradient) {
		super(initialLocation, translationSpeed, transformationSpeed, force, type, lifespan);
		this.gradient = gradient;
	}
	
	@Override
	public void draw(ShaderObject shader) {
		shader.putMat4("modelMatrix", bufferedModelMatrix);
		float colorT = timeAlive / (float) lifespan;
		shader.putUnifFloat4("color", gradient.getApproximateColor(colorT));
		type.draw(shader);
	}
}
