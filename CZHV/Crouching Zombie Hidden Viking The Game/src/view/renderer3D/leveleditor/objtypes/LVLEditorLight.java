package view.renderer3D.leveleditor.objtypes;

import java.lang.reflect.Field;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.lwjgl.util.vector.Vector3f;

public class LVLEditorLight extends LVLEditorObject{
	private float radius;
	private float cutoff;
	private Vector3f color;
	private Vector3f specularColor;
	private Vector3f direction;
	
	public LVLEditorLight(){
		super(LVLEditorLight.class, "Light", LVLEditorObject.LIGHT);
		color = new Vector3f(5,5,5);
		direction = new Vector3f();
	}
	
	public void putInMap(){
		
	}
	
	public LVLEditorObject getInstance(){
		LVLEditorLight l = new LVLEditorLight();
		return l;
	}
}
