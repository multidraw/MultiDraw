package rmi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl extends UnicastRemoteObject implements MultiDrawClient {

	private static final long serialVersionUID = -3431518669864255149L;
	public transient MultiDraw mD;
	
	public ClientImpl() throws RemoteException{
		mD = new MultiDraw(false, new ServerUtil(this));
	}

	@SuppressWarnings("unchecked")
	public <T> void update(T update, HashMap<String, Object> options)
			throws RemoteException {
		
		if ( update instanceof CanvasShape ){
			CanvasShape newShape = (CanvasShape)update;
			boolean isRemoved = ( options.get("removed") == null ) ? false : (Boolean) options.get("removed");
			if ( isRemoved ) {
				mD.guiView.getCanvas().removeObject(newShape, false);
			} else {
				mD.guiView.getCanvas().updateObject(newShape, false);
			}
			mD.guiView.getCanvas().refreshCanvas();
		} else if ( update instanceof ArrayList ){
			ArrayList<String> updateList = (ArrayList<String>)update;
			
			if ( options == null ){
				mD.sView.updateSessionList(updateList);
			} else {
				String session = (String) options.remove("joinSession");
				if ( session != null ){
					mD.sView.updateSessionUserList(session, updateList);
					if ( mD.guiView != null )
						mD.guiView.fillSessionMemberList();
				}
			}	
		}
		if ( mD.guiView != null )
			mD.guiView.getCanvas().refreshCanvas();
	}
	
	public static void main(String args[]) {
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		try {
			new ClientImpl();

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
