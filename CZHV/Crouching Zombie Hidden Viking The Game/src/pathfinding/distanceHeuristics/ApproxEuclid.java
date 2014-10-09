package pathfinding.distanceHeuristics;

public class ApproxEuclid implements DistanceHeuristic {

	@Override
	public float calculateValue(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float number = dx*dx + dy*dy;
		float xhalf = 0.5f*number;
	    int i = Float.floatToIntBits(number);
	    i = 0x5f3759df - (i>>1);
	    number = Float.intBitsToFloat(i);
	    number = number*(1.5f - xhalf*number*number);
	    return 1/number;
	}

}
