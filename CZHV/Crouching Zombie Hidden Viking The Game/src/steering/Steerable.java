package steering;

public interface Steerable {
	public Vector3D getVelocity();
	public float getMaxVelocity();
	public Vector3D getPosition();
	public float getMass();
	public void setSteer(Vector3D steer);
}
