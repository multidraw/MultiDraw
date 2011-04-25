package rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface MultiDrawClient extends Remote {

	/**
	 * Updates the client with the given update and options
	 * @param update - The update
	 * 		<ul>
	 * 			<li>CavnasShape - redraws the shape given.</li>
	 * 			<li>ArrayList<String> - updates a list of either users or sessions.</li>
	 * 		</ul>
	 * @param options - HashMap<String, Object> , options given for the update
	 * 		<ul>
	 * 			<li>removed - true (removes the shape from the canvas)<br>
	 * 						- false (updates the canvas with the new object)</li>
	 * 			<li>joinSession - sessionName (updates the users for that given session on the views)</li>
	 * 			<li>session - Session (The actual session to update our copy)</li>
	 * 		</ul>
	 * @throws RemoteException
	 */
	public <T> void update(T update, HashMap<String, Object> options) throws RemoteException;
}
