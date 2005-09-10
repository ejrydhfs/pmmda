/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.EjbConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.TagDefinition;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.generators.CartridgeImpl;

/**
 * The EJB cartridge generates all the artifacts for the data object framework
 * for a client server application based on J2EE or CORBA architecture.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class EjbCartridge extends CartridgeImpl {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "ejb";

  /**
   * Log instance for all instances of the class.
   */
  private static Log log = LogFactory.getLog(EjbCartridge.class);

  /**
   * Flag indicating if the package is an interface package or not. Only classes
   * belonging to interface packages are made public through CORBA IDL
   * descriptions.
   */
  private boolean isPackageInterface;

  /**
   * Default constructor of the class.
   */
  public EjbCartridge() {
    super("Entity Java Bean Cartridge", PREFIX, new VersionId(0, 3, 0));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);

    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge(BoCartridge.PREFIX));
    addCartridge(factory.getCartridge(CtoCartridge.PREFIX));
    addUndefinedProperty(properties, GENERATE_CORBA_KEY, "false");
    addUndefinedProperty(properties, GENERATE_BEAN_KEY, "false");
    addUndefinedProperty(properties, SERVER_PACKAGE_EXTENSION_KEY, "");
    addUndefinedProperty(properties, CLIENT_PACKAGE_EXTENSION_KEY, "");
    addUndefinedProperty(properties, ROOT_SERVER_CORBA_KEY, "");
    addUndefinedProperty(properties, ROOT_CLIENT_CORBA_KEY, "");
    addUndefinedProperty(properties, ROOT_SERVER_J2EE_KEY, "");
    addUndefinedProperty(properties, ROOT_CLIENT_J2EE_KEY, "");
  }

  public List<TagDefinition> getTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    if (clazz.equals(MetaPackage.class)) {
      types.add(new TagDefinition(SERVER_NAME_TAG, null, String.class));
    }
    return types;
  }

  public List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = null;
    if (MetaClass.class.equals(clazz)) {
      types = new ArrayList<TagDefinition>();
      types.add(new TagDefinition("ejb-is-root", Boolean.TYPE));
      types.add(new TagDefinition(SERVER_NAME_TAG, String.class));
    }
    return types;
  }

  public void preProcessPackage(MetaPackage metaPackage) {
    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    String qualifiedName = metaPackage.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(SERVER_PACKAGE_EXTENSION_KEY));
    metaPackage.addAttribute(SERVER_PACKAGE, buffer.toString());
    buffer.setLength(0);
    buffer.append(qualifiedName).append(
        context.getProperty(CLIENT_PACKAGE_EXTENSION_KEY));
    metaPackage.addAttribute(CLIENT_PACKAGE, buffer.toString());
  }

  public void processPackage(MetaPackage packages) {
    isPackageInterface = BoConstants.INTERFACE_STEREOTYPE
    .equalsIgnoreCase(packages.getStereotype());
    if (!isPackageInterface
        || !packages.booleanValueOfTag(BoConstants.GENERATE_TAG)
        || !shouldCreateServer(packages)) {
      return;
    }

    VelocityContext context = createContext(packages);
    try {
      if (getContext().getProperty(GENERATE_CORBA_KEY).equals("true")) {
        File serverRoot = getRoot(ROOT_SERVER_CORBA_KEY,
            BoConstants.ROOT_SERVER_FOLDER_KEY);
        File clientRoot = getRoot(ROOT_CLIENT_CORBA_KEY,
            BoConstants.ROOT_CLIENT_FOLDER_KEY);
        File IdlRoot = new File(getContext().getProperty(
            CtoConstants.ROOT_IDL_KEY));
        processTemplate(IdlRoot, packages
            .valueOfTag(CtoConstants.CTO_PACKAGE_IDL_TAG), packages
            .valueOfTag(SERVER_NAME_TAG)
            + CtoConstants.IDL_EXTENSION, retrieveTemplate("server-corba-idl",
            null), context);
        processJavaTemplate(serverRoot, packages
            .getAttribute(SERVER_PACKAGE), packages
            .valueOfTag(SERVER_NAME_TAG)
            + "Corba", retrieveTemplate("server-corba", null), context);
        processJavaTemplate(clientRoot, packages
            .getAttribute(CLIENT_PACKAGE), packages
            .valueOfTag(SERVER_NAME_TAG)
            + "CorbaWrapper", retrieveTemplate("server-corba-wrapper", null),
            context);
      }
      if (getContext().getProperty(GENERATE_BEAN_KEY).equals("true")) {
        File serverRoot = getRoot(ROOT_SERVER_J2EE_KEY,
            BoConstants.ROOT_SERVER_FOLDER_KEY);
        File clientRoot = getRoot(ROOT_CLIENT_J2EE_KEY,
            BoConstants.ROOT_CLIENT_FOLDER_KEY);
        File commonRoot = getRoot(ROOT_COMMON_J2EE_KEY,
            BoConstants.ROOT_COMMON_FOLDER_KEY);
        processJavaTemplate(commonRoot, packages
            .getAttribute(SERVER_PACKAGE), packages
            .valueOfTag(SERVER_NAME_TAG)
            + "Session", retrieveTemplate("server-session-if", null), context);
        processJavaTemplate(serverRoot, packages
            .getAttribute(SERVER_PACKAGE), packages
            .valueOfTag(SERVER_NAME_TAG)
            + "SessionBean", retrieveTemplate("server-session", null), context);
        processJavaTemplate(commonRoot, packages
            .getAttribute(SERVER_PACKAGE), packages
            .valueOfTag(SERVER_NAME_TAG)
            + "Home", retrieveTemplate("server-home", null), context);
        processJavaTemplate(clientRoot, packages
            .getAttribute(CLIENT_PACKAGE), packages
            .valueOfTag(SERVER_NAME_TAG)
            + "Wrapper", retrieveTemplate("server-wrapper", null), context);
        processTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME_TAG) + "-ejb-jar.xml",
            retrieveTemplate("server-descriptor", null), context);
        processTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME_TAG) + "-jboss.xml",
            retrieveTemplate("server-jboss-descriptor", null), context);
        processTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME_TAG) + "-jbosscmp-jdbc.xml",
            retrieveTemplate("server-jboss-jdbc-descriptor", null), context);
      }
    } catch (IOException e) {
      log.error("ejb write template generation failed", e);
    }
  }

  /**
   * Returns true if a EJB or CORBA server should be generated for the package.
   * @param packages package to check.
   * @return true if a server should be generated otherwise false.
   */
  private boolean shouldCreateServer(MetaPackage packages) {
    for (MetaClass clazz : packages.getClasses()) {
      if (BoConstants.DATA_OBJECT_STEREOTYPE.equals(clazz.getStereotype())
          && clazz.booleanValueOfTag(BoConstants.IS_ROOT_TAG)) {
        return true;
      }
    }
    return false;
  }
}