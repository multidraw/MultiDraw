package utils;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import rmi.client.MultiDrawClient;
import rmi.server.MultiDrawServer;
import tools.shapes.CanvasShape;

public class ServerUtil {

	private String session;
	private String userName;
	private ArrayList<CanvasShape> shapes;
	private MultiDrawClient client;
	
	public ServerUtil(MultiDrawClient c){
		client = c;
	}

	public MultiDrawServer getServerInstance() {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(InetAddress.getLocalHost()
					.getHostAddress(), 1099);
			return (MultiDrawServer) registry.lookup("MultiDrawServer");
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

	public String getSession() {
		return session;
	}

	public void setSession(String sessionIN) {
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
