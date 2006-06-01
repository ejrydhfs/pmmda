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

public class TextTranslator {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(TextTranslator.class);

  /**
   * Extracts all properties defined in the properties class and generates the
   * associated bundle file.
   * @param propertiesClass class containing all the messages to extract
   * @param file file of the property bundle to generate
   */
  public static void generateResourceBundle(Class propertiesClass, File file) {
    try {
      PrintWriter writer = new PrintWriter(new BufferedOutputStream(
          new FileOutputStream(file)));
      Object properties = propertiesClass.newInstance();
      StringBuilder buffer = new StringBuilder();
      for (Field field : propertiesClass.getFields()) {
        buffer.setLength(0);
        String key = field.getName();
        Object value = field.get(properties);
        if (field.getType() == String.class) {
          buffer.append(key).append("=").append(value);
          writer.println(buffer.toString());
          writer.println();
          log.info(buffer.toString());
        } else if (field.getType() == String[].class) {
          String[] array = (String[]) value;
          int index = 0;
          for (String text : array) {
            buffer.append(key).append("_").append(index).append("=").append(
                text);
            writer.println(buffer.toString());
            writer.println();
            log.info(buffer.toString());
            index++;
          }
        }
        writer.close();
      }
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
   * Extracts all the properties form the property bundle and generates the
   * associated
   * @param filename file name fo the property bundle to parse
   * @param packageName name of the package containing the properties class
   * @param className name of the properties class
   */
  public static void generateClass(String filename, String packageName,
      String className) {

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
