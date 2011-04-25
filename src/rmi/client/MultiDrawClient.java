package rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface MultiDrawClient extends Remote {

	/**
	 * Server calls this for every active user in a session when the drawer changes an object.
	 * @param changedShape - The changed shape.
	 * @throws RemoteException
	 */
	public <T> void update(T changedShape, HashMap<String, Object> opts) throws RemoteException;
}
