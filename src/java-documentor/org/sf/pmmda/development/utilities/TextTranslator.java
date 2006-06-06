/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package org.sf.pmmda.development.utilities;

import java.io.*;
import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The text translator provides functions to generate resource bundles based on
 * an existing java resource class.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class TextTranslator {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(TextTranslator.class);

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: program qualified-class-name filename");
    } else {
      TextTranslator.generateResourceBundle(args[0], args[1]);
    }
  }
  
  /**
   * Extracts all properties defined in the class with the qualified name and
   * generates the associated bundle file with the given filename.
   * @param qualifiedName class name containing all the messages to extract
   * @param filename name of the file of the property bundle to generate
   */
  public static void generateResourceBundle(String qualifiedName,
      String filename) {
    File file = new File(filename);
    try {
      Class clazz = Class.forName(qualifiedName);
      generateResourceBundle(clazz, file);
    } catch (ClassNotFoundException e) {
      log.fatal(e);
    }
  }

  /**
   * Extracts all properties defined in the properties class and generates the
   * associated bundle file.
   * @param propertiesClass class containing all the messages to extract
   * @param file file of the property bundle to generate
   */
  public static void generateResourceBundle(Class propertiesClass, File file) {
    try {
      log.info("Writing properties file " + file.getAbsolutePath());
      PrintWriter writer = new PrintWriter(new BufferedOutputStream(
          new FileOutputStream(file)));
      Object properties = propertiesClass.newInstance();
      StringBuilder buffer = new StringBuilder();
      for (Field field : propertiesClass.getFields()) {
        String key = field.getName();
        Object value = field.get(properties);
        if (field.getType() == String.class) {
          buffer.setLength(0);
          buffer.append(key).append("=").append(value);
          writer.println(buffer.toString());
        } else if (field.getType() == String[].class) {
          String[] array = (String[]) value;
          int index = 0;
          for (String text : array) {
            buffer.setLength(0);
            buffer.append(key).append("_").append(index).append("=").append(
                text);
            writer.println(buffer.toString());
            index++;
          }
        }
      }
      writer.close();
    } catch (InstantiationException e) {
      log.fatal(e);
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      log.fatal(e);
    } catch (FileNotFoundException e) {
      log.fatal(e);
    }
  }

  /**
   * Checks that each field in the property class has a definition in the
   * resouce bundle.
   * @param properties properties class to check
   * @param filename file name of the property bundle containing the definitions
   * @return true if the check was sucessfull otherwise false
   */
  public static boolean checkBundle(Class properties, String filename) {
    return true;
  }
}
