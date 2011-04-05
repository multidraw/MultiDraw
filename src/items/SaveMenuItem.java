package items;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class SaveMenuItem extends FileMenuItem {
	
	public SaveMenuItem(DrawingCanvasView c){
		super("Save", c);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(canvas);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();
			// Convert the canvas or w/e here and save
		}
	}

}
