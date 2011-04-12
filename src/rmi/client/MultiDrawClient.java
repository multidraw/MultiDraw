package rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tools.shapes.CanvasShape;

public interface MultiDrawClient extends Remote {

	/**
	 * Server calls this for every active user in a session when the drawer changes an object.
	 * @param changedShape - The changed shape.
	 * @throws RemoteException
	 */
	public void updateCanvas(CanvasShape changedShape, boolean isRemoved) throws RemoteException;
}
