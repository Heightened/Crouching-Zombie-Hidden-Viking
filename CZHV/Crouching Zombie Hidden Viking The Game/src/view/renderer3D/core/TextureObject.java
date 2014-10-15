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
		byte[] imgarray2 = new byte[imgarray.length];
		for (int i = 0; i < imgarray.length; i += 4){
			imgarray2[i + 0] = imgarray[i + 3];
			imgarray2[i + 1] = imgarray[i + 2];
			imgarray2[i + 2] = imgarray[i + 1];
			imgarray2[i + 3] = imgarray[i + 0];
		}
		data = BufferUtils.createByteBuffer(imgarray2.length);
		data.put(imgarray2);
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
        TOOLBOX.checkGLERROR(true);
	}

	public void setMINMAG(int filter){
		classInv();
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        TOOLBOX.checkGLERROR(true);
	}

	public void setWRAPST(int filter){
		classInv();
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, filter);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, filter);
        TOOLBOX.checkGLERROR(true);
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

	public void mipMap(){
		int scale = width/2;
		int iterator = 0;
		ByteBuffer prevdata = data;
		ByteBuffer newdata;
		byte[] newdataA;
		while (scale >= 1){
			iterator++;
			newdata = BufferUtils.createByteBuffer(scale*scale*4);//create array for new mipmap 
			newdataA = new byte[scale*scale*4];//create array for new mipmap 
			//fill array
			for (int i = 0; i < scale; i++){
				for (int j = 0; j < scale; j++){
					newdataA[i*scale*4 + j*4 + 0] = getPrevMipmapPixel(i, j, 0, prevdata, scale);
					newdataA[i*scale*4 + j*4 + 1] = getPrevMipmapPixel(i, j, 1, prevdata, scale);
					newdataA[i*scale*4 + j*4 + 2] = getPrevMipmapPixel(i, j, 2, prevdata, scale);
					byte alpha =  getPrevMipmapPixel(i, j, 3, prevdata, scale);
					if ((alpha & 0xFF) > 200){
						alpha = (byte)255;
					}else{

					}
					newdataA[i*scale*4 + j*4 + 3] = getPrevMipmapPixel(i, j, 3, prevdata, scale);
				}
			}
			newdata.put(newdataA);
			newdata.flip();
			//set new previous
			prevdata = newdata;
			//pass mipmap to opengl
			glTexImage2D(GL_TEXTURE_2D, iterator,internalFormat, scale, scale, 0,format, type, newdata);
			scale = scale/2;
		}
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		TOOLBOX.checkGLERROR(true);
		//plz GC hear me out
		data = null;
	}
	public byte getPrevMipmapPixel(int x, int y, int channel, ByteBuffer mipmap, int scale){
		return (byte)((unsignedToBytes(mipmap.get((x*2)*scale*2*4 + (y*2)*4 + channel))
				+ unsignedToBytes(mipmap.get((x*2+1)*scale*2*4 + (y*2)*4 + channel))
				+ unsignedToBytes(mipmap.get((x*2)*scale*2*4 + (y*2+1)*4 + channel))
				+ unsignedToBytes(mipmap.get((x*2+1)*scale*2*4 + (y*2+1)*4 + channel)))/4);
	}
	
	public int unsignedToBytes(byte b){
		return b&0xFF;
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
