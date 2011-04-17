package tools.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import views.DrawingCanvasView;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@SuppressWarnings("serial")
@XStreamAlias("FreehandShape")
public class FreehandShape extends CanvasShape{
	
	private int xMax, yMax;
	private int localXMax;
	private int localYMax;
	private int localXMin;
	private int localYMin;
	
	@XStreamImplicit(itemFieldName="Point")
	private Vector<Point> points;
	
	public FreehandShape(DrawingCanvasView canvas) {
		xMax = 0;
		yMax = 0;
		borders.x = Integer.MAX_VALUE;
		borders.y = Integer.MAX_VALUE;
		points = new Vector<Point>();
	}
	
	@Override
	public void draw(Graphics2D g, int x0, int y0, int x1, int y1) {
		g.setColor(getColor());

		g.drawLine(x0, y0, x1, y1);
		
		localXMax = Math.max(x0, x1);
		localYMax = Math.max(y0, y1);
		localXMin = Math.min(x0, x1);
		localYMin = Math.min(y0, y1);
		
		xMax = (xMax < localXMax) ? localXMax : xMax;
		yMax = (yMax < localYMax) ? localYMax : yMax;
		borders.x = (borders.x < localXMin) ? borders.x : localXMin;
		borders.y = (borders.y < localYMin) ? borders.y : localYMin;
		borders.height = yMax - borders.y;
		borders.width = xMax - borders.x;
		borders.update();
	}

	public void addPoint(Point p) {
		points.add(p);
	}
	
	@Override
	public void redraw(Graphics2D g) {
		for(int i = 1; i < points.size(); i++) {
			draw(g, points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y);
		}
	}

	@Override
	public void move(Graphics2D g, int deltaX, int deltaY) {
		borders.x += deltaX;
		borders.y += deltaY;		
		xMax = 0;
		yMax = 0;
		
		for(Point p : points) {
			p.x += deltaX;
			p.y += deltaY;
		}
		
		redraw(g);
	}

	@Override
	public void resize(Graphics2D g, String corner, int deltaX, int deltaY, Point startPoint) {}

	@Override
	public void resizeOutline(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition) {}

	public int getLocalXMax() {
		return localXMax;
	}

	public void setLocalXMax(int localXMax) {
		this.localXMax = localXMax;
	}

	public int getLocalYMax() {
		return localYMax;
	}

	public void setLocalYMax(int localYMax) {
		this.localYMax = localYMax;
	}

	public int getLocalXMin() {
		return localXMin;
	}

	public void setLocalXMin(int localXMin) {
		this.localXMin = localXMin;
	}

	public int getLocalYMin() {
		return localYMin;
	}

	public void setLocalYMin(int localYMin) {
		this.localYMin = localYMin;
	}
}
