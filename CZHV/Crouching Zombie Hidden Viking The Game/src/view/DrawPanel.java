package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	private static final int CELL_SIZE = 16;
	private Point viewPosition;
	private Dimension dimension;
	
	public DrawPanel(int viewX, int viewY, Dimension dimension){
		viewPosition = new Point(viewX, viewY);
		this.dimension = dimension;
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.green);
		g2.fillRect(0, 0, dimension.width, dimension.height);
		drawViking(g2, 5,5);
		drawZombie(g2, 10,6);
		drawGrid(g2);
	}
	
	public void setDimension(int width, int height){
		this.dimension.width = width;
		this.dimension.height = height;
	}
	
	public void setViewPosition(int x, int y){
		viewPosition.setLocation(x, y);
	}
	
	public void drawSolid(Graphics2D g2, int cellx, int celly){
		Point worldpos = cellToWorldSpace(cellx, celly);
		g2.setColor(Color.BLACK);
		g2.fillRect(worldpos.x, worldpos.y, CELL_SIZE+1, CELL_SIZE+1);
	}
	
	public void drawViking(Graphics2D g2, int cellx, int celly){
		drawOval(g2, Color.BLUE, cellToWorldSpace(cellx, celly));
	}
	
	public void drawZombie(Graphics2D g2, int cellx, int celly){
		drawOval(g2, Color.RED, cellToWorldSpace(cellx, celly));
	}
	
	public Point cellToWorldSpace(int cellx, int celly){
		return new Point(cellx*CELL_SIZE - viewPosition.x, celly*CELL_SIZE - viewPosition.y);
	}
	
	public void drawOval(Graphics2D g2, Color c, Point position){
		g2.setColor(c);
		g2.fillOval(position.x-CELL_SIZE, position.y-CELL_SIZE, CELL_SIZE*3, CELL_SIZE*3);
	}
	
	private Color gridColor = new Color(1,1,1,0.5f);
	public void drawGrid(Graphics2D g2){
		g2.setColor(gridColor);
		int numLinesHor = (int)(dimension.getWidth())/CELL_SIZE;
		int numLinesVer = (int)(dimension.getHeight())/CELL_SIZE;
		int dx = viewPosition.x%CELL_SIZE;
		int dy = viewPosition.y%CELL_SIZE;
		for (int x = -1; x < numLinesHor; x++){	
			g2.drawLine(x*CELL_SIZE-dx, -dy, x*CELL_SIZE-dx, dimension.height-dy);	
		}
		for (int y = 0; y < numLinesVer; y++){
			g2.drawLine(-dx-CELL_SIZE, y*CELL_SIZE-dy, dimension.width - dx, y*CELL_SIZE - dy);
		}	
	}
}
