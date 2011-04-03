package tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import tools.shapes.CanvasShape;
import tools.shapes.TwoEndShape;
import views.DrawingCanvasView;
/**
 * Draws any TwoEndShape on the DrawingCanvas.
 * 
 * This tool takes advantage of some generic behavior for drawing a shape. Any
 * TwoEndShape can be drawn with this tool where a TwoEndShape is defined as a
 * shape with a starting and ending point. The user clicks the mouse button to
 * define the starting location, drags the mouse, and releases the mouse at the
 * ending location.
 */
public class TwoEndShapeTool extends Tool {

	protected DrawingCanvasView canvas;
	protected Point startingMousePosition;
	protected Point currentMousePosition;
	protected Color saveColor;
	protected TwoEndShape shape;

	public TwoEndShapeTool(DrawingCanvasView c, TwoEndShape s) {
		if (c != null && s != null) {
			canvas = c;
			shape = s;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * Establish starting point for next drawing
	 */
	public void mousePressed(MouseEvent e) {
		setFillTypeAndColor();
		
		startingMousePosition = e.getPoint();
		currentMousePosition = startingMousePosition;
		shape.setStartPoint(startingMousePosition);
		shape.setX(shape.getStartPoint(), shape.getStartPoint());
		shape.setY(shape.getStartPoint(), shape.getStartPoint());
		
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();
		saveColor = iBGraphics.getColor();
		iBGraphics.setXORMode(Color.lightGray);
		iBGraphics.setColor(Color.white);
		shape.drawOutline(iBGraphics, startingMousePosition.x,
				startingMousePosition.y, startingMousePosition.x,
				startingMousePosition.y);
		
		canvas.repaint();
	}

	/*
	 * Resizes the TwoEndShape and updates the shape outline
	 */
	public void mouseDragged(MouseEvent e) {
		Point newMousePosition = e.getPoint();
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();

		shape.setX(shape.getStartPoint(), currentMousePosition);
		shape.setY(shape.getStartPoint(), currentMousePosition);
		
		/* erase previous temporary figure by redrawing it */
		shape.drawOutline(iBGraphics, startingMousePosition.x,
				startingMousePosition.y, currentMousePosition.x,
				currentMousePosition.y);
		
		shape.setX(shape.getStartPoint(), newMousePosition);
		shape.setY(shape.getStartPoint(), newMousePosition);

		/* draw new temporary figure */
		shape.drawOutline(iBGraphics, startingMousePosition.x,
				startingMousePosition.y, newMousePosition.x, newMousePosition.y);

		/* update current mouse coordinates */
		currentMousePosition = newMousePosition;
		canvas.repaint();
	}

	/*
	 * Draws the finalized TwoEndShape
	 */
	public void mouseReleased(MouseEvent e) {
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();

		shape.setEndPoint(e.getPoint());
		shape.setX(shape.getStartPoint(), shape.getEndPoint());
		shape.setY(shape.getStartPoint(), shape.getEndPoint());
		
		/* Erase final temporary figure */    //THIS DOESN'T DO ANYTHING
		shape.drawOutline(iBGraphics, startingMousePosition.x,
				startingMousePosition.y, currentMousePosition.x,
				currentMousePosition.y);

		/* Return graphics context to normal drawing mode and color */
		iBGraphics.setPaintMode();
		iBGraphics.setColor(saveColor);

		/* Draw final"permanent" figure */
		shape.draw(iBGraphics, startingMousePosition.x,
				startingMousePosition.y, e.getPoint().x, e.getPoint().y);

		canvas.repaint();
		
		canvas.addObject((CanvasShape) shape);
		try {
			shape = shape.getClass().newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	public void setFillTypeAndColor() {
		if (canvas.isFilledShape())
			shape.setFilled(true);
		else
			shape.setFilled(false);
		shape.setColor(canvas.getPenColor());
	}
}
