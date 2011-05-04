

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
		
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		
		if((startPoint.x < endPoint.x && startPoint.y < endPoint.y) || (startPoint.x > endPoint.x && startPoint.y > endPoint.y)) {
			xPoints[0] = borders.x;
			xPoints[1] = borders.x + borders.width;
			xPoints[2] = borders.x + borders.width;
			yPoints[0] = borders.y;
			yPoints[1] = borders.y;
			yPoints[2] = borders.y + borders.height;
		}
		else {
			xPoints[0] = borders.x;
			xPoints[1] = borders.x + borders.width;
			xPoints[2] = borders.x;
			yPoints[0] = borders.y;
			yPoints[1] = borders.y;
			yPoints[2] = borders.y + borders.height;
		}
				
		setShape(new Polygon(xPoints, yPoints, 3));
		
		if(isFilled())
			g2d.fill(shape);
		else
			g2d.draw(shape);
	}
	
	public void drawOutline(Graphics2D g2d, int x0, int y0, int x1, int y1) {
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		
		if((startPoint.x < endPoint.x && startPoint.y < endPoint.y) || (startPoint.x > endPoint.x && startPoint.y > endPoint.y)) {
			xPoints[0] = borders.x;
			xPoints[1] = borders.x + borders.width;
			xPoints[2] = borders.x + borders.width;
			yPoints[0] = borders.y;
			yPoints[1] = borders.y;
			yPoints[2] = borders.y + borders.height;
		}
		else {
			xPoints[0] = borders.x;
			xPoints[1] = borders.x + borders.width;
			xPoints[2] = borders.x;
			yPoints[0] = borders.y;
			yPoints[1] = borders.y;
			yPoints[2] = borders.y + borders.height;
		}
		
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
