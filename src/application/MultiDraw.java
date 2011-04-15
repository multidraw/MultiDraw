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
import javax.swing.KeyStroke;

import rmi.client.ClientImpl;
import tools.EraserTool;
import tools.FreehandTool;
import tools.SelectTool;
import tools.TextTool;
import tools.ToolList;
import tools.TwoEndShapeTool;
import tools.shapes.LineShape;
import tools.shapes.OvalShape;
import tools.shapes.RectangleShape;
import utils.ServerUtil;
import views.ControlPanelView;
import views.DrawingCanvasView;
import views.LoginView;
import views.MenuBarView;
import views.SessionView;
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
	
	public ClientImpl clientImpl;
	
	public DrawingCanvasView canvas;
	public ControlPanelView controlPanel;
	public ToolBarView toolBar;
	public MenuBarView menuBar;
	public LoginView loginView;
	public SessionView sessionView;
	
	public ToolList toolList;
	public boolean isApplet = false;
	public JFrame frame;


	public MultiDraw(boolean isApplet) {
		this.isApplet = isApplet;
		if (!isApplet) {
			init();
		}
	}
	
	public MultiDraw(boolean isApplet, ClientImpl clientImpl) {
		this.clientImpl = clientImpl;
		this.isApplet = isApplet;
		this.frame  = new JFrame();
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
		
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().validate();
		loginView = new LoginView();
		getContentPane().add(loginView);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(getContentPane(), BorderLayout.CENTER);
		frame.addWindowListener(new AppCloser());
		frame.pack();
		frame.setVisible(true);
		while(loginView.loggingIn);
		
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().validate();
		sessionView = new SessionView();
		getContentPane().add(sessionView);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(getContentPane(), BorderLayout.CENTER);
		frame.addWindowListener(new AppCloser());
		frame.pack();
		frame.setVisible(true);
		while(!sessionView.sessionSelected);
		
		getContentPane().removeAll();
		getContentPane().invalidate();
		getContentPane().validate();
		getContentPane().setLayout(new BorderLayout());
		canvas = createDrawingCanvas();
		getContentPane().add(canvas, BorderLayout.CENTER);
		controlPanel = createControlPanelView();
		canvas.setControlPanelView(controlPanel);
		
		
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		toolList = createToolList();
		toolBar = createToolBarView(toolList);
		getContentPane().add(toolBar, BorderLayout.WEST);
		menuBar = createMenuBarView(toolList, new FileMenuItemController( new OpenMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)),
				new FileMenuItemController(new SaveMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)));
		getContentPane().add(menuBar, BorderLayout.NORTH);
		frame.setTitle("MultiDraw");
		frame.pack();
		frame.setSize(600, 450);
		frame.setMinimumSize(new Dimension(500, 350));
		frame.setVisible(true);
	}

	/**
	 * Initialize a new DrawingCanvas
	 */
	protected DrawingCanvasView createDrawingCanvas() {
		return new DrawingCanvasView(clientImpl);
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
	
	public static class AppCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
			ServerUtil.getServerInstance().logout(ServerUtil.getUserName(), ServerUtil.getSession());
			} catch(Exception err) {
				err.printStackTrace();
			}
			System.exit(0);
		}
	}
}
