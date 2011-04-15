package tools.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import utils.Borders;

public abstract class CanvasShape implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean selected;
	protected Borders borders;
	protected Shape shape;
	protected Color color;
	protected boolean resizable;
	
	public CanvasShape() {
		selected = false;
		borders = new Borders();
	}
	
	/**
	 * Facilitates drawing the TwoEndShape on screen.
	 * 
	 * @param g Graphics context to use
	 * @param x0 starting x coordinate
	 * @param y0 starting y coordinate
	 * @param x1 ending x coordinate
	 * @param y1 ending y coordinate
	 */
	abstract public void draw(Graphics2D g, int x0, int y0, int x1, int y1);
	abstract public void redraw(Graphics2D g);
	abstract public void move(Graphics2D g, int deltaX, int deltaY);
	abstract public void resize(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition);
	abstract public void resizeOutline(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition);
	
	public void highlightSelected(Graphics2D g2d) {
		Color color = g2d.getColor();
		g2d.setColor((isSelected()) ? Color.GRAY : Color.WHITE);
		
		drawPoint(borders.x, borders.y, g2d);
		drawPoint(borders.x + borders.width, borders.y + borders.height, g2d);
		drawPoint(borders.x + borders.width, borders.y, g2d );
		drawPoint(borders.x, borders.y + borders.height, g2d);
		
		g2d.setColor(color);
	}
	
	public void drawPoint(double x, double y, Graphics2D g) {
		Rectangle2D.Double point = new Rectangle2D.Double(x-2, y-2, 4, 4);
		g.fill(point);
		g.draw(point);
	}
	
	public Borders getBorders() {
		return borders;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}	
}
