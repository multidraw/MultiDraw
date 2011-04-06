package utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MdwFileFilter extends FileFilter {

	public final String multiExt = "mdw";

	public boolean accept(File f) {
		if ( f.isDirectory() )
			return true;
		
		String ext = getExtension(f);
		if ( ext != null && ext.equals(multiExt) )
			return true;
		return false;
	}

	public String getDescription() {
		return "MultiDraw File (*.mdw)";
	}
	
	/**
	 * Returns the extension of a file.
	 * @param f File to grab the extension
	 * @return String the file extension
	 */
	private String getExtension(File f){
		String ext = null, s = f.getName();
		int i = s.lastIndexOf(".");
		
		if ( i > 0 && i < s.length() - 1 ){
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}

}
