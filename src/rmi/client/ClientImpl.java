package rmi.client;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmi.server.MultiDrawServer;
import tools.shapes.CanvasShape;
import utils.ServerUtil;
import application.MultiDraw;

public class ClientImpl implements MultiDrawClient {
	
	public MultiDrawServer serverInstance;
	public MultiDraw mD;
	
	public void setServerInstance(MultiDrawServer serverInstance) {
		this.serverInstance = serverInstance;
		mD = new MultiDraw(false);
	}
	
	public ClientImpl(){}

	@Override
	public void updateCanvas(CanvasShape changedShape, boolean isRemoved)
			throws RemoteException {
		if(isRemoved) {
			mD.canvas.removeObject(changedShape, false);
		} else {
			mD.canvas.addObject(changedShape, false);
		}
		mD.canvas.refreshCanvas();
		System.out.println(ServerUtil.getUserName());
		// TODO Auto-generated method stub

	}
	
	public static void main(String args[]) {
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		try {
			ClientImpl client = new ClientImpl();
			
			Registry registry = LocateRegistry.createRegistry(1100);
			registry.bind("MultiDrawClient",
					(MultiDrawClient) UnicastRemoteObject.exportObject(
							client, 0));
			System.out.println("Client ready");

			
			System.out.println(InetAddress.getLocalHost());
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		}
	}

}
