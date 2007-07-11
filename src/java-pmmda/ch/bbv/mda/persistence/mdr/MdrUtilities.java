/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipInputStream;

import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.VisibilityKind;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;

import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.persistence.PersistenceException;

/**
 * Utilities for converting between MDA/MDR models and vice versa
 * @author UeliKurmann
 * @version $Revision$
 */
public class MdrUtilities {

  public static final String XMI_EXTENSION = ".xmi";
  public static final String ZARGO_EXTENSION = ".zargo";
  public static final String ZARGO2_EXTENSION = ".zargo2";

  /**
   * Converts the visibility modifier from MDR to the pmMDA model
   * @param visibilityKind of the MDR model
   * @return visibilityCode of the pmMDA model
   */
  public static VisibilityCode convertVisibility(VisibilityKind visibilityKind) {

    VisibilityCode visibility = VisibilityCode
        .getCode(VisibilityCode.PUBLIC_ID);

    if (visibilityKind == VisibilityKindEnum.VK_PUBLIC) {
      visibility = VisibilityCode.getCode(VisibilityCode.PUBLIC_ID);
    } else if (visibilityKind == VisibilityKindEnum.VK_PROTECTED) {
      visibility = VisibilityCode.getCode(VisibilityCode.PROTECTED_ID);
    } else if (visibilityKind == VisibilityKindEnum.VK_PRIVATE) {
      visibility = VisibilityCode.getCode(VisibilityCode.PRIVATE_ID);
    } else if (visibilityKind == VisibilityKindEnum.VK_PACKAGE) {
      visibility = VisibilityCode.getCode(VisibilityCode.PACKAGE_ID);
    }

    return visibility;
  }

  /**
   * Converts the visibility modifier from pmMDA to the MDR model
   * @param visibility of the pmMDA model
   * @return visibility of the MDR model
   */
  public static VisibilityKind convertVisibility(VisibilityCode visibility) {
    VisibilityKind vis = null;

    if (visibility == null) {
      System.out.println("Visibility ist null. MDA2MDR");
    }

    if (VisibilityCode.PUBLIC_ID == visibility.getCode()) {
      vis = VisibilityKindEnum.VK_PUBLIC;
    } else if (VisibilityCode.PROTECTED_ID == visibility.getCode()) {
      vis = VisibilityKindEnum.VK_PROTECTED;
    } else if (VisibilityCode.PRIVATE_ID == visibility.getCode()) {
      vis = VisibilityKindEnum.VK_PRIVATE;
    } else if (VisibilityCode.PACKAGE_ID == visibility.getCode()) {
      vis = VisibilityKindEnum.VK_PACKAGE;
    }
    return vis;
  }

  /**
   * Converts the multiplicity from MDR to pmMDA model
   * @param multiplicity in MDR model
   * @return multiplicity in pmMDA model
   */
  public static ch.bbv.mda.Multiplicity convertMultiplicity(
      Multiplicity multiplicity) {
    ch.bbv.mda.Multiplicity mdaMultiplicity = null;
    MultiplicityRange range = null;
    try {
      range = (MultiplicityRange) multiplicity.getRange().iterator().next();
      mdaMultiplicity = new ch.bbv.mda.Multiplicity(range.getLower(), range
          .getUpper());
    } catch (RuntimeException e) {
      // ignore NullPointerExceptions etc.
    }
    return mdaMultiplicity;
  }

  /**
   * Returns the full qualified name of a classifier
   * @param clazz the classifier
   * @return full qualified name as string
   */
  public static String getFullQualifiedName(Classifier clazz) {
    StringBuilder sb = new StringBuilder();
    sb.append(clazz.getName());
    Namespace namespace = clazz.getNamespace();
    while ((namespace != null) && (namespace.getNamespace() != null)) {
      sb.insert(0, namespace.getName() + ".");
      namespace = namespace.getNamespace();
    }

    return sb.toString();
  }

  /**
   * Extracts a tagged value with specified name out of a model element
   * @param source the model element
   * @param tagName the name of the tagged value
   * @return the tagged value
   */
  public static org.omg.uml.foundation.core.TaggedValue getTag(
      ModelElement source, String tagName) {
    assert ((source != null) && (tagName != null));
    org.omg.uml.foundation.core.TaggedValue tag = null;
    for (Iterator<?> i = source.getTaggedValue().iterator(); i.hasNext();) {
      tag = (org.omg.uml.foundation.core.TaggedValue) i.next();

      if (tagName.equals(tag.getName())) {
        break;
      }
      tag = null;
    }
    return tag;
  }
  
  /**
   * Loads the provided file, opens the file containing the XMI model and
   * creates a MDR model.
   * 
   * @param file
   *          file containing the MDR model.
   * @return the MDR model defined in the file.
   * @throws FileNotFoundException
   *           the given file could not be found.
   * @throws IOException
   *           an error occurred when reading data.
   */
  public static UmlPackage loadXMIFile(File file) throws FileNotFoundException,
      IOException {
    
    InputStream source = null;
    
    if (file.getName().endsWith(ZARGO_EXTENSION)) {
      FileInputStream inputStream = new FileInputStream(file);
      ZipInputStream input = new ZipInputStream(inputStream); 
      String name = input.getNextEntry().getName();
      while (!name.endsWith(XMI_EXTENSION)) {
        name = input.getNextEntry().getName();
      }
      source = new BufferedInputStream(input);
    } else if(file.getName().endsWith(XMI_EXTENSION)) {
      source = new BufferedInputStream(new FileInputStream(file));
    } else{
      throw new FileNotFoundException("File not compatible!");
    }
    return loadXMIFromStream(source);
  }
  
  /**
   * Loads an XMI 1.2 file by its stream
   * @param inputStream
   * @return the mdr UmlPackage
   */
  public static UmlPackage loadXMIFromStream(InputStream inputStream){
    MdrLoader mdrLoader = new MdrLoader();
    UmlPackage uml = null;
    try {
      mdrLoader.init();
      uml = mdrLoader.read(inputStream);

    } catch (PersistenceException e) {
      e.printStackTrace();
    }
    return uml;
  }
  


}
