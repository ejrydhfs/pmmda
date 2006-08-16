/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_NAME;
import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_POSTFIX_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_PREFIX_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.COLUMN_SIZE;
import static ch.bbv.mda.cartridges.OrmConstants.DATABASE_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.GENERATOR_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.IS_PRIMARY;
import static ch.bbv.mda.cartridges.OrmConstants.ORM_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.ORM_TYPE_JDBC;
import static ch.bbv.mda.cartridges.OrmConstants.ORM_TYPE_SQL;
import static ch.bbv.mda.cartridges.OrmConstants.SCHEMA;
import static ch.bbv.mda.cartridges.OrmConstants.SOURCE_ROOT_FOLDER_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.TABLE_NAME;
import static ch.bbv.mda.cartridges.OrmConstants.TABLE_POSTFIX_KEY;
import static ch.bbv.mda.cartridges.OrmConstants.TABLE_PREFIX_KEY;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.UmlConstants;
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

  private Map<String, OrmSchema> schemas;

  /**
   * Root directory of the generated source files.
   */
  private File root;

  private File ddlXmlFile;

  /**
   * Flag indicating if the DDL generation is done with Apache database DdlUtils
   * project.
   */
  private boolean useGeneratorDdlUtils;

  /**
   * Default constructor of the class.
   */
  public OrmCartridge() {
    super("Object Relational Mapper Cartridge", PREFIX, new VersionId(0, 3, 0));
    schemas = new HashMap<String, OrmSchema>();
  }

  @Override
  public void initialize(Properties properties) {
    super.initialize(properties);
    TaggedValuesView.registerReferenceCode(JdbcTypeCode.class, JdbcTypeCode
        .getCodes(), false, true);
    CartridgeFactory factory = CartridgeFactory.getInstance();
    factory.getItemFactory().registerListener(TaggedValue.class,
        new OrmTagListener(this));
    useGeneratorDdlUtils = "ddlUtils".equalsIgnoreCase(getContext()
        .getProperty(GENERATOR_KEY));
  }

  @Override
  public void registerRules(Validator validator) {
    TagRule tagRule = new OrmTagRule();
    tagRule.register(validator);
  }

  @Override
  public void preProcessDatatype(MetaDatatype datatype) {
    String database = getContext().getProperty(DATABASE_KEY);
    datatype.addAttribute(ORM_TYPE_SQL, translateDatatype(datatype, "SQL",
        database, false));
    datatype.addAttribute(ORM_TYPE_JDBC, translateDatatype(datatype, "JDBC",
        null, false));
  }

  @Override
  public void preProcessModel(MetaModel model) {
    assert model != null;
    root = mkdirs(getContext().getProperty(SOURCE_ROOT_FOLDER_KEY));
  }

  @Override
  public void preProcessPackage(MetaPackage packages) {
    assert packages != null;
    String schemaName = packages.valueOfTag(SCHEMA);
    if ((schemaName != null) && !schemas.containsKey(schemaName)) {
      OrmSchema schema = new OrmSchema(schemaName);
      schemas.put(schemaName, schema);
      try {
        String orm = getContext().getProperty(ORM_KEY);
        String database = getContext().getProperty(DATABASE_KEY);
        schema.ormBoWriter = FileOperators.createPrintWriter(new File(root,
            schemaName + "-" + orm + "_bo_user.xml"));
        schema.ormLwWriter = FileOperators.createPrintWriter(new File(root,
            schemaName + "-" + orm + "_lw_user.xml"));
        if (useGeneratorDdlUtils) {
          ddlXmlFile = new File(root, schemaName + "-DdlUtils.xml");
          schema.ddlXmlWriter = FileOperators.createPrintWriter(ddlXmlFile);
        }
        schema.ddlWriter = FileOperators.createPrintWriter(new File(root, schemaName
            + "-createTables" + "-" + database + ".sql"));
        schema.ddlIntegrityWriter = FileOperators.createPrintWriter(new File(
            root, schemaName + "-createIntegrity" + "-" + database + ".sql"));
      } catch (IOException e) {
        log.error(e);
      }
    }
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
    String orm = getContext().getProperty(ORM_KEY);
    String database = getContext().getProperty(DATABASE_KEY);
    OrmSchema schema = schemas.get(clazz.retrievePackage().valueOfTag(SCHEMA));
    assert (schema != null);
    if (schema.getState() == OrmSchema.SchemaState.prefix) {
      VelocityContext context = createContext((MetaPackage) clazz.getContext());
      context.put("prefix", Boolean.TRUE);
      schema.ormBoWriter.print(processTemplate(retrieveTemplate(
          "xml-bo-repository", orm), context));
      schema.ormLwWriter.print(processTemplate(retrieveTemplate(
          "xml-lw-repository", orm), context));
      if (useGeneratorDdlUtils) {
        schema.ddlXmlWriter.print(processTemplate(retrieveTemplate("ddl",
            "Torque"), context));
      } else {
        schema.ddlWriter.print(processTemplate(
            retrieveTemplate("ddl", database), context));
        schema.ddlIntegrityWriter.print(processTemplate(retrieveTemplate(
            "ddl-integrity", database), context));
      }
      schema.setState(OrmSchema.SchemaState.body);
    }
    VelocityContext context = createPersistencyContext(clazz);
    schema.ormBoWriter.print(processTemplate(retrieveTemplate(
        "xml-bo-repository", orm), context));
    schema.ormLwWriter.print(processTemplate(retrieveTemplate(
        "xml-lw-repository", orm), context));
    if (useGeneratorDdlUtils) {
      schema.ddlXmlWriter.print(processTemplate(retrieveTemplate("ddl",
          "Torque"), context));
    } else {
      schema.ddlWriter.print(processTemplate(retrieveTemplate("ddl", database),
          context));
      schema.ddlIntegrityWriter.print(processTemplate(retrieveTemplate(
          "ddl-integrity", database), context));
    }
  }

  @Override
  public void postProcessModel(MetaModel model) {
    VelocityContext context = new VelocityContext();
    context.put("postfix", Boolean.TRUE);
    String orm = getContext().getProperty(ORM_KEY);
    String database = getContext().getProperty(DATABASE_KEY);
    for (OrmSchema schema : schemas.values()) {
      schema.ormBoWriter.print(processTemplate(retrieveTemplate(
          "xml-bo-repository", orm), context));
      schema.ormLwWriter.print(processTemplate(retrieveTemplate(
          "xml-lw-repository", orm), context));
      if (useGeneratorDdlUtils) {
        schema.ddlXmlWriter.print(processTemplate(retrieveTemplate("ddl",
            "Torque"), context));
      } else {
        schema.ddlWriter.print(processTemplate(
            retrieveTemplate("ddl", database), context));
        schema.ddlIntegrityWriter.print(processTemplate(retrieveTemplate(
            "ddl-integrity", database), context));
      }
      FileOperators.closePrintWriter(schema.ormBoWriter);
      FileOperators.closePrintWriter(schema.ormLwWriter);
      FileOperators.closePrintWriter(schema.ddlXmlWriter);
      if (useGeneratorDdlUtils) {
        Database db = new DatabaseIO().read(ddlXmlFile);
        Platform platform = PlatformFactory.createNewPlatformInstance(database);
        SqlBuilder builder = platform.getSqlBuilder();
        builder.setWriter(schema.ddlWriter);
        try {
          builder.createTables(db);
        } catch (IOException e) {
          log.error("Could not write the DDL description file with ddl Utils",
              e);
        }
      }
      FileOperators.closePrintWriter(schema.ddlWriter);
      FileOperators.closePrintWriter(schema.ddlIntegrityWriter);
    }
  }

  /**
   * Returns the translated type name of the type of the property. The
   * translation of datatypes is handled.
   * @param property property to infer
   * @param language language of the requested mapping
   * @return the component name of the property type
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
   * Returns the size parameter for the element. In case where no size is set
   * within the model then the default size of the type is used.
   * @param element element which size parameter should be returned.
   * @param index the index of the requested parameter.
   * @return the requested size parameter if defined otherwise "0".
   */
  public String getSizeParameter(MetaElement element, int index) {
    String database = getContext().getProperty(DATABASE_KEY);
    String sizes = element.valueOfTag(COLUMN_SIZE);
    if (sizes == null && element instanceof MetaProperty) {
      sizes = PropertyUtilities.getInfoOfType(getConfiguration(),
          ((MetaProperty) element).getTypeName(), "SQL", database, "default",
          false);
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
   * @param clazz clazz which persistent context should be generated
   * @return the computed context
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