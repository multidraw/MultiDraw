package utils.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filters the files that MultiDraw can open to .class.
 *
 */
public class ClassFileFilter extends FileFilter {

	public final String classExt = "class";

	public boolean accept(File f) {
		if ( f.isDirectory() )
			return true;
		
		String ext = MdwFileFilter.getExtension(f);
		if ( ext != null && ext.equals(classExt) )
			return true;
		return false;
	}

	public String getDescription() {
		return "Java Class File (*.class)";
	}
}
