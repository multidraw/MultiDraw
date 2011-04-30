package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLayeredPane;

import plugins.Plugin;

import tools.Tool;
import tools.shapes.CanvasShape;
import utils.ServerUtil;
import controllers.DrawingCanvasController;

/**
 * Main view of the MiniDraw program which serves a both a view and a model. The
 * model component is specified below, and the view component displays the image
 * contents of the model.
 * 
 * To prevent screen flicker when drawing or resizing, an ImageBuffer is used.
 * All drawing from the associated tools is executed on imageBuffer, which is
 * drawn to screen on updates which are specified by the tools.
 */
@SuppressWarnings("serial")
public class DrawingCanvasView extends JLayeredPane {
	protected DrawingCanvasController canvasController;
	protected ControlPanelView controlPanelView;
	public final Color BACKGROUND = Color.white;
	protected Color penColor = Color.black;
	protected Color savedColor;
	protected boolean shapeFilled;
	protected Tool currentTool;
	protected int canvasHeight;
	protected int canvasWidth;

	protected ArrayList<CanvasShape> shapes = new ArrayList<CanvasShape>();
	protected Integer currentSelectedObject = null;
	protected Graphics2D imageBufferGraphics;
	protected Image imageBuffer;
	protected ServerUtil utilInstance;
	private GuiView gView;

	/**
	 * Creates a default DrawingCanvas with a white background
	 */
	public DrawingCanvasView(ServerUtil utilInstance, GuiView gView) {
		canvasController = createDrawingCanvasController();
		addDrawingCanvasListener(canvasController);
		setBackground(BACKGROUND);
		this.utilInstance = utilInstance;
		this.gView = gView;
	}

	protected DrawingCanvasController createDrawingCanvasController() {
		return new DrawingCanvasController(this);
	}

	protected void addDrawingCanvasListener(DrawingCanvasController listener) {
		if (listener != null) {
			addMouseListener((MouseListener) listener);
			addMouseMotionListener((MouseMotionListener) listener);
			addKeyListener((KeyListener) listener);
		}
	}

	/**
	 * Painting the DrawingCanvas is simply displaying the contents of the
	 * imageBuffer, which is the temporary graphic.
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(imageBuffer, 0, 0, this);
	}

	/**
	 * Paints over the drawing canvas in the background color
	 */
	public void clearCanvas() {
		setObjects(new ArrayList<CanvasShape>());
		currentSelectedObject = null;
		imageBufferGraphics.setColor(BACKGROUND);
		imageBufferGraphics.fillRect(0, 0, canvasWidth, canvasHeight);
		imageBufferGraphics.setColor(penColor);
		repaint();
	}

	/**
	 * Updates the current drawing color
	 * 
	 * @param c
	 *            new drawing color
	 */
	public void setPenColor(Color c) {
		if (c != null) {
			penColor = c;
			imageBufferGraphics.setColor(c);
		}
	}

	/**
	 * Accessor method for current drawing color
	 * 
	 * @return current drawing color
	 */
	public Color getPenColor() {
		return penColor;
	}

	public boolean isFilledShape() {
		return shapeFilled;
	}

	public void setFilledShape(boolean filled) {
		this.shapeFilled = filled;
	}

	/**
	 * Updates current drawing tool
	 * 
	 * @param t
	 *            new drawing tool
	 */
	public void setCurrentTool(Tool t) {
		if (t != null)
			currentTool = t;
	}

	/**
	 * Accessor method for current drawing tool
	 * 
	 * @return current drawing tool
	 */
	public Tool getCurrentTool() {
		return currentTool;
	}

	/**
	 * Accessor method for imageBuffer
	 * 
	 * @return current image buffer graphics context
	 */
	public Graphics2D getImageBufferGraphics() {
		return imageBufferGraphics;
	}

	/**
	 * Adjusts the size of the DrawingCanvas as well as the imageBuffer to match
	 * the new DrawingCanvas size.
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		canvasHeight = height;
		canvasWidth = width;

		// Set up double buffering
		Image newimageBuffer = createImage(width, height);
		imageBufferGraphics = (Graphics2D) newimageBuffer.getGraphics();
		if (imageBuffer != null) {
			imageBufferGraphics.drawImage(imageBuffer, 0, 0, this);
		}
		imageBuffer = newimageBuffer;

		setPenColor(penColor);
		imageBufferGraphics.setFont(new Font("Serif", Font.PLAIN, 24));
		refreshCanvas();
	}

	public void refreshCanvas() {
		Collections.reverse(shapes);
		imageBufferGraphics.setColor(BACKGROUND);
		imageBufferGraphics.fillRect(0, 0, canvasWidth, canvasHeight);
		imageBufferGraphics.setColor(penColor);

		for (CanvasShape shape : shapes) {
			if (shape.isSelected())
				shape.highlightSelected(imageBufferGraphics);

			shape.redraw(imageBufferGraphics);
		}
		Collections.reverse(shapes);
		repaint();
	}

	public void removeSelection() {
		if (shapes != null && shapes.size() != 0
				&& currentSelectedObject != null) {
			shapes.get(currentSelectedObject).setSelected(false);
			shapes.get(currentSelectedObject).highlightSelected(
					imageBufferGraphics);
			updateObject(shapes.get(currentSelectedObject));
			currentSelectedObject = null;
			refreshCanvas();
		}
	}

	public void enableXORMode() {
		savedColor = imageBufferGraphics.getColor();
		imageBufferGraphics.setXORMode(Color.LIGHT_GRAY);
		imageBufferGraphics.setColor(BACKGROUND);
	}

	public void enablePaintMode() {
		imageBufferGraphics.setPaintMode();
		imageBufferGraphics.setColor(savedColor);
	}

	/**
	 * Accessor method for objects
	 * 
	 * @return list of objects on the canvas
	 */
	public ArrayList<CanvasShape> getObjects() {
		return shapes;
	}

	/**
	 * Setter for objects
	 * 
	 * @param objects
	 */
	public void setObjects(ArrayList<CanvasShape> list) {
		setObjects(list, true);
	}
	
	public void setObjects(ArrayList<CanvasShape> list, boolean isMine) {
		this.shapes = list;
		try {
			if (isMine) {
				utilInstance.getServerInstance().setCanvas(utilInstance.getUserName(), utilInstance.getSession().name, list);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void updateObject(CanvasShape shape) {
		updateObject(shape, true);
	}
	public void updateObject(CanvasShape shape, boolean isMine) {
		if (containsObject(shape)) {
			shapes.set(shapes.indexOf(shape), shape);
		} else {
			shapes.add(0, shape);
		}
		try {
			if (isMine && utilInstance.getSession().getDrawer().equals(utilInstance.getUserName())) {
				utilInstance.getServerInstance().updateCanvas(utilInstance.getUserName(), utilInstance.getSession().name, shape,
						false);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public boolean removeObject(CanvasShape shape) {
		return removeObject(shape, true);
	}
	
	public boolean removeObject(CanvasShape shape, boolean isMine) {
		if (containsObject(shape)) {
			shapes.remove(shapes.indexOf(shape));
			currentSelectedObject = null;
			try {
				if (isMine) {
					utilInstance.getServerInstance().updateCanvas(utilInstance.getUserName(), utilInstance.getSession().name, shape,
							true);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean containsObject(CanvasShape shape) {
		return (shapes.indexOf(shape) != -1);
	}

	public CanvasShape getObject(int index) {
		return shapes.get(index);
	}

	public Integer getCurrentSelectedObject() {
		return currentSelectedObject;
	}

	public void setCurrentSelectedObject(Integer currentSelectedObject) {
		this.currentSelectedObject = currentSelectedObject;
	}

	public ControlPanelView getControlPanelView() {
		return controlPanelView;
	}

	public void setControlPanelView(ControlPanelView controlPanelView) {
		this.controlPanelView = controlPanelView;
	}

	public void drawObjects(ArrayList<CanvasShape> importedShapes) {
		for (CanvasShape shape : importedShapes) {
			shape.redraw(imageBufferGraphics);
			shapes.add(0, shape);
		}
		repaint();
	}
	
	public void importPlugin(Plugin plugin){
		
	}
}
