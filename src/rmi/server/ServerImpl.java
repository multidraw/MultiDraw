package rmi.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rmi.client.MultiDrawClient;
import tools.shapes.CanvasShape;

public class ServerImpl implements MultiDrawServer {

	public HashMap<String, Session> sessions = new HashMap<String, Session>();
	public HashMap<String, String> allUsers = new HashMap<String, String>();

	public ServerImpl() {

	}

	public static void main(String args[]) {
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("MultiDrawServer",
					(MultiDrawServer) UnicastRemoteObject.exportObject(
							new ServerImpl(), 0));
			System.out.println("Server ready");
			System.out.println(InetAddress.getLocalHost());
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();

		}
	}

	@Override
	public boolean updateCanvas(String userName, String session, CanvasShape updatedShape,
			boolean removed) throws RemoteException {
		Session thisSession = sessions.get(session);
		if (!removed) {
			thisSession.addObject(updatedShape);
			pushUpdate(userName, session, updatedShape, false);
		} else {
			thisSession.removeObject(updatedShape);
			pushUpdate(userName, session, updatedShape, true);
		}
		sessions.put(session, thisSession);
		return true;
	}

	@Override
	public boolean passOffControl(String session, String userName)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<CanvasShape> connectToSession(String session, String userName)
			throws RemoteException {
		if (session == null) {
			sessions.put(userName, new Session(userName));
			return sessions.get(userName).getShapes();
		} else {
			sessions.put(session, sessions.get(session).joinSession(userName));
			return sessions.get(session).getShapes();
		}
	}

	@Override
	public boolean login(String userName, String ipAddress)
			throws RemoteException {
		if (allUsers.containsKey(userName) || userName.equals("")) {
			return false;
		} else {
			allUsers.put(userName, ipAddress);
			return true;
		}
	}
	
	@Override
	public boolean logout(String userName, String session) throws RemoteException {
		try {
			allUsers.remove(userName);
			sessions.get(session).leaveSession(userName);
			return true;
		} catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public Session getSession(String session) throws RemoteException {
		return sessions.get(session);
	}
	
	@Override
	public ArrayList<String> getSessions() throws RemoteException {
		Set<String> keys = sessions.keySet();
		Iterator<String> keyIt = keys.iterator();
		ArrayList<String> sessionKeys = new ArrayList<String>();
		while(keyIt.hasNext()) {
			sessionKeys.add(keyIt.next());
		}
		return sessionKeys;
	}
	
	private void pushUpdate(String userName, String session, CanvasShape alteredShape, boolean isRemoved) {
		for(String user : sessions.get(session).getActiveUsers()) {
			if(user.equalsIgnoreCase(userName)) {
				return;
			}
			String ipAddress = allUsers.get(user);
			try {
				Registry remoteRegistry = LocateRegistry.getRegistry(ipAddress, 1100);
				MultiDrawClient client = (MultiDrawClient) remoteRegistry.lookup("MultiDrawClient");
				client.updateCanvas(alteredShape, isRemoved);
			} catch (Exception e) {
				System.err.println("Update Push exception: " + e.toString());
				e.printStackTrace();
			}
		}
	}

	public class Session implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<CanvasShape> shapes;
		private ArrayList<String> activeUsers = new ArrayList<String>();
		private String drawer;

		public Session(String userName) {
			shapes = new ArrayList<CanvasShape>();
			activeUsers.add(userName);
			drawer = userName;
		}

		public Session joinSession(String userName) {
			activeUsers.add(userName);
			return this;
		}
		
		public void leaveSession(String userName) {
			activeUsers.remove(userName);
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
}
