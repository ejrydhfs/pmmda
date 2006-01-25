/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.BoConstants.KEY_CLASS;
import static ch.bbv.mda.cartridges.BoConstants.REFERENCE_CODE_STEREOTYPE;
import static ch.bbv.mda.cartridges.BoConstants.DATA_OBJECT_STEREOTYPE;

import java.util.Arrays;

import ch.bbv.application.VersionId;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeImpl;

/**
 * The Java cartridge provides mechanisms to generate source code artifacts for
 * the Java language.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class JavaCartridge extends CartridgeImpl implements Cartridge {
  /**
   * The list of primitive types in Java, C and C++.
   */
  public static final String[] PRIMITIVES_TYPES = { "boolean", "byte", "char",
      "double", "float", "int", "long", "short" };

  /**
   * Constructor fo the class.
   * @param name name of the cartridge.
   * @param abbreviation abbreviation of the cartridge.
   * @param version version of the catridge.
   */
  public JavaCartridge(String name, String abbreviation, VersionId version) {
    super(name, abbreviation, version);
  }

  /**
   * Returns true if the type of the property is a primitive build-in type.
   * @param member member to check if it is primitive.
   * @return true if the type is build-in otherwise false.
   * @pre (member != null) && (member.getClassifier() != null)
   */
  public boolean isTypePrimitive(MetaMember member) {
    assert (member != null) && (member.getClassifier() != null);
    MetaClassifier classifier = member.getClassifier();
    String typename = classifier.getAttribute("bo-datatype");
    if (typename == null) {
      typename = member.getTypeName();
    }
    return Arrays.binarySearch(PRIMITIVES_TYPES, typename) >= 0;
  }

  /**
   * Returns true if the property type is a data object.
   * @param property property to evaluate.
   * @return true if the property type is a data object otherwise false.
   */
  public boolean isDataobject(MetaProperty property) {
    MetaClass clazz = property.getModel().findClass(property.getTypeName());
    return (clazz != null) && (clazz.getStereotype() == DATA_OBJECT_STEREOTYPE);
  }

  /**
   * Returns true if the property type is a reference code.
   * @param property property to evaluate.
   * @return true if the property type is a reference code otherwise false.
   */
  public boolean isReferenceCode(MetaProperty property) {
    MetaClass clazz = property.getModel().findClass(property.getTypeName());
    return (clazz != null)
        && (clazz.getStereotype() == REFERENCE_CODE_STEREOTYPE);
  }

  /**
   * Returns true if the property is ordered. An indexed property is ordered if
   * the "bo-key-class" is not null.
   * @param property indexed property to evaluate.
   * @return true if the property is ordered otherwise false.
   */
  public boolean isOrdered(MetaIndexedProperty property) {
    String value = property.valueOfTag(KEY_CLASS);
    return (value != null) && (value.trim().length() > 0);
  }

  /**
   * Creates an import statement object.
   * @param packageName name of the package for the class containing the import
   *          statements.
   * @return the import statement structure.
   */
  public ImportStatements createImports(String packageName) {
    return new ImportStatements(packageName);
  }
}
