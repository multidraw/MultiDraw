package plugins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

import javax.swing.ImageIcon;

/**
 * Plugin is used to contain the metadata for each plugin.  That metadata is the description/icon that
 * the tool bar view will show, and the class types of the tool/plugin.
 *
 */
public class Plugin implements Serializable {

	private static final long serialVersionUID = 3689083384108501680L;
	private ImageIcon image;
	private String description, name;
	private Class<?> toolClass, shapeClass;
	public String toolName, shapeName;
	public byte [] toolJar, shapeJar;
	protected long timeStamp;

	/**
	 * Constructor to load in the image, and create the classes.
	 * @param imagePath - String, The filepath to the imageicon to use for the tool/shape..
	 * @param desc - String, The description of the tool/shape.
	 * @param tool - String, The filepath or the Class of the Tool to import.
	 * @param shape - String, The filepath or the Class of the Shape to import.
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public <T> Plugin(String imagePath, String name, String desc, T tool, T shape) throws MalformedURLException, ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
		if ( !imagePath.isEmpty() )
			image = new ImageIcon(imagePath);
		else 
			image = new ImageIcon("images/select.png");
		description = desc;
		this.name = name;

		if ( tool instanceof Class<?> && shape instanceof Class<?> ){
			shapeClass = (Class<?>)shape;
			toolClass = (Class<?>)tool;
		} else if ( tool instanceof Class<?> ){
			toolClass = (Class<?>)tool;
			initializeClasses(new String[]{(String)shape}, this.getClass().getDeclaredField("shapeClass"));
		} else if ( shape instanceof Class<?> ){
			shapeClass = (Class<?>)shape;
			initializeClasses(new String[]{(String)tool}, this.getClass().getDeclaredField("toolClass"));
		} else {
			initializeClasses(new String[]{(String)tool, (String)shape}, this.getClass().getDeclaredField("toolClass"), 
					this.getClass().getDeclaredField("shapeClass"));
		}
		
		shapeName = shapeClass.getName();
		toolName = toolClass.getName();
		timeStamp = System.currentTimeMillis();
	}

	/**
	 * Gets the icon for the plugin.
	 * @return ImageIcon - The icon, may be null.
	 */
	public ImageIcon getImage(){
		return image;
	}

	/**
	 * Gets the name for the 
	 * @return
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets the description for the plugin.
	 * @return String - The description.
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Gets the shape class for the shape imported.
	 * @return Class - The shape class.
	 */
	public Class<?> getShapeClass(){
		return shapeClass;
	}

	/**
	 * Gets the tool class for the tool imported.
	 * @return Class - The tool class.
	 */
	public Class<?> getToolClass(){
		return toolClass;
	}

	/**
	 * Initializes all of the classes with the given source files or package names.
	 * @param paths String [] - The absolute files path of the classes to load.
	 * @param fields Field - The fields to fill with the initialized class.
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void initializeClasses(String [] paths, Field ... fields) throws MalformedURLException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException{
		if ( paths.length != fields.length )	return;

		for ( int i = 0; i < paths.length; i++ ){
			URL pathUrl = getClasspathUrl(paths[i]);
			decompressJar(paths[i], fields[i]);
			fields[i].set(this, loadClass(pathUrl));
		}
	}

	/**
	 * Gets the classpath URL extracted from a filepath.  
	 * @param filepath String the filepath for which the URL is to be generated
	 * @return URL - The corresponding URL to the file for the new classpath.
	 * @throws MalformedURLException 
	 */
	private URL getClasspathUrl(String filepath) throws MalformedURLException{
		File file = new File(filepath);
		return file.toURI().toURL();
	}

	/**
	 * Saves the jar file in a byte array to be read in later if needed.
	 * @param filepath String the path to the jar file.
	 * @param field Field the current working field of the plugin.
	 */
	private void decompressJar(String filepath, Field field){
		File jar = new File(filepath);
		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		
		try{
			FileInputStream fis = new FileInputStream(jar);
			
			byte [] buf = new byte[1024];

			for ( int readNum; (readNum = fis.read(buf)) != -1; ){
				bs.write(buf, 0, readNum);
			}
		} catch ( Exception e ){
			e.printStackTrace();
		}

		if ( field.getName().startsWith("shape") ){
			shapeJar = bs.toByteArray(); 
		} else {
			toolJar = bs.toByteArray();
		}
	}

	/**
	 * Attempts to load a class from the given load path.
	 * @param loadPath - The path that contains either the jar file of the new class to load.
	 * @return Class<?> The class of the loaded file.
	 * @throws ClassNotFoundException 
	 */
	public static Class<?> loadClass(URL loadPath) throws ClassNotFoundException{
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		
		if ( cl instanceof URLClassLoader ){
			try{
				// Let the hacking begin.
				URLClassLoader ul = (URLClassLoader)cl;
				Class<?> [] types = new Class[1];
				types[0] = URL.class;
				Method method = URLClassLoader.class.getDeclaredMethod("addURL", types);
				method.setAccessible(true); // Tehe 4:20am
				
				Object [] args = new Object[1];
				
				URL jarUrl = new URL("jar", "", loadPath.toString() + "!/");
				JarURLConnection jarConn = (JarURLConnection) jarUrl.openConnection();
	
				String name = jarConn.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
				
				args[0] = jarUrl;
				method.invoke(ul, args); // Win.
				return Class.forName(name);
			} catch ( Exception e){
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((Plugin) obj).timeStamp == this.timeStamp;
	}
	
	@Override
	public int hashCode() {
		return (int) timeStamp;
	}
}
