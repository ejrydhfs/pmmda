/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.Application;
import ch.bbv.application.ComponentException;
import ch.bbv.application.Locator;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.EmptyHandler;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.validation.Validator;
import ch.bbv.explorer.Explorer;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.validation.RulesFactory;
import ch.bbv.utilities.PropertyUtilities;

/**
 * The MDA application defines the <em>pmMDA</em> application. The application
 * uses the component framework. The class is the entry point of the
 * <em>pmMDA</em> application.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdaApplication extends Application {
  /**
   * Name of the default properties file of the application.
   */
  public static final String PROPERTIES_NAME = "/pmmda.properties";

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(MdaApplication.class);

  /**
   * Command line arguments of the application.
   */
  private static String[] args;

  /**
   * Properties of the application.
   */
  private static Properties properties;

  /**
   * The explorer visualizing pmMDA
   */
  private static MdaExplorer explorer;

  /**
   * Entry point of the application.
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      properties = readProperties(PROPERTIES_NAME);
      new MdaApplication(args);
    } catch (ComponentException e) {
      log.fatal(e);
    }
  }

  /**
   * Convenience method returning the locator instace as an application.
   * @return the singleton locator as application instance
   */
  public static MdaApplication getApplication() {
    return (MdaApplication) Locator.getInstance();
  }

  /**
   * Constructor of the class.
   * @param args command line arguments
   * @throws ComponentException occured when creating and starting the
   *         application
   */
  public MdaApplication(String[] args) throws ComponentException {
    MdaApplication.args = args;
    register(this);
    setDataObjectMgr(new DataObjectMgr(new EmptyHandler(),
        new LightweightObjectHandlerImpl(), null, null));
    register(new Validator(Validator.NAME));
    register(new ResourcesManager(ResourcesManager.NAME));
    register(new CartridgeFactory(CartridgeFactory.NAME, getProperties()));
    initialize();
    startup();
  }

  @Override
  public void initialize() throws ComponentException {
    super.initialize();
    MdaExplorer.registerNodeTypes();
    (new RulesFactory()).registerAllRules();
  }

  @Override
  public void startup() throws ComponentException {
    super.startup();
    ResourcesManager resourcesManager = getResourcesManager();
    for (Cartridge cartridge : CartridgeFactory.getInstance().getCartridges()) {
      resourcesManager.registerResources(cartridge);
    }
    explorer = MdaExplorer.create(args);
  }

  /**
   * Returns the resources manager of the application.
   * @return the resources manager of the application
   */
  public ResourcesManager getResourcesManager() {
    Locator locator = Locator.getInstance();
    return (ResourcesManager) locator.getComponent(ResourcesManager.NAME);
  }

  /**
   * Returns the validator of the application.
   * @return the validator of the application
   */
  public Validator getValidator() {
    Locator locator = Locator.getInstance();
    return (Validator) locator.getComponent(Validator.NAME);
  }

  /**
   * Returns the cartridge factory of the application.
   * @return the cartridge factory of the application
   */
  public CartridgeFactory getCartridgeFactory() {
    Locator locator = Locator.getInstance();
    return (CartridgeFactory) locator.getComponent(CartridgeFactory.NAME);
  }

  /**
   * Returns the properties of the application.
   * @return properties of the application
   */
  public Properties getProperties() {
    return properties;
  }

  /**
   * Returns the explorer associated with pmMDA.
   * @return the explorer of the application
   */
  public Explorer getExplorer() {
    return explorer;
  }

  /**
   * Reads the properties of the application.
   * @param resource name of the resource containing the properties
   * @return the read properties if sucessful otherwise null
   */
  public static Properties readProperties(String resource) {
    PropertiesConfiguration properties = null;
    try {
      if (resource != null) {
        InputStream stream = MdaApplication.class.getResourceAsStream(resource);
        if (stream != null) {
          properties = new PropertiesConfiguration();
          properties.load(stream);
          stream.close();
        } else {
          if (log.isErrorEnabled()) {
            log.error("Properties file was not found in classpath " + resource);
          }
        }
      }
    } catch (ConfigurationException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
    return PropertyUtilities.convert(properties);
  }
}