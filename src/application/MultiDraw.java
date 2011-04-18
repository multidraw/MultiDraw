package application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;

import tools.ToolList;
import utils.ServerUtil;
import utils.StateMachine;
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
	
	public StateMachine sm = new StateMachine();

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
		LoginView lview = new LoginView(this);
		SessionView sview = new SessionView(this);
		GuiView gview = new GuiView(this, isApplet);
		
		sm.addStateTransition("Login", lview, sview);
		sm.addStateTransition("In Session", sview, gview);
		
		sm.start();
	}
	
	public static class AppCloser extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				ServerUtil.getServerInstance().logout(ServerUtil.getUserName(), ServerUtil.getSession());
			} catch(Exception err) {
				err.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
	}
}
