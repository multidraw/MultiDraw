package views;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import tools.ToolList;

/**
 * Provides a Menu-type view displaying the MiniDraw Tools supplied. Menus are
 * general hierarchical and display options via text sometimes accompanied by
 * icons.
 * 
 * Displays the associated buttons tools within the supplied ToolList. The
 * ToolList itself contains the various tool controllers that will be associated
 * with this view.
 */
@SuppressWarnings("serial")
public class MenuBarView extends JMenuBar {

	/**
	 * Constructor
	 * 
	 * Registers the tools provided in the actions list for display as menu
	 * options. This constructor should not be accessed directly, but rather,
	 * the factory method provided by MiniDraw should be used.
	 * 
	 * Only ToolControllers that are enabled, i.e. the tool is not null, are
	 * added to the ToolBar.
	 * 
	 * @param actions associated MiniDraw tools
	 * @return Initialized MenuBarView
	 */
	public MenuBarView(ToolList actions) {
		JMenu toolMenu = new JMenu("Tool Menu");
		
		for(Object tool : actions.getToolList()) {
			Action a = (Action) tool;
			if (a.isEnabled())
				toolMenu.add(a);
		}
		add(toolMenu);
	}
}
