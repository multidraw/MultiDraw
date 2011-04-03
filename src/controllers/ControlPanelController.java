package controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import tools.shapes.CanvasShape;
import tools.shapes.TwoEndShape;
import views.DrawingCanvasView;



/**
 * Controller which is associated with a ControlPanelView.  Functions as an
 * ActionListener for the Clear Canvas Button and an ItemListener for the 
 * Color Selection Combo Box.
 * 
 * Needs a reference to the view's DrawingCanvas in order to clear the canvas
 * and update the drawing color.
 */
public class ControlPanelController	implements ActionListener, ItemListener {

	protected DrawingCanvasView canvas;

	/**
	 * Creates a ControlPanelController associated with the specified 
	 * DrawingCanvas.
	 */
	public ControlPanelController(DrawingCanvasView c) {
		if( c != null )
			canvas = c;
		else
			throw new IllegalArgumentException();
	}


	/* 
	 * Fields actions performed by the Clear Canvas button on a ControlPanelView
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Clear Canvas"))
			canvas.clearCanvas();
		else if(canvas.isFilledShape())
			canvas.setFilledShape(false);
		else
			canvas.setFilledShape(true);
		if(canvas.getCurrentSelectedObject() != null) {
			CanvasShape shape = canvas.getObject(canvas.getCurrentSelectedObject());
			if(shape instanceof TwoEndShape) {
				((TwoEndShape) shape).setFilled(canvas.isFilledShape());
				canvas.updateObject(canvas.getCurrentSelectedObject(), shape);
				canvas.refreshCanvas();
			}
		}
	}
	
	/* 
	 * Fields actions performed by the color combo box on a ControlPanelView
	 */
	public void itemStateChanged(ItemEvent e)  {
		if (e.getStateChange() == ItemEvent.SELECTED)
			canvas.setPenColor(itemToColor(e.getItem()));
		if(canvas.getCurrentSelectedObject() != null) {
			CanvasShape shape = canvas.getObject(canvas.getCurrentSelectedObject());
			shape.setColor(canvas.getPenColor());
			canvas.updateObject(canvas.getCurrentSelectedObject(), shape);
			canvas.refreshCanvas();
		}
	}

	/**
	 * Used to determine which color to select. Maps a string constant to a new
	 * color. If the supplied argument is null or unrecognized, the pen color
	 * will remain the same. 
	 * 
	 * @param item Selected item from a ControlPanelView's combo box
	 * @return Associated color
	 */
	public Color itemToColor(Object item) {
		if("black".equals(item)) {
			return Color.black;
		}
		else if("blue".equals(item)) {
			return Color.blue;
		}
		else if("green".equals(item)) {
			return Color.green;
		}
		else if("red".equals(item)) {
			return Color.red;
		}
		else {
			return canvas.getPenColor();
		}
	}
}
