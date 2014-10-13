package view.renderer3D.leveleditor.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public interface XMLSerializeInterface {
	public void addImplementor(Class endClass, XMLSerializeInterface implementor);
	
	public ArrayList<FakeClass> getImplementors();
	public ArrayList<String> getHiddenVariables();
	public ArrayList<String> getAdditionalVariables();
	
	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException, IllegalArgumentException, IllegalAccessException;
	
	public String getVarAsString(String varname);
	
	public void setVarWithString(String var, String input);
	
	public HashMap<String, Field> getAllFields();
	
	public String varToString( Object o);
	
	public static Object stringToVar(String s, Class c){
		boolean isNull = false;
		if (s.equals("null")){
			isNull = true;
		}
		if (c.equals(Vector4f.class)){
			if (isNull){
				return null;
			}
			String[] split = s.split(" ");
			return new Vector4f(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]),1);
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
		return null;
	}


	public static FakeClass parse(HashMap<String, String> values) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException{
		if (values.get("type") == null){
			return null;
		}
		FakeClass type =  new FakeClass(values.get("type"));
		values.remove("type");
		for (String key : values.keySet()){
			Object o = stringToVar(values.get(key), type.getAnyField(key).getType());
			if (o != null){
				type.setValue(key, o);
			}
		}
		return type;
	}
}
