package tools.utils;

import java.util.ArrayList;

import tools.shapes.CanvasShape;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CanvasShapes")
public class XMLCanvasWrapper {
	
	@XStreamAlias("Shapes")
	private ArrayList<CanvasShape> shapes;
	
	public XMLCanvasWrapper () {
		shapes = new ArrayList<CanvasShape>();
	}
	
	public void setShapes(ArrayList<CanvasShape> shapes) {
		this.shapes = shapes;
	}
	
	public ArrayList<CanvasShape> getShapes() { 
		return shapes;
	}
}
