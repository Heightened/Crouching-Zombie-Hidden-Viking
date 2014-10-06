package view.renderer3D.core;

public class HalfPrecisionFloat {
	private class SinglePrecisionFloat {
		float value;
		int sign, exponent, mantissa;
		int bits;
		
		public SinglePrecisionFloat(float singlePrecisionFloat) {
			value = singlePrecisionFloat;
			
			bits = Float.floatToRawIntBits(singlePrecisionFloat);
			
			sign = bits >>> 31;
			exponent = (bits >>> 23) & ((1 << 8) - 1); //255; //Integer.parseInt("011111111", 2);
			mantissa = bits & ((1 << 23) - 1);
		}
	}
	
	int sign, exponent, mantissa;
	int bits;
	byte[] bytes;
	
	public HalfPrecisionFloat(float singlePrecisionFloat) {
		SinglePrecisionFloat oldFloat = new SinglePrecisionFloat(singlePrecisionFloat);
		
		System.out.println("\nHalfPrecisionFloat created");
		sign = oldFloat.sign;
		exponent = (oldFloat.exponent == 0) ? 0 : oldFloat.exponent - 127 + 15; // 2 ^ (HalfPrecisionFloat.exponent - 15) = 2 ^(SinglePrecisionFloat.exponent - 127)
		mantissa = oldFloat.mantissa >>> (23 - 10);
		
//		System.out.println("--- Bytes ---");
//		System.out.println(oldFloat.value);
		
		bits = (sign << 15) | (exponent << 10) | (mantissa);
		
//		System.out.println(Integer.toBinaryString(bits));
		
		bytes = new byte[2];
		bytes[0] = (byte) ((bits & (((1 << 8) - 1) << 8)) >>> 8);
		bytes[1] = (byte) (bits & ((1 << 8) - 1));
		
//		System.out.println(Integer.toBinaryString(bytes[0]));
//		System.out.println(Integer.toBinaryString(bytes[1]));
	}
}
