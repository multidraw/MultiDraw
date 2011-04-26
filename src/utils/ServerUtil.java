package utils;

import java.rmi.Naming;
import java.util.ArrayList;

import rmi.Session;
import rmi.client.MultiDrawClient;
import rmi.server.MultiDrawServer;
import tools.shapes.CanvasShape;

/**
 * The ServerUtil class maintains all of the "global" variables that the client will use within its
 * lifetime.  It is the pure definition of a helper for the main program so that these variables do not
 * need to be passed around the application.
 *
 */
public class ServerUtil {

	private Session session;	// The current session of the client.
	private String userName;	// The current username of the client.
	private MultiDrawClient client;	// The current client impl object of the client.
	private MultiDrawServer server;	// The current server object that the client interacts with.
	private ArrayList<CanvasShape> shapes = new ArrayList<CanvasShape>();
	
	/**
	 * Constructor for the utils class.
	 * @param c - MultiDrawClient The current client.
	 */
	public ServerUtil(MultiDrawClient c){
		client = c;
	}

	/**
	 * Connects to the server if not connected already.
	 * @return MultiDrawServer - The server object.
	 */
	public MultiDrawServer getServerInstance() {
		try {
			if ( server == null ){
				server = (MultiDrawServer)Naming.lookup("//cbarton.mine.nu:1099/MultiDrawServer");
				//server = (MultiDrawServer)Naming.lookup("//localhost:1099/MultiDrawServer");
			} 
			return server; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets the current client impl object.
	 * @param c - MultiDrawClient The client impl object.
	 */
	public void setClient(MultiDrawClient c) {
		client = c;
	}

	/**
	 * Gets the current client impl object.
	 * @return MultiDrawClient - Client impl object.
	 */
	public MultiDrawClient getClient() {
		return client;
	}

	/**
	 * Gets the current session that the client is in.
	 * @return Session - The current session.
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Sets the current session that the client is in.
	 * @param sessionIN - Session the current session.
	 */
	public void setSession(Session sessionIN) {
		session = sessionIN;
	}

	/**
	 * Gets the current username of the client.
	 * @return String - The current username.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the current username of the client.
	 * @param userNameIN - String the username.
	 */
	public void setUserName(String userNameIN) {
		userName = userNameIN;
	}
	
	public ArrayList<CanvasShape> getShapes() {
		return shapes ;
	}

	public void setShapes(ArrayList<CanvasShape> shapes) {
		this.shapes = shapes;
	}
}
