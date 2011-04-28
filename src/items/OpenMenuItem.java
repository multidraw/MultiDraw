package items;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

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
public class OpenMenuItem extends FileMenuItem {

	XStream xstream;
	BufferedReader reader;

	public OpenMenuItem(DrawingCanvasView c){
		super("Open", c);
		xstream = new XStream(new DomDriver());
		xstream.processAnnotations(TextShape.class);
		xstream.processAnnotations(RectangleShape.class);
		xstream.processAnnotations(OvalShape.class);
		xstream.processAnnotations(LineShape.class);
		xstream.processAnnotations(FreehandShape.class);
		xstream.processAnnotations(XMLCanvasWrapper.class);
	}

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new MdwFileFilter());

		int returnVal = fc.showOpenDialog(canvas);

		if ( returnVal == JFileChooser.APPROVE_OPTION ){
			File file = fc.getSelectedFile();
			openReader(file);
			readAndConvert();
			closeReader();
		}
	}

	public void openReader(File file){
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public void readAndConvert() {
		String line = "";
		StringBuilder contents = new StringBuilder();
		canvas.clearCanvas();

		try {
			while ((line = reader.readLine()) != null) {
				contents.append(line + "\n");
			}

			XMLCanvasWrapper wrapper = (XMLCanvasWrapper) xstream.fromXML(contents.toString());
			canvas.setObjects(wrapper.getShapes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		canvas.refreshCanvas();
	}

	public void closeReader() {
		try {
			if (reader != null)
				reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();}
	}

}
