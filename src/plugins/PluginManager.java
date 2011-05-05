package plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Holds all of the plugins whether we are in a session or not.
 * The definition of a loaded plugin is if it is loaded on the toolbar or not.
 *
 */
@SuppressWarnings("serial")
public class PluginManager extends HashMap<Plugin, Boolean> implements Iterable<Plugin> {
	private ArrayList<Plugin> sessionPlugins;
	private int pluginsToLoad;
	
	public PluginManager(){
		sessionPlugins = new ArrayList<Plugin>();
		pluginsToLoad = 0;
	}
	
	public boolean loaded(){
		return (size() == pluginsToLoad) ? true : false;
	}
	
	public boolean isLoaded(Plugin plugin){
		return get(plugin);
	}
	
	/**
	 * Reloads the plugin manager after leaving a session, this entails
	 * clearing the session plugin cache, and resetting the application plugins.
	 */
	public void clearSessionPlugins(){
		for ( Plugin plugin : sessionPlugins ){
			remove(plugin);
		}
		sessionPlugins.clear(); 
		
		for ( Plugin plugin : this )
			unload(plugin);
		
		pluginsToLoad = size();	 // We already have our plugins good to go.
	}
	
	public void load(Plugin plugin){
		put(plugin, true);
	}
	
	public void unload(Plugin plugin){
		put(plugin, false);
	}
	
	public void unloadAll(){
		for ( Plugin plugin : keySet() ){
			put(plugin,false);
		}
	}
	
	public void add(Plugin plugin, boolean isImported){
		if ( get(plugin) == null ){
			pluginsToLoad++;
			put(plugin, false);
			if ( !isImported )
				sessionPlugins.add(plugin);
		}
	}

	public Iterator<Plugin> iterator() {
		return keySet().iterator();
	}
}
