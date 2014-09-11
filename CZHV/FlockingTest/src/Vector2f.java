
public class Vector2f {
	public float x;
	public float y;
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	//fast SQRT APPROX, from quake C code. link: http://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
	final float threehalfs = 1.5F;
	public float getLength(){
		float number = x*x + y*y;
		float xhalf = 0.5f*number;
	    int i = Float.floatToIntBits(number);
	    i = 0x5f3759df - (i>>1);
	    number = Float.intBitsToFloat(i);
	    number = number*(1.5f - xhalf*number*number);
	    return 1/number;
	}
	
	public void scale(float s){
		x*=s;
		y*=s;
	}
	
	public void normalize(){
		float len = getLength();
		x *= 1/len;
		y *= 1/len;
	}
	
	public void truncate(float max){
		float len = getLength();
        if (len > max)
        {
            normalize();
            scale(max);
        }
	}
	
	public void invert(){
		x = -x;
		y = -y;
	}
}
