/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.explorer.actions;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * The simple file filter selects files with extension exists in a list of
 * expected ones.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SimpleFileFilter extends FileFilter {
	/** List of extensions which should be filtered.
	 */
	private String[] extensions;
	
	/** Description of the simple file filter.
	 */
	private String description;
	
	/** Constructor of the class.
	 * @param extensions list of extensions which should be filtered.
	 * @param description description of the simple filter.
	 */
	public SimpleFileFilter(String[] extensions, String description) {
		this.extensions = extensions;
		this.description = description;
	}
	
	public boolean accept (File file) {
		boolean shouldAccept = false;
		if (file.isDirectory()) {
			shouldAccept = true;
		} else {
			String name = file.getName().toLowerCase();
			for (int i = 0; i < extensions.length; i++) {
				if (name.endsWith(extensions[i])) {
					shouldAccept = true;
					break;
				}
			}
		}
		return shouldAccept;
	}
	
	public String getDescription() {
		return description;
	}
}
