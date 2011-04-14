package application;

import items.OpenMenuItem;
import items.SaveMenuItem;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
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
	
	protected String userName;
	protected String sessionName;
		
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
		userName = startup.login();
		sessionName = startup.chooseSession();
		
		
		
		setContentPane(new JTabbedPane());
		
		//Create Canvas Pane
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
		//End Create Canvas Pane
		
		//Create Session Pane
		JPanel sessionPane = new JPanel();
		sessionPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		MenuBarView sessionMenuBar = createMenuBarView(toolList, new FileMenuItemController( new OpenMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)),
				new FileMenuItemController(new SaveMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 6;
		sessionPane.add(sessionMenuBar, c);

		JLabel sessionHost = new JLabel("Current Drawing Host: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0.25;
		c.gridwidth = 1;
		sessionPane.add(sessionHost, c);
		
		JTextField hostName = new JTextField();
		hostName.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.gridy = 1;
		c.gridx = 4;
		c.weightx = 0.75;
		c.gridwidth = 1;
		sessionPane.add(hostName, c);

		JTextField sessionMembers = new JTextField();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.weightx = 0.95;
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 5;
		c.ipady = 250;
		sessionPane.add(sessionMembers, c);
		
		JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL);
		vbar.setUnitIncrement(2);
		vbar.setBlockIncrement(1);
		vbar.addAdjustmentListener(new AdjustmentListener(){

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				//slides the list of sessions up and down
			}
			
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.gridy = 2;
		c.gridx = 5;
		c.weightx = 0.05;
		c.gridwidth = 1;
		c.ipady = 250;
		sessionPane.add(vbar, c);
	
		JButton changeDrawer = new JButton("Change Editor");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.weightx = 0.0;
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 1;
		c.ipady = 0;
		sessionPane.add(changeDrawer, c);
		//End Create Session Pane
		
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
		frame.setLocation(100, 100);
		frame.setMinimumSize(new Dimension(600, 450));
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
