package utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import tools.shapes.CanvasShape;
import tools.shapes.LineShape;

@SuppressWarnings("serial")
public class Borders extends Rectangle {

	public Borders() {
		super();
	}
	
	public Borders(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public String toString() {
		return "X: " + x + " Y: " + y + " Width: " + width + " Height: "
				+ height;
	}
	
	public boolean contains(Point point, CanvasShape shape){
		if (shape instanceof LineShape) {
			if (shape.getShape().intersects(point.x - 4, point.y - 4, 8, 8))
				return true;
			return false;
		}
		return super.contains(point);
	}
	
	public void update() {
		super.setBounds(x, y, width, height);
	}
	
	public void update(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
	
	public boolean hitAnyCorner(Point point){
		return hitNWCorner(point) || hitNECorner(point) || hitSECorner(point) || hitSWCorner(point);
	}
	
	public String cornerHit(Point point) {
		if(hitNWCorner(point))
			return "NW";
		if(hitNECorner(point))
			return "NE";
		if(hitSECorner(point))
			return "SE";
		if(hitSWCorner(point))
			return "SW";
		else
			return "No Corner";
		
	}
	
	public boolean hitNWCorner(Point point) {
		Rectangle2D.Double corner = new Rectangle2D.Double(x-2, y-2, 4, 4);
		return corner.contains(point);
	}
	
	public boolean hitNECorner(Point point) {
		Rectangle2D.Double corner = new Rectangle2D.Double(getMaxX()-2, y-2, 4, 4);
		return corner.contains(point);
	}
	
	public boolean hitSECorner(Point point) {
		Rectangle2D.Double corner = new Rectangle2D.Double(getMaxX()-2, getMaxY()-2, 4, 4);
		return corner.contains(point);
	}
	
	public boolean hitSWCorner(Point point) {
		Rectangle2D.Double corner = new Rectangle2D.Double(x-2, getMaxY()-2, 4, 4);
		return corner.contains(point);
	}
}
