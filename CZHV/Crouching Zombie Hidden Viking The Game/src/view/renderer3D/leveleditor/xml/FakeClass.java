package view.renderer3D.leveleditor.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class FakeClass {
	private HashMap<String, Field> fields = new HashMap<>();
	private Object instance;
	private Class fakeClass;
	private String fullName;
	public FakeClass(ArrayList<String> additionalVariables, ArrayList<String> hiddenVariables, Class c, Object instance){
		this.instance = instance;
		this.fakeClass = c;
		this.fullName = fakeClass.getName();
		Field[] f = fakeClass.getDeclaredFields();
		for (Field field : f){
			if (!hiddenVariables.contains(field.getName())){
				fields.put(field.getName(), field);
				field.setAccessible(true);
			}
		}
		for (String s : additionalVariables){
			Field field = scanForField(s);
			if (field == null){
				try{
					throw new Exception("FIELD NOT FOUND");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			fields.put(s, field);
			field.setAccessible(true);
		}
	}
	
	
	public Field getAnyField(String s){
		Field f = fields.get(s);
		if (f == null){
			f = scanForField(s);
			if (f != null){
				f.setAccessible(true);
				fields.put(s, f);
			}
		}
		return f;
	}
	
	private Field scanForField(String s){
		Class current = fakeClass;
		while(!current.equals(java.lang.Object.class)){
			try{
				Field field = current.getDeclaredField(s);
				return field;
			}catch(Exception e){
				
			}
			current = current.getSuperclass();
		}
		return null;
	}
	
	public Object getValue(String varname) throws IllegalArgumentException, IllegalAccessException{
		return fields.get(varname).get(instance);
	}
	
	public void setValue(String varname, Object value){
		Field f = getAnyField(varname);
		try{
			f.set(instance, value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public HashMap<String, Field> getFields(){
		return fields;
	}
	
	public Object getInstance(){
		return this.instance;
	}
	
	public FakeClass(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		this.fakeClass = Class.forName(name);
		this.instance = fakeClass.newInstance();
		this.fullName = fakeClass.getName();
		Field[] f = fakeClass.getDeclaredFields();
		for (Field field : f){
			fields.put(field.getName(), field);
			field.setAccessible(true);
		}
	}
	
	public Class getFakeClass(){
		return fakeClass;
	}
	
	public String getFullName(){
		return fullName;
	}
}
