package rmi.server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import rmi.Session;
import rmi.client.MultiDrawClient;
import tools.shapes.CanvasShape;

public class ServerImpl extends UnicastRemoteObject implements MultiDrawServer {

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
			pushUpdate(userName, session, updatedShape, false);
		} else {
			thisSession.removeObject(updatedShape);
			pushUpdate(userName, session, updatedShape, true);
		}
		sessions.put(session, thisSession);
		return true;
	}

	@Override
	public synchronized boolean passOffControl(String session, String receiver) throws RemoteException {
		sessions.get(session).setDrawer(receiver);
		
		
		
		return false;
	}
	
	@Override
	public String getUserWithControl(String session) {
		return sessions.get(session).getDrawer();
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
	
	private void pushUpdate(String userName, String session, CanvasShape alteredShape, boolean isRemoved) {
		for(String user : sessions.get(session).getActiveUsers()) {
			if(user.equalsIgnoreCase(userName)) {
				return;
			}
			try{
				MultiDrawClient client = allUsers.get(user);
				client.updateCanvas(alteredShape, isRemoved);
			} catch (Exception e) {
				System.err.println("Update Push exception: " + e.toString());
				e.printStackTrace();
			}
		}
	}
}
