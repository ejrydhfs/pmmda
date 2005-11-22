/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.reference;

import java.util.List;

public class ReferenceCodeUtilities {
  public static ReferenceCode find(List<? extends ReferenceCode> codes, String description) {
    for (ReferenceCode code : codes) {
      if (description.equalsIgnoreCase(code.getDescription())) {
        return code;
      }
    }
    return null;
  }
}
