package rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import tools.shapes.CanvasShape;

@SuppressWarnings("serial")
public class Session implements Serializable{
	private ArrayList<CanvasShape> shapes;
	private ArrayList<String> activeUsers = new ArrayList<String>();
	private String drawer;

	public Session(String userName) {
		shapes = new ArrayList<CanvasShape>();
		activeUsers.add(userName);
		drawer = userName;
	}
	
	/**
	 * joinSession(String userName)
	 * Joins the current session with the given username.
	 * @param userName - String the user joining the session.
	 * @return Session - this session.
	 */
	public Session joinSession(String userName) {
		activeUsers.add(userName);
		return this;
	}
	
	public void leaveSession(String userName) {
		activeUsers.remove(userName);
	}
	
	public boolean isEmpty() {
		return (activeUsers.size() == 0) ? true : false;
	}

	public ArrayList<CanvasShape> getShapes() {
		return shapes;
	}

	public void setShapes(ArrayList<CanvasShape> shapes) {
		this.shapes = shapes;
	}

	public ArrayList<String> getActiveUsers() {
		return activeUsers;
	}

	public String getDrawer() {
		return drawer;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}
	
	public List<CanvasShape> getObjects() {
		return shapes;
	}

	/**
	 * Setter for objects
	 * 
	 * @param objects
	 */
	public void setObjects(ArrayList<CanvasShape> list) {
		this.shapes = list;
	}

	public void addObject(CanvasShape shape) {
		if (containsObject(shape)) {
			updateObject(shapes.indexOf(shape), shape);
		} else {
			shapes.add(0, shape);

		}
	}

	public void updateObject(int index, CanvasShape shape) {
		shapes.set(index, shape);
	}

	public boolean removeObject(CanvasShape shape) {
		if (containsObject(shape)) {
			shapes.remove(shapes.indexOf(shape));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean containsObject(CanvasShape shape) {
		return (shapes.indexOf(shape) != -1);
	}

	public CanvasShape getObject(int index) {
		return shapes.get(index);
	}

}