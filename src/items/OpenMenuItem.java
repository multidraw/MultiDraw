package items;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import utils.MdwFileFilter;
import utils.Util;
import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class OpenMenuItem extends FileMenuItem {
	
	public OpenMenuItem(DrawingCanvasView c){
		super("Open", c);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new MdwFileFilter());
		
		int returnVal = fc.showOpenDialog(canvas);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();

			try {
				canvas.drawObjects(Util.getShapesFromFile(file));				
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Error in reading the file.", "I/O Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
