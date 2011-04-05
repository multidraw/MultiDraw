package items;

import javax.swing.AbstractAction;

import views.DrawingCanvasView;

/**
 * Generic class for a File Menu Item.
 * 
 * This class represents each item that is under the "File" option in the Menu bar.
 */
@SuppressWarnings("serial")
public abstract class FileMenuItem extends AbstractAction{
	protected DrawingCanvasView canvas;
	
	public FileMenuItem(String name, DrawingCanvasView c){
		super(name);
		canvas = c;
	}
}
