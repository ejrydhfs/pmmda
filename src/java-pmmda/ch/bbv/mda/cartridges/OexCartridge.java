/* copyright 2000-20056 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.OexConstants.IS_VISIBLE;
import static ch.bbv.mda.cartridges.OexConstants.OEX_RICH_CLIENT_PACKAGE;
import static ch.bbv.mda.cartridges.OexConstants.RICH_CLIENT_PACKAGE_POSTFIX_KEY;
import static ch.bbv.mda.cartridges.OexConstants.ROOT_RICH_CLIENT_KEY;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;

/**
 * The OEX cartridge generates all the artifacts for the explorer framework to
 * create a user interface to manipulate data object graphs.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OexCartridge extends JavaCartridge implements Cartridge {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "oex";

  /**
   * Log instance for the class.
   */
  private static Log log = LogFactory.getLog(OexCartridge.class);

  /**
   * Default constructor of the class.
   */
  public OexCartridge() {
    super("Object Explorer Cartridge", PREFIX, new VersionId(0, 3, 0));
    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge(BoCartridge.PREFIX));
  }

  /**
   * Returns a list of visible fields of the class. Visible fields are displayed
   * in the explorer user interface.
   * @param clazz class to analyze.
   * @return the list of visible fields of the class.
   */
  public Collection getVisibleFields(MetaClass clazz) {
    Map<String, MetaProperty> fields = new HashMap<String, MetaProperty>();
    for (MetaIndexedProperty object : clazz.getIndexedProperties()) {
      String className = object.getTypeName();
      if (className == null) {
        continue;
      }
      MetaClass objectClass = clazz.getModel().findClass(className);
      if ((objectClass == null)
          || objectClass.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        continue;
      }
      for (MetaProperty property : objectClass.getProperties()) {
        if (property.booleanValueOfTag(IS_VISIBLE)) {
          fields.put(property.getName(), property);
        }
      }
      for (MetaIndexedProperty property : objectClass.getIndexedProperties()) {
        if (property.booleanValueOfTag(IS_VISIBLE)) {
          fields.put(property.getName(), property);
        }
      }
    }
    return fields.values();
  }

  @Override
  public void preProcessPackage(MetaPackage packages) {
    assert packages != null;
    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    String qualifiedName = packages.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(RICH_CLIENT_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(OEX_RICH_CLIENT_PACKAGE, buffer.toString());
    super.preProcessPackage(packages);
  }

  @Override
  public void postProcessPackage(MetaPackage packages) {
    assert packages != null;
    File sourceRoot = getRoot(ROOT_RICH_CLIENT_KEY,
        BoConstants.ROOT_CLIENT_FOLDER_KEY);
    if (!sourceRoot.exists()) {
      sourceRoot.mkdirs();
    }
    if (packages.booleanValueOfTag(BoConstants.GENERATE)
        && hasLightweightClasses(packages)) {
      VelocityContext context = createContext(packages);
      log.info("OEX Catridge processing package " + packages.qualifiedName());
      try {
        processJavaTemplate(sourceRoot, packages
            .getAttribute(OEX_RICH_CLIENT_PACKAGE), "TypeFactory",
            retrieveTemplate("type-factory"), context);
      } catch (IOException e) {
        log.error(e);
      }
    }
  }

  private boolean hasLightweightClasses(MetaPackage packages) {
    boolean hasLightweightClasses = false;
    for (MetaClass clazz : packages.getClasses()) {
      if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)
          && clazz.booleanValueOfAttribute(BoConstants.BO_LIGHTWEIGHT)) {
        hasLightweightClasses = true;
        break;
      }
    }
    return hasLightweightClasses;
  }

  /**
   * Retrieves the velocity template with the given name.
   * @param name name of the template to retrieve.
   * @return the template if found otherwise null.
   */
  private Template retrieveTemplate(String name) {
    return super.retrieveTemplate(name, getContext().getProperty(
        BoConstants.LANGUAGE_KEY));
  }
}