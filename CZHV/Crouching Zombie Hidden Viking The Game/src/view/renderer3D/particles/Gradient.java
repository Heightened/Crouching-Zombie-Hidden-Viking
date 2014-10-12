package view.renderer3D.particles;

import org.lwjgl.util.vector.Vector4f;

public class Gradient {	
	private class Anchor extends Vector4f {
		float t; // [0, 1]
		
		public Anchor(float t, float r, float g, float b, float a) {
			this.t = t;
			this.x = r;
			this.y = g;
			this.z = b;
			this.w = a;
		}
		
		public Vector4f getScaled(float scale) {
			return new Vector4f(scale * x, scale * y, scale * z, scale * w);
		}
	}
	
	Anchor[] anchors;
	int approximationPrecision = 1000;
	Vector4f[] colorLookup;
	
	public Gradient(float[] colors) {
		if (colors.length % 5 != 0) {
			//throw new Exception();
		}
		
		anchors = new Anchor[colors.length / 5];
		
		for (int i = 0; i < anchors.length; i++) {
			int off = i * 5;
			anchors[i] = new Anchor(colors[off], colors[off + 1], colors[off + 2], colors[off + 3], colors[off + 4]);
		}
		
		colorLookup = new Vector4f[approximationPrecision];
		
		for (int i = 0; i < colorLookup.length; i++) {
			colorLookup[i] = getColor(i / (float) approximationPrecision);
			System.out.println(i + ": " + colorLookup[i]);
		}
	}
	
	private Vector4f mix(float t, Anchor a, Anchor b) {
		return Vector4f.add(a.getScaled(1 - t), b.getScaled(t), null);
	}
	
	public Vector4f getColor(float t) {
		Anchor a = new Anchor(0, 0, 0, 0, 0);
		Anchor b = new Anchor(1, 0, 0, 0, 0);
		for (int i = 0; i < anchors.length; i++) {
			float anchorT = anchors[i].t;
			if (anchorT <= t) {
				if (anchorT >= a.t) {
					a = anchors[i];
				}
			} else {
				if (anchorT <= b.t) {
					b = anchors[i];
				}
			}
		}
		
		System.out.println("+++\n" + a.t + ": " + a + "\n" + b.t + ": " + b);
		
		return mix((t - a.t) / (b.t - a.t), a, b);
	}
	
	public Vector4f getApproximateColor(float t) {
		return colorLookup[(int) (t * (approximationPrecision - 1))];
	}
}
