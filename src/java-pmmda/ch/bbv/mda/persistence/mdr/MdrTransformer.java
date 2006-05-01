/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.persistence.mdr;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Namespace;
import org.openide.util.Lookup;

import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.ExternalTransformer;
import ch.bbv.mda.persistence.FilterIterator;

/**
 * The MDR transformer defines rules how to transform a MDR element into the
 * corresponding MDA representation. Special is the mapping of aggregations and
 * compositions to indexed properties to reflect the capabilities of modern
 * object oriented programming languages.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdrTransformer extends ExternalTransformer {
  private final static String UML_INSTANCE = "UMLInstance";

  private final static String UML_MM = "UML";

  private static final String STEREOTYPE_METAMODEL = "metamodel";

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(MdrTransformer.class);

  private MDRepository repository;

  /**
   * Maps between the source objects used as keys and the target objects used as
   * values. Source objects are NSUML entities, target objects are MDA
   * instances.
   */
  private Map<Object, MetaElement> elements;

  /**
   * Default constructor of the class.
   */
  public MdrTransformer() {
    elements = new HashMap<Object, MetaElement>();
  }

  /**
   * Loads the provided file, opens the file containing the XMI model and
   * creates a MDR model.
   * @param file file containing the MDR model.
   * @return the MDR model defined in the file.
   * @throws FileNotFoundException the given file could not be found.
   * @throws IOException an error occured when reading data.
   */
  public UmlPackage loadArgoModel(File file) throws FileNotFoundException,
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
    String uri = file.toURI().toString();
    repository = MDRManager.getDefault().getDefaultRepository();
    XmiReader xmiReader = (XmiReader) Lookup.getDefault().lookup(
        XmiReader.class);
    UmlPackage uml = null;
    try {
      repository.beginTrans(true);
      uml = (UmlPackage) repository.getExtent("UMLInstance");
      if (uml == null)
        uml = (UmlPackage) repository.createExtent("UMLInstance",
            getUmlPackage(repository));
      xmiReader.read(source, uri, uml);
      repository.endTrans(true);
    } catch (FileNotFoundException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    } catch (MalformedXMIException e) {
      log.error(e);
    } catch (CreationFailedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return uml;
  }

  /**
   * Synchronize the MDA model with the MDR model.
   * @param source MDR model used as source of the synchronization
   * @param target MDA model to synchronize
   */
  public void synchronize(UmlPackage source, MetaModel target) {
    for (Iterator<Namespace> i = new FilterIterator<Namespace>(source.getCore()
        .getNamespace().refAllOfType(), Namespace.class); i.hasNext();) {
      Namespace namespace = i.next();
      if (namespace.getNamespace() == null) {
        
      }
    }
  }

  private void extractPackage(
      org.omg.uml.modelmanagement.UmlPackage umlPackage, MetaModel context) {

  }

  private MofPackage getUmlPackage(MDRepository repository) throws Exception {
    ModelPackage umlMM = (ModelPackage) repository.getExtent("UML");
    if (umlMM == null)
      umlMM = (ModelPackage) repository.createExtent("UML");
    MofPackage result = getUmlPackage(umlMM);
    if (result == null) {
      XmiReader reader = (XmiReader) Lookup.getDefault().lookup(
          javax.jmi.xmi.XmiReader.class);
      reader.read(getClass().getResource(
          "/org/omg/uml/resources/01-02-15_Diff.xml").toString(), umlMM);
      result = getUmlPackage(umlMM);
    }
    return result;
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
      // .toString(), umlMM);
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
}