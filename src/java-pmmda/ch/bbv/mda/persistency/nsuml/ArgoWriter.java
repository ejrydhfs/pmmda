/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistency.nsuml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ch.bbv.mda.persistency.PersistencyException;
import ch.bbv.mda.persistency.ZipFileUtilities;
import ch.bbv.utilities.SystemUtilities;

/**
 * Returns the content of the *.argo file within a *.zargo archive readable to
 * ArgoUml.
 * @author AdrianBachofen
 * @version $Revision$
 */
public class ArgoWriter {

  public static final String format = "<?xml version = \"1.0\" encoding = \"ISO-8859-1\" ?>\r\n<!DOCTYPE argo SYSTEM \"argo.dtd\" >\r\n<argo>\r\n  <documentation>\r\n    <authorname>pmMDA</authorname>\r\n    <version>0.15.2</version>\r\n    <description>\r\n    This file has been generated using pmMDA - the MDA tool!\r\n    </description>\r\n  </documentation>\r\n\r\n  <member\r\n    type=\"xmi\"\r\n    name=\"{0}\"\r\n  />\r\n</argo>";

  /**
   * The ArgoUML extension with dot for ArgoUML generated model files.
   */
  private static final String ARGOUML_EXT = ".zargo";

  /**
   * Returns the content of an *.argo file
   * @param xmiFileName full file name of the xmi content with or without
   *        extension (.xmi) but with no path information
   * @return String containing the *.argo content
   */
  public static String getArgoContent(String xmiFileName) {
    if (!xmiFileName.endsWith(".xmi"))
      xmiFileName += ".xmi";
    return MessageFormat.format(format, new Object[] { xmiFileName });
  }

  /**
   * Writes a new *.zargo file.
   * @param file non existing file with extension *.zargo
   * @param input existing file with xmi content
   * @throws PersistencyException
   */
  public static void writeEntry(File file, File input)
      throws PersistencyException {
    ZipFileUtilities utilities = new ZipFileUtilities();
    try {
      File temporaryFile = File.createTempFile("pmMDA_zargo", ARGOUML_EXT);
      temporaryFile.deleteOnExit();
      ZipOutputStream output = new ZipOutputStream(new FileOutputStream(
          temporaryFile));
      FileInputStream inputStream = new FileInputStream(input);
      ZipEntry entry = new ZipEntry(file.getName().substring(0,
          file.getName().indexOf(ARGOUML_EXT))
          + ".xmi");
      output.putNextEntry(new ZipEntry(entry));
      SystemUtilities.copy(inputStream, output);
      output.closeEntry();
      inputStream.close();
      entry = new ZipEntry(file.getName().substring(0,
          file.getName().indexOf(ARGOUML_EXT))
          + ".argo");
      output.putNextEntry(new ZipEntry(entry));
      utilities.copy(ArgoWriter.getArgoContent(file.getName().substring(0,
          file.getName().indexOf(ARGOUML_EXT))
          + ".xmi"), output);
      output.closeEntry();
      output.close();
      temporaryFile.renameTo(file);
    } catch (IOException ioe) {
      throw new PersistencyException(ioe);
    }
  }
}