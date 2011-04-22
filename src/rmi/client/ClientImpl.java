package rmi.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl implements MultiDrawClient {
	
	public transient MultiDraw mD;
	
	public ClientImpl(){
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
			ClientImpl client = new ClientImpl();
			
			Registry registry = LocateRegistry.createRegistry(1101);
			registry.bind("MultiDrawClient",
					(MultiDrawClient) UnicastRemoteObject.exportObject(
							client, 0));
			System.out.println("Client ready");

		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		}
	}

}
