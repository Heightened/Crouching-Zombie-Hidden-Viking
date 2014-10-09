package view.renderer3D.leveleditor.objtypes;

import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import view.renderer3D.core.Dummy3DObj;

public class LVLEditorObject extends Dummy3DObj{
	public String name;
	public int type = -1;
	
	public static final int LIGHT = 0;
	
	public LVLEditorObject(String name, int type){
		this.name = name;
		this.type = type;
	}
	
	public void writeToInterface(JList variableList){
		DefaultListModel listModel = (DefaultListModel) variableList.getModel();
        listModel.removeAllElements();
		listModel.addElement("Name: " + name);
		listModel.addElement("P: " + getPosition());
		listModel.addElement("R: " + getRotation());
	}
	
	public void putInMap(){
		
	}
	
	public LVLEditorObject getInstance(){
		return null;
	}
	
	public String getVariableString(String name){
		if (name.equals("Name")){
			return name;
		}
		if (name.equals("P")){
			return getPosition().x + " " + getPosition().y + " " + getPosition().z;
		}
		if (name.equals("R")){
			return getRotation().x + " " + getRotation().y + " " + getRotation().z;
		}
		return "not found";
	}
	
	public void parseVariableString(String variable, String input){
		try{
			if (variable.equals("Name")){
				//ignore name changes
				return;
			}
			if (variable.equals("P")){
				String[] split = input.split(" ");
				setPosition(Float.parseFloat(split[0]),Float.parseFloat(split[1]), Float.parseFloat(split[2]));
				return;
			}
			if (variable.equals("R")){
				String[] split = input.split(" ");
				setRotation(Float.parseFloat(split[0]),Float.parseFloat(split[1]), Float.parseFloat(split[2]));
				return;
			}
			throw new Exception("UNKNOWN VARIABLE " + variable);
		}catch(Exception e){
			System.out.println("FOR THE LOVE OF GOD, FORMAT YOUR INPUT PROPERLY: " + e.getMessage());
		}
	}
	
	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException{
		writer.writeAttribute("Type", "" + type);
		writer.writeAttribute("P", getVariableString("P"));
		writer.writeAttribute("R", getVariableString("R"));
	}
	
	public static LVLEditorObject parse(HashMap<String, String> values){
		if (values.get("Type") == null){
			return null;
		}
		int type = Integer.parseInt(values.get("Type"));
		values.remove("Type");
		if (type == LIGHT){
			LVLEditorLight light = new LVLEditorLight();
			for (String key : values.keySet()){
				light.parseVariableString(key, values.get(key));
			}
			return light;
		}
		
		return null;
	}
	
}
