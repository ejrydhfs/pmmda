/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.utilities;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * The system utilities provides convenience methods to perform operting system
 * related operations. These operations are currently not available in the
 * standard librairies.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class SystemUtilities {
  /**
   * Size of the buffer used for the data transfer.
   */
  private static int BUFFER_SIZE = 2048;

  /**
   * Default private constructor to implement static method pattern.
   */
  private SystemUtilities() {
    // private constructor of static class
  }

  /**
   * Copies the source file to the target file. The new input output
   * architecture is the most efficient but portable solution.
   * @param source source file to copy.
   * @param target target file to copy to.
   * @throws IOException an error occured when copying the file.
   * @pre (source != null) && (target != null)
   */
  public static void copy(File source, File target) throws IOException {
    assert ((source != null) && (target != null));
    FileChannel in = new FileInputStream(source).getChannel();
    FileChannel out = new FileOutputStream(target).getChannel();
    in.transferTo(0, in.size(), out);
    in.close();
    out.close();
  }

  /**
   * Copies the source stream to the target stream. The new input output
   * architecture is the most efficient but portable solution.
   * @param source source stream to copy.
   * @param target target stream to copy to.
   * @throws IOException an error occured when copying the stream.
   * @pre (source != null) && (target != null)
   */
  public static void copy(InputStream source, OutputStream target)
      throws IOException {
    assert ((source != null) && (target != null));
    byte[] buffer = new byte[BUFFER_SIZE];
    int count;
    count = source.read(buffer, 0, BUFFER_SIZE);
    while (count != -1) {
      target.write(buffer, 0, count);
      count = source.read(buffer, 0, BUFFER_SIZE);
    }
  }

  /**
   * Returns the extension of the given file. The extension is the string
   * located after the last period in the file name.
   * @param file the file which extension should be returned.
   * @return the extension of the file if defined otherwise null.
   * @pre file != null
   */
  public static String getFileExtension(File file) {
    assert (file != null);
    String filename = file.getAbsolutePath();
    String extension = null;
    int index = filename.lastIndexOf(".");
    if (index != -1) {
      extension = filename.substring(index + 1);
    }
    return extension;
  }
}