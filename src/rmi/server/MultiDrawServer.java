package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import tools.shapes.CanvasShape;
import views.DrawingCanvasView;

public interface MultiDrawServer extends Remote {

	/**
	 * Client calls this when any shape is altered, added or removed
	 * @param session - the session key
	 * @param newShape - the newly drawn shape
	 * @param removed - if the object was removed or just moved/new
	 * @return true if it was successfully updated, false otherwise
	 * @throws RemoteException
	 */
	public boolean updateCanvas(String session, CanvasShape updatedShape, boolean removed) throws RemoteException;
	
	
	/**
	 * Client calls this if they have control and want to pass it off to someone
	 * else.
	 * @param session - the session key
	 * @param userName - the userName of the person they want to pass off to
	 * @return true if it was successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean passOffControl(String session, String userName) throws RemoteException;
	
	/**
	 * Client calls this if trying to connect to a session or create a new session
	 * @param session - the session key, if it is null the user wants to create a new session
	 * @param userName - the userName of the user
	 * @return the DrawingCanvasView associated with that session or a new drawing canvas view
	 * @throws RemoteException
	 */
	public DrawingCanvasView connectToSession(String session, String userName) throws RemoteException;
	
	/**
	 * Used to log a user into the system. 
	 * @param userName - the desired username of the user logging in
	 * @return true if it was a successful login, false otherwise 
	 * (e.g. userName is already in use)
	 * @throws RemoteException
	 */
	public boolean login(String userName, String ipAddress) throws RemoteException;
}
