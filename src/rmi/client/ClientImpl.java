package rmi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl extends UnicastRemoteObject implements MultiDrawClient {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3431518669864255149L;
	public transient MultiDraw mD;
	
	public ClientImpl() throws RemoteException{
		mD = new MultiDraw(false, new ServerUtil(this));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V> void update(T update, V opts)
			throws RemoteException {
		HashMap<String, Object> options = null;
		try {
			options = (HashMap<String, Object>)opts;  // Extract the options
		} catch ( ClassCastException e ){
			e.printStackTrace();
			return;
		}
	
		if ( update instanceof CanvasShape ){
			CanvasShape newShape = (CanvasShape)update;
			boolean isRemoved = ( options.get("removed") == null ) ? false : (Boolean) options.get("removed");
			if ( isRemoved ) {
				mD.guiView.getCanvas().removeObject(newShape, false);
			} else {
				System.out.println(mD + " " + mD.guiView.getCanvas());
				mD.guiView.getCanvas().updateObject(newShape, false);
			}
			mD.guiView.getCanvas().refreshCanvas();
		} else if ( update instanceof ArrayList ){
			ArrayList<String> sessions = (ArrayList<String>)update;
			mD.sView.updateSessionList(sessions);
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
