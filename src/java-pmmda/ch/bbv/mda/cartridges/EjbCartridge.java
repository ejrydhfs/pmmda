/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.EjbConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.generators.Cartridge;

/**
 * The EJB cartridge generates all the artifacts for the data object framework
 * for a client server application based on J2EE or CORBA architecture.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class EjbCartridge extends JavaCartridge implements Cartridge {
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

  @Override
  public void preProcessPackage(MetaPackage packages) {
    assert packages != null;
    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    String qualifiedName = packages.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(SERVER_PACKAGE_EXTENSION_KEY));
    packages.addAttribute(SERVER_PACKAGE, buffer.toString());
    buffer.setLength(0);
    buffer.append(qualifiedName).append(
        context.getProperty(CLIENT_PACKAGE_EXTENSION_KEY));
    packages.addAttribute(CLIENT_PACKAGE, buffer.toString());
  }

  @Override
  public void processPackage(MetaPackage packages) {
    assert packages != null;
    isPackageInterface = packages
        .hasStereotype(BoConstants.INTERFACE_STEREOTYPE);
    if (!isPackageInterface
        && !packages.booleanValueOfTag(BoConstants.GENERATE)
        && !shouldCreateServer(packages)) {
      return;
    }

    VelocityContext context = createContext(packages);
    try {
      if (getContext().getProperty(GENERATE_CORBA_KEY).equals("true")) {
        File serverRoot = getRoot(ROOT_SERVER_CORBA_KEY,
            BoConstants.ROOT_SERVER_FOLDER_KEY);
        if (!serverRoot.exists()) {
          serverRoot.mkdirs();
        }
        File clientRoot = getRoot(ROOT_CLIENT_CORBA_KEY,
            BoConstants.ROOT_CLIENT_FOLDER_KEY);
        if (!clientRoot.exists()) {
          clientRoot.mkdirs();
        }
        File IdlRoot = new File(getContext().getProperty(
            CtoConstants.ROOT_IDL_KEY));
        if (!IdlRoot.exists()) {
          IdlRoot.mkdirs();
        }
        processTemplate(IdlRoot, packages
            .valueOfTag(CtoConstants.CTO_PACKAGE_IDL), packages
            .valueOfTag(SERVER_NAME)
            + CtoConstants.IDL_EXTENSION, retrieveTemplate("server-corba-idl",
            null), context);
        processJavaTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "Corba", retrieveTemplate(
                "server-corba", null), context);
        processJavaTemplate(clientRoot, packages.getAttribute(CLIENT_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "CorbaWrapper",
            retrieveTemplate("server-corba-wrapper", null), context);
      }
      if (getContext().getProperty(GENERATE_BEAN_KEY).equals("true")) {
        File serverRoot = getRoot(ROOT_SERVER_J2EE_KEY,
            BoConstants.ROOT_SERVER_FOLDER_KEY);
        if (!serverRoot.exists()) {
          serverRoot.mkdirs();
        }
        File clientRoot = getRoot(ROOT_CLIENT_J2EE_KEY,
            BoConstants.ROOT_CLIENT_FOLDER_KEY);
        if (!clientRoot.exists()) {
          clientRoot.mkdirs();
        }
        File commonRoot = getRoot(ROOT_COMMON_J2EE_KEY,
            BoConstants.ROOT_COMMON_FOLDER_KEY);
        if (!commonRoot.exists()) {
          commonRoot.mkdirs();
        }
        processJavaTemplate(commonRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "Session", retrieveTemplate(
                "server-session-if", null), context);
        processJavaTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "SessionBean", retrieveTemplate(
                "server-session", null), context);
        processJavaTemplate(commonRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "Home", retrieveTemplate(
                "server-home", null), context);
        processJavaTemplate(clientRoot, packages.getAttribute(CLIENT_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "Wrapper", retrieveTemplate(
                "server-wrapper", null), context);
        processTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "-ejb-jar.xml",
            retrieveTemplate("server-descriptor", null), context);
        processTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "-jboss.xml", retrieveTemplate(
                "server-jboss-descriptor", null), context);
        processTemplate(serverRoot, packages.getAttribute(SERVER_PACKAGE),
            packages.valueOfTag(SERVER_NAME) + "-jbosscmp-jdbc.xml",
            retrieveTemplate("server-jboss-jdbc-descriptor", null), context);
      }
    } catch (IOException e) {
      log.error("ejb write template generation failed", e);
    }
  }

  /**
   * Returns true if an EJB or CORBA server should be generated for the package.
   * @param packages package to check.
   * @return true if a server should be generated otherwise false.
   */
  private boolean shouldCreateServer(MetaPackage packages) {
    for (MetaClass clazz : packages.getClasses()) {
      if ((clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE))
          && clazz.booleanValueOfTag(BoConstants.IS_ROOT)) {
        return true;
      }
    }
    return false;
  }
}