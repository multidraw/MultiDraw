package controllers;

import items.FileMenuItem;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Provides a way to keep track of the FileMenuItems that are implemented
 * in the application by performing their individual actions that they
 * come associated with.
 *
 */
@SuppressWarnings("serial")
public class FileMenuItemController extends JMenuItem{
	protected FileMenuItem item;
	
	public FileMenuItemController(FileMenuItem i, KeyStroke k){
		super(i);
		if ( k != null )
			setAccelerator(k);
		item = i;
	}
}
