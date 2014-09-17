package view.renderer3D.core;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixCZHV {
	public static Matrix4f getModelMatrix(Vector3f trans, Vector3f scale, Vector3f rot, Matrix4f destination){
		Matrix4f.mul(getTranslationMatrix(trans, scale),getRotationMatrix(rot),destination);
		return destination;
	}
	
	public static Matrix4f getModelMatrix(Matrix4f transscale, Matrix4f rotation, Matrix4f destination){
		Matrix4f.mul(transscale, rotation,destination);
		return destination;
	}
	
	public static Matrix4f getModelMatrix(Matrix4f trans, Matrix4f scale, Matrix4f rotation, Matrix4f destination){
        Matrix4f.mul(rotation, scale, destination);
        Matrix4f.mul(trans,destination,destination);
        return destination;
	}
	
	public static FloatBuffer MatrixToBuffer(Matrix4f source, FloatBuffer destination){
		destination.rewind();
		source.store(destination);
		destination.flip();
		return destination;
	}

	private static Matrix4f temprot = new Matrix4f();
	public static Matrix4f getRotationMatrix(Vector3f rot){
		float x = rot.x;
		float y = rot.y;
		float z = rot.z;
		temprot.m00 = cos(y)*cos(z);
		temprot.m01 = -cos(x)*sin(z)+sin(x)*sin(y)*cos(z);
		temprot.m02 = sin(x)*sin(z)+cos(x)*sin(y)*cos(z);
		temprot.m03 = 0.0f;
		
		temprot.m10 = cos(y)*sin(z);
		temprot.m11 = cos(x)*cos(z)+sin(x)*sin(y)*sin(z);
		temprot.m12 = -sin(x)*cos(z)+cos(x)*sin(y)*sin(z);
		temprot.m23 = 0.0f;
		
		temprot.m20 = -sin(y);
		temprot.m21 = sin(x)*cos(y);
		temprot.m22 = cos(x)*cos(y);
		temprot.m33 = 0.0f;
		
		temprot.m30 = 0.0f;
		temprot.m31 = 0.0f;
		temprot.m32 = 0.0f;
		temprot.m33 = 1.0f;
		return temprot;
	}

	private static Matrix4f temptrans = new Matrix4f();
	public static Matrix4f getTranslationMatrix(Vector3f trans, Vector3f scale){
		tempscale.m00 = scale.x;
		tempscale.m11 = scale.y;
		tempscale.m22 = scale.z;
		temptrans.m33 = 1.0f;
		
		temptrans.m30 = trans.x;
		temptrans.m31 = trans.y;
		temptrans.m32 = trans.z;
		return temptrans;
	}

	private static Matrix4f tempscale = new Matrix4f();
	public static Matrix4f getScaleMatrix(Vector3f scale){
		tempscale.m00 = scale.x;
		tempscale.m11 = scale.y;
		tempscale.m22 = scale.z;
		tempscale.m33 = 1.0f;
		return tempscale;
	}
	
	 public static float cos(float degree){
	        return (float)Math.cos(Math.toRadians(degree));
	    }
	    public static float sin(float degree){
	        return (float)Math.sin(Math.toRadians(degree));
	    }
}
