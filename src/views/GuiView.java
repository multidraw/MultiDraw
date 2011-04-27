package views;

import items.OpenMenuItem;
import items.SaveMenuItem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import rmi.Session;
import tools.EraserTool;
import tools.FreehandTool;
import tools.SelectTool;
import tools.TextTool;
import tools.ToolList;
import tools.TwoEndShapeTool;
import tools.shapes.LineShape;
import tools.shapes.OvalShape;
import tools.shapes.RectangleShape;
import utils.UserName;
import application.MultiDraw;
import controllers.FileMenuItemController;
import controllers.ToolController;

@SuppressWarnings("serial")
public class GuiView extends JTabbedPane implements ActionListener {
	protected DrawingCanvasView canvas;
	protected ControlPanelView controlPanel;
	protected ToolBarView toolBar;
	protected MenuBarView menuBar;
	protected ToolList toolList;
	protected boolean isApplet;
	private JTextField hostName;
	private JList sessionMembers;
	private DefaultListModel listModel;
	private JButton changeDrawer;
	protected MultiDraw md;

	public GuiView(boolean isApplet, MultiDraw md) {
		this.isApplet = isApplet;	
		this.md = md;
	}


	/**
	 * Creates the Gui View for the first time and shows it on the frame.
	 * 
	 * @param contentPane
	 * @param frame
	 */
	public void show(Container contentPane, JFrame frame, DrawingCanvasView currentCanvas){
		contentPane.removeAll();
		contentPane.invalidate();
		contentPane.validate();
		
		//Create Canvas Pane
		JPanel canvasPane = new JPanel();
		canvasPane.setLayout(new BorderLayout());

		if(currentCanvas == null){
			canvas = new DrawingCanvasView(md.utilInstance);
		}
		else{
			canvas = currentCanvas;
			removeAll();
		}
		canvasPane.add(canvas, BorderLayout.CENTER);

		if(md.utilInstance.getUserName().equals(md.utilInstance.getSession().getDrawer())){	
			controlPanel = createControlPanelView();
			canvasPane.add(controlPanel, BorderLayout.SOUTH);
			toolList = createToolList();
			toolBar = new ToolBarView(toolList);
			canvasPane.add(toolBar, BorderLayout.WEST);
			menuBar = new MenuBarView(toolList, new FileMenuItemController( new OpenMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)),
					new FileMenuItemController(new SaveMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)));
			canvasPane.add(menuBar, BorderLayout.NORTH);
		} else{
			menuBar = new MenuBarView(new FileMenuItemController( new OpenMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)),
					new FileMenuItemController(new SaveMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)));
			canvasPane.add(menuBar, BorderLayout.NORTH);
		}

		//Create Session Pane
		JPanel sessionPane = new JPanel();
		sessionPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		MenuBarView sessionMenuBar = new MenuBarView(new FileMenuItemController( new OpenMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)),
				new FileMenuItemController(new SaveMenuItem(canvas), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 6;
		sessionPane.add(sessionMenuBar, c);

		JLabel sessionHost = new JLabel("Current Session: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0.25;
		c.gridwidth = 1;
		sessionPane.add(sessionHost, c);

		hostName = new JTextField(md.utilInstance.getSession().name);
		hostName.setEditable(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.gridy = 1;
		c.gridx = 4;
		c.weightx = 0.75;
		c.gridwidth = 1;
		sessionPane.add(hostName, c);

		sessionMembers = new JList();
		JScrollPane sp = new JScrollPane(sessionMembers);
		fillSessionMemberList();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.5;
		c.weightx = 0.95;
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 5;
		c.ipady = 250;
		sessionPane.add(sp, c);

		if( md.utilInstance.getUserName().equals(md.utilInstance.getSession().getDrawer()) ){

			changeDrawer = new JButton("Pass Control");
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weighty = 0.5;
			c.weightx = 0.0;
			c.gridy = 3;
			c.gridx = 0;
			c.gridwidth = 1;
			c.ipady = 0;
			sessionPane.add(changeDrawer, c);
			changeDrawer.addActionListener(this);
		}

		JPanel toolDesignerPane = new JPanel();

		add("Session",sessionPane);
		add("Canvas",canvasPane);
		add("Tool Designer",toolDesignerPane);
		contentPane.add(this);
		frame.setTitle("MultiDraw");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		frame.setMinimumSize(new Dimension(300,200));
		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * Fills the JList in the sessionView.
	 */
	public void fillSessionMemberList() {
		listModel = new DefaultListModel();

		Session session = md.utilInstance.getSession();
		for(String member : session.getActiveUsers()) {
			UserName user = new UserName(member, session.getDrawer().equals(member), 
					md.utilInstance.getUserName().equals(member));
			
			listModel.addElement(user);
		}

		sessionMembers.setModel(listModel);
		sessionMembers.setSelectedIndex(0);
	}

	/**
	 *  This is called when a user clicks the pass control button.
	 *  Makes sure the user is not passing to themselves
	 */
	public void actionPerformed(ActionEvent e){
		UserName passToUser = (UserName) sessionMembers.getSelectedValue();
		
		try { 
			if (listModel.getSize() > 1 && !md.utilInstance.getUserName().equals(passToUser.getUserName())) {
				md.getServerInstance().passOffControl(md.utilInstance.getSession().name, 
						md.utilInstance.getUserName(), passToUser.getUserName());
			}
			else 
				JOptionPane.showMessageDialog(this, "You are attempting to assign control to yourself", 
						"Can't Pass Control", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {

		}
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
				"Line drawing tool", canvas, new TwoEndShapeTool(canvas, new LineShape())));

		actions.add(new ToolController("Rectangle",
				getImageIcon("images/rect.png"), "Rectangle drawing tool",
				canvas, new TwoEndShapeTool(canvas, new RectangleShape())));

		actions.add(new ToolController("Oval", getImageIcon("images/oval.png"),
				"Oval drawing tool", canvas, new TwoEndShapeTool(canvas, new OvalShape())));

		actions.add(new ToolController("Freehand", getImageIcon("images/freehand.png"),
						"freehand drawing tool", canvas, new FreehandTool(canvas)));

		actions.add(new ToolController("Text", getImageIcon("images/text.png"),
						"text drawing tool", canvas, new TextTool(canvas)));

		actions.add(new ToolController("Eraser", getImageIcon("images/eraser.png"),
						"Eraser drawing tool", canvas, new EraserTool(canvas)));

		actions.add(new ToolController("Select", getImageIcon("images/select.png"), 
				"Selector Tool", canvas, new SelectTool(canvas)));

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

	public DrawingCanvasView getCanvas() {
		return canvas;
	}
}
