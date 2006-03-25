/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.explorer.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

/**
 * The interface defines all the file extensions and descriptive text for the
 * file types supported in the application and used in operations such as open,
 * save, or save as.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FileTypes {
  /**
   * The extensions associated with a specific file filter. These extensions are
   * used to infer the file extension if the user did not provide one.
   */
  private List<SimpleFileFilter> filters;

  /**
   * The default extension used in the application. This extension is used to
   * infer the file extension if no file filter is selected and the user did not
   * provide an extension.
   */
  private String defaultExtension;

  /**
   * Constructor of the class.
   */
  public FileTypes() {
    filters = new ArrayList<SimpleFileFilter>();
  }

  /**
   * Adds a simple file filter.
   * @param filter file filter to add
   * @pre filter != null
   */
  public void add(SimpleFileFilter filter) {
    assert filter != null;
    filters.add(filter);
  }

  /**
   * Gets the default extension.
   * @return the default extension of the application
   * @see #setDefaultExtension(String)
   */
  public String getDefaultExtension() {
    return defaultExtension;
  }

  /**
   * Sets the default extension.
   * @param defaultExtension the new default extension
   * @see #getDefaultExtension()
   */
  public void setDefaultExtension(String defaultExtension) {
    this.defaultExtension = defaultExtension;
  }

  /**
   * Adds all registered file filters to the chooser.
   * @param chooser file chooser to which the file filters must be added
   */
  public void addFileFilters(JFileChooser chooser) {
    for (FileFilter filter : filters) {
      chooser.addChoosableFileFilter(filter);
    }
  }

  /**
   * Returns the extension associated with the filter
   * @param filter filter which extension should be retrieved
   * @return the extension associated with the filter if found otherwise null
   * @pre filter != null
   */
  public String getExtension(FileFilter filter) {
    assert filter != null;
    String extension = null;
    if (filter instanceof SimpleFileFilter) {
      extension = ((SimpleFileFilter) filter).getDefaultExtension();
    }
    return extension;
  }

  /**
   * Normalizes the file extension. If the file has no extension or an extension
   * not defined in the file filter the default extension defined either in
   * simple file filter or in the file types is used.
   * @param filter file filter to infer for a default extension
   * @param file file to complete if needed
   * @return the completed file if sucessful otherwise the original file
   * @throws IOException if file manipulation encountered an error
   */
  public File normalizeFile(FileFilter filter, File file) throws IOException {
    SimpleFileFilter simpleFileFilter = null;
    if ((filter != null) && filter instanceof SimpleFileFilter) {
      simpleFileFilter = (SimpleFileFilter) filter;
      if (hasExtension(file) && !simpleFileFilter.hasLegalExtension(file)) {
        file = new File(FilenameUtils.removeExtension(file.getCanonicalPath()));
      }
    }
    if (!hasExtension(file)) {
      String extension = null;
      if (simpleFileFilter != null) {
        extension = simpleFileFilter.getDefaultExtension();
      }
      if (extension == null) {
        extension = getDefaultExtension();
      }
      if (extension != null) {
        StringBuilder buffer = new StringBuilder(file.getCanonicalPath());
        buffer.append(".").append(extension);
        return new File(buffer.toString());
      }

    }
    return file;
  }

  /**
   * Checks if the given file has a legal extension defined in the file filter.
   * @param filter filter which extension should be retrieved
   * @param file file which extension should be checked
   * @return true if the extension is defined in file filter otherwise false
   * @pre (filter != null) && (file != null)
   */
  public boolean hasLegalExtension(FileFilter filter, File file) {
    assert (filter != null) && (file != null);
    boolean hasLegalExtension = false;
    if (filter instanceof SimpleFileFilter) {
      hasLegalExtension = ((SimpleFileFilter) filter).hasLegalExtension(file);
    }
    return hasLegalExtension;
  }

  /**
   * Checks if the given file has an extension. An extension is identified with
   * the character '.' in the filename and the last occurence of the character
   * is not the first character of the name.
   * @param file file to check
   * @return true if an extension is found otherwise false
   * @throws IOException if file manipulation encountered an error
   */
  public boolean hasExtension(File file) throws IOException {
    return FilenameUtils.getExtension(file.getCanonicalPath()).length() > 0;
  }
}