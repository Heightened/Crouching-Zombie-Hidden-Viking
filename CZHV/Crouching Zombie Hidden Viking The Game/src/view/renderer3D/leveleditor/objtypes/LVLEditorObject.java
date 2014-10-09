package view.renderer3D.leveleditor.objtypes;

import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Dummy3DObj;

public class LVLEditorObject extends Dummy3DObj{
	public String name;
	public int type = -1;

	public static final int LIGHT = 0;
	
	private HashMap<String, Field> fields;
	private Class lvl;

	public LVLEditorObject(Class lvl, String name, int type){
		this.lvl = lvl;
		this.name = name;
		this.type = type;
	
		//commence the ugly
		fields = new HashMap<>();
		Field[] f = lvl.getDeclaredFields();
		for (Field field : f){
			fields.put(field.getName(), field);
			field.setAccessible(true);
		}
	}

	public void writeToInterface(JList variableList){
		System.out.println("WRITE");
		DefaultListModel listModel = (DefaultListModel) variableList.getModel();
		listModel.removeAllElements();
		listModel.addElement("Name: " + name);
		listModel.addElement("P: " + varToString(getPosition()));
		listModel.addElement("R: " + varToString(getRotation()));

		System.out.println("WRITE2");
		try{
			for (String s : fields.keySet()){
				Field field = fields.get(s);
				Object value = field.get(this);
				listModel.addElement(s + ":" + getVariableString(s));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("WRITE3");
	}

	public Object getVariable(String fieldName){
		try{
			Field field = this.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(this);
		}catch(Exception e){
			return null;
		}
	}

	public void putInMap(){

	}

	public LVLEditorObject getInstance(){
		return null;
	}

	public String getVariableString(String name) throws IllegalArgumentException, IllegalAccessException{
		if (name.equals("Name")){
			return name;
		}
		if (name.equals("P")){
			return varToString(getPosition());
		}
		if (name.equals("R")){
			return varToString(getRotation());
		}
		Field f = fields.get(name);
		if (f!= null){
			return varToString( f.get(this));
		}
		return "not found";
	}
	
	public String varToString( Object o){
		if (o == null){
			return "null";
		}
		if (o instanceof Vector4f){
			Vector4f i = (Vector4f)o;
			return i.x + " " + i.y + " " + i.z;
		}
		if (o instanceof Vector3f){
			Vector3f i = (Vector3f)o;
			return i.x + " " + i.y + " " + i.z;
		}
		return o.toString();
	}
	
	public Object stringToVar(String s, Class c){
		System.out.println("input " + s + " " + c);
		boolean isNull = false;
		if (s.equals("null")){
			isNull = true;
		}
		if (c.equals(Vector4f.class)){
			if (isNull){
				return null;
			}
			String[] split = s.split(" ");
			return new Vector3f(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
		}
		if (c.equals(Vector3f.class)){
			if (isNull){
				return null;
			}
			String[] split = s.split(" ");
			return new Vector3f(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]));
		}
		if (c.equals(String.class)){
			return s;
		}
		try{
			return Integer.parseInt(s);
		}catch(Exception e){
			
		}
		try{
			return Float.parseFloat(s);
		}catch(Exception e){
			
		}
		System.out.println("STR");
		return null;
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
			Field f = fields.get(variable);
			if (f != null){
				Class c = f.getType();
				Object o = stringToVar(input, c);
				f.set(this, o);
				return;
			}
			throw new Exception("UNKNOWN VARIABLE " + variable);
		}catch(Exception e){
			System.out.println("FOR THE LOVE OF GOD, FORMAT YOUR INPUT PROPERLY: " + e + " " + e.getMessage());
		}
	}

	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException, IllegalArgumentException, IllegalAccessException{
		writer.writeAttribute("Type", "" + type);
		writer.writeAttribute("P", varToString(getPosition()));
		writer.writeAttribute("R", varToString(getRotation()));
		for (String s : fields.keySet()){
			writer.writeAttribute(s, varToString(fields.get(s).get(this)));
		}
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
