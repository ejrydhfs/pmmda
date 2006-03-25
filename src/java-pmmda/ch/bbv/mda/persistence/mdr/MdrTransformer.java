/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.persistence.mdr;

import java.io.*;
import java.util.Iterator;
import java.util.zip.ZipInputStream;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
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
   * Encoding rule of the XML file containing the XMI data.
   */
  public static final String XML_ENCODING = "UTF-8";

  private final static String UML_INSTANCE = "UMLInstance";

  private final static String UML_MM = "UML";

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(MdrTransformer.class);

  private MDRepository repository;

  /**
   * Loads the provided file, opens the file containing the XMI model and
   * creates a NSUML model.
   * @param file file containing the NSUML model.
   * @return the NSUML model defined in the file.
   * @throws FileNotFoundException the given file could not be found.
   * @throws IOException an error occured when reading data.
   */
  public RefPackage loadArgoModel(File file) throws FileNotFoundException,
      IOException {
    InputStream source = null;
    ZipInputStream input = null;
    FileInputStream inputStream = new FileInputStream(file);
    if (!file.getName().endsWith(".xmi")) {
      input = new ZipInputStream(inputStream);
      String name = input.getNextEntry().getName();
      while (!name.endsWith(".xmi")) {
        name = input.getNextEntry().getName();
      }
      source = new BufferedInputStream(input);
    } else {
      source = new BufferedInputStream(new FileInputStream(file));
    }
    funnyClassLoaders("etc/repository");
    String uri = file.toURI().toString();
    repository = MDRManager.getDefault().getDefaultRepository();
    XmiReader xmiReader = (XmiReader) Lookup.getDefault().lookup(
        XmiReader.class);
    RefPackage packages = null;
    try {
      xmiReader.read(source, uri, packages);
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    } catch (MalformedXMIException e) {
      log.error(e);
    }
    return packages;
  }

  /*
   * See MOF2UML in NetBeans mdr module
   */
  private MofPackage getUmlPackage() throws Exception {
    ModelPackage umlMM = (ModelPackage) repository.getExtent(UML_MM);
    if (umlMM == null) {
      log.debug("Creating extend " + UML_MM);
      umlMM = (ModelPackage) repository.createExtent(UML_MM);
    }
    MofPackage result = getUmlPackage(umlMM);
    if (result == null) {
      log.debug("Reading resources/01-02-15_Diff.xml");
      // reader.read(UmlPackage.class.getResource("resources/01-02-15_Diff.xml")
      //    .toString(), umlMM);
    }
    result = getUmlPackage(umlMM);
    return result;
  }

  /*
   * See MOF2UML in NetBeans mdr module
   */
  private MofPackage getUmlPackage(ModelPackage umlMM) {
    for (Iterator it = umlMM.getMofPackage().refAllOfClass().iterator(); it
        .hasNext();) {
      MofPackage pkg = (MofPackage) it.next();
      if (pkg.getContainer() == null && UML_MM.equals(pkg.getName())) {
        return pkg;
      }
    }
    return null;
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
    MDRManager manager = MDRManager.getDefault();
    Thread.currentThread().setContextClassLoader(oldLoader);
    return manager;
  }

}