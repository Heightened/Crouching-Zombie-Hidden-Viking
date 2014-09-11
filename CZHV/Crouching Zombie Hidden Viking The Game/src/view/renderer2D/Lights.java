package view.renderer2D;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Lights {
	private final static int lightRadius = 100;
	private BufferedImage lightMap;
	ArrayList<Point> lightList;
	private Graphics2D g2;
	private Point view;
	private int cellSize;
	public Lights(int cellsize){
		this.cellSize = cellsize;
		lightList = new ArrayList<>();
		lightMap = new BufferedImage(256,128, BufferedImage.TYPE_4BYTE_ABGR);
		g2 = (Graphics2D)lightMap.getGraphics();
	}
	
	public void setViewPosition(Point p){
		view = p;
	}
	
	public void addLight(Point p){
		lightList.add(p);
	}
	
	public void render(){
		AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
		g2.setComposite(comp);
		g2.setColor(Color.black);
		g2.fillRect(0, 0, 256, 128);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1.0f);
		g2.setComposite(comp);
		Color white = new Color(1,1,1,0);
		Color black = new Color(0,0,0,1f);
		for (Point p : lightList){
		     float radius = 20;
		     float[] dist = {0.0f, 1f};
		     Color[] colors = {white, black};
		     Point rp = new Point(p.x - view.x/cellSize, p.y - view.y/cellSize);
		     RadialGradientPaint paint = new RadialGradientPaint(rp, radius, dist, colors);
		     g2.setPaint(paint);
			g2.fillOval(p.x-lightRadius/2 - view.x/cellSize, p.y-lightRadius/2 - view.y/cellSize, lightRadius, lightRadius);
		}
		lightList.clear();
		lightList.add(new Point(0,0));
		lightList.add(new Point(128,0));
		lightList.add(new Point(128,128));
		lightList.add(new Point(0,128));
	}
	
	public BufferedImage getLightMap(){
		return lightMap;
	}
	
}
