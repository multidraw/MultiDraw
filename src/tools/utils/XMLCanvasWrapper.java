package tools.utils;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import tools.shapes.CanvasShape;

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
