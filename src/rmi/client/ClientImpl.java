package rmi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import rmi.Session;
import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl extends UnicastRemoteObject implements MultiDrawClient {

	private static final long serialVersionUID = -3431518669864255149L;
	public transient MultiDraw md;

	public ClientImpl() throws RemoteException{
		md = new MultiDraw(false, new ServerUtil(this));
	}

	@SuppressWarnings("unchecked")
	public <T> void update(T update, HashMap<String, Object> options)
	throws RemoteException {

		// Keep up their session if needed.
		Session session = (Session)options.remove("session");
		String method = (String) options.remove("method");
		System.out.println(method);
		if ( session != null && session.isActive(md.utilInstance.getUserName())){
			md.utilInstance.setSession(session);			
		}

		if ( method.equals("updateCanvas")){
			CanvasShape newShape = (CanvasShape)update;
			boolean isRemoved = ( options.get("remove") == null ) ? false : (Boolean) options.get("remove");
			if ( isRemoved ) {
				md.guiView.getCanvas().removeObject(newShape, false);
			} else {
				md.guiView.getCanvas().updateObject(newShape, false);
			}
			md.guiView.getCanvas().refreshCanvas();
		} else if(method.equals("setCanvas")){
			md.guiView.getCanvas().setObjects((ArrayList<CanvasShape>) update, false);
			md.guiView.getCanvas().refreshCanvas();
		} else if ( method.equals("connectToSession") || method.equals("leaveSession") ){
			ArrayList<String> updateList = (ArrayList<String>)update;
			String joinSession;

			if ( (joinSession = (String)options.get("joinSession")) == null ){
				md.sView.updateSessionList(updateList);
			} else {
				md.sView.updateSessionUserList(joinSession, updateList);	
			}	
			
			if ( md.guiView != null )
				md.guiView.fillSessionMemberList();
		} else if( method.equals("passOffControl") ) {
			md.guiView.fillSessionMemberList();

			if(md.utilInstance.getUserName().equals(options.get("newDrawer"))) {
				JOptionPane.showMessageDialog(md.getContentPane(), "You have been assigned drawing control. Go Wild!", 
						"Control Passed", JOptionPane.WARNING_MESSAGE);
				md.guiView.show(md.getContentPane(), md.frame, md.guiView.getCanvas());
			}
			else if (md.utilInstance.getUserName().equals(options.get("oldDrawer"))) {
				md.guiView.show(md.getContentPane(), md.frame, md.guiView.getCanvas());
			}
		}
		
		// Refresh the canvas if something happened there.
		if ( md.guiView != null )
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
