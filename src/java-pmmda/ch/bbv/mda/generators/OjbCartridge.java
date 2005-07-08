/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.generators;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.validation.OjbTagRule;
import ch.bbv.mda.generators.validation.TagRule;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.ui.TaggedValuesView;

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
 * @author Marcel Baumann
 * @version $Revision$
 */
public class OjbCartridge extends CartridgeImpl implements Cartridge {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "ojb";

  /**
   * Key part for a property of the cartridge used to retrieve user values.
   */
  public static final String DDL_START_KEY = "ojb-cartridge.ddl.";

  /**
   * Key part for a property of the cartridge used to retrieve user values.
   */
  public static final String DDL_INTEGRITY_START_KEY = "ojb-cartridge.ddl-integrity.";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String SOURCE_ROOT_FOLDER_KEY = "ojb-cartridge.root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_FK_INDEX_KEY = "ojb-cartridge.generate-fk-index";

  /**
   * Key for a property of the cartridge defining the inheritance mode.
   */
  public static final String INHERITANCE_KEY = "ojb-cartridge.inheritance";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String DATABASE_KEY = "ojb-cartridge.database";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String TABLE_PREFIX_KEY = "obj-cartridge.table-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String TABLE_POSTFIX_KEY = "obj-cartridge.table-postfix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String COLUMN_PREFIX_KEY = "obj-cartridge.column-prefix";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String COLUMN_POSTFIX_KEY = "obj-cartridge.column-postfix";

  /**
   * The tag shows if the class is persisten or not.
   */
  public static final String SCHEMA_TAG = "ojb-schema";

  /**
   * If the class is persistent, this tag contains the name of the table in
   * which the instances are persisted.
   */
  public static final String TABLE_NAME_TAG = "ojb-table-name";

  /**
   * JDBC column name of the property if the property is persistent.
   */
  public static final String COLUMN_NAME_TAG = "ojb-column-name";

  /**
   * JDBC type of the property if the property is persistent.
   */
  public static final String COLUMN_TYPE_TAG = "ojb-column-type";

  /**
   * JDBC type size of the property if the property is peristent and if the
   * information is relevant for the associated JDBC type.
   */
  public static final String COLUMN_SIZE_TAG = "ojb-column-size";

  /**
   * Indicates if the property is a primary key for the class it belongs to.
   */
  public static final String IS_PRIMARY_TAG = "ojb-is-primary";

  /**
   * True if the property persistent representation can be null. Such a property
   * must not be a primitive Java type and not a primary key.
   */
  public static final String CAN_BE_NULL_TAG = "ojb-can-be-null";

  /**
   * Indicates if the property to realise optimistic locking. The JDBC type
   * should be integer or timestamp if the flag is set to true.
   */
  public static final String OPTIMISTIC_LOCKING_TAG = "ojb-locking";

  /**
   * Defines if the property is used for ordering.
   */
  public static final String ORDER_BY_TAG = "ojb-order-by";

  /**
   * Attribute defining the computed table name.
   */
  public static final String TABLE_NAME = TABLE_NAME_TAG;

  /**
   * Attribute defining the computed column name.
   */
  public static final String COLUMN_NAME = COLUMN_NAME_TAG;

  /**
   * Database identifier for a database product.
   */
  public static final String DB_MYSQL = "MySQL";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_DB2 = "DB2";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_HSQL = "HSQL";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_POSTGRES = "POSTGRES";

  /**
   * Database identifier for a database product.
   */
  public static final String DB_TORQUE = "TORQUE";

  /**
   * Logger of all instance of the class.
   */
  private static Log log = LogFactory.getLog(OjbCartridge.class);

  /**
   * Velocity template used to generate the associated artifact.
   */
  private PrintWriter ddlWriter;

  /**
   * Velocity template used to generate the associated artifact.
   */
  private PrintWriter ddlIntegrityWriter;

  /**
   * Velocity template used to generate the associated artifact.
   */
  private PrintWriter ojbBoWriter;

  /**
   * Velocity template used to generate the associated artifact.
   */
  private PrintWriter ojbLwWriter;

  /**
   * Root directory of the generated source files.
   */
  private File root;

  /**
   * Default constructor of the class.
   */
  public OjbCartridge() {
    super("Object Java Bridge Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);
    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge(BoCartridge.PREFIX));
    addUndefinedProperty(properties, SOURCE_ROOT_FOLDER_KEY, "");
    addUndefinedProperty(properties, GENERATE_FK_INDEX_KEY, "false");
    addUndefinedProperty(properties, INHERITANCE_KEY, "expand");
    addUndefinedProperty(properties, DATABASE_KEY, "MySQL");
    addUndefinedProperty(properties, TABLE_PREFIX_KEY, "");
    addUndefinedProperty(properties, TABLE_POSTFIX_KEY, "");
    addUndefinedProperty(properties, COLUMN_PREFIX_KEY, "");
    addUndefinedProperty(properties, COLUMN_POSTFIX_KEY, "");
    TaggedValuesView.registerReferenceCode(JdbcTypeCode.class, JdbcTypeCode
        .getCodes(), false);
  }

  public List<TagDefinition> getTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    if (clazz.equals(MetaModel.class)) {
    } else if (clazz.equals(MetaPackage.class)) {
      types.add(new TagDefinition(SCHEMA_TAG, "", String.class));
    } else if (clazz.equals(MetaClass.class)) {
      types.add(new TagDefinition(BoCartridge.PERSISTENCE_TAG, "false",
          Boolean.TYPE));
      types.add(new TagDefinition(TABLE_NAME_TAG, null, String.class));
    } else if (clazz.equals(MetaProperty.class)) {
      types.add(new TagDefinition(COLUMN_NAME_TAG, null, String.class));
      types
          .add(new TagDefinition(COLUMN_TYPE_TAG, "String", JdbcTypeCode.class));
      types.add(new TagDefinition(COLUMN_SIZE_TAG, "255", Integer.TYPE));
      types.add(new TagDefinition(CAN_BE_NULL_TAG, "false", Boolean.TYPE));
      types.add(new TagDefinition(IS_PRIMARY_TAG, "false", Boolean.TYPE));
      types
          .add(new TagDefinition(OPTIMISTIC_LOCKING_TAG, "false", Boolean.TYPE));
    } else if (clazz.equals(MetaIndexedProperty.class)) {
      types.add(new TagDefinition(COLUMN_NAME_TAG, null, String.class));
      types.add(new TagDefinition(ORDER_BY_TAG, null, String.class));
    } else {
      types = null;
    }
    return types;
  }

  public List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = null;
    if (clazz == MetaClass.class) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("ojb-is-persistent", Boolean.TYPE));
      types.add(new TagDefinition("ojb-isPersistent", Boolean.TYPE));
    } else if (clazz == MetaProperty.class) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("ojb-jdbc-type", String.class));
      types.add(new TagDefinition("ojb-jdbc-size", Integer.TYPE));
      types.add(new TagDefinition("ojb-jdbcType", String.class));
      types.add(new TagDefinition("ojb-jdbcSize", Integer.TYPE));
      types.add(new TagDefinition("ojb-orderBy", String.class));
      types.add(new TagDefinition("ojb-foreign-key", String.class));
      types.add(new TagDefinition("ojb-canBeNull", Boolean.TYPE));
      types.add(new TagDefinition("ojb-isPrimary", Boolean.TYPE));
    } else if (clazz == MetaIndexedProperty.class) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("ojb-jdbc-type", String.class));
      types.add(new TagDefinition("ojb-jdbc-size", Integer.TYPE));
      types.add(new TagDefinition("ojb-jdbcType", String.class));
      types.add(new TagDefinition("ojb-jdbcSize", Integer.TYPE));
      types.add(new TagDefinition("ojb-orderBy", String.class));
      types.add(new TagDefinition("ojb-foreign-key", String.class));
      types.add(new TagDefinition("ojb-canBeNull", Boolean.TYPE));
      types.add(new TagDefinition("ojb-isPrimary", Boolean.TYPE));
      types.add(new TagDefinition(COLUMN_NAME_TAG, String.class));
      types.add(new TagDefinition(COLUMN_TYPE_TAG, JdbcTypeCode.class));
      types.add(new TagDefinition(COLUMN_SIZE_TAG, Integer.TYPE));
      types.add(new TagDefinition(OPTIMISTIC_LOCKING_TAG, Boolean.TYPE));
    }
    return types;
  }

  public void registerRules(Validator validator) {
    TagRule tagRule = new OjbTagRule();
    tagRule.register(validator);
  }

  public void processModel(MetaModel metaModel) {
    root = new File(getContext().getProperty(SOURCE_ROOT_FOLDER_KEY));
    String target = getContext().getProperty(DATABASE_KEY);
    try {
      ojbBoWriter = FileOperators.createPrintWriter(new File(root,
          "repository_bo_user.xml"));
      ojbLwWriter = FileOperators.createPrintWriter(new File(root,
          "repository_lw_user.xml"));
      ddlWriter = FileOperators.createPrintWriter(new File(root, "createTables"
          + target + ".sql"));
      ddlIntegrityWriter = FileOperators.createPrintWriter(new File(root,
          "createIntegrity" + target + ".sql"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void preProcessClass(MetaClass clazz) {
    if (clazz.booleanValueOfTag(BoCartridge.PERSISTENCE_TAG)) {
      Properties context = getContext();
      StringBuilder buffer = new StringBuilder();
      buffer.append(context.get(TABLE_PREFIX_KEY)).append(
          clazz.getTaggedValue(TABLE_NAME_TAG).getValue()).append(
          context.get(TABLE_POSTFIX_KEY));
      clazz.addAttribute(TABLE_NAME, buffer.toString());
      String columnPrefix = (String) context.get(COLUMN_PREFIX_KEY);
      String columnPostfix = (String) context.get(COLUMN_POSTFIX_KEY);
      for (MetaProperty property : clazz.getProperties()) {
        if (isPersistent(property)) {
          buffer.setLength(0);
          buffer.append(columnPrefix).append(
              property.valueOfTag(COLUMN_NAME_TAG)).append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
      for (MetaProperty property : clazz.getIndexedProperties()) {
        if (isPersistent(property)) {
          buffer.setLength(0);
          buffer.append(columnPrefix).append(
              property.valueOfTag(COLUMN_NAME_TAG)).append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
    }
  }

  public void processClass(MetaClass clazz) {
    if (!clazz.retrievePackage().booleanValueOfTag(BoCartridge.GENERATE_TAG)
        || !BoCartridge.DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(clazz
            .getStereotype())
        || !clazz.booleanValueOfTag(BoCartridge.PERSISTENCE_TAG)) {
      return;
    }

    VelocityContext context = createPersistencyContext(clazz);
    ojbBoWriter.print(processTemplate(retrieveTemplate("ojb-xml-bo-repository",
        null), context));
    ojbLwWriter.print(processTemplate(retrieveTemplate("ojb-xml-lw-repository",
        null), context));
    ddlWriter.print(processTemplate(retrieveTemplate("ddl", null), context));
    ddlIntegrityWriter.print(processTemplate(retrieveTemplate("ddl-integrity",
        null), context));
  }

  public void postProcessModel(MetaModel metaModel) {
    FileOperators.closePrintWriter(ojbBoWriter);
    FileOperators.closePrintWriter(ojbLwWriter);
    FileOperators.closePrintWriter(ddlWriter);
    FileOperators.closePrintWriter(ddlIntegrityWriter);
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
      if (property.booleanValueOfTag(IS_PRIMARY_TAG)) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(property.valueOfTag(COLUMN_NAME_TAG));
        nrOfPrimaryProperties++;
      }
    }
    if (buffer.length() > 0) {
      context.put("primaryKeys", buffer.toString());
    }
    clazz.setNrOfPrimaryProperties(nrOfPrimaryProperties);
    return context;
  }

  /**
   * Returns true if the property should be generated, meaning the column name
   * of the property is not null.
   * @param metaProperty property which persistency flag should be checked.
   * @return true if the element should be generated otherwise false.
   */
  private boolean isPersistent(MetaProperty metaProperty) {
    return (metaProperty.getTaggedValue(COLUMN_NAME_TAG) != null);
  }
}