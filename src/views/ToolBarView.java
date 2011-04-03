package views;

import javax.swing.Action;
import javax.swing.JToolBar;

import tools.ToolList;
  
/**
 * Provides a ToolBar-type view displaying the MiniDraw Tools supplied.  
 * Toolbars generally consist of picture-buttons sometimes accompanied with 
 * text.
 *
 * Displays the associated buttons tools within the supplied ToolList.  The
 * ToolList itself contains the various tool controllers that will be associated
 * with this view.
 * 
 * For more details, see the documentation of ToolController and ToolList.
 */
@SuppressWarnings("serial")
public class ToolBarView extends JToolBar {
  
  /**
   * Constructor
   * 
   * Registers the tools provided in the actions list for display as ToolBar
   * buttons.  This constructor should not be used directly, but rather, the
   * factory method provided by the MiniDraw class should be used.
   * 
   * Only ToolControllers that are enabled, i.e. the tool is not null, are added
   * to the ToolBar.
   * 
   * @param actions Associated MiniDraw ToolControllers
   * @return Initialized ToolBarView
   */
  public ToolBarView(ToolList actions) {
    super(VERTICAL);
	
	for(Object tool : actions.getToolList()) {
		Action a = (Action) tool;
		if (a.isEnabled())
			this.add(a);
	}
  }				  
}
