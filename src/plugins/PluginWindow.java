package plugins;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import utils.filefilters.ImageFileFilter;
import utils.filefilters.JarFileFilter;
import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class PluginWindow<T> extends JPanel implements ActionListener{
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

		add(new JLabel("PNG Image to be displayed:"));
		add(imageField);
		add(imageBrowse);

		/* Add the description row */
		descriptionField = new JTextField(20);

		add(new JLabel("Enter the tool description:"));
		add(descriptionField);
		add(new JLabel());

		/* Add the name row */
		nameField = new JTextField(10);

		add(new JLabel("Enter the tool name:"));
		add(nameField);
		add(new JLabel());

		/* Add the tool row */
		toolField = new JTextField(20);
		JButton toolBrowse = new JButton("Browse...");
		toolBrowse.setActionCommand("tool");
		toolBrowse.addActionListener(this);

		add(new JLabel("Pick the tool plugin (.jar) or enter pkg name of existing MultiDraw tool:"));
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

		add(importBtn);
		add(new JLabel());

		setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		try {
			if ( "tool".equals(cmd) ){
				toolField.setText(getFilepath(new JarFileFilter()));
			} else if ( "shape".equals(cmd) ){
				shapeField.setText(getFilepath(new JarFileFilter()));
			} else if ( "image".equals(cmd) ){
				imageField.setText(getFilepath(new ImageFileFilter()));
			} else if ( "import".equals(cmd) ){
				T shape = (T) shapeField.getText();
				T tool = (T) toolField.getText();
				File shapeFile = new File((String)shape);
				File toolFile = new File((String)tool);
				
				if ( ((String)tool).isEmpty() || ((String)shape).isEmpty() ) {
					JOptionPane.showMessageDialog(this, "No Shape Class or Tool Class entered!",  "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if ( !toolFile.exists() ){
					try{
						tool = (T) Class.forName((String)tool);	
					} catch (ClassNotFoundException e1){
						JOptionPane.showMessageDialog(this, "No Tool found by Class " + tool, "Validation Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else if ( !shapeFile.exists() ){
					try{
						shape = (T) Class.forName((String)shape);
					} catch (ClassNotFoundException e1){
						JOptionPane.showMessageDialog(this, "No Shape found by Class " + shape, "Validation Error", JOptionPane.ERROR_MESSAGE);
						return;
					}	
				} 
				
				if("".equals(imageField.getText())) {
					imageField.setText("images/tool.png");
				}
				if("".equals(descriptionField.getText())) {
					JOptionPane.showMessageDialog(this, "No Tool Description Specified", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if("".equals(nameField.getText())) {
					JOptionPane.showMessageDialog(this, "No Tool Name Specified", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				canvas.importPlugin(new Plugin(imageField.getText(), descriptionField.getText(), nameField.getText(), tool, shape));
				JOptionPane.showMessageDialog(this, "Tool Uploaded Successfully", "Tool Upload", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Class Files are invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
		} catch ( MalformedURLException e1){
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Class Filepaths are invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Returns the filepath of the selected file from a new FileChooser.
	 * @return String - The filepath of the string.
	 */
	private String getFilepath(FileFilter ... filters){
		JFileChooser fc = new JFileChooser();
		for ( FileFilter f : filters ){
			fc.addChoosableFileFilter(f);
		}
		
		int returnVal = fc.showOpenDialog(this);

		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			return fc.getSelectedFile().getAbsolutePath();
		} else return "";
	}
}
