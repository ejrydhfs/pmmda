/**
 * copyright 2000-2007 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators;

import ch.bbv.application.VersionId;
import ch.bbv.mda.generators.CartridgeInfo.Platform;

/**
 * Default Implementation for Java Cartridges
 * 
 * @author Ueli Kurmann
 * @version 1.0
 * 
 */
public abstract class CartridgeImplJava extends CartridgeImpl {

  public CartridgeImplJava(String name, String abbreviation, VersionId version) {
    super(name, abbreviation, version);
  }

  public Platform getPlatform() {
    return Platform.java;
  }
}
