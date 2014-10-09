package pathfinding.distanceHeuristics;

public class Manhattan implements DistanceHeuristic {

	@Override
	public float calculateValue(float x1, float y1, float x2, float y2) {
		float dx = Math.abs(x1 - x2);
		float dy = Math.abs(y1 - y2);
		return dx + dy;
	}

}
