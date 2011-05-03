package rmi.client;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import plugins.Plugin;

import rmi.Session;
import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl extends UnicastRemoteObject implements MultiDrawClient {

	private static final long serialVersionUID = -3431518669864255149L;
	public transient MultiDraw md;

	public ClientImpl() throws RemoteException{
		super(1100);
		md = new MultiDraw(false, new ServerUtil(this));
	}

	@SuppressWarnings("unchecked")
	public <T> void update(T update, HashMap<String, Object> options) throws RemoteException {
		// Keep up their session if needed.
		Session session = (Session)options.remove("session");
		String method = (String) options.remove("method");
		if ( session != null && session.isActive(md.utilInstance.getUserName())){
			md.utilInstance.setSession(session);			
		}

		if ( "updateCanvas".equals(method)){
			CanvasShape newShape = (CanvasShape)update;
			boolean isRemoved = ( options.get("remove") == null ) ? false : (Boolean) options.get("remove");
			if ( isRemoved ) {
				md.guiView.getCanvas().removeObject(newShape, false);
			} else {
				md.guiView.getCanvas().updateObject(newShape, false);
			}
			md.guiView.getCanvas().refreshCanvas();
		} else if("setCanvas".equals(method)){
			md.guiView.getCanvas().setObjects((ArrayList<CanvasShape>) update, false);
			md.guiView.getCanvas().refreshCanvas();
		} else if ( "connectToSession".equals(method) || "leaveSession".equals(method) ){
			ArrayList<String> updateList = (ArrayList<String>)update;
			String joinSession;

			if ( (joinSession = (String)options.get("joinSession")) == null ){
				md.sView.updateSessionList(updateList);
			} else {
				md.sView.updateSessionUserList(joinSession, updateList);	
			}	
			
			if ( md.guiView != null )
				md.guiView.fillSessionMemberList();
		} else if( "passOffControl".equals(method) ) {
			md.guiView.fillSessionMemberList();
			if(md.utilInstance.getUserName().equals(options.get("newDrawer"))) {
				JOptionPane.showMessageDialog(md.getContentPane(), "You have been assigned drawing control. Go Wild!", 
						"Control Passed", JOptionPane.INFORMATION_MESSAGE);
				md.guiView.show(md.getContentPane(), md.frame, md.guiView.getCanvas());
			}
			else if (md.utilInstance.getUserName().equals(options.get("oldDrawer"))) {
				md.guiView.show(md.getContentPane(), md.frame, md.guiView.getCanvas());
			}
		} else if("killAllUsers".equals(method)) {
			md.serverDown = true;
			WindowEvent wev = new WindowEvent(md.frame, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		} else if ( update instanceof Plugin ){
			if ( md.guiView != null ){
				try {
					md.guiView.addPlugin((Plugin) update);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Refresh the canvas if something happened there.
		if ( md.guiView != null && md.guiView.getCanvas() != null && md.guiView.getCanvas().getImageBufferGraphics() != null)
			md.guiView.getCanvas().refreshCanvas();
	}

	public static void main(String args[]) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			new ClientImpl();
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
