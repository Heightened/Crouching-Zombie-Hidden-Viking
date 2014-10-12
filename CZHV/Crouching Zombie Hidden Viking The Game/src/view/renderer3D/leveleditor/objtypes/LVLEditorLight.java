package view.renderer3D.leveleditor.objtypes;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class LVLEditorLight extends LVLEditorObject{
	private float radius;
	private float cutoff;
	private Vector3f color;
	private Vector3f specularColor;
	private Vector3f direction;
	private Vector3f direction1;
	
	public LVLEditorLight(){
		super("Light");
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
