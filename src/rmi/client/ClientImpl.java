package rmi.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl extends UnicastRemoteObject implements MultiDrawClient {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3431518669864255149L;
	public transient MultiDraw mD;
	private ServerUtil serverU;
	
	public ClientImpl() throws RemoteException{
		this.serverU = new ServerUtil();
		serverU.setClient(this);
		mD = new MultiDraw(false, serverU);
	}

	@Override
	public void updateCanvas(CanvasShape changedShape, boolean isRemoved)
			throws RemoteException {
		if(isRemoved) {
			mD.guiView.getCanvas().removeObject(changedShape, false);
		} else {
			System.out.println(mD + " " + mD.guiView.getCanvas());
			mD.guiView.getCanvas().updateObject(changedShape, false);
		}
		mD.guiView.getCanvas().refreshCanvas();
	}
	
	public static void main(String args[]) {
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		try {
			ClientImpl t = new ClientImpl();
			System.out.println("Client ready " + t.serverU.getUserName());

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
