/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.OrmConstants.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.generators.CartridgeImpl;
import ch.bbv.mda.generators.validation.OrmTagRule;
import ch.bbv.mda.generators.validation.TagRule;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.operators.ModelOperators;
import ch.bbv.mda.ui.TaggedValuesView;
import ch.bbv.utilities.PropertyUtilities;

/**
 * The OJB persistence handler generates all persistency artifacts to make all
 * data objects of a set of package persistent. Currently the following
 * persistency artifacts are supported.
 * <ul>
 * <li>The XML description file for the Jakarta OJB impedence mismatch
 * persistency layer.</li>
 * <li>The XML description file for the Jakarta Torque impedence mismatch
 * persistency layer.</li>
 * <li>The DDL SQL creation statement for major databases. Less used products
 * are supported through the Torque target.</li>
 * <li>The DDL integrity rules SQL creation statement for major database. Less
 * used products are supported through the Torque target.</li>
 * </ul>
 * <p>
 * The cartridge should not specify the type of the collection used in OJB. The
 * transfer framework uses the bean collection classes which are different from
 * OJB's ones. If specified and different OJB mechanisms do not set anymore the
 * foreign key values.
 * <p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class OrmCartridge extends CartridgeImpl implements Cartridge {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "orm";

  /**
   * Logger of all instance of the class.
   */
  private static Log log = LogFactory.getLog(OrmCartridge.class);

  /**
   * Writer used to generate the associated artifact.
   */
  private PrintWriter ddlWriter;

  /**
   * Writer used to generate the associated artifact.
   */
  private PrintWriter ddlIntegrityWriter;

  /**
   * Writer used to generate the associated artifact.
   */
  private PrintWriter ormBoWriter;

  /**
   * Writer used to generate the associated artifact.
   */
  private PrintWriter ormLwWriter;

  /**
   * Root directory of the generated source files.
   */
  private File root;

  /**
   * Flag indicating if currently processed class is the first of the package or
   * not.
   */
  private boolean isFirstClassOfPackage;

  /**
   * Contains database key: brand of database to gernerate for
   * @see OrmConstants
   */
  private String databaseKey = "";

  /**
   * Contains database key: brand of orm to generate for
   * @see OrmConstants
   */
  private String ormKey = "";

  /**
   * Default constructor of the class.
   */
  public OrmCartridge() {
    super("Object Relational Mapper Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  @Override
  public void initialize(Properties properties) {
    super.initialize(properties);
    TaggedValuesView.registerReferenceCode(JdbcTypeCode.class, JdbcTypeCode
        .getCodes(), false, true);
    CartridgeFactory factory = CartridgeFactory.getInstance();
    factory.getItemFactory().registerListener(TaggedValue.class,
        new OrmTagListener(this));
    databaseKey = getContext().getProperty(DATABASE_KEY);
    ormKey = getContext().getProperty(ORM_KEY);
  }

  @Override
  public void registerRules(Validator validator) {
    TagRule tagRule = new OrmTagRule();
    tagRule.register(validator);
  }

  @Override
  public void preProcessDatatype(MetaDatatype datatype) {
    datatype.addAttribute(ORM_TYPE_SQL, translateDatatype(datatype, "SQL",
        databaseKey, false));
    datatype.addAttribute(ORM_TYPE_JDBC, translateDatatype(datatype, "JDBC",
        null, false));
  }

  @Override
  public void processModel(MetaModel model) {
    assert model != null;
    root = new File(getContext().getProperty(SOURCE_ROOT_FOLDER_KEY));
    if (!root.exists()) {
      root.mkdirs();
    }
    try {
      ormBoWriter = FileOperators.createPrintWriter(new File(root, ormKey
          + "_bo_user.xml"));
      ormLwWriter = FileOperators.createPrintWriter(new File(root, ormKey
          + "_lw_user.xml"));
      ddlWriter = FileOperators.createPrintWriter(new File(root, "createTables"
          + databaseKey + ".sql"));
      ddlIntegrityWriter = FileOperators.createPrintWriter(new File(root,
          "createIntegrity" + databaseKey + ".sql"));
    } catch (IOException e) {
      log.error(e);
    }
  }

  @Override
  public void processPackage(MetaPackage packages) {
    assert packages != null;
    isFirstClassOfPackage = true;
  }

  @Override
  public void preProcessClass(MetaClass clazz) {
    assert clazz != null;
    if (clazz.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
      Properties context = getContext();
      StringBuilder buffer = new StringBuilder();
      buffer.append(context.get(TABLE_PREFIX_KEY)).append(
          clazz.getTaggedValue(TABLE_NAME).getValue()).append(
          context.get(TABLE_POSTFIX_KEY));
      clazz.addAttribute(TABLE_NAME, buffer.toString());
      String columnPrefix = (String) context.get(COLUMN_PREFIX_KEY);
      String columnPostfix = (String) context.get(COLUMN_POSTFIX_KEY);
      for (MetaProperty property : clazz.getProperties()) {
        if (isPersistent(property)) {
          buffer.setLength(0);
          String columnName = property.valueOfTag(COLUMN_NAME);
          if (columnName == null || columnName.equals("")) {
            columnName = property.getName().toUpperCase();
          }
          buffer.append(columnPrefix).append(columnName).append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
      for (MetaProperty property : clazz.getIndexedProperties()) {
        if (isPersistent(property)) {
          buffer.setLength(0);
          String columnName = property.valueOfTag(COLUMN_NAME);
          if (columnName == null || columnName.equals("")) {
            columnName = property.getName().toUpperCase();
          }
          buffer.append(columnPrefix).append(columnName).append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
    }
  }

  @Override
  public void processClass(MetaClass clazz) {
    assert clazz != null;
    if (!clazz.retrievePackage().booleanValueOfTag(BoConstants.GENERATE)
        || !clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)
        || !clazz.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
      return;
    }
    if (isFirstClassOfPackage) {
      VelocityContext context = createContext((MetaPackage) clazz.getContext());
      context.put("prefix", Boolean.TRUE);
      ormBoWriter.print(processTemplate(retrieveTemplate("xml-bo-repository",
          ormKey), context));
      ormLwWriter.print(processTemplate(retrieveTemplate("xml-lw-repository",
          ormKey), context));
      ddlWriter.print(processTemplate(retrieveTemplate("ddl", databaseKey),
          context));
      Template template = retrieveTemplate("ddl-integrity", databaseKey);
      if (template != null) {
        ddlIntegrityWriter.print(processTemplate(template, context));
      }
      isFirstClassOfPackage = false;
    }
    VelocityContext context = createPersistencyContext(clazz);
    ormBoWriter.print(processTemplate(retrieveTemplate("xml-bo-repository",
        ormKey), context));
    ormLwWriter.print(processTemplate(retrieveTemplate("xml-lw-repository",
        ormKey), context));
    ddlWriter.print(processTemplate(retrieveTemplate("ddl", databaseKey),
        context));
    Template template = retrieveTemplate("ddl-integrity", databaseKey);
    if (template != null) {
      ddlIntegrityWriter.print(processTemplate(template, context));
    }
  }

  @Override
  public void postProcessModel(MetaModel model) {
    VelocityContext context = new VelocityContext();
    context.put("postfix", Boolean.TRUE);
    String orm = getContext().getProperty(ORM_KEY);
    ormBoWriter.print(processTemplate(
        retrieveTemplate("xml-bo-repository", orm), context));
    ormLwWriter.print(processTemplate(
        retrieveTemplate("xml-lw-repository", orm), context));
    FileOperators.closePrintWriter(ormBoWriter);
    FileOperators.closePrintWriter(ormLwWriter);
    FileOperators.closePrintWriter(ddlWriter);
    FileOperators.closePrintWriter(ddlIntegrityWriter);
  }

  /**
   * Returns the translated type name of the type of the property. The
   * translation of datatypes is handled.
   * @param property property to infer.
   * @param language language of the requested mapping.
   * @return the component name of the property type.
   * @pre (property != null) && (language != null)
   */
  public String getDatatype(MetaProperty property, String language) {
    assert (property != null) && (language != null);
    String attribute = null;
    if (language.equalsIgnoreCase("JDBC")) {
      attribute = ORM_TYPE_JDBC;
    } else if (language.equalsIgnoreCase("SQL")) {
      attribute = ORM_TYPE_SQL;
    }
    String qualifiedName = null;
    if (attribute != null) {
      qualifiedName = property.getClassifier().getAttribute(attribute);
    }
    if (qualifiedName == null) {
      qualifiedName = property.getTypeName();
      qualifiedName = ModelOperators.getComponentName(qualifiedName);
    }
    return qualifiedName;
  }

  /**
   * Returns the SQL type representation of the type of the property. The
   * translation of datatypes is handled.
   * @param property property to infer.
   * @return the component name of the property type.
   * @pre property != null
   */
  public String getSqlType(MetaProperty property) {
    assert property != null;
    String type = property.valueOfTag(OrmConstants.COLUMN_TYPE);
    if (type == null) {
      type = getDatatype(property, "SQL");
    }
    return type;
  }

  /**
   * Flag indicating if the class is the first one being processed in the
   * package.
   * @return value of the flag.
   */
  public boolean isFirstClassOfPackage() {
    return isFirstClassOfPackage;
  }

  /**
   * Returns the size parameter for the element. In case where no size is set
   * within the model then the default size of the type is used.
   * @param element element which size parameter should be returned.
   * @param index the index of the requested parameter.
   * @return the requested size parameter if defined otherwise "0".
   */
  public String getSizeParameter(MetaElement element, int index) {
    String sizes = element.valueOfTag(COLUMN_SIZE);
    if (sizes == null && element instanceof MetaProperty) {
      sizes = PropertyUtilities.getInfoOfType(getConfiguration(),
          ((MetaProperty) element).getTypeName(), "SQL", databaseKey,
          "default", false);
    }
    String size;
    if (sizes != null) {
      int position = sizes.indexOf(".");
      if (position > -1) {
        switch (index) {
        case 0:
          size = position != -1 ? sizes.substring(0, position) : "0";
          break;
        case 1:
          size = position != -1 ? sizes.substring(position + 1) : "0";
          break;
        default:
          size = "0";
        }
      } else {
        size = sizes;
      }
    } else {
      size = "0";
    }
    return size;
  }

  /**
   * Creates the context for a persistent class.
   * @param clazz clazz which persistent context should be generated.
   * @return the computed context.
   */
  protected VelocityContext createPersistencyContext(MetaClass clazz) {
    VelocityContext context = createContext(clazz);
    StringBuilder buffer = new StringBuilder();
    int nrOfPrimaryProperties = 0;
    for (MetaProperty property : clazz.getProperties()) {
      if (property.booleanValueOfTag(IS_PRIMARY)) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        String columnName = property.valueOfTag(COLUMN_NAME);
        if (columnName == null || columnName.equals("")) {
          columnName = property.getName().toUpperCase();
        }
        buffer.append(columnName);
        nrOfPrimaryProperties++;
      }
    }
    if (buffer.length() > 0) {
      context.put("primaryKeys", buffer.toString());
    }
    clazz.setNrOfPrimaryProperties(nrOfPrimaryProperties);
    return context;
  }
}