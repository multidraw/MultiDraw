package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import tools.shapes.CanvasShape;

import com.thoughtworks.xstream.XStream;

public class Util {
	public static String getXML(ArrayList<CanvasShape> o){
		XStream xstream = new XStream();
		xstream.alias("shapes", ArrayList.class);
		
		return xstream.toXML(o);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<CanvasShape> getShapesFromFile(File xmlFile) throws FileNotFoundException{
		XStream xstream = new XStream();
		xstream.alias("shapes", ArrayList.class);
		
		return (ArrayList<CanvasShape>) xstream.fromXML(new FileReader(xmlFile));
	}
}
