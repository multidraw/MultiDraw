

import java.awt.Graphics2D;
import java.awt.Polygon;

import tools.shapes.TwoEndShape;

@SuppressWarnings("serial")
public class TriangleShape extends TwoEndShape {
	
	public void draw(Graphics2D g2d, int x0, int y0, int x1, int y1) {
		g2d.setColor(getColor());
		
		borders.width = Math.abs(x0 - x1);
		borders.height = Math.abs(y0 - y1);
		borders.update();
		
		int[] xPoints = { borders.x, borders.x + borders.width, borders.width };
		int[] yPoints = { borders.y, borders.y + borders.height, borders.height };
		
		setShape(new Polygon(xPoints, yPoints, 3));
		
		if(isFilled())
			g2d.fill(shape);
		else
			g2d.draw(shape);
	}
	
	public void drawOutline(Graphics2D g2d, int x0, int y0, int x1, int y1) {
		int[] xPoints = { borders.x, borders.x + borders.width, borders.width };
		int[] yPoints = { borders.y, borders.y + borders.height, borders.height };
		
		if(isFilled())
			g2d.fill(new Polygon(xPoints, yPoints, 3));
		else
			g2d.draw(new Polygon(xPoints, yPoints, 3));
	}

	@Override
	public void move(Graphics2D g, int deltaX, int deltaY) {
		borders.x += deltaX;
		borders.y += deltaY;		
		redraw(g);
	}	
}
