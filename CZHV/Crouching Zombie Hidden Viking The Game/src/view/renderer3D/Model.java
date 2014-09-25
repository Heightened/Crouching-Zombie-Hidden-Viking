package view.renderer3D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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
			
			int i = 0;
			for (String[] face : faces) {
				System.out.println(Arrays.toString(face));
				for (String vertex : face) {					
					if (vertex.equals("f")) {
						continue;
					}
					
					int[] attributes = getIntArray(0, vertex.split("/"));
					System.out.println(Arrays.toString(attributes));
					
					float[] position = distinctVertices.get(attributes[0] - 1);
					//float[] textureCoord = distinctTextureCoords.get(attributes[1] - 1);
					float[] textureCoord = new float[] {0.0f, 0.0f};
					float[] normal = distinctNormals.get(attributes[2] - 1);
					
					vertices[i * 3] = position[0];
					vertices[i * 3 + 1] = position[1];
					vertices[i * 3 + 2] = position[2];
					
					uvcoords[i * 2] = textureCoord[0];
					uvcoords[i * 2 + 1] = textureCoord[1];
					
					normals[i * 3] = normal[0];
					normals[i * 3 + 1] = normal[1];
					normals[i * 3 + 2] = normal[2];
					
					i++;
				}
			}
			
			initialize(size, vertices, uvcoords, normals);
			
			// Delete reference to unused information
			
			faces = null;
		}
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