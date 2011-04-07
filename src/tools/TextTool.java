package tools;

import java.awt.Font;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import tools.shapes.TextShape;
import views.DrawingCanvasView;

/**
 * Draws text on the canvas.
 * 
 * The user selects the starting point with the mouse and then types in the
 * desired text. Currently, the font is not selectable. A new text box will
 * begin when the user clicks on a different canvas location. Changing the
 * current tool, in effect, also stops current text entry.
 */
public class TextTool extends Tool {

	protected DrawingCanvasView canvas;
	protected Point startingPosition;
	protected StringBuffer text;
	protected Font font = new Font("Serif", Font.PLAIN, 24);
	protected TextShape shape;
	protected final String PERMITTED_CHARACTERS = "[\\w \\[\\]\\{\\}\\\\/\\^\\-\\=\\+\\|\\(\\)\\$\\?\\*!@#%&<>,\\.'\"`~:;]";

	public TextTool(DrawingCanvasView c) {
		if (c != null)
			canvas = c;
		else
			throw new IllegalArgumentException();
		cursor = new Cursor(Cursor.TEXT_CURSOR);
	}

	/*
	 * Returns focus to the drawing canvas and stores the starting location for
	 * the text display.
	 */
	public void mousePressed(MouseEvent e) {
		shape = new TextShape();

		canvas.requestFocus();
		startingPosition = e.getPoint();
		Graphics2D iBGraphics = canvas.getImageBufferGraphics();
		iBGraphics.setFont(font);
		text = new StringBuffer();
		shape.draw(iBGraphics, startingPosition.x - 3, startingPosition.y - 19,
				0, 24);
		shape.setColor(canvas.getPenColor());
		canvas.addObject(shape);
	}

	/*
	 * Adds a character to the string buffer
	 */
	public void keyTyped(KeyEvent e) {
		String nextChar = String.valueOf(e.getKeyChar());

		if (nextChar.matches(PERMITTED_CHARACTERS)) {
			Graphics2D iBGraphics = canvas.getImageBufferGraphics();

			text.append(nextChar);

			shape.updateString(text, e.isShiftDown());

			iBGraphics.drawString(text.toString(), startingPosition.x,
					startingPosition.y);

			canvas.repaint();
		}
	}
}
