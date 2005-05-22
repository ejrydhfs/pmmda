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

/**
 * The OEX cartridge generates all the artifacts for the explorer framework to
 * create a user interface to manipulate data object graphs.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OexCartridge extends CartridgeImpl {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "bog";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String RICH_CLIENT_PACKAGE_POSTFIX_KEY = "oex-cartridge.rich-client-package-extension";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_RICH_CLIENT_KEY = "oex-cartridge.rich-client-folder";

  /**
   * Tag name defining the class of the property view used to display the class.
   */
  public static final String VIEW_CLASS_TAG = "oex-view-class";

  /**
   * Tag name indicating if the class is visible in the explorer or not.
   */
  public static final String IS_VISIBLE_TAG = "oex-visible";

  /**
   * Tag name of the package containing the rich client source code artifacts.
   */
  public static final String OEX_RICH_CLIENT_PACKAGE = "oex-rich-client-package";

  /**
   * Log instance for the class.
   */
  private static Log log = LogFactory.getLog(OexCartridge.class);

  /**
   * velocity template to generate user interface types for the rich client.
   */
  private Template javaRichClientTypeTemplate;

  /**
   * Default constructor of the class.
   */
  public OexCartridge() {
    super("Object Explorer Cartridge", PREFIX, new VersionId(0, 3, 0));

    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge("dog"));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);
    addUndefinedProperty(properties, RICH_CLIENT_PACKAGE_POSTFIX_KEY, "");
    try {
      String postfix = "-" + getContext().getProperty(DogCartridge.LANGUAGE_KEY)
          + ".vm";
      javaRichClientTypeTemplate = Velocity.getTemplate("oex-typeFactory"
          + postfix);
    } catch (ParseErrorException e) {
      log.error(e);
    } catch (ResourceNotFoundException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }
  }

  /**
   * Returns a list of visible fields of the class. Visible fields are displayed
   * in the explorer user interface.
   * @param clazz class to analyze.
   * @return the list of visible fields of the class.
   */
  public Collection getVisibleFields(MetaClass clazz) {
    Map<String, MetaProperty> fields = new HashMap<String, MetaProperty>();
    MetaModel model = (MetaModel) clazz.getRoot();
    for (MetaIndexedProperty object : clazz.getIndexedProperties()) {
      String className = object.getTypeName();
      if (className == null) {
        continue;
      }

      MetaClass objectClass = model.findClass(className);
      if ((objectClass == null)
          || !DogCartridge.DATA_OBJECT_STEREOTYPE.equals(objectClass
              .getStereotype())) {
        continue;
      }

      for (MetaProperty property : objectClass.getProperties()) {
        if (property.booleanValueOfTag(IS_VISIBLE_TAG)) {
          fields.put(property.getName(), property);
        }
      }
      for (MetaIndexedProperty property :objectClass.getIndexedProperties()) {
        if (property.booleanValueOfTag(IS_VISIBLE_TAG)) {
          fields.put(property.getName(), property);
        }
      }
    }
    return fields.values();
  }

  public List<TagDefinition> getTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    if (clazz.equals(MetaClass.class)) {
      types.add(new TagDefinition(VIEW_CLASS_TAG, "", String.class));
    } else if (clazz.equals(MetaProperty.class)) {
      types.add(new TagDefinition(IS_VISIBLE_TAG, "false", Boolean.TYPE));
    } else if (clazz.equals(MetaIndexedProperty.class)) {
      types.add(new TagDefinition(IS_VISIBLE_TAG, "false", Boolean.TYPE));
    }
    return types;
  }

  public List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = null;
    if (clazz == MetaClass.class) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("oex-is-root", Boolean.class));
    }
    return types;
  }

  public void preProcessPackage(MetaPackage metaPackage) {
    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    String qualifiedName = metaPackage.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(RICH_CLIENT_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(OEX_RICH_CLIENT_PACKAGE, buffer.toString());
    super.preProcessPackage(metaPackage);
  }

  public void processModel(MetaModel model) {
  }

  public void postProcessPackage(MetaPackage metaPackage) {
    File sourceRoot = getRoot(ROOT_RICH_CLIENT_KEY,
        DogCartridge.ROOT_CLIENT_FOLDER_KEY);
    if (metaPackage.booleanValueOfTag(DogCartridge.GENERATE_TAG)) {
      VelocityContext context = createContext(metaPackage);
      log
          .info("OEX Catridge processing package "
              + metaPackage.qualifiedName());
      try {
        PrintWriter writer = createJavaWriter(sourceRoot, metaPackage
            .getAttribute(OEX_RICH_CLIENT_PACKAGE), "TypeFactory");
        writer.print(processTemplate(javaRichClientTypeTemplate, context));
        writer.close();
      } catch (IOException e) {
        log.error(e);
      }
    }
  }
}