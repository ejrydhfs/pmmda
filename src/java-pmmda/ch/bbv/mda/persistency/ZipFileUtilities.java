/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistency;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * The Zip file utilities class provides convenience methods to manipulate a ZIP
 * file containing a XMI model.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ZipFileUtilities {
  private static final int BUFFER_SIZE = 4096;

  /**
   * Replace the entry with the given name in the zip file. The content of the
   * new entry is given as stream. This method implements the following
   * algorithm.
   * <ul>
   * <li>Create a temporary file in the same directory where the file is
   * located.</li>
   * <li>Copy each entry for the original file to the temporary file. If the
   * entry name is the one passed as parameters, replace the entry with the
   * given stream.</li>
   * <li>Close the temporary file. If the operation was sucessfull, delete the
   * original file and rename the temporary name to the original file.
   * </ul>
   * @param file zip file containing the entry.
   * @param entryName
   * @param stream
   */
  public void replaceEntry(File file, String entryName, InputStream stream)
      throws PersistencyException {
    try {
      File temporaryFile = File.createTempFile("pmMDA_zargo", ".zargo");
      temporaryFile.deleteOnExit();
      FileInputStream inputStream = new FileInputStream(file);
      ZipInputStream input = new ZipInputStream(inputStream);
      ZipOutputStream output = new ZipOutputStream(new FileOutputStream(
          temporaryFile));
      ZipEntry entry = input.getNextEntry();
      while (entry != null) {
        output.putNextEntry(new ZipEntry(entry));
        if (!entry.getName().equals(entryName)) {
          copy(input, output);
        } else {
          copy(stream, output);
        }
        input.closeEntry();
        output.closeEntry();
        entry = input.getNextEntry();
      }
      input.close();
      inputStream.close();
      output.close();
      boolean isSuccess = file.delete();
      if (!isSuccess) {
        throw new PersistencyException();
      }
      isSuccess = temporaryFile.renameTo(file);
      if (!isSuccess) {
        throw new PersistencyException();
      }
    } catch (IOException ioe) {
      throw new PersistencyException(ioe);
    }
  }

  public void copy(InputStream source, OutputStream target) throws IOException {
    byte[] buffer = new byte[BUFFER_SIZE];
    int nrOfReadBytes;
    do {
      nrOfReadBytes = source.read(buffer, 0, BUFFER_SIZE);
      if (nrOfReadBytes != -1) {
        target.write(buffer, 0, nrOfReadBytes);
      }
    } while (nrOfReadBytes != -1);
  }

  public void copy(String source, OutputStream target) throws IOException {
    byte[] buffer;
    buffer = source.getBytes();
    target.write(buffer);
  }

}