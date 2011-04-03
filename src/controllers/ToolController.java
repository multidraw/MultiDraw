package controllers;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import tools.EraserTool;
import tools.SelectTool;
import tools.Tool;
import views.DrawingCanvasView;

/**
 * Maps tool selection to a specific tool.
 * 
 * Provides tool selection ability for any visual components that utilize
 * AbstractAction's. This is especially important as it allows for the addition
 * of a new Tool without rewriting a new ToolController.
 * 
 * In summary, an AbstractAction is an object recognized by smart containers
 * such as JMenuBar's and JToolBar's. These containers automatically register
 * the associate listners and extract any information necessary to properly
 * display the object.
 */
@SuppressWarnings("serial")
public class ToolController extends AbstractAction {

	protected DrawingCanvasView canvas;
	protected Tool tool;

	/**
	 * Initializes a ToolController as specified.
	 * 
	 * @param name
	 *            Name of the tool which will appear on any menu containers
	 * @param icon
	 *            Icon which will appear in any tool bar containers
	 * @param tip
	 *            Tooltip help text that will appear on mouse hover
	 * @param c
	 *            Reference to the DrawingCanvas of the Tool
	 * @param t
	 *            Reference to the Tool which should be active upon selection
	 */
	public ToolController(String name, Icon icon, String tip,
			DrawingCanvasView c, Tool t) {
		super(name, icon);
		tool = t;
		putValue(Action.DEFAULT, icon);
		putValue(Action.SHORT_DESCRIPTION, tip);
		setEnabled(tool != null);
		canvas = c;
	}

	/**
	 * Fires whenever a tool is selected
	 */
	public void actionPerformed(ActionEvent e) {

		if ((tool instanceof EraserTool) && (canvas.getCurrentTool() instanceof SelectTool) && (canvas.getCurrentSelectedObject() != null)) {
			((EraserTool) tool).eraseObject();
		} else {
			canvas.removeSelection();
			canvas.setCurrentTool(tool);
			canvas.setCursor(tool.getCursor());
		}
		canvas.setCurrentSelectedObject(null);
	}
}
