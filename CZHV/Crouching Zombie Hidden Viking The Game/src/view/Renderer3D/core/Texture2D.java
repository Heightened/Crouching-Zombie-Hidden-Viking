package view.renderer3D.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Texture2D {
	int index = -1;
	
	public Texture2D(String filename){
		index = GL11.glGenTextures();
		BufferedImage bi = null;
		try{
			bi = ImageIO.read(new File(filename));
		}catch (Exception e ){
			e.printStackTrace();
			return;
		}
		byte[] imgarray = ((DataBufferByte)bi.getRaster().getDataBuffer()).getData();
		ByteBuffer b = BufferUtils.createByteBuffer(imgarray.length);
		b.put(imgarray);
		b.flip();
		bind();
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR );
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR );
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT );
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT );
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 
		                bi.getWidth(),
		                bi.getHeight(), 
		                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
		                b);
	}
	
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, index); 
	}
}
