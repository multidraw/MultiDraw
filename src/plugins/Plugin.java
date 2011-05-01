package plugins;

import java.io.Serializable;

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

	/**
	 * Constructor to load in the image, and create the classes.
	 * @param imagePath - String The filepath to the imageicon to use for the tool/shape..
	 * @param desc - String The description of the tool/shape.
	 * @param toolPath - String THe filepath of the Tool to import.
	 * @param shapePath - String The filepath of the Shape to import.
	 */
	public Plugin(String imagePath, String name, String desc, String toolPath, String shapePath) throws ClassNotFoundException{
		PluginClassLoader classLoader = new PluginClassLoader(Plugin.class.getClassLoader());

		if ( !imagePath.isEmpty() )
			image = new ImageIcon(imagePath);
		else 
			image = new ImageIcon("images/select.png");
		description = desc;
		this.name = name;

		toolClass = classLoader.loadClass(toolPath);
		shapeClass = classLoader.loadClass(shapePath);
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
}
