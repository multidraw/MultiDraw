package utils;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import rmi.client.MultiDrawClient;
import rmi.server.MultiDrawServer;
import tools.shapes.CanvasShape;

public class ServerUtil {
	
	private static String session;
	private static String userName;
	private static ArrayList<CanvasShape> shapes;
	private static MultiDrawClient client;
	
	public static MultiDrawServer getServerInstance() {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress(), 1099);
			return (MultiDrawServer) registry.lookup("MultiDrawServer");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setClient(MultiDrawClient c){
		client = c;
	}
	
	public static MultiDrawClient getClient(){
		return client;
	}

	public static String getSession() {
		return session;
	}

	public static void setSession(String sessionIN) {
		session = sessionIN;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userNameIN) {
		userName = userNameIN;
	}

	public static ArrayList<CanvasShape> getShapes() {
		return shapes;
	}

	public static void setShapes(ArrayList<CanvasShape> shapes) {
		ServerUtil.shapes = shapes;
	}
}
