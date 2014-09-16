/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view.renderer3D.core;


import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
/**
 *
 * @author Vouwfietsman
 */
public class TextureObject {
	private int width;
	private int height;
	private String name;
	private boolean bound = false;
	private int textureID;

	private int internalFormat;
	private int format;
	private int type;
	private ByteBuffer data;

	public TextureObject(String name, int width, int height, int internalFormat, int format, int type) {
		this.width = width;
		this.height = height;
		this.name = name;
		this.internalFormat = internalFormat;
		this.format = format;
		this.type = type;
	}

	public TextureObject(String filename){
		BufferedImage bi = null;
		try{
			bi = ImageIO.read(new File(filename));
		}catch (Exception e ){
			e.printStackTrace();
			return;
		}
		byte[] imgarray = ((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
		data = BufferUtils.createByteBuffer(imgarray.length);
		data.put(imgarray);
		data.flip();

		this.width = bi.getWidth();
		this.height = bi.getHeight();
		this.name = filename;
		this.internalFormat = GL11.GL_RGBA;
		this.format = GL11.GL_RGBA;
		this.type = GL11.GL_UNSIGNED_BYTE;
	}


	public void setup(){
		textureID = glGenTextures();
		bind(0);
		glTexImage2D(GL_TEXTURE_2D, 0,internalFormat, width, height, 0,format, type, data);
		TOOLBOX.checkGLERROR();
	}

	public void setMINMAG(int filter){
		classInv();
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		TOOLBOX.checkGLERROR();
	}

	public void setWRAPST(int filter){
		classInv();
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, filter);
		TOOLBOX.checkGLERROR();
	}

	public void bind(int activeNum){
		bound = true;
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + activeNum);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void unbind(){
		bound = false;
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getTextureID() {
		return textureID;
	}



	private void classInv(){
		if (!bound){
			System.err.println("OPERATING ON " + this + " WHILE NOT BOUND");
			TOOLBOX.printStackTraceFromHere();
			System.exit(0);
		}
	}

	@Override
	public String toString(){
		return name + " " + textureID + " " + width + " " + height;
	}
}
