/* copyright 2000-2006 Marcel Baumann 
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
   * Checks if the two given text files have the same content or not.
   * @param source source file to compare to.
   * @param target target file to compare to.
   * @return true if both files have the same content othwerwise false.
   * @throws IOException if an IO error occured.
   */
  public static boolean equals(File source, File target) throws IOException {
    boolean equals = false;
    if (source.exists() && target.exists()) {
      if (source.length() == target.length()) {
        equals = true;
        BufferedReader sourceReader = new BufferedReader(new InputStreamReader(
            new FileInputStream(source)));
        BufferedReader targetReader = new BufferedReader(new InputStreamReader(
            new FileInputStream(source)));
        String sourceLine = sourceReader.readLine();
        String targetLine = targetReader.readLine();
        while ((sourceLine != null) || (targetLine != null)) {
          if (((sourceLine == null) || (targetLine == null))
              || !sourceLine.equals(targetLine)) {
            equals = false;
            break;
          }
          sourceLine = sourceReader.readLine();
          targetLine = targetReader.readLine();
        }
      }
    }
    return equals;
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
    File file = createFile(root, packageName, filename);
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
      //System.out.println("FileOperators#createFile: unable to create file: " +root.getAbsolutePath()+packageName+filename);
      throw new IOException("file not created " + root.getAbsolutePath() + "/"
          + packageName + "/" + filename);
    }
    //System.out.println("FileOperators#createFile: created file: " +file.getAbsolutePath());
    return file;
  }

  /**
   * Creates a new print writer for the given file.
   * @param file file for which a print writer should be created.
   * @return the new print writer.
   * @throws FileNotFoundException the creation of the print writer generated an
   *           error.
   * @pre file != null
   */
  public static PrintWriter createPrintWriter(File file)
      throws FileNotFoundException {
    assert (file != null);
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
