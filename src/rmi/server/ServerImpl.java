package rmi.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rmi.client.MultiDrawClient;
import tools.shapes.CanvasShape;

public class ServerImpl extends UnicastRemoteObject implements MultiDrawServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3899322995886823259L;
	public Hashtable<String, Session> sessions = new Hashtable<String, Session>();
	public Hashtable<String, MultiDrawClient> allUsers = new Hashtable<String, MultiDrawClient>();

	public ServerImpl() throws RemoteException{

	}

	public static void main(String args[]) {
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("MultiDrawServer", new ServerImpl());
			System.out.println("Server ready");
			System.out.println(InetAddress.getLocalHost());
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();

		}
	}

	@Override
	public synchronized boolean updateCanvas(String userName, String session, CanvasShape updatedShape,
			boolean removed) throws RemoteException {
		Session thisSession = sessions.get(session);
		if (!removed) {
			thisSession.addObject(updatedShape);
			pushUpdate(userName, updatedShape, HashMapCreator.create(new Object[]{"remove", false, "session", session}));
		} else {
			thisSession.removeObject(updatedShape);
			pushUpdate(userName, updatedShape, HashMapCreator.create(new Object[]{"remove", true, "session", session}));
		}
		sessions.put(session, thisSession);
		return true;
	}

	@Override
	public synchronized boolean passOffControl(String session, String userName)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<CanvasShape> connectToSession(String session, String userName)
			throws RemoteException {
		if (session == null) {
			sessions.put(userName, new Session(userName));
			session = userName;
		} else {
			sessions.put(session, sessions.get(session).joinSession(userName));	
		}
		pushUpdate(userName, sessions.get(session).getActiveUsers(), HashMapCreator.create(new Object[]{"session", session}));
		return sessions.get(session).getShapes();
	}

	@Override
	public boolean login(MultiDrawClient client, String userName)
			throws RemoteException {
		if (allUsers.containsKey(userName) || userName.equals("")) {
			return false;
		} else {
			allUsers.put(userName, client);
			return true;
		}
	}
	
	@Override
	public boolean logout(String userName, String session) throws RemoteException {
		try {
			allUsers.remove(userName);
			Session currentSession = sessions.get(session);
			currentSession.leaveSession(userName);
			
			if(currentSession.isEmpty())
				sessions.remove(session);
			
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
	
	/**
	 * Pushes a update to the clients.
	 * @param <T> - Type of class for the Clients to receive
	 * @param <V> - Values of the options array
	 * @param userName - String current username of the client.
	 * @param update - The updated object to send off
	 * 					( CanvasShape, ArrayList<String> )
	 * @param opts - Options for the push
	 * 				("session" => sessionName, "removed" => true/false)
	 */
	@SuppressWarnings("unchecked")
	private synchronized <T, V> void pushUpdate(String userName, T update, V opts) {
		HashMap<String, Object> options = null;
		try {
			options = (HashMap<String, Object>)opts;  // Extract the options
		} catch ( ClassCastException e ){
			e.printStackTrace();
			return;
		}
		
		String session = (String) options.remove("session");
		ArrayList<String> users = ( session == null ) ? (ArrayList<String>)allUsers.keys() : sessions.get(session).getActiveUsers();
		
		for( String user : users ) {
			if(user.equalsIgnoreCase(userName)) {
				return;
			}
			try{
				MultiDrawClient client = allUsers.get(user);
				client.update(update, options);
			} catch (Exception e) {
				System.err.println("Update Push exception: " + e.toString());
				e.printStackTrace();
			}
		}
	}

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
	
	private static class HashMapCreator{
		public static HashMap<String, Object> create(Object [] args){
			if ( args.length % 2 != 0 )
				return null;
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			for ( int i = 0; i < args.length; i++ ){
				map.put((String)args[i], args[i+1]);
			}
			return map;
		}
	}
}
