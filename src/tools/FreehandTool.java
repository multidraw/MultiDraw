package tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import tools.shapes.FreehandShape;
import views.DrawingCanvasView;

/**
 * Draws a Freehand shape on the DrawingCanvas.
 * 
 * A Freehand shape can consist of many different points and simply follows the
 * user's mouse. As the user moves the mouse, line segments are drawn to screen.
 */
public class FreehandTool extends Tool {

	/* Class member variables */
	protected DrawingCanvasView canvas;
	protected FreehandShape shape;
	protected Point startingMousePosition;
	
	public FreehandTool(DrawingCanvasView c) {
		if (c != null)
			canvas = c;
		else
			throw new IllegalArgumentException();
	}

	protected void drawLineSegment(Point p1, Point p2) {
		shape.draw(canvas.getImageBufferGraphics(), p1.x, p1.y, p2.x, p2.y);
	}

	/* 
	 * Establish starting point for next drawing
	 */
	public void mousePressed(MouseEvent e) {
		startingMousePosition = e.getPoint();
		shape = new FreehandShape(canvas);
		shape.setColor(canvas.getPenColor());
		shape.addPoint(startingMousePosition);

	}

	/*
	 * Draws the next line segment on the canvas.
	 */
	public void mouseDragged(MouseEvent e) {
		Point newMousePosition = e.getPoint();
		drawLineSegment(startingMousePosition, newMousePosition);
		/* update current mouse coordinates */
		startingMousePosition = newMousePosition;
		shape.addPoint(newMousePosition);
	}
	
	public void mouseReleased(MouseEvent e) {
		canvas.addObject((tools.shapes.CanvasShape) shape);
	}
}
