package rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultiDrawClient extends Remote {

	/**
	 * Server calls this for every active user in a session when the drawer changes an object.
	 * @param changedShape - The changed shape.
	 * @throws RemoteException
	 */
	public <T, V> void update(T changedShape, V opts) throws RemoteException;
}
