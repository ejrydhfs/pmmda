/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.generators;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.application.VersionId;
import ch.bbv.mda.*;
import ch.bbv.utilities.TextUtilities;

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
   * Extension for IDL source file.
   */
  public static final String IDL_EXTENSION = ".idl";

  /**
   * Property defining the root folder where all IDL files are generated.
   */
  public static final String ROOT_IDL_KEY = "cto-cartridge.idl-root-folder";

  /**
   * Property defining the root folder where all Java transformer files are
   * generated.
   */
  public static final String ROOT_JAVA_KEY = "cto-cartridge.java-root-folder";

  /**
   * Property defining the package containing all transformers of a data object
   * package.
   */
  public static final String TRANSFORM_PACKAGE_POSTFIX = "cto-cartridge.transform-package-postfix";

  /**
   * Property defining the prefix for all IDL structures.
   */
  public static final String IDL_PREFIX_KEY = "cto-cartridge.idl-prefix";

  /**
   * Property defining the postfix for all IDL structures.
   */
  public static final String IDL_POSTFIX_KEY = "cto-cartridge.idl-postfix";

  /**
   * Tag defining the name of the package containing the IDL sources.
   */
  public static final String CTO_PACKAGE_IDL_TAG = "cto-package-idl";

  /**
   * Attribute defining the name of the IDL structure of the data object.
   */
  public static final String CTO_MO_CLASS = "cto-mo-class";

  /**
   * Attribute defining the name of the IDL structure of the lightweight object.
   */
  public static final String CTO_LW_CLASS = "cto-lw-class";

  /**
   * Attribute defining the IDL type of a property.
   */
  public static final String CTO_IDL_TYPE = "cto-idl-type";

  /**
   * Attribute defining the folder containing the data objects to CORBA
   * transformer Java classes.
   */
  public static final String CTO_BO2DTO_PACKAGE = "cto-bo2dto-package";

  /**
   * Attribute defining the folder containing the ligthweight data objects to
   * CORBA transformer Java classes.
   */
  public static final String CTO_LW2DTO_PACKAGE = "cto-lw2dto-package";

  /**
   * Attribute defining the name of the transformer class for the data object
   * transformers.
   */
  public static final String CTO_BO2DTO_CLASS = "cto-bo2dto-class";

  /**
   * Attribute defining the name of the transformer class for the lightweight
   * data object transformers.
   */
  public static final String CTO_LW2DTO_CLASS = "cto-lw2dto-class";

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
  public static final String[] PRIMITIVE_TYPES = { "boolean", "byte", "char", "double",
      "float", "integer", "long", "short" };

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
    java2idl.put("Double", "NullableDouble");
    java2idl.put("java.lang.Double", "NullableDouble");
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
   * Log instance for the class.
   */
  private static Log log = LogFactory.getLog(CtoCartridge.class);

  private Template idlDtoBoTemplate;

  private Template idlDtoLwTemplate;

  private Template javaBo2DtoTemplate;

  private Template javaLw2DtoTemplate;

  /**
   * Constructor of the class.
   */
  public CtoCartridge() {
    super("CORBA Transfer Object Cartridge", PREFIX, new VersionId(0, 3, 0));
    try {
      idlDtoBoTemplate = Velocity.getTemplate("cto-idlDtoBo.vm");
      idlDtoLwTemplate = Velocity.getTemplate("cto-idlDtoLw.vm");
      javaBo2DtoTemplate = Velocity.getTemplate("cto-javaBo2Dto.vm");
      javaLw2DtoTemplate = Velocity.getTemplate("cto-javaLw2Dto.vm");
    } catch (ParseErrorException pee) {
      pee.printStackTrace();
    } catch (ResourceNotFoundException rnfe) {
      rnfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initialize(Properties properties) {
    super.initialize(properties);

    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge(DogCartridge.PREFIX));
    addUndefinedProperty(properties, ROOT_IDL_KEY, "");
    addUndefinedProperty(properties, TRANSFORM_PACKAGE_POSTFIX, "");
    addUndefinedProperty(properties, IDL_PREFIX_KEY, "");
    addUndefinedProperty(properties, IDL_POSTFIX_KEY, "");
  }

  public List<TagDefinition> getTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    if (clazz.equals(MetaPackage.class)) {
      types.add(new TagDefinition(CTO_PACKAGE_IDL_TAG, "", String.class));
    }
    return types;
  }

  public TaggedValue getTaggedValueWithElementDependentDefault(
      TagDefinition taggedValueType, MetaElement element) {
    TaggedValue value = new TaggedValue(taggedValueType);
    updateTaggedValueWithElementDependentDefault(value, element);
    return value;
  }

  public void updateTaggedValueWithElementDependentDefault(
      TaggedValue taggedValue, MetaElement element) {
    if (taggedValue.getType().getTag().equals(CTO_PACKAGE_IDL_TAG)) {
      String name = element.getName();
      if (name != null) {
        taggedValue.setValue(name + "PackageIdl");
      }
    }
  }

  public void preProcessPackage(MetaPackage metaPackage) {
    assert (metaPackage != null);

    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    buffer.append(metaPackage.qualifiedName()).append(
        context.getProperty(TRANSFORM_PACKAGE_POSTFIX));
    metaPackage.addAttribute(CTO_BO2DTO_PACKAGE, buffer.toString());
    metaPackage.addAttribute(CTO_LW2DTO_PACKAGE, buffer.toString());
  }

  public void preProcessClass(MetaClass clazz) {
    assert (clazz != null);

    Properties context = getContext();
    String name = clazz.getName();
    StringBuilder buffer = new StringBuilder();
    buffer.append(context.getProperty(IDL_PREFIX_KEY)).append(name).append(
        context.getProperty(DogCartridge.BO_CLASS_POSTFIX_KEY)).append(
        context.getProperty(IDL_POSTFIX_KEY));
    clazz.addAttribute(CTO_MO_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(context.getProperty(IDL_PREFIX_KEY)).append(name).append(
        context.getProperty(DogCartridge.LW_CLASS_POSTFIX_KEY)).append(
        context.getProperty(IDL_POSTFIX_KEY));
    clazz.addAttribute(CTO_LW_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(clazz.getAttribute(DogCartridge.BO_CLASS)).append(
        "Transformer");
    clazz.addAttribute(CTO_BO2DTO_CLASS, buffer.toString());
    buffer.setLength(0);
    buffer.append(clazz.getAttribute(DogCartridge.LW_CLASS)).append(
        "Transformer");
    clazz.addAttribute(CTO_LW2DTO_CLASS, buffer.toString());

    for (Iterator i = clazz.getProperties().iterator(); i.hasNext();) {
      MetaProperty property = (MetaProperty) i.next();
      String idlType = java2idl.get(property.getTypeName());
      if (idlType != null) {
        property.addAttribute(CTO_IDL_TYPE, idlType);
      }
    }
    for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
      MetaProperty property = (MetaProperty) i.next();
      String idlType = java2idl.get(property.getTypeName());
      if (idlType != null) {
        property.addAttribute(CTO_IDL_TYPE, idlType);
      }
    }
  }

  public void processModel(MetaModel model) {
  }

  public void processClass(MetaClass clazz) {
    File javaRoot = getRoot(ROOT_JAVA_KEY, DogCartridge.ROOT_COMMON_FOLDER_KEY);
    File idlRoot = new File(getContext().getProperty(ROOT_IDL_KEY));
    if (!clazz.retrievePackage().booleanValueOfTag(DogCartridge.GENERATE_TAG)) {
      return;
    }
    try {
      if (DogCartridge.DATA_OBJECT_STEREOTYPE.equals(clazz.getStereotype())) {
        MetaPackage metaPackage = (MetaPackage) clazz.getContext();
        VelocityContext context = createContext(clazz);
        PrintWriter writer = createWriter(idlRoot, metaPackage
            .valueOfTag(CTO_PACKAGE_IDL_TAG), clazz.getAttribute(CTO_MO_CLASS)
            + IDL_EXTENSION);
        writer.print(processTemplate(idlDtoBoTemplate, context));
        writer.close();
        writer = createJavaWriter(javaRoot, metaPackage
            .getAttribute(CTO_BO2DTO_PACKAGE), clazz
            .getAttribute(CTO_BO2DTO_CLASS));
        writer.print(processTemplate(javaBo2DtoTemplate, context));
        writer.close();
        if (clazz.booleanValueOfAttribute(DogCartridge.BO_LIGHTWEIGHT)) {
          writer = createWriter(idlRoot, metaPackage
              .valueOfTag(CTO_PACKAGE_IDL_TAG), clazz
              .getAttribute(CTO_LW_CLASS)
              + IDL_EXTENSION);
          writer.print(processTemplate(idlDtoLwTemplate, context));
          writer.close();
          writer = createJavaWriter(javaRoot, metaPackage
              .getAttribute(CTO_LW2DTO_PACKAGE), clazz
              .getAttribute(CTO_LW2DTO_CLASS));
          writer.print(processTemplate(javaLw2DtoTemplate, context));
          writer.close();
        }
      }
    } catch (IOException ioe) {
      log.equals(ioe);
      ioe.printStackTrace();
    }
  }

  /**
   * Creates the transform statement from a business object to a data transfer
   * object.
   * @param property property to transform.
   * @param param name of the object from which the value is retrieved.
   * @return Java statement performing the transformation.
   */
  public String getBo2DtoConversion(MetaProperty property, String param) {
    StringBuilder buffer = new StringBuilder();
    String uName = TextUtilities.identifierFirstUpper(property.getName());
    String typeName = property.getTypeName();
    if (Arrays.binarySearch(NULLABLE_TYPES, typeName) >= 0) {
      buffer.append("TransformerCto.create(").append(param).append(".get")
          .append(uName).append("())");
    } else {
      buffer.append(param).append(".get").append(uName).append("()");
      if (!(Arrays.binarySearch(PRIMITIVE_TYPES, typeName) >= 0)) {
        if ("Boolean".equals(typeName)) {
          buffer.append(".booleanValue()");
        } else if ("Byte".equals(typeName)) {
          buffer.append(".byteValue()");
        } else if ("Short".equals(typeName)) {
          buffer.append(".shortValue()");
        } else if ("Integer".equals(typeName)) {
          buffer.append(".intValue()");
        } else if ("Long".equals(typeName)) {
          buffer.append(".longValue()");
        } else if ("Float".equals(typeName)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typeName)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typeName)) {
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
    String typeName = property.getTypeName();
    if (Arrays.binarySearch(NULLABLE_TYPES, typeName) >= 0) {
      buffer.append("TransformerCto.extract(").append(param).append(".")
          .append(property.nameFirstLower()).append(")");
    } else {
      buffer.append(param).append(".").append(property.nameFirstLower());
      if (!(Arrays.binarySearch(PRIMITIVE_TYPES, typeName) >= 0)) {
        if ("Boolean".equals(typeName)) {
          buffer.append(".booleanValue()");
        } else if ("Byte".equals(typeName)) {
          buffer.append(".byteValue()");
        } else if ("Short".equals(typeName)) {
          buffer.append(".shortValue()");
        } else if ("Integer".equals(typeName)) {
          buffer.append(".intValue()");
        } else if ("Long".equals(typeName)) {
          buffer.append(".longValue()");
        } else if ("Float".equals(typeName)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typeName)) {
          buffer.append(".longValue()");
        } else if ("Double".equals(typeName)) {
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
   *        with the class.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForBo(MetaClass clazz,
      String packageName) {
    DogCartridge cartridge = (DogCartridge) CartridgeFactory.getInstance()
        .getCartridge(DogCartridge.PREFIX);
    return cartridge.retrieveImportStatementsForBo(clazz, packageName);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class which import statements should be infered.
   * @param packageName name of the package containing the artifact associated
   *        with the class.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForIf(MetaClass clazz,
      String packageName) {
    DogCartridge cartridge = (DogCartridge) CartridgeFactory.getInstance()
        .getCartridge(DogCartridge.PREFIX);
    return cartridge.retrieveImportStatementsForIf(clazz, packageName);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class which import statements should be infered.
   * @param packageName name of the package containing the artifact associated
   *        with the class.
   * @return list of all import statements for the class.
   */
  public ImportStatements retrieveImportStatementsForLw(MetaClass clazz,
      String packageName) {
    DogCartridge cartridge = (DogCartridge) CartridgeFactory.getInstance()
        .getCartridge(DogCartridge.PREFIX);
    return cartridge.retrieveImportStatementsForLw(clazz, packageName);
  }

}