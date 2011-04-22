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
	
	public ClientImpl() throws RemoteException{
		ServerUtil.setClient(this);
		mD = new MultiDraw(false);
	}

	@Override
	public void updateCanvas(CanvasShape changedShape, boolean isRemoved)
			throws RemoteException {
		if(isRemoved) {
			mD.guiView.getCanvas().removeObject(changedShape, false);
		} else {
			System.out.println(mD + " " + mD.guiView.getCanvas());
			mD.guiView.getCanvas().addObject(changedShape, false);
		}
		mD.guiView.getCanvas().refreshCanvas();
		System.out.println(ServerUtil.getUserName());
	}
	
	public static void main(String args[]) {
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		try {
			new ClientImpl();
			
			System.out.println("Client ready");

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		}
	}

}
