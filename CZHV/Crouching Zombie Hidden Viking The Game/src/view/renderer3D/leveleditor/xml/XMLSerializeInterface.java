package view.renderer3D.leveleditor.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public interface XMLSerializeInterface {
	public default void addImplementor(Class endClass, Object implementor){
		Class<?> current = this.getClass().getSuperclass();
		getImplementors().add(new FakeClass(getAdditionalVariables(), getHiddenVariables(), implementor.getClass(), implementor));
		while(!current.equals( endClass) && !current.equals(java.lang.Object.class)){
			getImplementors().add(new FakeClass(new ArrayList<String>(), getHiddenVariables(), current, implementor));
			current = current.getSuperclass();
		}
	}
	
	public ArrayList<FakeClass> getImplementors();
	public ArrayList<String> getHiddenVariables();
	public ArrayList<String> getAdditionalVariables();
	
	public default void writeToXML(XMLStreamWriter writer) throws XMLStreamException, IllegalArgumentException, IllegalAccessException{
		writer.writeAttribute("type", this.getClass().getName());
		for(FakeClass fakeClass : getImplementors()){
			for (String varName : fakeClass.getFields().keySet()){
				writer.writeAttribute(varName, varToString(fakeClass.getValue(varName)));
			}
		}
	}
	
	public default String getVarAsString(String varname){
		for (FakeClass f : getImplementors()){
			try{
				Object o = f.getValue(varname);
				return varToString(o);
			}catch(Exception e){
				
			}
		}
		return null;
	}
	
	public default void setVarWithString(String var, String input){
		for (FakeClass f : getImplementors()){
			try{
				f.setValue(var, stringToVar(input, f.getValue(var).getClass()));
				return;
			}catch(Exception e){
				
			}
		}
	}
	
	public default HashMap<String, Field> getAllFields(){
		HashMap<String, Field> fields = new HashMap<String, Field>();
		for(FakeClass fakeClass : getImplementors()){
			for (String varName : fakeClass.getFields().keySet()){
				fields.put(varName, fakeClass.getFields().get(varName));
			}
		}
		return fields;
	}
	
	public default String varToString( Object o){
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
