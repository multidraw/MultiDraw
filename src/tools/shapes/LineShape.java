package tools.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Defines a basic line class that runs from a starting point to and ending
 * point.
 */
@XStreamAlias("LineShape")
public class LineShape extends TwoEndShape {

	/**
	 * Draws a simple line from the starting to ending point.
	 */
	public void draw(Graphics2D g2d, int x0, int y0, int x1, int y1) {
		g2d.setColor(getColor());

		borders.width = Math.abs(x0 - x1);
		borders.height = Math.abs(y0 - y1);
		borders.update();

		setShape(new Line2D.Double(x0, y0, x1, y1));
		g2d.draw(shape);
	}

	/**
	 * Draws the outline of a line from the starting to ending point.
	 */
	public void drawOutline(Graphics2D g2d, int x0, int y0, int x1, int y1) {
		g2d.draw(new Line2D.Double(x0, y0, x1, y1));
	}

	@Override
	public void redraw(Graphics2D g) {
		draw(g, startPoint.x, startPoint.y, endPoint.x, endPoint.y);
	}

	@Override
	public void highlightSelected(Graphics2D g2d) {
		Color color = g2d.getColor();
		g2d.setColor((isSelected()) ? Color.GRAY : Color.WHITE);

		drawPoint(startPoint.x, startPoint.y, g2d);
		drawPoint(endPoint.x, endPoint.y, g2d);

		g2d.setColor(color);
	}
	
	@Override
	public void move(Graphics2D g, int deltaX, int deltaY) {
		borders.x += deltaX;
		borders.y += deltaY;

		startPoint = new Point(startPoint.x + deltaX, startPoint.y + deltaY);
		endPoint = new Point(endPoint.x + deltaX, endPoint.y + deltaY);

		redraw(g);
	}

	@Override
	public void resize(Graphics2D g, String corner, int deltaX, int deltaY,
			Point startPosition) {
		Point point = hitPoint(startPosition);
		if (point != null) {
			if (point.equals(endPoint)) {
				endPoint = new Point(endPoint.x + deltaX, endPoint.y + deltaY);
			} else {
				startPoint = new Point(startPoint.x + deltaX, startPoint.y
						+ deltaY);
			}
			setX(startPoint, endPoint);
			setY(startPoint, endPoint);
		}
	}
	
	@Override
	public void resizeOutline(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition) {
		if(hitPoint(startPosition).equals(endPoint)) 
			g.draw(new Line2D.Double(startPosition.x + deltaX, startPosition.y + deltaY, startPoint.x, startPoint.y));
		else
			g.draw(new Line2D.Double(startPosition.x + deltaX, startPosition.y + deltaY, endPoint.x, endPoint.y));
	}

	public Point hitPoint(Point point) {
		if (hitEndPoint(point))
			return endPoint;
		if (hitStartPoint(point))
			return startPoint;
		else
			return null;
	}

	public boolean hitEndPoint(Point point) {
		Rectangle box = new Rectangle(endPoint.x - 4, endPoint.y - 4, 8, 8);
		return box.contains(point);
	}

	public boolean hitStartPoint(Point point) {
		Rectangle box = new Rectangle(startPoint.x - 4, startPoint.y - 4, 8, 8);
		return box.contains(point);
	}
}
