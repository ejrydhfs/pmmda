/* 
 * copyright 2005 http://www.bbv.ch
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 */

package ch.bbv.mda.cartridges.dotNet;

import static ch.bbv.mda.cartridges.OrmConstants.DATABASE_KEY;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaClassifier;
import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.UmlConstants;
import ch.bbv.mda.cartridges.BoConstants;
import ch.bbv.mda.cartridges.OrmConstants;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeImplNet;
import ch.bbv.mda.generators.CartridgeInfo.Platform;
import ch.bbv.mda.ui.TaggedValuesView;

import com.jgoodies.validation.ValidationResult;

/**
 * The NHibernate.NET cartridge generates all persistency artifacts to make all
 * data objects of a set of package persistent. Currently the following
 * persistency artifacts are supported.
 * <ul>
 * <li>The XML description file for the Hibernate.NET impedence mismatch
 * persistency layer.</li>
 * </ul>
 * 
 * @author Mikkala Pedersen
 */
public class NHibernateCartridge extends CartridgeImplNet implements Cartridge {
  private static final String HIBERNATE_SERIALIZABLE = "Serializable";

  public static final String INHERITANCE_STRATEGY_TAG = ".net-nhibernate-inheritance-strategy";
  public static final String NET_ASSEMBLY_TAG = ".net-nhibernate-assembly";
  public static final String COLUMN_HIBERNATE_TYPE_TAG = ".net-nhibernate-column-hibernate-type";

  private static final String TABLE_NAME_ATTRIBUTE = ".net-nhibernate-table-name";
  private static final String COLUMN_NAME_ATTRIBUTE = ".net-nhibernate-column-name";
  private static final String COLUMN_TYPE_ATTRIBUTE = ".net-nhibernate-column-db-type";
  private static final String COLUMN_SIZE_ATTRIBUTE = ".net-nhibernate-column-size";
  private static final String DATA_OBJECTS_PACKAGE_ATTRIBUTE = ".net-data-object-package";
  private static final String DATA_OBJECT_CLASS_NAME_ATTRIBUTE = ".net-data-object-class";

  public static final String PREFIX = "DotNetNHibernate";

  private static Log log = LogFactory.getLog(NHibernateCartridge.class);
  private Template templateHibernateNETDoRepository;
  private Template templateHibernateNETLwRepository;
  private Template templateDDL;
  private Template templateDDLIntegrity;
  private Template templateDDLStoredProcedure;
  private String database = OrmConstants.DB_MYSQL;

  /**
   * Currently processed model.
   */
  private MetaModel model;

  /**
   * Default constructor of the hibernate.net cartridge.
   */
  public NHibernateCartridge() {
    super("NHibernate Cartridge", PREFIX, new VersionId(0, 0, 5));
  }

  /**
   * Sets the cartridge context for the cartridge. The context contains all user
   * specific configuration values.
   * 
   * @param properties
   *          new cartridge context properties.
   * @pre (properties != null)
   */
  public void initialize(Properties properties) {
    assert (properties != null);
    super.initialize(properties);

    try {
      database = properties.getProperty(DATABASE_KEY);
    } catch (NullPointerException e) {
    }
    TaggedValuesView.registerReferenceCode(NHibernateTypeCode.class,
        NHibernateTypeCode.getCodes(), false, false);
    TaggedValuesView.registerReferenceCode(MySqlTypeCode.class, MySqlTypeCode
        .getCodes(), false, false);

    try {
      templateHibernateNETDoRepository = retrieveTemplate(
          "nhibernate-do-xml-repository", null);
    } catch (Exception e) {
      log
          .fatal(
              "Could not load the velocity template for the NHibernate data object repository.xml",
              e);
    }

    try {
      templateHibernateNETLwRepository = retrieveTemplate(
          "nhibernate-lw-xml-repository", null);
    } catch (Exception e) {
      log
          .fatal(
              "Could not load the velocity template for the ojb.net lightweight object repository.xml",
              e);
    }

    try {
      templateDDL = retrieveTemplate("nhibernate-ddl-net", database);
    } catch (Exception e) {
      log
          .fatal(
              "Error while loading the velocity template for the ddl genneration for pmMDA.NET",
              e);
    }

    try {
      templateDDLIntegrity = retrieveTemplate("nhibernate-ddl-integrity-net",
          database);
    } catch (Exception e) {
      log
          .fatal(
              "Error while loading the velocity template for the integrity script genneration for pmMDA.NET",
              e);
    }
    
    try {
        templateDDLStoredProcedure = retrieveTemplate("nhibernate-ddl-storedprocedure-net", database);
      } catch (Exception e) {
        log
            .fatal(
                "Error while loading the velocity template for the stored procedure generation for pmMDA.NET",
                e);
      }
  }

  @Override
  public void preProcessPackage(MetaPackage metaPackage,
      ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(metaPackage)) {
      return;
    }
    Properties context = getContext();
    StringBuffer buffer = new StringBuffer();
    String qualifiedName = metaPackage.getQualifiedName();
    buffer
        .append(qualifiedName)
        .append(
            context
                .getProperty(NetCartridgeConstants.DATA_OBJECTS_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(DATA_OBJECTS_PACKAGE_ATTRIBUTE, buffer.toString());

    super.preProcessPackage(metaPackage, messages);
  }

  @Override
  public void preProcessClass(MetaClass clazz, ValidationResult messages) {
    if (DogNetCartridge.IsIgnored(clazz)) {
      return;
    }
    Properties context = getContext();
    String name = clazz.getName();
    StringBuffer buffer = new StringBuffer();
    buffer
        .append(
            context
                .getProperty(NetCartridgeConstants.DATA_OBJECTS_CLASS_PREFIX_KEY))
        .append(name)
        .append(
            context
                .getProperty(NetCartridgeConstants.DATA_OBJECTS_CLASS_POSTFIX_KEY));
    clazz.addAttribute(DATA_OBJECT_CLASS_NAME_ATTRIBUTE, buffer.toString());
    setAttributeValue(clazz, OrmConstants.TABLE_NAME, TABLE_NAME_ATTRIBUTE,
        clazz.getName() + "Table");
    clazz.addAttribute(UmlConstants.PERSISTENCE_TAG, Boolean
        .toString(isPersistent(clazz)));
    MetaClass baseClass = getRootBaseClass(clazz);
    clazz.addAttribute(INHERITANCE_STRATEGY_TAG, baseClass
        .valueOfTag(INHERITANCE_STRATEGY_TAG));
    String typeName = "";
    for (MetaProperty metaProperty : clazz.getProperties()) {
      if (!isPersistent(metaProperty)) {
        continue;
      }
      setAttributeValue(metaProperty, OrmConstants.COLUMN_NAME,
          COLUMN_NAME_ATTRIBUTE, metaProperty.nameFirstUpperSingular()
              + "Column");

      MetaClass propertyType = model.findClass(metaProperty.getTypeName());
      typeName = getOrmTypeFromMappingsFile(null, metaProperty.getTypeName());
      if (typeName != null && !typeName.equals("")) {
        metaProperty.addAttribute(COLUMN_HIBERNATE_TYPE_TAG, typeName);
      } else {
        NHibernateTypeCode code = NHibernateTypeCode.getCode(metaProperty
            .getTypeName());

        if (code != null) { // type is known by hibernate - base type
          metaProperty.addAttribute(COLUMN_HIBERNATE_TYPE_TAG, code
              .getShortDescription());
        } else { // type defined within model or complex .NET framework type
          NetCartridgeHelper netHelper = new NetCartridgeHelper(this, model);
          typeName = metaProperty.getTypeName();
          if (propertyType != null) {
            String assembly = propertyType.retrievePackage().valueOfTag(
                NET_ASSEMBLY_TAG);
            typeName = netHelper
                .getFullyQualifiedDotnetType(typeName, assembly);
          } else { // komplex .NET type
            typeName = HIBERNATE_SERIALIZABLE;
          }
          metaProperty.addAttribute(COLUMN_HIBERNATE_TYPE_TAG, typeName);
        }
      }

      // Set column size in case of string
      String columnSize = metaProperty.getAttribute(OrmConstants.COLUMN_SIZE);
      if (columnSize == null) {
        columnSize = "";
      }
      String typeTag = metaProperty.getAttribute(COLUMN_HIBERNATE_TYPE_TAG);
      if (typeTag == null) {
        typeTag = metaProperty.getTypeName();
      }
      if (columnSize.equals("") && typeTag.equalsIgnoreCase("string")) {
        setAttributeValue(metaProperty, OrmConstants.COLUMN_SIZE,
            COLUMN_SIZE_ATTRIBUTE, "255");
      } else {
        setAttributeValue(metaProperty, OrmConstants.COLUMN_SIZE,
            COLUMN_SIZE_ATTRIBUTE, columnSize);
      }

      typeName = getSQLTypeFromMappingsFile(database, metaProperty
          .getTypeName());
      if (typeName != null) {
        setAttributeValue(metaProperty, OrmConstants.COLUMN_TYPE,
            COLUMN_TYPE_ATTRIBUTE, typeName);
      } else {
        String sqlType = null;
        if (propertyType != null) {
          if (propertyType.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
            sqlType = getReferenceCodeType(database);
          } else if (propertyType
              .hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
            sqlType = MySqlTypeCode.getCode(MySqlTypeCode.INTEGER)
                .getShortDescription(); // foreign key
          } else {
            MySqlTypeCode code = MySqlTypeCode.getCode(propertyType);
            if (code != null)
              sqlType = code.getShortDescription();
          }
        } else {
          MySqlTypeCode code = MySqlTypeCode.getCode(metaProperty
              .retrieveTypeClassName());
          if (code != null)
            sqlType = code.getShortDescription();
        }
        if (sqlType != null) {
          setAttributeValue(metaProperty, OrmConstants.COLUMN_TYPE,
              COLUMN_TYPE_ATTRIBUTE, sqlType);
        } else {
          setAttributeValue(metaProperty, OrmConstants.COLUMN_TYPE,
              COLUMN_TYPE_ATTRIBUTE, "UNKNOWN_SQL_TYPE");
          if (propertyType == null
              || !propertyType.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
            // If the property type is null, then it must be a type that is not
            // defined in the model.
            // --> A mapping to a primitive type must exist.
            // If the property type is set but it is not persistent a mapped
            // MySql type is also required.
            // If the property type is a persistent data object it's not
            // necessary to have a MySqlType.
            // --> ignore UNKNOWN_SQL_TYPE
            String message = "";
            MetaElement metaElement = metaProperty.getContext();
            while (metaElement != null) {
              message = metaElement.getName() + "." + message;
              metaElement = metaElement.getContext();
            }
            log.fatal("Could not find Sql type for attribute: " + message
                + metaProperty.getName() + " : "
                + metaProperty.retrieveTypeClassName());
          }
        }
      }
    }
    for (MetaIndexedProperty indexedProperty : clazz.getIndexedProperties()) {
      if (!isPersistent(indexedProperty)) {
        continue;
      }
      setAttributeValue(indexedProperty, OrmConstants.COLUMN_NAME,
          COLUMN_NAME_ATTRIBUTE, indexedProperty.nameFirstUpperSingular()
              + "Column");
    }
  }

  @Override
  public void preProcessModel(MetaModel metaModel, ValidationResult messages) {
    super.preProcessModel(metaModel, messages);
    this.model = metaModel;
  }

  @Override
  public void postProcessModel(MetaModel metaModel, ValidationResult messages) {
    super.postProcessModel(metaModel, messages);
    this.model = null;
  }

  /**
   * Processes the model. The context contains the project information. Makes
   * sure the output directory exists.
   * 
   * @param metaModel
   *          MDA model to process.
   * @param messages
   *          ValidationResults is the container where additional messages for
   *          the user are added.
   * @pre model != null
   */
  @Override
  public void processModel(MetaModel metaModel, ValidationResult messages) {
    super.processModel(metaModel, messages);
    // get the root folder from the properies
    File rootDirectory = new File(getContext().getProperty(
        NetCartridgeConstants.SOURCE_ROOT_FOLDER_KEY));
    if (!rootDirectory.exists()) {
      rootDirectory.mkdirs();
    }

    VelocityContext context = createPersistencyContext(metaModel);

    try {
      processTemplate(rootDirectory, null, "repository_do.hbm.xml",
          templateHibernateNETDoRepository, context, messages);
      processTemplate(rootDirectory, null, "repository_lw.hbm.xml",
          templateHibernateNETLwRepository, context, messages);

      processTemplate(rootDirectory, null, "pmMDA.NET.createTables." + database
          + ".sql", templateDDL, context, messages);
      processTemplate(rootDirectory, null, "pmMDA.NET.createIntegrity."
          + database + ".sql", templateDDLIntegrity, context, messages);
      processTemplate(rootDirectory, null, "pmMDA.NET.createStoredProcedures." + database
              + ".sql", templateDDLStoredProcedure, context, messages);
    } catch (IOException ex) {
      String msg = "Failed to process the velocity templates generating hibernate mapping files.";
      log.error(msg);
      log.error(ex.getMessage(), ex);
      messages.addError(msg);
    }

  }

  /**
   * Creates the velocity context for a model.
   * 
   * @param model
   *          model which persistent context should be generated.
   * @return the computed context.
   */
  protected VelocityContext createPersistencyContext(MetaModel model) {
    VelocityContext context = createContext(model);
    NetCartridgeHelper netHelper = new NetCartridgeHelper(this, model);
    context.put("netHelper", netHelper);
    return context;
  }

  /**
   * Returns the base class which don't derive from any other class.
   * 
   * @param clazz
   *          The meta class to check
   * @return The base class which don't derive from another class.
   */
  private MetaClass getRootBaseClass(MetaClass clazz) {
    List<MetaClass> ancestors = clazz.retrieveAncestors();
    while (ancestors.size() > 0) {
      clazz = ancestors.get(0);
      ancestors = clazz.retrieveAncestors();
    }
    return clazz;
  }

  /**
   * Set the attribute value of a meta element to the value of a tagged value or
   * a default value (unspecifiedValue) if the tagged value is not set.
   * 
   * @param element
   *          The meta element
   * @param tagKey
   *          The tagged value (typically orm key)
   * @param attributekey
   *          tagged value for hibernate
   * @param unspecifiedValue
   *          The default value used when the tagged values is not specified.
   */
  private void setAttributeValue(MetaElement element, String tagKey,
      String attributekey, String unspecifiedValue) {
    TaggedValue tag = element.getTaggedValue(tagKey);
    if (tag != null && tag.getType() != null) {
      String currentTagValue = tag.getValue();
      if (currentTagValue == null || currentTagValue.equals("")) {
        currentTagValue = unspecifiedValue;
      }
      element.addAttribute(attributekey, currentTagValue);
    } else {
      element.addAttribute(attributekey, unspecifiedValue);
    }
  }

  /**
   * <p>
   * Returns whether a meta class is persistent.
   * </p>
   * <p>
   * A class is persitent if the persietent tagged value is true or if at least
   * one property is persistent.
   * </p>
   * 
   * @param clazz
   *          The meta class to check for persistence
   * @return True if the meta class is persistent. Otherwiese false.
   */
  private boolean isPersistent(MetaClass clazz) {
    assert (clazz != null);
    if (clazz.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
      return true;
    }
    for (MetaProperty property : clazz.getProperties()) {
      if (property.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
        return true;
      }
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      if (property.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns mapped data type
   * 
   * @param dialect
   *          of language (e.g. "mssqlserver" for "sql" language).
   * @param typeName
   *          to be mapped
   * @return null if no mapping available, otherwise mapped type.
   */
  private String getSQLTypeFromMappingsFile(String dialect, String typeName) {
    MetaClassifier propertyType = model.findClassifier(typeName);
    if (propertyType != null && propertyType instanceof MetaDatatype) {
      typeName = translateDatatype((MetaDatatype) propertyType, "SQL", dialect,
          false);
    } else {
      typeName = null;
    }
    return typeName;
  }

  /**
   * Returns mapped data type
   * 
   * @param dialect
   *          of language (e.g. "nhibernate" for "orm" language).
   * @param typeName
   *          to be mapped
   * @return null if no mapping available, otherwise mapped type.
   */
  private String getOrmTypeFromMappingsFile(String dialect, String typeName) {
    MetaClassifier propertyType = model.findClassifier(typeName);
    if (propertyType != null && propertyType instanceof MetaDatatype) {
      typeName = translateDatatype((MetaDatatype) propertyType, "ORM", dialect,
          false);
    } else {
      typeName = null;
    }
    return typeName;
  }

  /**
   * Depending on the database the type for a reference code is different. This
   * is a temporary solution until we have finished the ref code concept
   * 
   * @param database:
   *          OrmConstants.DB_??????
   * @return sqlType to be used for corresponding database
   */
  private String getReferenceCodeType(String database) {
    if (database.equalsIgnoreCase(OrmConstants.DB_MYSQL))
      return "INTEGER";
    if (database.equalsIgnoreCase(OrmConstants.DB_MS_SQL_SERVER))
      return "INTEGER";
    return "";
  }
}
