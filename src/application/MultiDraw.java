package application;

import items.OpenMenuItem;
import items.SaveMenuItem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import tools.EraserTool;
import tools.FreehandTool;
import tools.SelectTool;
import tools.TextTool;
import tools.ToolList;
import tools.TwoEndShapeTool;
import tools.shapes.LineShape;
import tools.shapes.OvalShape;
import tools.shapes.RectangleShape;
import views.ControlPanelView;
import views.DrawingCanvasView;
import views.MenuBarView;
import views.ToolBarView;
import controllers.FileMenuItemController;
import controllers.ToolController;

/**
 * Represents the MiniDraw program and can be initialized as either an
 * application or an applet. This contains references to the main components of
 * MiniDraw and is responsible for initializing those references and any
 * associations.
 */
@SuppressWarnings("serial")
public class MultiDraw extends JApplet {
	
	protected DrawingCanvasView canvas;
	protected ControlPanelView controlPanel;
	protected ToolBarView toolBar;
	protected MenuBarView menuBar;
	
	protected ToolList toolList;
	protected boolean isApplet = false;

	public MultiDraw(boolean isApplet) {
		this.isApplet = isApplet;
		if (!isApplet) {
			init();
		}
	}

	public MultiDraw() {
		/* invoked as Applet */
		this(true);
	}

	/**
	 * Invoked implicitly if MiniDraw is executed as an applet. Otherwise, the
	 * constructor will explicitly call this method to initialize and display
	 * all of the MiniDraw components.
	 */
	public void init() {
		StartupScreen startup = new StartupScreen(this);
		startup.login();
		startup.chooseSession();
		
		setContentPane(new JTabbedPane());
		
		JPanel canvasPane = new JPanel();
		canvasPane.setLayout(new BorderLayout());
		canvas = createDrawingCanvas();
		canvasPane.add(canvas, BorderLayout.CENTER);
		controlPanel = createControlPanelView();
		canvas.setControlPanelView(controlPanel);
				
		canvasPane.add(controlPanel, BorderLayout.SOUTH);
		toolList = createToolList();
		toolBar = createToolBarView(toolList);
		canvasPane.add(toolBar, BorderLayout.WEST);
		menuBar = createMenuBarView(toolList, new FileMenuItemController( new OpenMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)),
				new FileMenuItemController(new SaveMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)));
		canvasPane.add(menuBar, BorderLayout.NORTH);
		
		JPanel sessionPane = new JPanel();
		
		JPanel toolDesignerPane = new JPanel();

		getContentPane().add("Session",sessionPane);
		getContentPane().add("Canvas",canvasPane);
		getContentPane().add("Tool Designer",toolDesignerPane);

	}

	/**
	 * Initialize a new DrawingCanvas
	 */
	protected DrawingCanvasView createDrawingCanvas() {
		return new DrawingCanvasView();
	}

	/**
	 * Initialize a new ControlPanelView
	 **/
	protected ControlPanelView createControlPanelView()
			throws NullPointerException {
		if (canvas != null) {
			return new ControlPanelView(canvas);
		} else {
			throw new NullPointerException("Canvas not initialized.");
		}
	}

	/**
	 * Initialize a new ToolBarView
	 * 
	 * @param toolList
	 *            list of tools to display on the view; null list is accepted
	 * @return newly initialized ToolBarView with indicated tools included
	 **/
	protected ToolBarView createToolBarView(ToolList toolList) {
		return new ToolBarView(toolList);
	}

	/**
	 * Initialize a new MenuBarView
	 * 
	 * @param toolList
	 *            list of tools to display on the view; null list is accepted
	 * @param items
	 * 			  all the menu options you could ever want
	 * @return newly initialized ToolBarView with indicated tools included
	 **/
	protected MenuBarView createMenuBarView(ToolList toolList, FileMenuItemController ... items) {
		return new MenuBarView(toolList, items);
	}

	/**
	 * Configure tool list used for ToolBar and MenuBar construction.
	 * 
	 * ToolList is initialized by adding several ToolControllers.
	 * ToolControllers extend the abstract action class allowing easy display of
	 * the tool via a menu and a tool bar button. More details are provided in
	 * the ToolController documentation.
	 **/
	protected ToolList createToolList() {
		ToolList actions = new ToolList();

		actions.add(new ToolController("Line", getImageIcon("images/line.png"),
				"Line drawing tool", canvas, new TwoEndShapeTool(canvas,
						new LineShape())));

		actions.add(new ToolController("Rectangle",
				getImageIcon("images/rect.png"), "Rectangle drawing tool",
				canvas, new TwoEndShapeTool(canvas, new RectangleShape())));

		actions.add(new ToolController("Oval", getImageIcon("images/oval.png"),
				"Oval drawing tool", canvas, new TwoEndShapeTool(canvas,
						new OvalShape())));

		 actions.add(
		 new ToolController("Freehand",
		 getImageIcon("images/freehand.png"),
		 "freehand drawing tool",
		 canvas,
		 new FreehandTool(canvas)));

		 actions.add(
		 new ToolController("Text",
		 getImageIcon("images/text.png"),
		 "text drawing tool",
		 canvas,
		 new TextTool(canvas)));

		 actions.add(
		 new ToolController("Eraser",
		 getImageIcon("images/eraser.png"),
		 "Eraser drawing tool",
		 canvas,
		 new EraserTool(canvas)));

		actions.add(new ToolController("Select",
				getImageIcon("images/select.png"), "Selector Tool", canvas,
				new SelectTool(canvas)));

		return actions;
	}

	/**
	 * Attempts to load an Image from the local disk.
	 * 
	 * Retrieving the current working directory varies whether the program was
	 * executed as an Applet or an Application. Assumes that the images will be
	 * located at the top directory of the binary files.
	 * 
	 * @param fileName
	 *            file name of the image relative to the current working
	 *            directory
	 * @return new ImageIcon
	 */
	protected ImageIcon getImageIcon(String fileName) {
		URL url;
		if (isApplet) {
			try {
				url = MultiDraw.class.getResource(fileName);
			} catch (Exception e) {
				return null;
			}
			return new ImageIcon(url);
		} else {
			return new ImageIcon(fileName);
		}
	}

	/* Main method */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		
		
		frame.setTitle("MultiDraw");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new MultiDraw(false), BorderLayout.CENTER);
		frame.addWindowListener(new AppCloser());
		frame.pack();
		frame.setSize(600, 450);
		frame.setMinimumSize(new Dimension(500, 350));
		frame.setVisible(true);
	}

	/**
	 * Inner class for terminating the application.
	 * 
	 * When executed as an application, closing the window does not necessarily
	 * trigger application termination. This class catches the window closing
	 * event and terminates the application.
	 */
	static class AppCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
