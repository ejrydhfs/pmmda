/**
 * copyright 2000-2007 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators;

/**
 * Cartrdige Info
 * 
 * @author Ueli Kurmann
 * 
 */
public class CartridgeInfo {

  public enum Platform {
    java, dotNet, cpp;

    public static String getPath(Platform platform) {
      switch (platform) {
      case java:
        return "java";
      case dotNet:
        return "net";
      default:
        return "others";
      }
    }
  }

}
