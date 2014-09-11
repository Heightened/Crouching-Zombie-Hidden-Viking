
public class Vector2f {
	public float x;
	public float y;
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getLength(){
		return (float)(Math.sqrt(Math.pow(x,2) + Math.pow(y,2)));
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
