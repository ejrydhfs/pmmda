/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.List;

import ch.bbv.dog.DataObjectHandler;

/**
 * The remote reference code retriever retrieves code values from a remote
 * server.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RemoteReferenceCodeRetriever implements ReferenceCodeRetriever {
  /** 
   * The data objec handler used to retrieve the data.
   */
  private DataObjectHandler handler;

  /**
   * Constructor of the class.
   * @param handler handler used to communicate with the remote server.
   */
  public RemoteReferenceCodeRetriever(DataObjectHandler handler) {
    this.handler = handler;
  }

  public ReferenceCodeInfo retrieveReferenceCode(ReferenceCodeInfo info) {
    throw new UnsupportedOperationException();
  }

  public List retrieveReferenceCodes(List infos) {
    return null;
  }

  public void storeReferenceCodes(List infos) {
  }
}