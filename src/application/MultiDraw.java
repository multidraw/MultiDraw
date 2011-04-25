package application;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;

import rmi.server.MultiDrawServer;
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
	public GuiView guiView;
	public SessionView sView;
	
	public ToolList toolList;
	public boolean isApplet = false;
	public JFrame frame;
	
	public MultiDrawState state;
	public ServerUtil utilInstance;
	
	public MultiDraw(boolean isApplet, ServerUtil serv) {
		this.utilInstance = serv;
		this.isApplet = isApplet;
		this.frame  = new JFrame();
		if (!isApplet) {
			init();
		}
	}

	public MultiDraw(ServerUtil serv) {
		/* invoked as Applet */
		this(true, serv);
	}

	/**
	 * Invoked implicitly if MiniDraw is executed as an applet. Otherwise, the
	 * constructor will explicitly call this method to initialize and display
	 * all of the MiniDraw components.
	 */
	public void init() {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new AppCloser(this));
		showLoginWindow();
	}
	
	public void showLoginWindow(){
		frame.setMinimumSize(new Dimension(0,0));
		state = MultiDrawState.AUTH_SCREEN;
		LoginView lview = new LoginView(this);
		lview.show(getContentPane(), frame);
	}
	
	public void showSessionsWindow(){
		frame.setMinimumSize(new Dimension(0,0));
		state = MultiDrawState.SESSIONS_SCREEN;
		sView = new SessionView(this);
		sView.show(getContentPane(), frame);
	}
	
	public void showGUIWindow(){
		state = MultiDrawState.GUI_SCREEN;
		guiView = new GuiView(isApplet, this);
		guiView.show(getContentPane(), frame, null);
	}
	
	public MultiDrawServer getServerInstance() {
		return utilInstance.getServerInstance();
	}
	
	private enum MultiDrawState {
		AUTH_SCREEN,
		SESSIONS_SCREEN,
		GUI_SCREEN;
	}
	
	public class AppCloser extends WindowAdapter {
		private MultiDraw md;
	
		public AppCloser(MultiDraw m){
			md = m;
		}
		
		public void windowClosing(WindowEvent e) {
			try {
				utilInstance.getServerInstance().logout(utilInstance.getUserName(), utilInstance.getSession());
				switch ( md.state ){
				case AUTH_SCREEN:
					System.exit(0);
					break;
				case SESSIONS_SCREEN:
					md.showLoginWindow();
					break;
				case GUI_SCREEN:
					md.showSessionsWindow();
					break;
				}
			} catch(Exception err) {
				err.printStackTrace();
			} 
		}
	}
}
