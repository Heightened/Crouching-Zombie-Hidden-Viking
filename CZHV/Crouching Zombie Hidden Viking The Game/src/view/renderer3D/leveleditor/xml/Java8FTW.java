package view.renderer3D.leveleditor.xml;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Java8FTW {
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
