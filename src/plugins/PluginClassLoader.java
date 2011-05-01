package plugins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This is where the loading magic of the plugins happen. We will take in either a filename
 * or a existing fully qualified class path and load it dynamically.
 *
 */
public class PluginClassLoader extends ClassLoader {

	/**
	 * Constructor, initializes the class loader with its parent.
	 * @param parent ClassLoader - The current classloader of the calling class.
	 */
	public PluginClassLoader(ClassLoader parent){
		super(parent);
	}
	
	/**
	 * Loads the class either from a filepath or the qualified name of an existing class in the JVM.
	 * @param String - The filepath, may either be in format of "/foo/bar/baz.class" or in format of "foo.bar.Baz"
	 * @return Class - The Class that is defined in the filepath.
	 */
	public Class<?> loadClass(String filePath) throws ClassNotFoundException {
        try {
        	File file = new File(filePath);
        	if ( file.exists() ){
	        	filePath = file.toURI().toString();
	            URL myUrl = new URL(filePath);
	            URLConnection connection = myUrl.openConnection();
	            InputStream input = connection.getInputStream();
	            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	            int data = input.read();
	
	            while(data != -1){
	                buffer.write(data);
	                data = input.read();
	            }
	
	            input.close();
	
	            byte[] classData = buffer.toByteArray();
	
	            return defineClass(null, classData, 0, classData.length);
        	} else{
        		return Class.forName(filePath);
        	}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        return null;
    }
}
