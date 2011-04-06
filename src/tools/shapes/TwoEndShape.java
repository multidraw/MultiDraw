package tools.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Defines the basic notion of a TwoEndShape.
 * 
 * This class is not meant to be instantiated directly, but rather, should be
 * used as a parent class for all TwoEndShapes. The core identity of a
 * TwoEndedShape is defined by both the draw and drawOutline methods which are
 * explained below.
 */
@XStreamAlias("TwoEndShape")
public abstract class TwoEndShape extends CanvasShape {
	protected boolean filled;
	protected boolean selected;
	protected Point startPoint;
	protected Point endPoint;
	protected Color color;
	
	private int savedX;
	private int savedY;
	private int savedWidth;
	private int savedHeight;

	/**
	 * Facilitates drawing an outline as the user drags the mouse.
	 * 
	 * @param g - Graphics context to use
	 * @param x0 - starting x coordinate
	 * @param y0 - starting y coordinate
	 * @param x1 - ending x coordinate
	 * @param y1 - ending y coordinate
	 */
	abstract public void drawOutline(Graphics2D g2d, int x0, int y0, int x1,
			int y1);

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getX() {
		return borders.x;
	}

	public int getY() {
		return borders.y;
	}

	public void setX(Point start, Point end) {
		borders.x = (start.x <= end.x) ? start.x : end.x;
	}

	public void setY(Point start, Point end) {
		borders.y = (start.y <= end.y) ? start.y : end.y;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public void resize(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition) {
		resizeBorders(corner, deltaX, deltaY);
		draw(g, borders.x, borders.y, borders.x + borders.width, borders.y + borders.height);
	}
	
	@Override
	public void resizeOutline(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition) {
		preserveBorders();
		resizeBorders(corner, deltaX, deltaY);
		drawOutline(g, borders.x, borders.y, borders.x + borders.width, borders.y + borders.height);
		restoreBorders();
	}
	
	private void resizeBorders(String corner, int deltaX, int deltaY) {
		if (corner.equals("NW") && deltaX < borders.width && deltaY < borders.height) {
			borders.x += deltaX;
			borders.y += deltaY;
			borders.width -= deltaX;
			borders.height -= deltaY;
		} else if (corner.equals("NE") && -deltaX < borders.width && deltaY < borders.height) {
			borders.y += deltaY;
			borders.width += deltaX;
			borders.height -= deltaY;
		} else if (corner.equals("SE") && -deltaX < borders.width && -deltaY < borders.height) {
			borders.width += deltaX;
			borders.height += deltaY;
		} else if (corner.equals("SW") && deltaX < borders.width && -deltaY < borders.height){
			borders.x += deltaX;
			borders.width -= deltaX;
			borders.height += deltaY;
		}
	}
	
	private void preserveBorders() {
		savedX = borders.x;
		savedY = borders.y;
		savedWidth = borders.width;
		savedHeight = borders.height;
	}
	
	private void restoreBorders() {
		borders.x = savedX;
		borders.y = savedY;
		borders.width = savedWidth;
		borders.height = savedHeight;
	}

	@Override
	public void redraw(Graphics2D g) {
		draw(g, borders.x, borders.y , borders.x + borders.width, borders.y + borders.height);
	}
}
