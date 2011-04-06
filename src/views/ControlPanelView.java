package views;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.ControlPanelController;



/**
 * Panel which provides options for clearing the canvas and selecting the 
 * current drawing tool.
 */
@SuppressWarnings("serial")
public class ControlPanelView extends JPanel {

	protected DrawingCanvasView canvas;
	protected ControlPanelController controlPanelController;
	protected JButton clearButton;
	protected JComboBox colorBox;
	protected JCheckBox fillBox;

	/**
	 * Adds the clear canvas button, the color combo box for selecting the
	 * drawing color, and the filled checkbox.  Creates and registers the view's controller.
	 * 
	 * @param c reference to the associated DrawingCanvas
	 */
	public ControlPanelView(DrawingCanvasView c) {
		if( c == null )
			throw new IllegalArgumentException();

		canvas = c;
		clearButton = new JButton("Clear Canvas");
		add(clearButton);

		add(new JLabel("Pen color:"));
		colorBox = new JComboBox();
		colorBox.addItem("black");
		colorBox.addItem("blue");
		colorBox.addItem("green");
		colorBox.addItem("red");
		add(colorBox);

		fillBox = new JCheckBox("Filled");
		add(fillBox);

		ControlPanelController CPcontroller = createControlPanelController();
		addControlPanelListener(CPcontroller);
	}

	/**
	 * Creates a controller associated with this view's canvas.
	 * 
	 * This is implemented separately from addControlPanelListener() in the 
	 * event that a controller for a subclass of this view differs from this
	 * view's controller.  For example, adding a different component to a
	 * ControlPanelView would require a different controller.
	 * 
	 * @return new controller
	 */
	protected ControlPanelController createControlPanelController() {
		return new ControlPanelController(canvas);
	}

	/**
	 * Registers a ControlPanelController with this view's components.
	 * 
	 * This is implemented separately from createControlPanelController() in the
	 * event that a view subclass requires different listener registration for
	 * the components displayed on that view.
	 * 
	 * @param listener controller to register
	 */
	protected void addControlPanelListener(ControlPanelController listener)  {
		if( listener != null ) {
			clearButton.addActionListener((ActionListener)listener);
			colorBox.addItemListener((ItemListener)listener);
			fillBox.addActionListener((ActionListener)listener);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void setFillBoxState(boolean filled) {
		fillBox.setSelected(filled);
	}
	
	public void setColorSelected(Color color) {
		String colorString;
		if(color.equals(Color.black)) {
			colorString = "black";
		} else if(color.equals(Color.blue)) {
			colorString = "blue";
		} else if(color.equals(Color.red)) {
			colorString = "red";
		} else {
			colorString = "green";
		}
		colorBox.setSelectedItem((Object) colorString);
	}
}
