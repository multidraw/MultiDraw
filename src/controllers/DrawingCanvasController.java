package controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import tools.Tool;
import views.DrawingCanvasView;

public class DrawingCanvasController implements MouseListener, MouseMotionListener, KeyListener {

	protected DrawingCanvasView canvas;

	public DrawingCanvasController(DrawingCanvasView c) {
		canvas = c;
	}

	public void mousePressed(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mouseReleased(e);
		}
	}

	public void mouseDragged(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mouseDragged(e);
		}
	}

	public void mouseClicked(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mouseClicked(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mouseEntered(e);
		}
	}

	public void mouseExited(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mouseExited(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		Tool tool = canvas.getCurrentTool();
		if (tool != null) {
			tool.mouseMoved(e);
		}
	}

	public void keyPressed(KeyEvent e) {
		Tool tool = (Tool) canvas.getCurrentTool();
		if (tool != null) {
			tool.keyPressed(e);
		}
	}

	public void keyReleased(KeyEvent e) {
		Tool tool = (Tool) canvas.getCurrentTool();
		if (tool != null) {
			tool.keyReleased(e);
		}
	}

	public void keyTyped(KeyEvent e) {
		Tool tool = (Tool) canvas.getCurrentTool();
		if (tool != null) {
			tool.keyTyped(e);
		}
	}
}
