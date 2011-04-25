package utils;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import rmi.Session;
import rmi.client.MultiDrawClient;
import rmi.server.MultiDrawServer;
import tools.shapes.CanvasShape;

public class ServerUtil {

	private Session session;
	private String userName;
	private ArrayList<CanvasShape> shapes;
	private MultiDrawClient client;
	private MultiDrawServer server;
	
	public ServerUtil(MultiDrawClient c){
		client = c;
	}

	public MultiDrawServer getServerInstance() {
		Registry registry;
		try {
			if ( server == null ){
				registry = LocateRegistry.getRegistry(InetAddress.getLocalHost()
						.getHostAddress(), 1099);
				server = (MultiDrawServer) registry.lookup("MultiDrawServer");
			} 
			return server; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setClient(MultiDrawClient c) {
		client = c;
	}

	public MultiDrawClient getClient() {
		return client;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session sessionIN) {
		session = sessionIN;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userNameIN) {
		userName = userNameIN;
	}

	public ArrayList<CanvasShape> getShapes() {
		return shapes;
	}

	public void setShapes(ArrayList<CanvasShape> shapes) {
		this.shapes = shapes;
	}
}
