package items;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import utils.MdwFileFilter;
import utils.Util;
import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class SaveMenuItem extends FileMenuItem {
	
	public SaveMenuItem(DrawingCanvasView c){
		super("Save", c);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new MdwFileFilter());
		fc.setSelectedFile(new File("temp.mdw"));
		int returnVal = fc.showSaveDialog(canvas);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();
			if ( file.exists() ){
				int response = JOptionPane.showConfirmDialog(null, file.getName() + " already exists.\nDo you want to replace it?", "Confirm Save As", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if ( response == JOptionPane.CANCEL_OPTION ) return;
			} 
			
			try {
				FileWriter fw = new FileWriter(file);
				String canvasXml = Util.getXML(canvas.getObjects());
				fw.write(canvasXml);
				fw.flush();
				fw.close();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Error in writing the file.", "I/O Error", JOptionPane.ERROR_MESSAGE);
			}
			
			
		}
	}
	
}
