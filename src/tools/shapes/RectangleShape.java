package tools.shapes;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RectangleShape")
public class RectangleShape extends TwoEndShape {
	
	/* 
	 * Defines how to draw the rectangle with corners at the starting and ending
	 * point. The smallest coordinates of the starting and ending positions are
	 * used to locate the origin of the rectangular shape.  The absolute value
	 * of the differences in the x and y coordinates are used for the width and
	 * height respectively.
	 */
	public void draw(Graphics2D g2d, int x0, int y0, int x1, int y1) {
		g2d.setColor(getColor());
		
		borders.width = Math.abs(x0 - x1);
		borders.height = Math.abs(y0 - y1);
		borders.update();
		
		setShape(new Rectangle2D.Double(borders.x, borders.y, borders.width, borders.height));
		
		if(isFilled())
			g2d.fill(shape);
		else
			g2d.draw(shape);
	}
	
	public void drawOutline(Graphics2D g2d, int x0, int y0, int x1, int y1) {		
		if(isFilled())
			g2d.fill(new Rectangle2D.Double(borders.x, borders.y, Math.abs(x0-x1), Math.abs(y0-y1)));
		else
			g2d.draw(new Rectangle2D.Double(borders.x, borders.y, Math.abs(x0-x1), Math.abs(y0-y1)));
	}

	@Override
	public void move(Graphics2D g, int deltaX, int deltaY) {
		borders.x += deltaX;
		borders.y += deltaY;		
		redraw(g);
	}	
}
