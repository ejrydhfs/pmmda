/* copyright 2000-2005 Marcel Baumann
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
   * Object relational mapper to use for the artifacts generation.
   */
  private String orm;

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
   * Default constructor of the class.
   */
  public OrmCartridge() {
    super("Object Relational Mapper Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);
    TaggedValuesView.registerReferenceCode(JdbcTypeCode.class, JdbcTypeCode
        .getCodes(), false);
    CartridgeFactory factory = CartridgeFactory.getInstance();
    factory.getItemFactory().registerListener(TaggedValue.class,
        new OrmTagListener(this));
  }

  @Override
  public void registerRules(Validator validator) {
    TagRule tagRule = new OrmTagRule();
    tagRule.register(validator);
  }

  @Override
  public void processModel(MetaModel model) {
    assert model != null;
    root = new File(getContext().getProperty(SOURCE_ROOT_FOLDER_KEY));
    String target = getContext().getProperty(DATABASE_KEY);
    orm = getContext().getProperty(ORM_KEY);
    try {
      ormBoWriter = FileOperators.createPrintWriter(new File(root, orm
          + "_bo_user.xml"));
      ormLwWriter = FileOperators.createPrintWriter(new File(root, orm
          + "_lw_user.xml"));
      ddlWriter = FileOperators.createPrintWriter(new File(root, "createTables"
          + target + ".sql"));
      ddlIntegrityWriter = FileOperators.createPrintWriter(new File(root,
          "createIntegrity" + target + ".sql"));
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
          buffer.append(columnPrefix).append(property.valueOfTag(COLUMN_NAME))
              .append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
      for (MetaProperty property : clazz.getIndexedProperties()) {
        if (isPersistent(property)) {
          buffer.setLength(0);
          buffer.append(columnPrefix).append(property.valueOfTag(COLUMN_NAME))
              .append(columnPostfix);
          property.addAttribute(COLUMN_NAME, buffer.toString());
        }
      }
    }
  }

  @Override
  public void processClass(MetaClass clazz) {
    assert clazz != null;
    if (!clazz.retrievePackage().booleanValueOfTag(BoConstants.GENERATE)
        || !BoConstants.DATA_OBJECT_STEREOTYPE.equalsIgnoreCase(clazz
            .getStereotype())
        || !clazz.booleanValueOfTag(UmlConstants.PERSISTENCE_TAG)) {
      return;
    }
    if (isFirstClassOfPackage) {
      VelocityContext context = createContext((MetaPackage)clazz.getContext());
      context.put("prefix", Boolean.TRUE);
      ormBoWriter.print(processTemplate(retrieveTemplate(orm
          + "-xml-bo-repository", null), context));
      ormLwWriter.print(processTemplate(retrieveTemplate(orm
          + "-xml-lw-repository", null), context));
      isFirstClassOfPackage = false;
    }
    VelocityContext context = createPersistencyContext(clazz);
    ormBoWriter.print(processTemplate(retrieveTemplate(orm
        + "-xml-bo-repository", null), context));
    ormLwWriter.print(processTemplate(retrieveTemplate(orm
        + "-xml-lw-repository", null), context));
    ddlWriter.print(processTemplate(retrieveTemplate("ddl", null), context));
    ddlIntegrityWriter.print(processTemplate(retrieveTemplate("ddl-integrity",
        null), context));
    isFirstClassOfPackage = false;
  }

  public void postProcessModel(MetaModel model) {
    VelocityContext context = new VelocityContext();
    context.put("postfix", Boolean.TRUE);
    ormBoWriter.print(processTemplate(retrieveTemplate(orm
        + "-xml-bo-repository", null), context));
    ormLwWriter.print(processTemplate(retrieveTemplate(orm
        + "-xml-lw-repository", null), context));
    FileOperators.closePrintWriter(ormBoWriter);
    FileOperators.closePrintWriter(ormLwWriter);
    FileOperators.closePrintWriter(ddlWriter);
    FileOperators.closePrintWriter(ddlIntegrityWriter);
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
   * Returns the size parameter for the element.
   * @param element element which size parameter should be returned.
   * @param index the index of the requested parameter.
   * @return the requested size parameter if defined otherwise "0".
   */
  public String getSizeParameter(MetaElement element, int index) {
    String sizes = element.valueOfTag(COLUMN_SIZE);
    String size;
    if (sizes != null) {
      int position = sizes.indexOf(".");
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
        buffer.append(property.valueOfTag(COLUMN_NAME));
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
    return (metaProperty.getTaggedValue(COLUMN_NAME) != null);
  }
}