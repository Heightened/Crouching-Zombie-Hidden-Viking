package view.Renderer2D;

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
	public Lights(){
		lightList = new ArrayList<>();
		lightMap = new BufferedImage(128,128, BufferedImage.TYPE_4BYTE_ABGR);
		g2 = (Graphics2D)lightMap.getGraphics();
		lightList.add(new Point(0,0));
		lightList.add(new Point(20,0));
		lightList.add(new Point(0,0));
		lightList.add(new Point(0,2000));
	}
	
	public void setViewPosition(Point p){
		view = p;
	}
	
	public void render(){
		AlphaComposite comp = AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
		g2.setComposite(comp);
		g2.setColor(Color.black);
		g2.fillRect(0, 0, 128, 128);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1.0f);
		g2.setComposite(comp);
		Color white = new Color(1,1,1,0);
		Color black = new Color(0,0,0,1f);
		for (Point p : lightList){
		     float radius = 20;
		     float[] dist = {0.0f, 1f};
		     Color[] colors = {white, black};
		     Point rp = new Point(p.x - view.x/16, p.y - view.y/16);
		     RadialGradientPaint paint = new RadialGradientPaint(rp, radius, dist, colors);
		     g2.setPaint(paint);
			g2.fillOval(p.x-lightRadius/2 - view.x/16, p.y-lightRadius/2 - view.y/16, lightRadius, lightRadius);
		}
		
	}
	
	public BufferedImage getLightMap(){
		return lightMap;
	}
	
}
