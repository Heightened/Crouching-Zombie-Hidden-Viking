package view.renderer3D.core.resources;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import view.renderer3D.core.TextureObject;

public class Resource {
	//public static boolean loaded = false;
	public static void load() {
		System.out.println("Loading assets...");
	}
	
	private static class ResourceContainer {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	
	
	public static class viking {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	public static class zombie {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	public static class wall {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	public static class wallend {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	public static class gate {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	public static class tower {
		public static Model model;
		public static TextureObject texture;
		public static HashMap<String, Animation> animation = new HashMap<String, Animation>();
		
		public static void setModel(String modelPath) {
			model = new Model(modelPath);
		}
		
		public static void setTexture(String texturePath) {
			texture = new TextureObject(texturePath);
			texture.setup();
			texture.setMINMAG(GL11.GL_LINEAR);
			texture.setWRAPST(GL11.GL_REPEAT);
			texture.mipMap();
			texture.unbind();
		}
		
		public static void setAnimation(String name, String animationPath) {
			animation.put(name, new Animation(120, animationPath));
		}
	}
	
	
	static {
		viking.setModel("viking.obj");
		viking.setAnimation("run", "Animation/Viking/Run/run");
		viking.setTexture("Texture/Viking2048.png");

		zombie.setAnimation("walk", "Animation/Zombie/Walk/walk");
		zombie.setTexture("Texture/Viking2048.png");
		
		wall.setModel("Model/wallsquare.obj");
		wall.setTexture("Texture/OldSandstone1024.png");
		
		wallend.setModel("Model/wallsquare.obj");
		wallend.setTexture("Texture/RustyPaint1024.png");
		
		gate.setModel("Model/wallsquareGate.obj");
		gate.setTexture("Texture/RustyPaint1024.png");
		
		tower.setModel("Model/wallsquareTower.obj");
		tower.setTexture("Texture/RustyPaint1024.png");
		
		System.out.println("Loading complete.");
	}
} 