package items;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class OpenMenuItem extends FileMenuItem {
	
	public OpenMenuItem(DrawingCanvasView c){
		super("Open", c);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilter(){
			
			public final String multiExt = "mdw";

			@Override
			public boolean accept(File f) {
				if ( getExtension(f) == multiExt )
					return true;
				return false;
			}

			public String getDescription() {
				return "MultiDraw Drawings (*.mdw)";
			}
			
			/**
			 * Returns the extension of a file.
			 * @param f File to grab the extension
			 * @return String the file extension
			 */
			private String getExtension(File f){
				String ext = null, s = f.getName();
				int i = s.lastIndexOf(".");
				
				if ( i > 0 && i < s.length() - 1 ){
					ext = s.substring(i+1).toLowerCase();
				}
				return ext;
			}
		});
		
		int returnVal = fc.showOpenDialog(canvas);
		
		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();
			// Open the file here
		}
	}

}
