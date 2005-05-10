/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.operators;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The file operators class provides a set of operators related to file
 * operations.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FileOperators {
  /**
   * Copies the content of the source directory to the target directory.
   * @param source source directory to copy.
   * @param target destination directory.
   * @throws IOException if the copy operations encountered a IO problem.
   */
  public static void copyDirectory(File source, File target) throws IOException {
    if (source.isDirectory()) {
      if (!target.exists()) {
        target.mkdir();
      }
      for (String child : source.list()) {
        copyDirectory(new File(source, child), new File(target, child));
      }
      copyFile(source, target);
    }
  }

  /**
   * Copies the source file to the target file.
   * @param source source file to copy.
   * @param target target file to copy to.
   * @throws IOException if the copy operations encountered a IO problem.
   */
  public static void copyFile(File source, File target) throws IOException {
    InputStream in = new FileInputStream(source);
    OutputStream out = new FileOutputStream(target);
    byte[] buffer = new byte[2048];
    int length = in.read(buffer);
    while (length > 0) {
      out.write(buffer, 0, length);
      length = in.read(buffer);
    }
    in.close();
    out.close();
  }
  
  /**
   * Extracts the source code defined between the given line patterns from the
   * file.
   * @param source the source file to parse.
   * @param startPattern start pattern of the code block.
   * @param endPattern end pattern of the code block.
   * @return the extracted lines of code.
   * @throws IOException if an error occured when accessing the file.
   */
  public static List<String> extractSourceCode(File source,
      String startPattern, String endPattern) throws IOException {
    List<String> lines = new ArrayList<String>();
    if (source.exists()) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          new FileInputStream(source)));
      String line = reader.readLine();
      while (line != null && !line.trim().contains(startPattern)) {
        line = reader.readLine();
      }
      line = reader.readLine();
      if (line != null) {
        while (line != null && !line.trim().contains(endPattern)) {
          lines.add(line);
          line = reader.readLine();
        }
      }
    }
    return lines;
  }

  /**
   * Creates a print writer for an artifact.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param filename name of the file containing the artifact.
   * @return the requested print writer.
   * @throws IOException An exception occured when trying to create the file.
   */
  public static PrintWriter createWriter(File root, String packageName,
      String filename) throws IOException {
    File file = null;
    file = createFile(root, packageName, filename);
    return new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
  }

  /**
   * Creates a file in the directory associated with the package. The root
   * defines the start of source tree. The name of the file is the name of the
   * class.
   * @param root root directory of the source tree.
   * @param packageName name of the package.
   * @param filename name of the file.
   * @return the created file.
   * @throws IOException An exception occured when trying to create the file.
   * @pre (root != null && root.isDirectory()) && (className != null &&
   *      className.length() > 0)
   */
  public static File createFile(File root, String packageName, String filename)
      throws IOException {
    assert ((root != null) && root.isDirectory())
        && ((filename != null) && (filename.length() > 0));

    File file = null;
    File directory;
    if (packageName != null) {
      directory = new File(root, packageName.replace('.', '/'));
    } else {
      directory = root;
    }
    if (directory.isDirectory() || directory.mkdirs()) {
      file = new File(directory, filename);
    }
    if (file == null) {
      throw new IOException("file not created " + root.getAbsolutePath() + "/"
          + packageName + "/" + filename);
    }
    return file;
  }

  /**
   * Creates a new print writer for the given file.
   * @param file file for which a print writer should be created.
   * @return the new print writer.
   * @throws FileNotFoundException the creation of the print writer generated an
   *           error.
   * @pre file != null && file.canWrite()
   */
  public static PrintWriter createPrintWriter(File file)
      throws FileNotFoundException {
    assert (file != null) && file.canWrite();
    return new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
  }

  /**
   * Closes the print writer if not null.
   * @param writer print writer to close.
   */
  public static void closePrintWriter(PrintWriter writer) {
    if (writer != null) {
      writer.close();
    }
  }
}
