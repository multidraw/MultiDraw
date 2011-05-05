package rmi.server;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import plugins.Plugin;
import rmi.Session;
import rmi.client.MultiDrawClient;
import rmi.server.callbacks.AsyncCallback;
import rmi.server.callbacks.Callback;
import rmi.server.callbacks.Notifier;
import tools.shapes.CanvasShape;
import utils.ServerUtil;

public class ServerImpl extends UnicastRemoteObject implements MultiDrawServer {

	private static final long serialVersionUID = 3899322995886823259L;
	private Hashtable<String, Session> sessions = new Hashtable<String, Session>();
	private Hashtable<String, MultiDrawClient> allUsers = new Hashtable<String, MultiDrawClient>();
	private AsyncCallback clientCallback;
	private DefaultListModel clientListModel;

	public ServerImpl() throws RemoteException {
		super(1155);
		clientCallback = new AsyncCallback(Thread.NORM_PRIORITY-1, 3);

		JFrame serverFrame = new JFrame("MultiDrawServer");
		serverFrame.setLayout(new BoxLayout(serverFrame.getContentPane(), BoxLayout.Y_AXIS));

		JPanel northPnl = new JPanel();
		northPnl.setLayout(new BoxLayout(northPnl, BoxLayout.X_AXIS));
		JLabel header = new JLabel("MultiDraw Server Platform");
		northPnl.add(header);

		clientListModel = new DefaultListModel();
		JList clientList = new JList(clientListModel);
		clientList.setMaximumSize(new Dimension(300, 400));

		serverFrame.add(header);
		serverFrame.add(new JScrollPane(clientList));

		serverFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				killAllUsers();
				try { Thread.sleep(4000); } 
				catch (InterruptedException e1) {}
				finally { clientCallback.stop(); }
			}
		});

		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setSize(new Dimension(300,450));
		serverFrame.setVisible(true);

		clientCallback.start();
	}

	public static void main(String args[]) {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager()); 
		}
		try {
			LocateRegistry.createRegistry(1099);
			Naming.bind("MultiDrawServer", new ServerImpl());
			System.out.println("Server ready");
			System.out.println(InetAddress.getLocalHost());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean updateCanvas(String userName, String session, CanvasShape updatedShape,
			boolean removed) throws RemoteException {
		Session thisSession = sessions.get(session);

		synchronized ( thisSession ){
			if (!removed) {
				thisSession.addObject(updatedShape);
				registerPushCallback(userName, updatedShape, HashMapCreator.create(new Object[]{"method","updateCanvas","remove", removed, "sessionid", session}));	
			} else {
				thisSession.removeObject(updatedShape);
				registerPushCallback(userName, updatedShape, HashMapCreator.create(new Object[]{"method","updateCanvas","remove", removed, "sessionid", session}));	
			}
		}
		return true;
	}
	
	public void setCanvas(String userName, String session, ArrayList<CanvasShape> updatedShapes) throws RemoteException {
		Session thisSession = sessions.get(session);
		synchronized ( thisSession ){
			thisSession.setObjects(updatedShapes);
			registerPushCallback(userName, updatedShapes, HashMapCreator.create(new Object[]{"method","setCanvas","remove", false, "sessionid", session}));	
		}
	}

	public void passOffControl(String session, String passer, String receiver) throws RemoteException {
		Session currentSession = sessions.get(session);

		synchronized ( currentSession ){
			currentSession.setDrawer(receiver);
			registerPushCallback(null, null, HashMapCreator.create(new Object[]{"method","passOffControl","session", currentSession, "sessionid", session, "oldDrawer", passer, "newDrawer", receiver}));
		}
	}

	private void killAllUsers() {
		registerPushCallback(null, null, HashMapCreator.create(new Object[]{"method", "killAllUsers"}));
	}
	
	public boolean uploadPlugin(String jarName, byte[] pluginJar, boolean force) throws RemoteException, IOException{
		return ServerUtil.saveFile(jarName, pluginJar, force);
	}
	
	public void addPlugin(String userName, String session, Plugin plugin) throws RemoteException{
		Session currentSession = sessions.get(session);
		currentSession.addPlugin(plugin);
		synchronized ( currentSession ){
			registerPushCallback(userName, plugin, HashMapCreator.create(new Object[]{"sessionid", session, "shapejarName", plugin.shapeName, "tooljarName", plugin.toolName}));
		}
	}
	
	public ArrayList<CanvasShape> connectToSession(String session, String userName) throws RemoteException {
		Session updatedSession = null;
		if (session == null) {
			updatedSession = new Session(userName);
			sessions.put(userName, updatedSession);
			
			registerPushCallback(userName, new ArrayList<String>(sessions.keySet()), HashMapCreator.create(new Object[]{"method","connectToSession","session", updatedSession, "specific", userName}));
		} else {
			updatedSession = sessions.get(session);
			synchronized ( updatedSession ){
				updatedSession.joinSession(userName);
				sessions.put(session, updatedSession);
				registerPushCallback(null, updatedSession.getActiveUsers(), HashMapCreator.create(new Object[]{"method","connectToSession","session", updatedSession, "joinSession", session}));
			}
		}
		return sessions.get(updatedSession.name).getShapes();
	}

	public void leaveSession(String userName, String session) throws RemoteException{
		Session currentSession = sessions.get(session);
		synchronized ( currentSession ){
			currentSession.leaveSession(userName);
		}
		
		if (currentSession.isEmpty())
			sessions.remove(session);
		
		else if(userName.equals(currentSession.getDrawer())){
			String reciever = currentSession.getActiveUsers().get(0);
			passOffControl(session, userName, reciever);
		}

		registerPushCallback(userName, new ArrayList<String>(sessions.keySet()), HashMapCreator.create(new Object[]{"method","leaveSession","session", currentSession}));
	}

	public boolean login(MultiDrawClient client, String userName)
	throws RemoteException {
		if (allUsers.containsKey(userName) || userName.equals("")) {
			return false;
		} else {
			allUsers.put(userName, client);
			clientListModel.addElement(userName);
			return true;
		}
	}

	public void logout(final String userName) throws RemoteException {
		allUsers.remove(userName);
		clientListModel.removeElement(userName);
	}

	public Session getSession(String session) throws RemoteException {
		return sessions.get(session);
	}

	public ArrayList<String> getSessions() throws RemoteException {
		return new ArrayList<String>(sessions.keySet());
	}

	/**
	 * Pushes a update to the clients.
	 * @param <T> - Type of class for the Clients to receive
	 * @param userName - String current username of the client.
	 * @param update - The updated object to send off <br>
	 * 					( CanvasShape, ArrayList<String> )
	 * @param options - Options for the push <br>
	 * 				{"sessionid" => sessionName, "removed" => true/false, "joinSession" => sessionName}<br>
	 * 				{"refresh" => "session", "oldDrawer"/"newDrawer" => userName, "session" => Session}<br>
	 * 				{"method" => currentMethod callback
	 */
	private <T> void pushUpdate(String userName, T update, HashMap<String, Object> options) {
		String sessionid = null;

		if ( options != null ){
			sessionid = (String) options.remove("sessionid");
		}

		ArrayList<String> users = ( sessionid == null ) ? new ArrayList<String>(allUsers.keySet()) : sessions.get(sessionid).getActiveUsers();
		
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutputStream oStream;
		try {
			oStream = new ObjectOutputStream( bStream );
			oStream.writeObject ( update );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		byte[] byteVal = bStream. toByteArray();
		System.out.println("Pushing update:" + update + " with opts: "  + options + " to clients: " + users + ", payload: " + byteVal.length);
		
		String session = (String)options.remove("specific");
		for( String user : users ) {
			if(user.equalsIgnoreCase(userName) && session == null) {
				continue;
			}
			try{
				MultiDrawClient client = allUsers.get(user);
				// Let the gross continue... because not all servers want their http classes exposed just download the class!
				if ( update instanceof Plugin ){
					Plugin plug = (Plugin)update;
					if ( plug.shapeJar.length > 0 )
						client.update(plug.shapeJar, HashMapCreator.create(new Object[]{"jarName", options.remove("shapejarName")}));
					if ( plug.toolJar.length > 0 )
						client.update(plug.toolJar, HashMapCreator.create(new Object[]{"jarName", options.remove("tooljarName")}));
				}
				client.update(update, options);
			} catch (Exception e) {
				System.err.println("Update Push exception: " + e.toString());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Registers a push callback from the thread pool, see the arguments for @see {@link #pushUpdate(String, Object, HashMap)}
	 * @param <T>
	 * @param userName
	 * @param update
	 * @param options
	 */
	private <T> void registerPushCallback(final String userName, final T update, final HashMap<String, Object> options){
		clientCallback.doCallback(new Callback() {
			public void executeCallback(Notifier n, Object arg) {
				pushUpdate(userName, update, options);
				n.resetCallbackTime();
			}
		});
	}

	/**
	 * Creates a instant HashMap in one step, useful for inline options hashes.
	 *
	 */
	private static class HashMapCreator{
		/**
		 * Creates the HashMap with the given Object[] array of parameters.
		 * @syntax HashMapCreator.create(new Object[]{"foo", "bar", ...})
		 * @param args - Object[] stored as [key, value, key, value], where the keys are Strings and 
		 * 				the values are Objects.
		 * @return HashMap<String, Object> The new HashMap
		 */
		public static HashMap<String, Object> create(Object [] args){
			if ( args.length % 2 != 0 )
				return null;

			HashMap<String, Object> map = new HashMap<String, Object>();
			for ( int i = 0; i < args.length; i+= 2 ){
				map.put((String)args[i], args[i+1]);
			}
			return map;
		}
	}
}
