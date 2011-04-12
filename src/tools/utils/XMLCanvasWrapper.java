package tools.utils;

import java.util.ArrayList;
import java.util.List;

import tools.shapes.CanvasShape;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CanvasShapes")
public class XMLCanvasWrapper {
	
	@XStreamAlias("Shapes")
	private List<CanvasShape> shapes;
	
	public XMLCanvasWrapper () {
		shapes = new ArrayList<CanvasShape>();
	}
	
	public void setShapes(List<CanvasShape> shapes) {
		this.shapes = shapes;
	}
	
	public List<CanvasShape> getShapes() { 
		return shapes;
	}
}
