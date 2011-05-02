package rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import plugins.Plugin;
import tools.shapes.CanvasShape;

/**
 * Session maintains all of the state of the governed session.  In that it includes the current 
 * drawer and all of the users.
 *
 */
public class Session implements Serializable{
	/**
	 * String - The current name of the session.
	 */
	public String name;
	
	private static final long serialVersionUID = 1346464814726331606L;
	private ArrayList<CanvasShape> shapes;
	private ArrayList<String> activeUsers;
	private String drawer;
	private HashMap<Plugin, Boolean> plugins;

	
	/**
	 * Constructor for the session creating it and adding the creator as the drawer.
	 * @param userName - String The creator/current drawer of the session.
	 */
	public Session(String userName) {
		activeUsers = new ArrayList<String>();
		shapes = new ArrayList<CanvasShape>();
		activeUsers.add(userName);
		drawer = userName;
		name = userName;
		plugins = new HashMap<Plugin, Boolean>();
	}
	
	/**
	 * Joins the current session with the given username.
	 * @param userName - String the user joining the session.
	 * @return Session - this session.
	 */
	public Session joinSession(String userName) {
		activeUsers.add(userName);
		return this;
	}
	
	/**
	 * Leaves the session for the given username.
	 * @param userName - String The user that is leaving the session.
	 */
	public void leaveSession(String userName) {
		activeUsers.remove(userName);
	}
	
	/**
	 * Determines if this session is empty or not.
	 * @return - boolean 
	 * 	<ul>
	 *		<li>true - Session is empty.</li><li>false - Session has members.</li>
	 *	</ul>
	 */
	public boolean isEmpty() {
		return (activeUsers.size() == 0) ? true : false;
	}

	public ArrayList<CanvasShape> getShapes() {
		return shapes;
	}

	public void setShapes(ArrayList<CanvasShape> shapes) {
		this.shapes = shapes;
	}

	/**
	 * Gets all of the active users of the session.
	 * @return ArrayList<String> The active users.
	 */
	public ArrayList<String> getActiveUsers() {
		return activeUsers;
	}
	
	public boolean isActive(String username) {
		return activeUsers.contains(username);
	}

	/**
	 * Gets the current drawer of the session.
	 * @return String The current drawer.
	 */
	public String getDrawer() {
		return drawer;
	}

	/**
	 * Sets the current drawer for this session.
	 * @param drawer String The new drawer.
	 */
	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	/**
	 * Setter for objects
	 * 
	 * @param objects
	 */
	public void setObjects(ArrayList<CanvasShape> list) {
		this.shapes = list;
	}

	/**
	 * Adds an object to this session.
	 * @param shape - CanvasShape The new shape to add.
	 */
	public void addObject(CanvasShape shape) {
		if (containsObject(shape)) {
			shapes.set(shapes.indexOf(shape), shape);
		} else {
			shapes.add(0, shape);

		}
	}

	/**
	 * Removes an object from this session.
	 * @param shape - CanvasShape The shape to remove.
	 * @return boolean - true if removed; false otherwise.
	 */
	public boolean removeObject(CanvasShape shape) {
		if (containsObject(shape)) {
			shapes.remove(shapes.indexOf(shape));
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determines if the given object is in the session.
	 * @param shape - CanvasShape The shape to query over.
	 * @return boolean - true if exists; false otherwise.
	 */
	public boolean containsObject(CanvasShape shape) {
		return (shapes.indexOf(shape) != -1);
	}

	public HashMap<Plugin, Boolean> getPlugins() {
		return plugins;
	}

	public void setPlugins(HashMap<Plugin, Boolean> plugins) {
		this.plugins = plugins;
	}
	
	public void addPlugin(Plugin plugin) {
		addPlugin(plugin, false);
	}
	
	public void addPlugin(Plugin plugin, Boolean loaded ) {
		this.plugins.put(plugin, loaded);
	}
}