package tools;

import views.DrawingCanvasView;

/**
 * Draws the background color on the canvas, thus erasing.
 * 
 * Follows the user's mouse movement and erases as the user drags the mouse
 * across the DrawingCanvas.
 */
public class EraserTool extends Tool {

	/* Class Member Variables */
	protected DrawingCanvasView canvas;

	public EraserTool(DrawingCanvasView c) {
		if (c != null)
			canvas = c;
		else
			throw new IllegalArgumentException();
	}

	public void eraseObject() {
		canvas.removeObject(canvas.getObject(canvas.getCurrentSelectedObject()));
		canvas.refreshCanvas();
	}

}
