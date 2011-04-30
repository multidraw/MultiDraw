package plugins;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import utils.filefilters.ClassFileFilter;
import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class PluginWindow extends JDialog implements ActionListener{
	private DrawingCanvasView canvas;
	
	private JTextField imageField, descriptionField, toolField, shapeField;

	public PluginWindow(DrawingCanvasView c){
		canvas = c;
		
		setLayout(new GridLayout(5, 3, 10, 10));
		
		/* Add the image row */
		imageField = new JTextField(20);
		JButton imageBrowse = new JButton("Browse...");
		imageBrowse.setActionCommand("image");
		imageBrowse.addActionListener(this);
		
		add(new JLabel("Pick image to be displayed:"));
		add(imageField);
		add(imageBrowse);
		
		/* Add the description row */
		descriptionField = new JTextField(20);

		add(new JLabel("Enter the tool description"));
		add(descriptionField);
		add(new JLabel());
		
		/* Add the tool row */
		toolField = new JTextField(20);
		toolField.setEditable(false);
		JButton toolBrowse = new JButton("Browse...");
		toolBrowse.setActionCommand("tool");
		toolBrowse.addActionListener(this);

		add(new JLabel("Pick the tool plugin to import:"));
		add(toolField);
		add(toolBrowse);
		
		/* Add the shape row */
		shapeField = new JTextField(20);
		shapeField.setEditable(false);
		JButton shapeBrowse = new JButton("Browse...");
		shapeBrowse.setActionCommand("shape");
		shapeBrowse.addActionListener(this);

		add(new JLabel("Pick the shape plugin to import:"));
		add(shapeField);
		add(shapeBrowse);
		
		/* Add the action row */
		JButton importBtn = new JButton("Import!");
		importBtn.setActionCommand("import");
		importBtn.addActionListener(this);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.setActionCommand("cancel");
		cancelBtn.addActionListener(this);
		
		add(importBtn);
		add(new JLabel());
		add(cancelBtn);
		
		pack();
		
		setResizable(false);
		setSize(new Dimension(400, 200));
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();

		if ( "tool".equals(cmd) ){
			toolField.setText(getFilepath());
		} else if ( "shape".equals(cmd) ){
			shapeField.setText(getFilepath());
		} else if ( "image".equals(cmd) ){
			imageField.setText(getFilepath());
		} else if ( "import".equals(cmd) ){
			canvas.importPlugin(new Plugin(imageField.getText(), descriptionField.getText(),
					toolField.getText(), shapeField.getText()));
			dispose();
		} else if ( "cancel".equals(cmd) ){
			dispose();
		}
	}
	
	private String getFilepath(){
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new ClassFileFilter());
		
		int returnVal = fc.showOpenDialog(this);
	
		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			return fc.getSelectedFile().getAbsolutePath();
		} else return "";
	}
}
