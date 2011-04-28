package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.Session;
import rmi.client.MultiDrawClient;
import tools.shapes.CanvasShape;

public interface MultiDrawServer extends Remote {

	/**
	 * Client calls this when any shape is altered, added or removed
	 * @param userName - name of drawer
	 * @param session - the session key
	 * @param newShape - the newly drawn shape
	 * @param removed - if the object was removed or just moved/new
	 * @return true if it was successfully updated, false otherwise
	 * @throws RemoteException
	 */
	public boolean updateCanvas(String userName, String session, CanvasShape updatedShape, boolean removed) throws RemoteException;
	
	
	/**
	 * Client calls this if they have control and want to pass it off to someone
	 * else.
	 * @param passer - the userName of the person that is passing control
	 * @param session - the session key
	 * @param receiver - the userName of the person they want to pass off to
	 * @return true if it was successful, false otherwise
	 * @throws RemoteException
	 */
	public boolean passOffControl(String session, String passer, String receiver) throws RemoteException;
	
	/**
	 * Client calls this if trying to connect to a session or create a new session
	 * @param session - the session key, if it is null the user wants to create a new session
	 * @param userName - the userName of the user
	 * @return the DrawingCanvasView associated with that session or a new drawing canvas view
	 * @throws RemoteException
	 */
	public ArrayList<CanvasShape> connectToSession(String session, String userName) throws RemoteException;
	
	/**
	 * Leaves the session with the given username.
	 * @param userName String - the user to leave the session
	 * @param session String - the sessionid
	 * @throws RemoteException
	 */
	public void leaveSession(String userName, String session) throws RemoteException;
	
	/**
	 * Used to log a user into the system. 
	 * @param userName - the desired username of the user logging in
	 * @return true if it was a successful login, false otherwise 
	 * (e.g. userName is already in use)
	 * @throws RemoteException
	 */
	public boolean login(MultiDrawClient client, String userName) throws RemoteException;
	
	/**
	 * logout a user
	 * @param userName - the username
	 * @throws RemoteExcpetion
	 */
	public void logout(String userName) throws RemoteException;
	
	/**
	 * Used to get the session information (users in session and such)
	 * @param session - the session key
	 * @return the Session that corresponds to that sessionKey
	 * @throws RemoteException
	 */
	public Session getSession(String session) throws RemoteException;
	
	/**
	 * Get a list of the sessions available
	 * @return - array of strings
	 * @throws RemoteException
	 */
	public ArrayList<String> getSessions() throws RemoteException;
	
	public void setCanvas(String userName, String session, ArrayList<CanvasShape> updatedShape) throws RemoteException;
}
