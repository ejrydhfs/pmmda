/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.generators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
   * Extension for IDL source file.
   */
  public static final String IDL_EXTENSION = ".idl";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_CORBA_KEY = "ejb-cartridge.generate-corba";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String GENERATE_BEAN_KEY = "ejb-cartridge.generate-bean";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String SERVER_PACKAGE_EXTENSION_KEY = "ejb-cartridge.server-package-extension";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String CLIENT_PACKAGE_EXTENSION_KEY = "ejb-cartridge.client-package-extension";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_SERVER_CORBA_KEY = "ejb-cartridge.server-corba-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_CLIENT_CORBA_KEY = "ejb-cartridge.client-corba-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_SERVER_J2EE_KEY = "ejb-cartridge.server-j2ee-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_CLIENT_J2EE_KEY = "ejb-cartridge.client-j2ee-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_COMMON_J2EE_KEY = "ejb-cartridge.common-j2ee-root-folder";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String LOCATOR_CLASS_KEY = "ejb-cartridge.server-locator-classname";

  /**
   * Tag name of the server providing the DOG services.
   */
  public static final String SERVER_NAME_TAG = "ejb-server-name";

  /**
   * Tag name for the server package name.
   */
  public static final String SERVER_PACKAGE = "ejb-server-package";

  /**
   * Tag name for the client package name.
   */
  public static final String CLIENT_PACKAGE = "ejb-client-package";

  /**
   * Log instance for the class.
   */
  private static Log log = LogFactory.getLog(EjbCartridge.class);

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template idlDogCorbaTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template dogCorbaTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template dogCorbaWrapperTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template ejbIfTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template ejbTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template ejbHomeTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template dogWrapperTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template ejbDogXmlTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template ejbJbossXmlTemplate;

  /**
   * Velocity template to generate the source code artifacts.
   */
  private Template ejbJbossJdbcXmlTemplate;

  /**
   * Default constructor of the class.
   */
  public EjbCartridge() {
    super("Entity Java Bean Cartridge", PREFIX, new VersionId(0, 3, 0));
    try {
      idlDogCorbaTemplate = Velocity.getTemplate("ejb-bogServerCorbaIdl.vm");
      dogCorbaTemplate = Velocity.getTemplate("ejb-bogServerCorba.vm");
      dogCorbaWrapperTemplate = Velocity
          .getTemplate("ejb-bogServerCorbaWrapper.vm");
      ejbIfTemplate = Velocity.getTemplate("ejb-bogServerSessionIf.vm");
      ejbTemplate = Velocity.getTemplate("ejb-bogServerSession.vm");
      ejbHomeTemplate = Velocity.getTemplate("ejb-bogServerHome.vm");
      dogWrapperTemplate = Velocity.getTemplate("ejb-bogServerWrapper.vm");
      ejbDogXmlTemplate = Velocity.getTemplate("ejb-bogServerDescriptor.vm");
      ejbJbossXmlTemplate = Velocity
          .getTemplate("ejb-bogServerJbossDescriptor.vm");
      ejbJbossJdbcXmlTemplate = Velocity
          .getTemplate("ejb-bogServerJbossJdbcDescriptor.vm");
    } catch (ParseErrorException e) {
      log.fatal(e);
    } catch (ResourceNotFoundException e) {
      log.fatal(e);
    } catch (Exception e) {
      log.fatal(e);
    }
  }

  public void initialize(Properties properties) {
    super.initialize(properties);

    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge("dog"));
    addCartridge(factory.getCartridge("cto"));
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
      types.add(new TagDefinition(SERVER_NAME_TAG, "", String.class));
    }
    return types;
  }

  public TaggedValue getTaggedValueWithElementDependentDefault(
      TagDefinition taggedValueType, MetaElement element) {
    TaggedValue value = new TaggedValue(taggedValueType);
    updateTaggedValueWithElementDependentDefault(value, element);
    return value;
  }

  public void updateTaggedValueWithElementDependentDefault(
      TaggedValue taggedValue, MetaElement element) {
    if (taggedValue.getType().getTag().equals(SERVER_NAME_TAG)) {
      String name = element.getName();
      if (name != null) {
        taggedValue.setValue(name + "Server");
      }
    }
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

  public void processPackage(MetaPackage metaPackage) {
    if (!metaPackage.booleanValueOfTag(DogCartridge.GENERATE_TAG)) {
      return;
    }

    VelocityContext context = createContext(metaPackage);
    try {
      if (getContext().getProperty(GENERATE_CORBA_KEY).equals("true")) {
        File serverRoot = getRoot(ROOT_SERVER_CORBA_KEY,
            DogCartridge.ROOT_SERVER_FOLDER_KEY);
        File clientRoot = getRoot(ROOT_CLIENT_CORBA_KEY,
            DogCartridge.ROOT_CLIENT_FOLDER_KEY);
        File IdlRoot = new File(getContext().getProperty(
            CtoCartridge.ROOT_IDL_KEY));
        processTemplate(IdlRoot, metaPackage
            .valueOfTag(CtoCartridge.CTO_PACKAGE_IDL_TAG), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + IDL_EXTENSION, idlDogCorbaTemplate, context);
        processJavaTemplate(serverRoot, metaPackage
            .getAttribute(SERVER_PACKAGE), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + "Corba", dogCorbaTemplate, context);
        processJavaTemplate(clientRoot, metaPackage
            .getAttribute(CLIENT_PACKAGE), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + "CorbaWrapper", dogCorbaWrapperTemplate, context);
      }
      if (getContext().getProperty(GENERATE_BEAN_KEY).equals("true")) {
        File serverRoot = getRoot(ROOT_SERVER_J2EE_KEY,
            DogCartridge.ROOT_SERVER_FOLDER_KEY);
        File clientRoot = getRoot(ROOT_CLIENT_J2EE_KEY,
            DogCartridge.ROOT_CLIENT_FOLDER_KEY);
        File commonRoot = getRoot(ROOT_COMMON_J2EE_KEY,
            DogCartridge.ROOT_COMMON_FOLDER_KEY);
        processJavaTemplate(commonRoot, metaPackage
            .getAttribute(SERVER_PACKAGE), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + "Session", ejbIfTemplate, context);
        processJavaTemplate(serverRoot, metaPackage
            .getAttribute(SERVER_PACKAGE), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + "SessionBean", ejbTemplate, context);
        processJavaTemplate(commonRoot, metaPackage
            .getAttribute(SERVER_PACKAGE), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + "Home", ejbHomeTemplate, context);
        processJavaTemplate(clientRoot, metaPackage
            .getAttribute(CLIENT_PACKAGE), metaPackage
            .valueOfTag(SERVER_NAME_TAG)
            + "Wrapper", dogWrapperTemplate, context);
        processTemplate(serverRoot, metaPackage.getAttribute(SERVER_PACKAGE),
            metaPackage.valueOfTag(SERVER_NAME_TAG) + "-ejb-jar.xml",
            ejbDogXmlTemplate, context);
        processTemplate(serverRoot, metaPackage.getAttribute(SERVER_PACKAGE),
            metaPackage.valueOfTag(SERVER_NAME_TAG) + "-jboss.xml",
            ejbJbossXmlTemplate, context);
        processTemplate(serverRoot, metaPackage.getAttribute(SERVER_PACKAGE),
            metaPackage.valueOfTag(SERVER_NAME_TAG) + "-jbosscmp-jdbc.xml",
            ejbJbossJdbcXmlTemplate, context);
      }
    } catch (IOException e) {
      log.error("ejb write template generation failed", e);
    }
  }
}