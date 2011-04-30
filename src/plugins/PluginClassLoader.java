package plugins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PluginClassLoader extends ClassLoader {

	public PluginClassLoader(ClassLoader parent){
		super(parent);
	}
	
	public Class<?> loadClass(String filePath) throws ClassNotFoundException {
        try {
        	filePath = new File(filePath).toURI().toString();
        	System.out.println(filePath);
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        return null;
    }
}
