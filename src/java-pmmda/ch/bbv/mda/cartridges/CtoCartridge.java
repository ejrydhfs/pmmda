/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.CtoConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.generators.CartridgeImpl;
import ch.bbv.mda.operators.ModelOperators;

/**
 * The data transfer object handler generates all DTO artifacts to make all data
 * objects of a set of packagages transmitable through CORBA middlware.
 * Currently the following artifacts are generated.
 * <ul>
 * <li>The IDL description for the data objects sent through CORBA.</li>
 * <li>The IDL description for the lightweight objects sent through CORBA.
 * </li>
 * <li>The IDL description for the preloaded codes sent through CORBA.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class CtoCartridge extends CartridgeImpl implements Cartridge {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "cto";

  /**
   * List of all supported Java types which can have a null value.
   */
  public static final String[] NULLABLE_TYPES = { "Boolean",
      "java.lang.Boolean", "Byte", "java.lang.Byte", "Currency", "Double",
      "java.lang.Double", "Float", "java.lang.Float", "Integer",
      "java.lang.Integer", "java.sql.Date", "java.sql.Timestamp",
      "java.util.Date", "Long", "java.lang.Long", "Short", "java.lang.Short",
      "String", "java.lang.String" };

  /**
   * List of all primitive types of the Java language.
   */
  public static final String[] PRIMITIVE_TYPES = { "boolean", "byte", "char",
      "double", "float", "integer", "long", "short" };

  /**
   * Map between Java types and IDL corresponding representation.
   */
  private static Map<String, String> java2idl;

  static {
    Arrays.sort(NULLABLE_TYPES);
    Arrays.sort(PRIMITIVE_TYPES);
    java2idl = new HashMap<String, String>();
    java2idl.put("java.sql.Date", "NullDate");
    java2idl.put("java.util.Date", "NullDate");
    java2idl.put("java.sql.Timestamp", "NullTimestamp");
    java2idl.put("Boolean", "NullBoolean");
    java2idl.put("java.lang.Boolean", "NullBoolean");
    java2idl.put("String", "NullString");
    java2idl.put("java.lang.String", "NullString");
    java2idl.put("Short", "NullShort");
    java2idl.put("java.lang.Short", "NullShort");
    java2idl.put("Integer", "NullInteger");
    java2idl.put("java.lang.Integer", "NullInteger");
    java2idl.put("Double", "NullDouble");
    java2idl.put("java.lang.Double", "NullDouble");
    java2idl.put("Float", "NullFloat");
    java2idl.put("java.lang.Float", "NullFloat");
    java2idl.put("Long", "NullLong");
    java2idl.put("java.lang.Long", "NullLong");
    java2idl.put("Currency", "NullCurrency");
    java2idl.put("boolean", "boolean");
    java2idl.put("byte", "octet");
    java2idl.put("char", "char");
    java2idl.put("double", "double");
    java2idl.put("float", "float");
    java2idl.put("int", "long");
    java2idl.put("long", "long long");
    java2idl.put("short", "short");
  }

  /**
   * Log instance for all instance of the class.
   */
  private static Log log = LogFactory.getLog(CtoCartridge.class);

  /**
   * Flag indicating if the package is an interface package or not. Only classes
   * belonging to interface packages are made public through CORBA IDL
   * descriptions.
   */
  private boolean isPackageInterface;

  /**
   * Constructor of the class.
   */
  public CtoCartridge() {
    super("CORBA Transfer Object Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  @Override
  public void preProcessDatatype(MetaDatatype datatype) {
    assert datatype != null;
    String idlType = java2idl.get(datatype
        .getAttribute(BoConstants.BO_TYPE_JAVA));
    if (idlType != null) {
      datatype.addAttribute(CTO_IDL_TYPE, idlType);
    }
  }

  @Override
  public void preProcessPackage(MetaPackage packages) {
    assert packages != null;
    String attribute = expandIdentifier(null, packages.qualifiedName(),
        TRANSFORM_PACKAGE_POSTFIX);
    packages.addAttribute(CTO_BO2DTO_PACKAGE, attribute);
    packages.addAttribute(CTO_LW2DTO_PACKAGE, attribute);
  }

  @Override
  public void preProcessClass(MetaClass clazz) {
    assert clazz != null;
    Properties context = getContext();
    String name = clazz.getName();
    StringBuilder buffer = new StringBuilder();
    buffer.append(context.getProperty(IDL_PREFIX_KEY)).append(name).append(
        context.getProperty(BoConstants.BO_CLASS_POSTFIX_KEY)).append(
        context.getProperty(IDL_POSTFIX_KEY));
    clazz.addAttribute(CTO_MO_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(context.getProperty(IDL_PREFIX_KEY)).append(name).append(
        context.getProperty(BoConstants.LW_CLASS_POSTFIX_KEY)).append(
        context.getProperty(IDL_POSTFIX_KEY));
    clazz.addAttribute(CTO_LW_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(clazz.getAttribute(BoConstants.BO_CLASS)).append(
        "Transformer");
    clazz.addAttribute(CTO_BO2DTO_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(clazz.getAttribute(BoConstants.LW_CLASS)).append(
        "Transformer");
    clazz.addAttribute(CTO_LW2DTO_CLASS, buffer.toString());
    String idlType = java2idl.get(clazz.qualifiedName());
    if (idlType != null) {
      clazz.addAttribute(CTO_IDL_TYPE, idlType);
    }
  }

  @Override
  public void processPackage(MetaPackage packages) {
    assert packages != null;
    isPackageInterface = packages
        .hasStereotype(BoConstants.INTERFACE_STEREOTYPE);
  }

  @Override
  public void processClass(MetaClass clazz) {
    assert clazz != null;
    if (!isPackageInterface
        && !clazz.retrievePackage().booleanValueOfTag(BoConstants.GENERATE)) {
      return;
    }
    File javaRoot = getRoot(ROOT_JAVA_KEY, BoConstants.ROOT_COMMON_FOLDER_KEY);
    if (!javaRoot.exists()) {
      javaRoot.mkdirs();
    }
    File idlRoot = new File(getContext().getProperty(ROOT_IDL_KEY));
    if (!idlRoot.exists()) {
      idlRoot.mkdirs();
    }
    try {
      if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        MetaPackage metaPackage = (MetaPackage) clazz.getContext();
        VelocityContext context = createContext(clazz);
        processTemplate(idlRoot, metaPackage.valueOfTag(CTO_PACKAGE_IDL), clazz
            .getAttribute(CTO_MO_CLASS)
            + IDL_EXTENSION, retrieveTemplate("idl-dto-bo", null), context);
        processJavaTemplate(javaRoot, metaPackage
            .getAttribute(CTO_BO2DTO_PACKAGE), clazz
            .getAttribute(CTO_BO2DTO_CLASS), retrieveTemplate("java-bo2dto",
            null), context);
        if (clazz.booleanValueOfAttribute(BoConstants.BO_LIGHTWEIGHT)) {
          processTemplate(idlRoot, metaPackage.valueOfTag(CTO_PACKAGE_IDL),
              clazz.getAttribute(CTO_LW_CLASS) + IDL_EXTENSION,
              retrieveTemplate("idl-dto-lw", null), context);
          processJavaTemplate(javaRoot, metaPackage
              .getAttribute(CTO_LW2DTO_PACKAGE), clazz
              .getAttribute(CTO_LW2DTO_CLASS), retrieveTemplate("java-lw2dto",
              null), context);
        }
      }
    } catch (IOException e) {
      log.error(e);
    }
  }

  /**
   * Returns the IDL type of the type of the property.
   * @param property property wich IDL representation is requested.
   * @return the IDL type of the property.
   */
  public String getIdlType(MetaProperty property) {
    MetaClassifier classifier = property.getClassifier();
    String idlType = classifier.getAttribute(CTO_IDL_TYPE);
    if (idlType == null) {
      idlType = java2idl.get(classifier.qualifiedName());
    }
    return idlType;
  }

  /**
   * Creates the transform statement from a business object to a data transfer
   * object. The case of datatype is also handled.
   * @param property property to transform.
   * @param param name of the object from which the value is retrieved.
   * @return Java statement performing the transformation.
   */
  public String getBo2DtoConversion(MetaProperty property, String param) {
    StringBuilder buffer = new StringBuilder();
    String uName = ModelOperators.identifierFirstUpper(property.getName());
    uName = ModelOperators.identifierSingular(uName);
    String typename = getJavaType(property);
    if (Arrays.binarySearch(NULLABLE_TYPES, typename) >= 0) {
      buffer.append("TransformerCto.create(").append(param).append(".get")
          .append(uName).append("())");
    } else {
      String getter = ("boolean".equals(typename)) ? ".is" : ".get";
      buffer.append(param).append(getter).append(uName).append("()");
      if (!(Arrays.binarySearch(PRIMITIVE_TYPES, typename) >= 0)) {
        if ("Boolean".equals(typename)) {
          buffer.append(".booleanValue()");
        } else if ("Byte".equals(typename)) {
          buffer.append(".byteValue()");
        } else if ("Short".equals(typename)) {
          buffer.append(".shortValue()");
        } else if ("Integer".equals(typename)) {
          buffer.append(".intValue()");
        } else if ("Long".equals(typename)) {
          buffer.append(".longValue()");
        } else if ("Float".equals(typename)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typename)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typename)) {
          buffer.append(".toString()");
        }
      }
    }
    return buffer.toString();
  }

  /**
   * Creates the transform statement from a data transfer object to a business
   * object.
   * @param property property to transform.
   * @param param name of the object from which the value is retrieved.
   * @return Java statement performing the transformation.
   */
  public String getDto2BoConversion(MetaProperty property, String param) {
    StringBuilder buffer = new StringBuilder();
    String typename = getJavaType(property);
    if (Arrays.binarySearch(NULLABLE_TYPES, typename) >= 0) {
      buffer.append("TransformerCto.extract(").append(param).append(".")
          .append(property.nameFirstLower()).append(")");
    } else {
      buffer.append(param).append(".").append(property.nameFirstLower());
      if (!(Arrays.binarySearch(PRIMITIVE_TYPES, typename) >= 0)) {
        if ("Boolean".equals(typename)) {
          buffer.append(".booleanValue()");
        } else if ("Byte".equals(typename)) {
          buffer.append(".byteValue()");
        } else if ("Short".equals(typename)) {
          buffer.append(".shortValue()");
        } else if ("Integer".equals(typename)) {
          buffer.append(".intValue()");
        } else if ("Long".equals(typename)) {
          buffer.append(".longValue()");
        } else if ("Float".equals(typename)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typename)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typename)) {
          buffer.append(".toString()");
        }
      }
    }
    return buffer.toString();
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class which import statements should be infered.
   * @param packageName name of the package containing the artifact associated
   *          with the class.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForBo(MetaClass clazz,
      String packageName) {
    BoCartridge cartridge = (BoCartridge) CartridgeFactory.getInstance()
        .getCartridge(BoCartridge.PREFIX);
    return cartridge.retrieveImportStatementsForBo(clazz, packageName);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class which import statements should be infered.
   * @param packageName name of the package containing the artifact associated
   *          with the class.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForIf(MetaClass clazz,
      String packageName) {
    BoCartridge cartridge = (BoCartridge) CartridgeFactory.getInstance()
        .getCartridge(BoCartridge.PREFIX);
    return cartridge.retrieveImportStatementsForIf(clazz, packageName);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class which import statements should be infered.
   * @param packageName name of the package containing the artifact associated
   *          with the class.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForLw(MetaClass clazz,
      String packageName) {
    BoCartridge cartridge = (BoCartridge) CartridgeFactory.getInstance()
        .getCartridge(BoCartridge.PREFIX);
    return cartridge.retrieveImportStatementsForLw(clazz, packageName);
  }

  private String getJavaType(MetaProperty property) {
    MetaClassifier classifier = property.getClassifier();
    String typename = classifier.getAttribute(BoConstants.BO_TYPE_JAVA);
    if (typename == null) {
      typename = classifier.qualifiedName();
    }
    return typename;
  }
}