package tools;

import java.util.Vector;

import controllers.ToolController;


/**
 * A basic vector list that is intended to contain ToolControllers.
 * 
 * Type checking is performed on object insertion, which forces a slight
 * performance penalty on program startup and for every subsequent tool that is
 * added to the list. This seems to balance extensibility with type checking by
 * confining updates to one location if an alternative ToolController class is
 * used.
 */
public class ToolList {

	private Vector<Object> toolList;

	public ToolList() {
		toolList = new Vector<Object>(5, 1);
	}

	/**
	 * Adds the specified ToolController to the list
	 * @param item ToolController to add
	 */
	public void add(ToolController item) {
		if (item != null)
			toolList.add(item);
	}

	public Vector<Object> getToolList() {
		return toolList;
	}
}
