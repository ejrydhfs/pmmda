/*
 * copyright 2000-2004 Marcel Baumann Licensed under the Apache License, Version
 * 2.0 (the "Licence); you may not use this file except in compliance with the
 * License. $Id$
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
import ch.bbv.mda.ui.TaggedValuesView;

/**
 * The OJB persistency handler generates all persistency artifacts to make all
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
   * The tag shows if the class is persistent or not.
   */
  public static final String IS_PERSISTENT_TAG = "ojb-is-persistent";

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
   * Logger of the cartridge factory class.
   */
  private static Log log = LogFactory.getLog(OjbCartridge.class);

  private Template ojbXmlBoRepositoryTemplate;

  private Template ojbXmlLwRepositoryTemplate;

  private Template ddlDbTemplate;

  private Template ddlDbIntegrityTemplate;

  private PrintWriter ddlWriter;

  private PrintWriter ddlIntegrityWriter;

  private PrintWriter ojbBoWriter;

  private PrintWriter ojbLwWriter;

  private VelocityContext primaryContext;

  /**
   * Root directory of the generated source files.
   */
  private File rootDirectory;

  /**
   * Package currently being processed.
   */
  private MetaPackage metaPackage;

  /**
   * Default constructor of the class.
   */
  public OjbCartridge() {
    super("Object Java Bridge Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);

    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge("dog"));
    addUndefinedProperty(properties, SOURCE_ROOT_FOLDER_KEY, "");
    addUndefinedProperty(properties, DATABASE_KEY, "MySQL");
    addUndefinedProperty(properties, TABLE_PREFIX_KEY, "");
    addUndefinedProperty(properties, TABLE_POSTFIX_KEY, "");
    addUndefinedProperty(properties, COLUMN_PREFIX_KEY, "");
    addUndefinedProperty(properties, COLUMN_POSTFIX_KEY, "");
    TaggedValuesView.registerReferenceCode(JdbcTypeCode.class, JdbcTypeCode
        .getCodes(), false);
    try {
      String target = properties.getProperty(DATABASE_KEY);
      String key = DDL_START_KEY + target;
      String template = properties.getProperty(key);
      if (template == null) {
        MissingResourceException e = new MissingResourceException(
            "Definition error in the properies file", getPrefix(), key);
        log.fatal(e);
        throw e;
      }
      ddlDbTemplate = Velocity.getTemplate(template);
      key = DDL_INTEGRITY_START_KEY + target;
      template = properties.getProperty(key);
      if (template == null) {
        MissingResourceException e = new MissingResourceException(
            "Definition error in the properies file", getPrefix(), key);
        log.fatal(e);
        throw e;
      }
      ddlDbIntegrityTemplate = Velocity.getTemplate(template);
      ojbXmlBoRepositoryTemplate = Velocity
          .getTemplate("ojb-xmlBoRepository.vm");
      ojbXmlLwRepositoryTemplate = Velocity
          .getTemplate("ojb-xmlLwRepository.vm");
    } catch (ParseErrorException pee) {
      pee.printStackTrace();
    } catch (ResourceNotFoundException rnfe) {
      rnfe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List getTaggedValueTypes(Class clazz) {
    List types = new ArrayList();
    if (clazz.equals(MetaModel.class)) {
    } else if (clazz.equals(MetaPackage.class)) {
      types.add(new TaggedValueType(SCHEMA_TAG, "", String.class, this));
    } else if (clazz.equals(MetaClass.class)) {
      types.add(new TaggedValueType(IS_PERSISTENT_TAG, "false", Boolean.TYPE,
          this));
      types.add(new TaggedValueType(TABLE_NAME_TAG, null, String.class, this));
    } else if (clazz.equals(MetaProperty.class)) {
      types.add(new TaggedValueType(COLUMN_NAME_TAG, null, String.class, this));
      types.add(new TaggedValueType(COLUMN_TYPE_TAG, "String",
          JdbcTypeCode.class, this));
      types
          .add(new TaggedValueType(COLUMN_SIZE_TAG, "255", Integer.TYPE, this));
      types.add(new TaggedValueType(CAN_BE_NULL_TAG, "false", Boolean.TYPE,
          this));
      types
          .add(new TaggedValueType(IS_PRIMARY_TAG, "false", Boolean.TYPE, this));
      types.add(new TaggedValueType(OPTIMISTIC_LOCKING_TAG, "false",
          Boolean.TYPE, this));
    } else if (clazz.equals(MetaIndexedProperty.class)) {
      types.add(new TaggedValueType(COLUMN_NAME_TAG, null, String.class, this));
      types.add(new TaggedValueType(ORDER_BY_TAG, null, String.class, this));
    } else {
      types = null;
    }
    return types;
  }

  public TaggedValue getTaggedValueWithElementDependentDefault(
      TaggedValueType taggedValueType, MetaElement element) {
    TaggedValue value = new TaggedValue(taggedValueType);
    updateTaggedValueWithElementDependentDefault(value, element);
    return value;
  }

  public void updateTaggedValueWithElementDependentDefault(
      TaggedValue taggedValue, MetaElement element) {
    String name = element.getName();
    if (taggedValue.getType().getTag().equals(TABLE_NAME_TAG)) {
      if (name != null) {
        taggedValue.setValue(name + "Table");
      }
    } else if (taggedValue.getType().getTag().equals(SCHEMA_TAG)) {
      if (name != null) {
        taggedValue.setValue(name + "Schema");
      }
    } else if (taggedValue.getType().getTag().equals(COLUMN_NAME_TAG)) {
      if (name != null) {
        taggedValue.setValue(name + "Column");
      }
    } else if (element instanceof MetaProperty
        && taggedValue.getType().getTag().equals(COLUMN_TYPE_TAG)) {
      MetaProperty property = (MetaProperty) element;
      // remove package name
      String type = property.getTypeName();
      if (type != null) {
        int idx = type.lastIndexOf(".");
        if (idx != -1) {
          type = type.substring(idx + 1);
        }
        // convert textual string representation into jdbc code
        List codes = JdbcTypeCode.getCodes();
        for (Iterator i = codes.iterator(); i.hasNext();) {
          JdbcTypeCode jdbcCode = (JdbcTypeCode) i.next();
          if (jdbcCode.getShortDescription().toLowerCase().equals(
              type.toLowerCase())) {
            taggedValue.setValue(jdbcCode.getShortDescription());
            break;
          }
        }
      }
    }
  }

  public void processPackage(MetaPackage metaPackage) {
    this.metaPackage = metaPackage;
    if (!metaPackage.booleanValueOfTag(BogCartridge.GENERATE_TAG)) {
      return;
    }
  }

  public void processModel(MetaModel metaModel) {
    rootDirectory = new File(getContext().getProperty(SOURCE_ROOT_FOLDER_KEY));

    String target = getContext().getProperty(DATABASE_KEY);
    primaryContext = createContext(metaModel);
    try {
      ojbBoWriter = createPrintWriter(new File(rootDirectory,
          "repository_bo_user.xml"));
      ojbLwWriter = createPrintWriter(new File(rootDirectory,
          "repository_lw_user.xml"));
      ddlWriter = createPrintWriter(new File(rootDirectory, "createTables"
          + target + ".sql"));
      ddlIntegrityWriter = createPrintWriter(new File(rootDirectory,
          "createIntegrity" + target + ".sql"));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void preProcessClass(MetaClass clazz) {
    if (clazz.booleanValueOfTag(IS_PERSISTENT_TAG)) {
      Properties context = getContext();
      StringBuffer buffer = new StringBuffer();
      buffer.append(context.get(TABLE_PREFIX_KEY)).append(
          clazz.getTaggedValue(TABLE_NAME_TAG).getValue()).append(
          context.get(TABLE_POSTFIX_KEY));
      clazz.addAttribute(TABLE_NAME, buffer.toString());

      String columnPrefix = (String) context.get(COLUMN_PREFIX_KEY);
      String columnPostfix = (String) context.get(COLUMN_POSTFIX_KEY);
      for (Iterator i = clazz.getProperties().iterator(); i.hasNext();) {
        MetaProperty property = (MetaProperty) i.next();
        if (isPersistent(property)) {
          buffer.setLength(0);
          buffer.append(columnPrefix).append(
              property.valueOfTag(COLUMN_NAME_TAG)).append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
      for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
        MetaProperty property = (MetaProperty) i.next();
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
    if (!clazz.retrievePackage().booleanValueOfTag(BogCartridge.GENERATE_TAG)
        || !BogCartridge.DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(clazz
            .getStereotype()) || !clazz.booleanValueOfTag(IS_PERSISTENT_TAG)) {
      return;
    }

    VelocityContext context = createPersistencyContext(clazz);
    ojbBoWriter.print(processTemplate(ojbXmlBoRepositoryTemplate, context));
    ojbLwWriter.print(processTemplate(ojbXmlLwRepositoryTemplate, context));
    ddlWriter.print(processTemplate(ddlDbTemplate, context));
    ddlIntegrityWriter.print(processTemplate(ddlDbIntegrityTemplate, context));
  }

  public void postProcessModel(MetaModel metaModel) {
    ojbBoWriter.close();
    ojbLwWriter.close();
    ddlWriter.close();
    ddlIntegrityWriter.close();
  }

  /**
   * Creates the context for a persistent class.
   * @param clazz clazz which persistent context should be generated.
   * @return the computed context.
   */
  protected VelocityContext createPersistencyContext(MetaClass clazz) {
    VelocityContext context = new VelocityContext(primaryContext);
    context.put("database", getContext().getProperty(DATABASE_KEY));
    context.put("package", metaPackage);
    context.put("class", clazz);

    List properties = new ArrayList();
    List aggregations = new ArrayList();
    List references = new ArrayList();
    StringBuffer buffer = new StringBuffer();
    int nrOfPrimaryProperties = 0;
    for (Iterator i = clazz.getProperties().iterator(); i.hasNext();) {
      MetaProperty property = (MetaProperty) i.next();
      if (!isPersistent(property)) {
        continue;
      }
      properties.add(property);
      if (property.booleanValueOfTag(IS_PRIMARY_TAG)) {
        if (buffer.length() > 0) {
          buffer.append(", ");
        }
        buffer.append(property.valueOfTag(COLUMN_NAME_TAG));
        nrOfPrimaryProperties++;
      }
      if (property.retrieveIsReferenceCode()) {
        references.add(property);
      }
    }
    for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
      MetaIndexedProperty property = (MetaIndexedProperty) i.next();
      aggregations.add(property);
    }
    if (buffer.length() > 0) {
      context.put("primaryKeys", buffer.toString());
    }
    clazz.setNrOfPrimaryProperties(nrOfPrimaryProperties);
    context.put("properties", properties);
    context.put("aggregations", aggregations);
    context.put("keys", references);
    return context;
  }

  /**
   * Returns true if the property should be generated, meaning the column name
   * of the property is not null.
   * @param metaProperty property which persistency flag should be checked.
   * @return true if the element should be generated otherwise false.
   */
  private boolean isPersistent(MetaProperty metaProperty) {
    TaggedValue value = metaProperty.getTaggedValue(COLUMN_NAME_TAG);
    return (value != null);
  }
}