package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.util.ArrayList;

import plugins.Plugin;

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
	private String host;
	
	/**
	 * Constructor for the utils class.
	 * @param c - MultiDrawClient The current client.
	 */
	public ServerUtil(MultiDrawClient c, String host){
		client = c;
		this.host = host;
	}

	/**
	 * Connects to the server if not connected already.
	 * @return MultiDrawServer - The server object.
	 */
	public MultiDrawServer getServerInstance() {
		try {
			if ( server == null ){
				server = (MultiDrawServer)Naming.lookup("//"+host+":1099/MultiDrawServer");
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
	
	public static boolean saveFile(String jarName, byte[] pluginJar, boolean force) throws IOException{
		File jar = new File(getPluginPath(jarName));	// Cheap way to get the working directory

		if ( jar.exists() && !force )
			return false;
		
		BufferedOutputStream bs;
		try {
			bs = new BufferedOutputStream(new FileOutputStream(jar));
			bs.write(pluginJar);
			bs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		
		try{
			Plugin.loadClass(jar.toURI().toURL());
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return true;
		
	}
	
	/**
	 * Returns the filepath of the specified jar file.
	 * @param jarName String, The name of the Jar file
	 * @return String the filepath to use.
	 */
	public static String getPluginPath(String jarName){
		return new File("").getAbsolutePath() + "/plugins/"+ jarName + ".jar";
	}
}
