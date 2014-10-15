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
	
	
}
