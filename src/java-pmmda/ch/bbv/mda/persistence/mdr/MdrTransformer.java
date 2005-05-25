/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.persistence.mdr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.openide.util.Lookup;

/**
 * The MDR transformer defines rules how to transform a MDR element into the
 * corresponding MDA representation. Special is the mapping of aggregations and
 * compositions to indexed properties to reflect the capabilities of modern
 * object oriented programming languages.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdrTransformer {
  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(MdrTransformer.class);

  private MDRepository repository;

  private RefPackage packages;

  public void readModel(String xmiFile) {
    funnyClassLoaders("etc/repository");
    String uri = new File(xmiFile).toURI().toString();
    repository = MDRManager.getDefault().getDefaultRepository();
    XmiReader xmiReader = (XmiReader) Lookup.getDefault().lookup(
        XmiReader.class);
    try {
      xmiReader.read(uri, packages);
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    } catch (MalformedXMIException e) {
      log.error(e);
    }
  }

  private MDRManager funnyClassLoaders(String fileName) {
    File file = new File(fileName);
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(
        this.getClass().getClassLoader());

    System.getProperties().put("org.netbeans.mdr.persistence.Dir", fileName);

    MDRManager mm = MDRManager.getDefault();

    Thread.currentThread().setContextClassLoader(oldLoader);

    return mm;
  }

}