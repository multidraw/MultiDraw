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
		GuiView guiView = new GuiView(isApplet);
		add(guiView);
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
