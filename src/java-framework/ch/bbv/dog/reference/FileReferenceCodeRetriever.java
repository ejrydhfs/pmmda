/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.io.*;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The file reference code retriever provides mechanisms to save to and retrieve
 * reference code values of a type from a serialized file representation.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FileReferenceCodeRetriever implements ReferenceCodeRetriever {
  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(FileReferenceCodeRetriever.class);

  /**
   * name of the file containing the serialized representation of the reference
   * codes.
   */
  private String filename;

  /**
   * Constructor of the class.
   * @param filename name of the file containing the serialized representation
   *          of the codes.
   */
  public FileReferenceCodeRetriever(String filename) {
    this.filename = filename;
  }

  public ReferenceCodeInfo retrieveReferenceCode(ReferenceCodeInfo info) {
    throw new UnsupportedOperationException();
  }

  public List<ReferenceCodeInfo> retrieveReferenceCodes(List infos) {
    List<ReferenceCodeInfo> codes = null;
    if (canBeRead(filename)) {
      codes = retrieveCodesFromFile(new File(filename));
    }
    return codes;
  }

  public void storeReferenceCodes(List<ReferenceCodeInfo> infos) {
    try {
      ObjectOutput out;
      out = new ObjectOutputStream(new FileOutputStream(new File(filename)));
      out.writeObject(infos);
      out.close();
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
  }

  private List<ReferenceCodeInfo> retrieveCodesFromFile(File file) {
    List<ReferenceCodeInfo> infos = null;
    try {
      ObjectInput in;
      in = new ObjectInputStream(new FileInputStream(file));
      infos = (List<ReferenceCodeInfo>) in.readObject();
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return infos;
  }

  private boolean canBeRead(String filename) {
    boolean exists = false;
    if (filename != null) {
      File file = new File(filename);
      exists = file.exists() && file.canRead();
    }
    return exists;
  }
}
