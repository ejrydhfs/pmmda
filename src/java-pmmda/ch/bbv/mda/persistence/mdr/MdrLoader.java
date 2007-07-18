/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence.mdr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.lib.jmi.xmi.ReaderFactory;
import org.netbeans.lib.jmi.xmi.WriterFactory;

import ch.bbv.mda.persistence.PersistenceException;

/**
 * Reads and writes a UML 1.4 model in XMI compliant format as supported in the
 * Netbeans MDR. The in-memory model of MDR is used.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MdrLoader {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(MdrLoader.class);

  /**
   * name of a MOF extent that will serve as a target extent for the UML2MOF
   * transformation.
   */
  private static final String MOF_INSTANCE = "MOFInstance";

  /**
   * name of a UML extent (instance of UML metamodel) that the UML models will
   * be loaded into.
   */
  private static final String UML_INSTANCE = "UMLInstance";

  /**
   * name of a MOF extent that will contain definition of UML metamodel.
   */
  private static final String UML_MM = "UML";

  private MDRepository repository;

  private ModelPackage mof;

  private org.omg.uml.UmlPackage packages;

  static {
    System.setProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName",
        "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
  }

  /**
   * Initializes the repository.
   * @return The UmlPackage extent.
   * @throws PersistenceException When an error during repository initialization
   *         occurs.
   */
  public org.omg.uml.UmlPackage init() throws PersistenceException {
    assert (packages == null || repository == null);
    // TODO: Ueberarbeiten. Funktioniert so. ist aber haesslich!
    try {

      MDRManager manager = MDRManager.getDefault();
      if (manager == null) {
        log.error("Could not retrieve the MDR manager");
      }

      repository = manager.getDefaultRepository();

      mof = (ModelPackage) repository.getExtent(MOF_INSTANCE);
      if (mof != null) {
        mof.refDelete();
        mof = null;
      }

      if (mof == null) {
        mof = (ModelPackage) repository.createExtent(MOF_INSTANCE);
      }

      packages = (org.omg.uml.UmlPackage) repository.getExtent(UML_INSTANCE);
      if (packages != null) {
        packages.refDelete();
        packages = null;
      }

      if (packages == null) {
        packages = (org.omg.uml.UmlPackage) repository.createExtent(
            UML_INSTANCE, getUmlPackage());
      }

    } catch (CreationFailedException e) {
      throw new PersistenceException(
          "The creation of the UmlPackage extent failed", e);
    } catch (Exception e) {
      throw new PersistenceException("An unexpected error occured", e);
    }
    return packages;
  }

  /**
   * Shuts the repository down.
   */
  public void dispose() {
    packages = null;
    if (repository != null) {
      repository.shutdown();
      repository = null;
    }
  }

  /**
   * Reads an XMI file into the Uml package extent.
   * @param file The file to read.
   * @return The UML package extent.
   * @throws PersistenceException When a problem during read occurs or the
   *         repository has not been initialized.
   */
  public org.omg.uml.UmlPackage read(InputStream file)
      throws PersistenceException {
    assert (packages != null) || (repository != null);
    try {
      final XMIReaderFactory factory = new ReaderFactory();

      factory.createXMIReader().read(file, "asdf", packages);
    } catch (IOException e) {
      throw new PersistenceException("The specified file could not be read", e);
    } catch (MalformedXMIException e) {
      throw new PersistenceException("The XMI file structure is malformed", e);
    }
    return packages;
  }

  
  /**
   * Writes the repository into a XMI file.
   * @param file The file to write the repository into.
   * @throws PersistenceException When either the repository has not been
   *         initialized or an error during file write occurs.
   */
  public void write(File file) throws PersistenceException {
    assert (packages != null || repository != null);
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(file);
      final XMIWriterFactory factory = new WriterFactory();
      factory.createXMIWriter().write(out, packages, null);
    } catch (IOException e) {
      throw new PersistenceException("An error during writing occured", e);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException ex) {
          // Do nothing
        }
      }
    }
  }

  
  

 

  /**
   * Finds a "UML" package as the tompost package of UML metamodel. This is the
   * package that needs to be instantiated in order to create an UML extent.
   * @return the UML MOF top most package.
   * @throws Exception if the JMI MDR interface encounters a problem.
   */
  private MofPackage getUmlPackage() throws Exception {
    ModelPackage umlMM = (ModelPackage) repository.getExtent(UML_MM);
    if (umlMM == null) {
      umlMM = (ModelPackage) repository.createExtent(UML_MM);
    }
    MofPackage result = getUmlPackage(umlMM);
    if (result == null) {
      XMIReaderFactory readFact = new ReaderFactory();
      XmiReader reader = readFact.createXMIReader();
      reader.read(org.omg.uml.UmlPackage.class.getResource(
          "resources/01-02-15_Diff.xml").toString(), umlMM);
      result = getUmlPackage(umlMM);
    }
    return result;
  }

  /**
   * Finds the top most "UML" package in a given extend.
   * @param extent MOF extent that should be searched for "UML" package.
   * @return the requested package if found otherwise null.
   * @pre extent != null
   */
  private MofPackage getUmlPackage(ModelPackage extent) {
    for (Object mofPkg:extent.getMofPackage().refAllOfClass()){
      MofPackage pkg = (MofPackage) mofPkg;
      if (pkg.getContainer() == null && "UML".equals(pkg.getName())) {
        return pkg;
      }
    }
    return null;
  }

}
