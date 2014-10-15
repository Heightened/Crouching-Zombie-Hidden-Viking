package view.renderer3D.core.resources;

import org.lwjgl.opengl.GL11;

import view.renderer3D.core.TextureObject;

public class Resource {
	public static Model viking;
	public static TextureObject vikingTexture;
	
	static {
		viking = new Model("viking.obj");
		vikingTexture = new TextureObject("Viking2048.png");
		vikingTexture.setup();
		vikingTexture.setMINMAG(GL11.GL_LINEAR);
		vikingTexture.setWRAPST(GL11.GL_REPEAT);
		vikingTexture.mipMap();
		vikingTexture.unbind();
	}
}