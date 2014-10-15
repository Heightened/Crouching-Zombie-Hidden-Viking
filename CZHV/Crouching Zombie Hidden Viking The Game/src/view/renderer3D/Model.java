package view.renderer3D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.ShaderObject;

public class Model {
	ArrayList<float[]> distinctVertices = null;
	ArrayList<float[]> distinctTextureCoords = null;
	ArrayList<float[]> distinctNormals = null;
	
	private class modelSegment extends Drawable3D {
		int size;
		ArrayList<String[]> faces = new ArrayList<String[]>();
		
		public modelSegment() {
			super();
		}
		
		private int[] getIntArray(int offset, String[] words) {
			int[] intArray = new int[words.length - offset];
			for (int i = 0; i < intArray.length; i++) {
				String word = words[i + offset];
				intArray[i] = (!word.isEmpty()) ? Integer.parseInt(word) : 1;
			}
			return intArray;
		}
		
		public void initializeSegment() {
			// Process faces
			float[] vertices = new float[size * 3];
			float[] uvcoords = new float[size * 2];
			float[] normals = new float[size * 3];
			float[] tangents = new float[size * 3];
			
			int i = 0;
			for (String[] face : faces) {
				//System.out.println(Arrays.toString(face));
				float[][] position = new float[3][];
				float[][] textureCoord = new float[3][];
				float[][] normal = new float[3][];
				for (int index = 1; index < 4; index++) {
					if (face[index].equals("f")) {
						continue;
					}
					
					int index1 = index - 1;
					
					int[] attributes = getIntArray(0, face[index].split("/"));
					//System.out.println(Arrays.toString(attributes));
					
					position[index1] = distinctVertices.get(attributes[0] - 1);
					//float[] textureCoord = distinctTextureCoords.get(attributes[1] - 1);
					textureCoord[index1] = new float[] {0.0f, 0.0f};
					normal[index1] = distinctNormals.get(attributes[2] - 1);
					
					vertices[i * 3] = position[index1][0];
					vertices[i * 3 + 1] = position[index1][1];
					vertices[i * 3 + 2] = position[index1][2];
					
					uvcoords[i * 2] = textureCoord[index1][0];
					uvcoords[i * 2 + 1] = textureCoord[index1][1];
					
					normals[i * 3] = normal[index1][0];
					normals[i * 3 + 1] = normal[index1][1];
					normals[i * 3 + 2] = normal[index1][2];
					
					i++;
				}
				float[] tangent = calcTangentNonIP2(position[0], position[1], position[2], normal[0], normal[1], normal[2], textureCoord[0], textureCoord[1], textureCoord[2]);

				i -= 3;
				tangents[i*3] = tangent[0];
				tangents[i*3 + 1] = tangent[1];
				tangents[i*3 + 2] = tangent[2];
				tangents[i*3] = tangent[0];
				tangents[i*3 + 1] = tangent[1];
				tangents[i*3 + 2] = tangent[2];
				tangents[i*3] = tangent[0];
				tangents[i*3 + 1] = tangent[1];
				tangents[i*3 + 2] = tangent[2];
				i += 3;
			}
			
			initialize(size, vertices, uvcoords, normals, tangents);
			
			// Delete reference to unused information
			
			faces = null;
		}
	}
	
	 public static float[] calcTangentNonIP2(float[] v1, float[] v2,float[] v3,float[] n1,float[] n2,float[] n3,float[] w1,float[] w2,float[] w3){
	       Vector3f v2v1 = new Vector3f();
	       Vector3f v3v1 = new Vector3f();
	       Vector3f tangent = new Vector3f();
	       Vector3f biTangent = new Vector3f();
	        //Calculate the vectors from the current vertex to the two other vertices in the triangle
	       v2v1.x = v2[0] - v1[0];
	       v2v1.y = v2[1] - v1[1];
	       v2v1.z = v2[2] - v1[2];
	       
	       v3v1.x = v3[0] - v1[0];
	       v3v1.y = v3[1] - v1[1];
	       v3v1.z = v3[2] - v1[2];
	      //  Vector3f.sub( v2, v1 , v2v1);
	       // Vector3f.sub( v3, v1 , v3v1);

	        // Calculate c2c1_T and c2c1_B
	        float c2c1_T = w2[0] - w1[0];
	        float c2c1_B = w2[1] - w1[1];

	        // Calculate c3c1_T and c3c1_B
	        float c3c1_T = w3[0] - w1[0];
	        float c3c1_B = w3[1] - w1[1];

	        float fDenominator = c2c1_T * c3c1_B - c3c1_T * c2c1_B;
	        float fScale1 = 1.0f / fDenominator;

	        tangent.x = ( c3c1_B * v2v1.x - c2c1_B * v3v1.x ) * fScale1;
	        tangent.y = ( c3c1_B * v2v1.y - c2c1_B * v3v1.y ) * fScale1;
	        tangent.z = ( c3c1_B * v2v1.z - c2c1_B * v3v1.z ) * fScale1;

	        biTangent.x = ( -c3c1_T * v2v1.x + c2c1_T * v3v1.x ) * fScale1;
	        biTangent.y = ( -c3c1_T * v2v1.y + c2c1_T * v3v1.y ) * fScale1;
	        biTangent.z = ( -c3c1_T * v2v1.z + c2c1_T * v3v1.z ) * fScale1;

	        float[] retFloat = new float[3];
	        retFloat[0] = tangent.x;
	        retFloat[1] = tangent.y;
	        retFloat[2] = tangent.z;
	        return retFloat;
	    }
	
	modelSegment[] model;
	
	public Model(Model original) {
		super();
		
		model = original.model;
	}
	
	private float[] getFloatArray(int offset, String[] words) {
		float[] floatArray = new float[words.length - offset];
		for (int i = 0; i < floatArray.length; i++) {
			floatArray[i] = Float.parseFloat(words[i + offset]);
		}
		return floatArray;
	}
	
	protected void finalizeVertexData() {
		distinctVertices = null;
		distinctTextureCoords = null;
		distinctNormals = null;
	}
	
	public Model(String objPath) {
		super();
		
		distinctVertices = new ArrayList<float[]>();
		distinctTextureCoords = new ArrayList<float[]>();
		distinctNormals = new ArrayList<float[]>();
		
		// Open .obj file
		
		BufferedReader objFile = null;;
		try {
			objFile = new BufferedReader(new FileReader(objPath));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open .obj file");
			e.printStackTrace();
		}
		
		// Read .obj file
		
		String line = "";
		String[] words = null;
		modelSegment segment = null;
		ArrayList<modelSegment> segments = new ArrayList<modelSegment>();
		
		try {
			while ((line = objFile.readLine()) != null) {
				words = line.split(" ");
				
				switch (words[0]) {
					case "o":
						segment = new modelSegment();
						segments.add(segment);
						break;
					case "v":
						distinctVertices.add(getFloatArray(1, words));
						break;
					case "vt":
						distinctTextureCoords.add(getFloatArray(1, words));
						break;
					case "vn":
						distinctNormals.add(getFloatArray(1, words));
						break;
					case "f":
						segment.size += (words.length - 1);
						segment.faces.add(words);
						break;
				}
			}
			
			objFile.close();
		} catch (IOException e) {
			System.out.println("Something went wrong while reading the .obj file");
			e.printStackTrace();
		}
		
		// Finalize model
		
		model = new modelSegment[segments.size()];
		for (int i = 0; i < model.length; i++) {
			model[i] = segments.get(i);
			model[i].initializeSegment();
		}
		
		// Final operations on temporary data
		
		finalizeVertexData();
	}
	
	public void draw(ShaderObject shader) {
		for (modelSegment s : model) {
			s.draw(shader);
		}
	}
}