/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dnm.users.client;

import java.util.Arrays;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;

import ch.bbv.application.ComponentException;
import ch.bbv.application.Locator;
import ch.bbv.cmdlineparser.CmdLineBuilder;
import ch.bbv.dog.CacheHandler;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.persistence.ojb.PersistenceHandler;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The application implements the users specific locator class for the client.
 * Convenience methods are provided to access the components. The application is
 * configured to support 2 tiers, CORBA client server and J2EE client server
 * modes through start-up parameters.The command line provides the following
 * options.
 * <dl>
 * <dt>mode (corba direct j2ee)</dt>
 * <dd>defines the mode of the client.</dd>
 * <dt>ORBInitialPort</dt>
 * <dd>port of the CORBA broker as defined in the Java CORBA package.</dd>
 * <dt>ORBInitialHost</dt>
 * <dd>name of the host where the CORBA broker is running as defined in the
 * Java CORBA package.</dd>
 * </dl>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Application extends Locator {
  /**
   * The name of the ORB subsystem as registered.
   */
  public static final String ORB_NAME = "CORBA_ORB";

  /**
   * The schema name of the database containing the persistent data.
   */
  public static final String SCHEMA = "orm-dnm";

  /**
   * The mode for the 2-tier mode of the application.
   */
  public static final String USE_DIRECT = "direct";

  /**
   * The mode for the CORBA mode of the application.
   */
  public static final String USE_CORBA = "corba";

  /**
   * The mode for the J2EE mode of the application.
   */
  public static final String USE_J2EE = "j2ee";

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(Application.class);

  /**
   * Command line arguments of the application.
   */
  private static String[] args;

  /**
   * Constructor of the class. The data object handler instiates a cache handler
   * with cache enabled and without copy cache.
   * @param mode mode of the application. The legal values are defined as
   *          constants (USE_DIRECT, USE_CORBA, USE_J2EE).
   * @throws ComponentException occured when building the application.
   */
  public Application(String mode) throws ComponentException {
    this(mode, false);
  }

  /**
   * Constructor of the class. The data object handler instiates a cache handler
   * with cache enabled and without copy cache.
   * @param mode mode of the application. The legal values are defined as
   *          constants (USE_DIRECT, USE_CORBA, USE_J2EE).
   * @param commandLineMode flag indicating if the application is used in
   *          command line mode or in user interface mode.
   * @throws ComponentException occured when building the application.
   */
  public Application(String mode, boolean commandLineMode)
      throws ComponentException {
    DataObjectManager manager = null;
    register(this);
    if (mode.equalsIgnoreCase(USE_DIRECT)) {
      log.info("Application started in direct mode");
      manager = new DataObjectManager(new CacheHandler(new PersistenceHandler(
          false), true, true), new LightweightObjectHandlerImpl(), null);
    } else if (mode.equalsIgnoreCase(USE_CORBA)) {
      log.info("Application started in CORBA mode");
      register(ORB.init(args, null), ORB_NAME);
      manager = new DataObjectManager(new CacheHandler(
          createHandler("ch.bbv.dnm.users.client.UsersServerCorbaWrapper"),
          true, false), new LightweightObjectHandlerImpl(), null);
    } else if (mode.equalsIgnoreCase(USE_J2EE)) {
      log.info("Application started in J2EE mode");
      manager = new DataObjectManager(new CacheHandler(
          createHandler("ch.bbv.dnm.users.client.UsersServerWrapper"), true,
          false), new LightweightObjectHandlerImpl(), null);
    }
    register(manager);
    UserExplorer.registerNodeTypes();
    initialize();
    startup();
    manager.getDataObjectHandler().openDatasource(SCHEMA, false);
    if (!commandLineMode) {
      UserExplorer.create("User Explorer", SCHEMA);
    }
  }

  /**
   * Convenience method returning the locator instace as an application.
   * @return the singleton locator as application instance.
   */
  public static Application getApplication() {
    return (Application) Locator.getInstance();
  }

  /**
   * Entry point to create a client instance.
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    Application.args = args;
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    options.addOption(CmdLineBuilder.createNoArgOption("h", "help",
        "display the list of available commands", false));
    group.addOption(CmdLineBuilder.createNoArgOption("v", "verbose",
        "set the logging level to verbose", false));
    group.addOption(CmdLineBuilder.createNoArgOption("q", "quiet",
        "set the logging level to quiet", false));
    options.addOptionGroup(group);
    options.addOption(CmdLineBuilder.createArgOption("m", "mode",
        "defines the mode of the application [direct | CORBA | J2EE]", "mode",
        false, String.class));
    options.addOption(CmdLineBuilder.createArgOption("ORBInitialPort", null,
        "the ORB TCP/IP port", "port", false, String.class));
    options.addOption(CmdLineBuilder.createArgOption("ORBInitialHost", null,
        "the host where the ORB is running", "host", false, String.class));
    options.addOption(CmdLineBuilder.createJavaProperty());
    Parser parser = new PosixParser();
    try {
      CommandLine line = parser.parse(options, args, true);
      if (line.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pmMDA", options);
        return;
      }
      if (line.hasOption("v")) {
        log.info("logging level set to verbose");
      }
      if (line.hasOption("q")) {
        log.info("logging level set to quiet");
      }
      String mode = line.getOptionValue("m", USE_DIRECT);
      // Handles the case where mode is not defined at all.
      mode = (mode == null ? USE_DIRECT : mode);
      if (log.isInfoEnabled()) {
        log.info(Arrays.asList(args));
      }
      new Application(mode);
    } catch (ComponentException e) {
      log.fatal(e);
    } catch (ParseException e) {
      log.fatal(e);
    }
  }

  /**
   * Returns the data object manager of the application. The manager provides
   * access the data object handler, lock manager and lightweight object
   * handler.
   * @return the data object handler of the application.
   */
  public DataObjectManager getDataObjectManager() {
    Locator locator = Locator.getInstance();
    return (DataObjectManager) locator.getComponent(DataObjectManager.NAME);
  }

  /**
   * Returns the CORBA Object Request Broker if run in CORBA mode otherwise
   * null.
   * @return the ORB of the application if defined otherwise null.
   */
  public final ORB getOrb() {
    return (ORB) getSubsystem(ORB_NAME);
  }

  /**
   * Creates the data object handler of the application. Reflection is used to
   * insure the correct class is instantiated through factory creation pattern.
   * The class is created through the MDA approach and is not know at
   * compilation time of this class.
   * @param qualifiedName qualified name of the data object handler class to
   *          instantiated. The class must provide a static create method.
   * @return the requested instance if sucessful otherwise null
   * @pre qualifiedName != null
   */
  protected final DataObjectHandler createHandler(String qualifiedName) {
    assert qualifiedName != null;
    return (DataObjectHandler) ReflectUtilities.createInstance(qualifiedName);
  }
}