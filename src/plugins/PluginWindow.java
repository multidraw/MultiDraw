package plugins;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import utils.filefilters.ClassFileFilter;
import utils.filefilters.ImageFileFilter;
import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class PluginWindow extends JDialog implements ActionListener{
	private DrawingCanvasView canvas;

	private JTextField imageField, descriptionField, nameField, toolField, shapeField;

	public PluginWindow(DrawingCanvasView c){
		canvas = c;

		setLayout(new GridLayout(6, 3, 10, 10));

		/* Add the image row */
		imageField = new JTextField(20);
		imageField.setEditable(false);
		JButton imageBrowse = new JButton("Browse...");
		imageBrowse.setActionCommand("image");
		imageBrowse.addActionListener(this);

		add(new JLabel("Pick image to be displayed:"));
		add(imageField);
		add(imageBrowse);

		/* Add the description row */
		descriptionField = new JTextField(20);

		add(new JLabel("Enter the tool description:"));
		add(descriptionField);
		add(new JLabel());

		/* Add the name row */
		nameField = new JTextField(10);

		add(new JLabel("Enter the name:"));
		add(nameField);
		add(new JLabel());

		/* Add the tool row */
		toolField = new JTextField(20);
		JButton toolBrowse = new JButton("Browse...");
		toolBrowse.setActionCommand("tool");
		toolBrowse.addActionListener(this);

		add(new JLabel("Pick the tool plugin or enter pkg name:"));
		add(toolField);
		add(toolBrowse);

		/* Add the shape row */
		shapeField = new JTextField(20);
		JButton shapeBrowse = new JButton("Browse...");
		shapeBrowse.setActionCommand("shape");
		shapeBrowse.addActionListener(this);

		add(new JLabel("Pick the shape plugin or enter pkg name:"));
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
		setSize(new Dimension(700, 200));
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		try {
			if ( "tool".equals(cmd) ){
				toolField.setText(getFilepath(new ClassFileFilter()));
			} else if ( "shape".equals(cmd) ){
				shapeField.setText(getFilepath(new ClassFileFilter()));
			} else if ( "image".equals(cmd) ){
				imageField.setText(getFilepath(new ImageFileFilter()));
			} else if ( "import".equals(cmd) ){
				String shape = shapeField.getText();
				String tool = toolField.getText();
				File shapeFile = new File(shape);
				File toolFile = new File(tool);

				if ( tool.isEmpty() || shape.isEmpty() )
					JOptionPane.showMessageDialog(this, "No Shape Class or Tool Class entered!",  "Validation Error", JOptionPane.ERROR_MESSAGE);
				else if ( !toolFile.exists() ){
					try{
						Class.forName(tool);	
					} catch (ClassNotFoundException e1){
						JOptionPane.showMessageDialog(this, "No Tool found by Class " + tool, "Validation Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if ( !shapeFile.exists() ){
					try{
						Class.forName(shape);
					} catch (ClassNotFoundException e1){
						JOptionPane.showMessageDialog(this, "No Shape found by Class " + shape, "Validation Error", JOptionPane.ERROR_MESSAGE);	
					}	
				} else {
					canvas.importPlugin(new Plugin(imageField.getText(), descriptionField.getText(),
							nameField.getText(), toolField.getText(), shapeField.getText()));
					dispose();
				}

			} else if ( "cancel".equals(cmd) ){
				dispose();
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Class Files are invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Returns the filepath of the selected file from a new FileChooser.
	 * @return String - The filepath of the string.
	 */
	private String getFilepath(FileFilter f){
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(f);

		int returnVal = fc.showOpenDialog(this);

		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			return fc.getSelectedFile().getAbsolutePath();
		} else return "";
	}
}
