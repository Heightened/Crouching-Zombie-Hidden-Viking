package view.renderer3D.leveleditor.objtypes;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class LVLEditorLight extends LVLEditorObject{
	public float radius;
	public float cutoff;
	public Vector3f color;
	public Vector3f specularColor;
	public Vector3f direction;
	
	public LVLEditorLight(){
		super("Light", new Vector3f(1,0.9f,0.2f));
		color = new Vector3f(5,5,5);
		direction = new Vector3f();
	}
	
	@Override
	public LVLEditorObject getInstance(){
		LVLEditorLight l = new LVLEditorLight();
		return l;
	}
	
	@Override
	public ArrayList<String> getHiddenVariables(){
		ArrayList<String> l = super.getHiddenVariables();
		l.add("direction1");
		return l;
	}
}
