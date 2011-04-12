package rmi.client;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmi.server.MultiDrawServer;
import tools.shapes.CanvasShape;

public class ClientImpl implements MultiDrawClient {
	
	static public MultiDrawServer serverInstance;

	@Override
	public void updateCanvas(CanvasShape changedShape, boolean isRemoved)
			throws RemoteException {
		// TODO Auto-generated method stub

	}
	
	public static void main(String args[]) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("MultiDrawClient",
					(MultiDrawClient) UnicastRemoteObject.exportObject(
							new ClientImpl(), 0));
			System.out.println("Client ready");
			
			registry = LocateRegistry.getRegistry("Server Address"); //TODO get server address;
			serverInstance = (MultiDrawServer) registry.lookup("MultiDrawServer");
			System.out.println(InetAddress.getLocalHost());
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		}
	}

}
