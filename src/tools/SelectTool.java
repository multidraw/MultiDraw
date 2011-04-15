package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import tools.shapes.CanvasShape;
import tools.shapes.TwoEndShape;
import utils.Borders;
import views.DrawingCanvasView;

public class SelectTool extends Tool {

	protected DrawingCanvasView canvas;
	protected Color saveColor;
	protected Point startPosition;
	protected Point mostRecentPosition;
	protected int mostRecentDeltaX;
	protected int mostRecentDeltaY;

	protected CanvasShape selection;
	protected boolean activeSelection = false;
	protected String resizeCorner;
	protected boolean resizing = false;

	protected Cursor grab_cursor = new Cursor(Cursor.MOVE_CURSOR);
	protected Cursor hand_cursor = new Cursor(Cursor.HAND_CURSOR);

	public SelectTool(DrawingCanvasView canvas) {
		if (canvas != null) {
			this.canvas = canvas;
		} else {
			throw new IllegalArgumentException();
		}
		cursor = hand_cursor;
	}

	public void mousePressed(MouseEvent e) {
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();

		startPosition = e.getPoint();
		mostRecentPosition = startPosition;
		mostRecentDeltaX = 0;
		mostRecentDeltaY = 0;

		if (canvas.getObjects() != null) {
			boolean found = false;

			for (CanvasShape canvasObject : canvas.getObjects()) {
				Borders borders = canvasObject.getBorders();
				if ((borders.contains(startPosition, canvasObject) || canvasObject
						.isResizable()) && !found) {
					selection = canvasObject;

					if (canvasObject.isResizable()) {
						resizing = true;
						resizeCorner = borders.cornerHit(startPosition);
					} else {
						canvas.setCursor(grab_cursor);
					}
					activeSelection = true;
					found = true;
				}
			}
			canvas.removeSelection();
			selection = (found) ? selection : null;

			if (selection != null) {
				selection.setSelected(true);
				canvas.setCurrentSelectedObject(canvas.getObjects().indexOf(
						selection));
				selection.highlightSelected(iBGraphics);
				if (selection instanceof TwoEndShape) {
					canvas.setFilledShape(((TwoEndShape) selection).isFilled());
					canvas.getControlPanelView().setFillBoxState(
							canvas.isFilledShape());
				}
				
				canvas.setPenColor(selection.getColor());
				canvas.getControlPanelView().setColorSelected(selection.getColor());
			}

			canvas.enableXORMode();
			canvas.repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();
		Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER, 5.0f, new float[] { 10.0f }, 0.0f);
		Stroke savedStroke = iBGraphics.getStroke();

		if (activeSelection && resizing) {
			int deltaX = e.getPoint().x - startPosition.x;
			int deltaY = e.getPoint().y - startPosition.y;

			iBGraphics.setStroke(dashed);
			selection.resizeOutline(iBGraphics, resizeCorner, mostRecentDeltaX,
					mostRecentDeltaY, startPosition);
			selection.resizeOutline(iBGraphics, resizeCorner, deltaX, deltaY,
					startPosition);
			iBGraphics.setStroke(savedStroke);

			mostRecentDeltaX = deltaX;
			mostRecentDeltaY = deltaY;
		} else if (activeSelection) {
			iBGraphics.setStroke(dashed);
			iBGraphics.draw(new Line2D.Double(startPosition.x, startPosition.y,
					mostRecentPosition.x, mostRecentPosition.y));
			iBGraphics.draw(new Line2D.Double(startPosition.x, startPosition.y,
					e.getPoint().x, e.getPoint().y));
			iBGraphics.setStroke(savedStroke);
		}
		canvas.repaint();
		mostRecentPosition = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();
		canvas.enablePaintMode();

		if (activeSelection && resizing) {
			int deltaX = e.getPoint().x - startPosition.x;
			int deltaY = e.getPoint().y - startPosition.y;
			selection.resize(iBGraphics, resizeCorner, deltaX, deltaY,
					startPosition);
			canvas.refreshCanvas();
			resizing = false;
		} else if (activeSelection) {
			int deltaX = e.getPoint().x - startPosition.x;
			int deltaY = e.getPoint().y - startPosition.y;
			selection.move(iBGraphics, deltaX, deltaY);
			canvas.refreshCanvas();
		}
		activeSelection = false;
	}

	public void mouseMoved(MouseEvent e) {
		if (selection != null && !activeSelection && (selection instanceof TwoEndShape)) {
			if (selection.getBorders().hitNWCorner(e.getPoint())) {
				canvas.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
				selection.setResizable(true);
			} else if (selection.getBorders().hitNECorner(e.getPoint())) {
				canvas.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
				selection.setResizable(true);
			} else if (selection.getBorders().hitSECorner(e.getPoint())) {
				canvas.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
				selection.setResizable(true);
			} else if (selection.getBorders().hitSWCorner(e.getPoint())) {
				canvas.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
				selection.setResizable(true);
			} else if (selection.getBorders().contains(e.getPoint(), selection)) {
				canvas.setCursor(grab_cursor);
				selection.setResizable(false);
			} else {
				canvas.setCursor(hand_cursor);
				selection.setResizable(false);
			}
		}
	}
}
