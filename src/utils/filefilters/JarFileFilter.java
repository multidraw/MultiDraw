package utils.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filters the files that MultiDraw can open to .jar.
 *
 */
public class JarFileFilter extends FileFilter {

	public final String jarExt = "jar";

	public boolean accept(File f) {
		if ( f.isDirectory() )
			return true;
		
		String ext = MdwFileFilter.getExtension(f);
		if ( ext != null && ext.equals(jarExt) )
			return true;
		return false;
	}

	public String getDescription() {
		return "Java Jar File (*.jar)";
	}
}
