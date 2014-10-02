package view.renderer3D.core;

import java.awt.Point;
import java.util.Collection;

import model.map.Cell;

public interface RendererInfoInterface {
	public Object click(float x, float y);
	public Collection<Cell> squareSelect(Point strart, Point end);
}
