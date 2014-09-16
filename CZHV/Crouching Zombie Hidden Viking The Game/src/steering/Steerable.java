package steering;

public interface Steerable {
	public Vector getVelocity();
	public float getMaxVelocity();
	public Vector getPosition();
	public float getMass();
	public void setSteer(Vector steer);
}
