/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.reference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The local reference code retriever manages a set of reference codes defined
 * in the program. Each descriptor contains the legal values of the reference
 * code type.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class LocalReferenceCodeRetriever implements ReferenceCodeRetriever {
  /**
   * The list of reference codes available to the retriever.
   */
  private List<ReferenceCodeInfo> infos;

  /**
   * Default constructor of the class.
   */
  public LocalReferenceCodeRetriever() {
    infos = new ArrayList<ReferenceCodeInfo>();
  }

  /**
   * Adds a reference code information to the list of locally managed codes.
   * @param info reference code information to add.
   */
  public void add(ReferenceCodeInfo info) {
    infos.add(info);
  }

  public ReferenceCodeInfo retrieveReferenceCode(ReferenceCodeInfo info) {
    return null;
  }

  public List retrieveReferenceCodes(List infos) {
    for (int i = 0; i < infos.size(); i++) {
      ReferenceCodeInfo info = (ReferenceCodeInfo) infos.get(i);
      ReferenceCodeInfo descriptor = getInfo(info);
      if (descriptor != null) {
        info.setCodes(descriptor.getCodes());
      }
    }
    return infos;
  }

  public void storeReferenceCodes(List infos) {
    throw new UnsupportedOperationException();
  }

  private ReferenceCodeInfo getInfo(ReferenceCodeInfo info) {
    ReferenceCodeInfo descriptor = null;
    for (Iterator i = infos.iterator(); i.hasNext();) {
      ReferenceCodeInfo codeInfo = (ReferenceCodeInfo) i.next();
      if (codeInfo.isAvailable()
          && codeInfo.getClassName().equals(info.getClassName()))
        ;
      descriptor = codeInfo;
      break;
    }
    return descriptor;
  }
}