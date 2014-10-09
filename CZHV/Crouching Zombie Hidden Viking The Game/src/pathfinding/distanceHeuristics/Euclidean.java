package pathfinding.distanceHeuristics;

public class Euclidean implements DistanceHeuristic {

	@Override
	public float calculateValue(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return (float) Math.sqrt( dx*dx + dy*dy);
	}

}
