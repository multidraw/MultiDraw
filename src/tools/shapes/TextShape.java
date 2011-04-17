package tools.shapes;

import java.awt.Graphics2D;
import java.awt.Point;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("TextShape")
public class TextShape extends CanvasShape {
	
	private String text = "";

	@Override
	public void draw(Graphics2D g, int x0, int y0, int x1, int y1) {
		borders.x = x0;
		borders.y = y0;
		borders.height = y1;
		borders.width = x1;
		
		borders.update();
	}

	@Override
	public void redraw(Graphics2D g) {
		g.setColor(getColor());
		g.drawString(text, borders.x + 3, borders.y + 19);
	}
	
	public void updateString(StringBuffer txtBuf, boolean isShiftDown) {
		borders.width += (isShiftDown ? 18 : 12);
		borders.update();
		text = (txtBuf == null) ? text : txtBuf.toString(); 
	}
	
	public String getText() {
		return text;
	}

	@Override
	public void move(Graphics2D g, int deltaX, int deltaY) {
		borders.x += deltaX;
		borders.y += deltaY;	
		redraw(g);
	}

	@Override
	public void resize(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition) {}

	@Override
	public void resizeOutline(Graphics2D g, String corner, int deltaX, int deltaY, Point startPosition) {}

}
