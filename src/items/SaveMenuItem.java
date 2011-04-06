package items;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tools.shapes.FreehandShape;
import tools.shapes.LineShape;
import tools.shapes.OvalShape;
import tools.shapes.RectangleShape;
import tools.shapes.TextShape;
import tools.utils.XMLCanvasWrapper;
import utils.MdwFileFilter;
import views.DrawingCanvasView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@SuppressWarnings("serial")
public class SaveMenuItem extends FileMenuItem {

	XStream xstream;
	BufferedWriter writer;
	XMLCanvasWrapper wrapper;

	public SaveMenuItem(DrawingCanvasView c){
		super("Save", c);
		xstream = new XStream(new DomDriver());
		xstream.processAnnotations(TextShape.class);
		xstream.processAnnotations(RectangleShape.class);
		xstream.processAnnotations(OvalShape.class);
		xstream.processAnnotations(LineShape.class);
		xstream.processAnnotations(FreehandShape.class);
		xstream.processAnnotations(XMLCanvasWrapper.class);
		wrapper = new XMLCanvasWrapper();
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new MdwFileFilter());
		fc.setSelectedFile(new File("temp.mdw"));
		int returnVal = fc.showSaveDialog(canvas);

		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();

			if ( file.exists() ){
				int response = JOptionPane.showConfirmDialog(null, file.getName() +
						" already exists.\nDo you want to replace it?", 
						"Confirm Save As", JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.QUESTION_MESSAGE);
				if ( response == JOptionPane.CANCEL_OPTION ) return;
			} 

			openWriter(file);
			wrapper.setShapes(canvas.getObjects());
			appendToFile(xstream.toXML(wrapper));
			closeWriter();
		}
	}

	public void openWriter(File file) {
		try {
			writer = new BufferedWriter(new FileWriter(file));	
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "An error occurred while opening the new file.",
					"I/O Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "An error occurred while opening the new file.",
					"I/O Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void appendToFile(String xmlObject) {
		try {
			writer.append(xmlObject);
			writer.newLine();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "An error occurred while writing to the file.",
					"I/O Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	public void closeWriter() {
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "An error occurred while closing the file.",
					"I/O Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
