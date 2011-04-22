package application;

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
	public GuiView guiView;
	
	public ToolList toolList;
	public boolean isApplet = false;
	public JFrame frame;
	
	public MultiDrawState state;
	
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
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new AppCloser(this));
		showLoginWindow();
	}
	
	public void showLoginWindow(){
		state = MultiDrawState.AUTH_SCREEN;
		LoginView lview = new LoginView(this);
		lview.show(getContentPane(), frame);
	}
	
	public void showSessionsWindow(){
		state = MultiDrawState.SESSIONS_SCREEN;
		SessionView sview = new SessionView(this);
		sview.show(getContentPane(), frame);
	}
	
	public void showGUIWindow(){
		state = MultiDrawState.GUI_SCREEN;
		guiView = new GuiView(isApplet);
		guiView.show(getContentPane(), frame);
	}
	
	private enum MultiDrawState {
		AUTH_SCREEN,
		SESSIONS_SCREEN,
		GUI_SCREEN;
	}
	
	public static class AppCloser extends WindowAdapter {
		private MultiDraw md;
	
		public AppCloser(MultiDraw m){
			md = m;
		}
		
		public void windowClosing(WindowEvent e) {
			try {
				ServerUtil.getServerInstance().logout(ServerUtil.getUserName(), ServerUtil.getSession());
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
