package view.renderer3D.leveleditor.objtypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Dummy3DObj;
import view.renderer3D.leveleditor.xml.FakeClass;
import view.renderer3D.leveleditor.xml.GenericSerializeInterface;
import view.renderer3D.leveleditor.xml.XMLSerializeInterface;

public class LVLEditorObject extends Dummy3DObj implements XMLSerializeInterface{
	public String name;
	
	private XMLSerializeInterface xmlInterface;

	public LVLEditorObject(String name, Vector3f editorColor){
		this.name = name;
		this.editorColor = editorColor;
		xmlInterface = new GenericSerializeInterface();//if only we had java 1.8 QQQQQQQQQ moltipol inharitince bleeeeez
		xmlInterface.addImplementor(Dummy3DObj.class, this);
	}

	public void writeToInterface(JList variableList){
		DefaultListModel listModel = (DefaultListModel) variableList.getModel();
		listModel.removeAllElements();
		HashMap<String, Field> fields = getAllFields();
		try{
			for (String s : fields.keySet()){
				Field field = fields.get(s);
				Object value = field.get(this);
				listModel.addElement(s + ":" + varToString(value));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public LVLEditorObject getInstance(){
		return null;
	}
	
	ArrayList<String> hiddenVariables;
	@Override
	public ArrayList<String> getHiddenVariables(){
		if (hiddenVariables == null){
			hiddenVariables = new ArrayList<>();
			hiddenVariables.add("implementors");
			hiddenVariables.add("hiddenVariables");
			hiddenVariables.add("additionalVariables");
			hiddenVariables.add("xmlInterface");
			hiddenVariables.add("editorColor");
		}
		return hiddenVariables;
	}
	
	private Vector3f editorColor;
	public Vector3f getEditorColor(){
		return editorColor;
	}
	
	ArrayList<String> additionalVariables;
	@Override
	public ArrayList<String> getAdditionalVariables(){
		if (additionalVariables == null){
			additionalVariables = new ArrayList<>();
			additionalVariables.add("position");
			additionalVariables.add("rotation");
		}
		return additionalVariables;
	}

	
	private ArrayList<FakeClass> implementors = new ArrayList<>();
	@Override
	public ArrayList<FakeClass> getImplementors() {
		return implementors;
	}

	@Override
	public void addImplementor(Class endClass, XMLSerializeInterface implementor) {
		xmlInterface.addImplementor(endClass, implementor);
	}

	@Override
	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException,
			IllegalArgumentException, IllegalAccessException {
		xmlInterface.writeToXML(writer);
		
	}

	@Override
	public String getVarAsString(String varname) {
		return xmlInterface.getVarAsString(varname);

	}

	@Override
	public void setVarWithString(String var, String input) {
		xmlInterface.setVarWithString(var, input);
		
	}

	@Override
	public HashMap<String, Field> getAllFields() {
		return xmlInterface.getAllFields();
	}

	@Override
	public String varToString(Object o) {
		return xmlInterface.varToString(o);
	}

	

}
