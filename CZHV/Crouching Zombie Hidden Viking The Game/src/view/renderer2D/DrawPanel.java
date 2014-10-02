package view.renderer2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import model.character.GameCharacter;

import javax.swing.JPanel;

import model.item.Item;
import model.map.Cell;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener{
	private int cellSize;
	private Point viewPosition;
	private Dimension dimension;
	private BufferedImage lightmap;
	private ArrayList<RenderInfoController> renderControllers;
	private Collection<Cell> cells;
	private Collection<Cell> solidCells;
	
	
	public DrawPanel(int cellsize, int viewX, int viewY, Dimension dimension, BufferedImage lightmap){
		this.cellSize = cellsize;
		this.lightmap = lightmap;
		viewPosition = new Point(viewX, viewY);
		this.dimension = dimension;
		renderControllers = new ArrayList<>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void setCells(Collection<Cell> c){
		cells = c;
	}
	
	public void setSolidCells(Collection<Cell> c){
		solidCells = c;
	}
	
	float distance = 0;
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.green);
		g2.fillRect(0, 0, dimension.width, dimension.height);
		if (cells != null && solidCells != null){
			for (Cell c : cells){
				if(!c.getCharacterHolder().isEmpty())
				{
					GameCharacter character = c.getCharacterHolder().getItem();
					Color color;
					if(character.isInfected())
						color = Color.RED;
					else if(character.isDead())
						color = Color.DARK_GRAY;
					else
						color = Color.BLUE;
					drawCharacter(g2, color, c.getX(), c.getY());
				}
				if(!c.getItemHolder().isEmpty())
				{
					Item item = c.getItemHolder().getItem();
					drawItem(g2, Color.GRAY, c.getX(), c.getY());
				}
				if(!c.getDecorHolder().isEmpty())
				{
					if(c.getDecorHolder().getItem().isPassible())
						drawPassibleDecor(g2, c.getX(), c.getY());
				}
			}
			for (Cell c : solidCells){
				drawSolid(g2,c.getX(), c.getY());
			}
		}
		drawGrid(g2);
		drawSolid(g2, 0, 0);
		g2.drawImage(lightmap,-viewPosition.x%cellSize-cellSize,-viewPosition.y%cellSize-cellSize,256*cellSize,128*cellSize,null);
		distance += 0.2;
		distance %= 8;
		drawCharacter(g2,Color.yellow, 0, 0, -distance, Math.PI*1.75);
	}
	
	public Point getViewPosition(){
		return viewPosition;
	}
	
	public void setDimension(int width, int height){
		this.dimension.width = width;
		this.dimension.height = height;
	}
	
	public void setViewPosition(int x, int y){
		viewPosition.setLocation(x, y);
	}
	
	public void register(RenderInfoController r){
		renderControllers.add(r);
	}
	
	public void drawSolid(Graphics2D g2, int cellx, int celly){
		Point worldpos = cellToWorldSpace(cellx, celly);
		g2.setColor(Color.BLACK);
		g2.fillRect(worldpos.x, worldpos.y, cellSize+1, cellSize+1);
	}
	
	public void drawPassibleDecor(Graphics2D g2, int cellx, int celly){
		Point worldpos = cellToWorldSpace(cellx, celly);
		g2.setColor(Color.PINK);
		g2.fillRect(worldpos.x, worldpos.y, cellSize+1, cellSize+1);
	}
	
	public void drawCharacter(Graphics2D g2, Color c, int cellx, int celly){
		drawOval(g2, c, cellToWorldSpace(cellx, celly), 3*cellSize);
	}
	
	public void drawItem(Graphics2D g2, Color c, int cellx, int celly){
		drawOval(g2, c, cellToWorldSpace(cellx, celly), cellSize);
	}
	
	public void drawCharacter(Graphics2D g2, Color c, int cellx, int celly, float distance, double radians){
		Point pos = cellToWorldSpace(cellx, celly);
		pos.x += (float)(Math.cos(radians)*distance);
		pos.y += (float)(Math.sin(radians)*distance);
		drawOval(g2, c, pos, cellSize);
	}
	
	public Point cellToWorldSpace(int cellx, int celly){
		return new Point(cellx*cellSize - viewPosition.x, celly*cellSize - viewPosition.y);
	}
	
	public Point worldToCellSpace(int mousex, int mousey){
		return new Point((mousex + viewPosition.x)/cellSize,(mousey + viewPosition.y)/cellSize);
	}
	
	public void drawOval(Graphics2D g2, Color c, Point position, int radius){
		g2.setColor(c);
		g2.fillOval(position.x-radius/2+cellSize/2, position.y-radius/2+cellSize/2, radius, radius);
	}
	
	private Color gridColor = new Color(0.3f,0.3f,0.3f,0.5f);
	public void drawGrid(Graphics2D g2){
		g2.setColor(gridColor);
		int numLinesHor = (int)(dimension.getWidth())/cellSize;
		int numLinesVer = (int)(dimension.getHeight())/cellSize;
		int dx = viewPosition.x%cellSize;
		int dy = viewPosition.y%cellSize;
		for (int x = -1; x < numLinesHor; x++){	
			g2.drawLine(x*cellSize-dx, -dy-cellSize, x*cellSize-dx, dimension.height-dy);	
		}
		for (int y = 0; y < numLinesVer; y++){
			g2.drawLine(-dx-cellSize, y*cellSize-dy, dimension.width - dx, y*cellSize - dy);
		}	
	}
	

	private int mousex;
	private int mousey;
	private int viewx;
	private int viewy;
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int mousex = arg0.getX();
		int mousey = arg0.getY();
		Point p = worldToCellSpace(mousex, mousey);
		for (RenderInfoController r : renderControllers){
			r.clickWorldPos(p);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mousex = e.getX();
		mousey = e.getY();
		viewx = viewPosition.x;
		viewy = viewPosition.y;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int dx = arg0.getX() - mousex;
		int dy = arg0.getY() - mousey;
		viewPosition.x = viewx - dx;
		viewPosition.y = viewy - dy;
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
