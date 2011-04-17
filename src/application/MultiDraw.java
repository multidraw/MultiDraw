package application;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;

import tools.ToolList;
import utils.ServerUtil;
import views.GuiView;
import views.LoginView;
import views.SessionView;

/**
 * Represents the MiniDraw program and can be initialized as either an
 * application or an applet. This contains references to the main components of
 * MiniDraw and is responsible for initializing those references and any
 * associations.
 */
@SuppressWarnings("serial")
public class MultiDraw extends JApplet {
	public LoginView loginView;
	public SessionView sessionView;
	public GuiView	guiView;
	
	public ToolList toolList;
	public boolean isApplet = false;
	public JFrame frame;

	public MultiDraw(boolean isApplet) {
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
		guiView = new GuiView(isApplet);
		getContentPane().add(guiView);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(getContentPane(), BorderLayout.CENTER);
		frame.addWindowListener(new AppCloser());
		frame.pack();
		frame.setVisible(true);
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
