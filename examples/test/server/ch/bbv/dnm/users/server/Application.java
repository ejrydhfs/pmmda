/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dnm.users.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;

import ch.bbv.application.ComponentException;
import ch.bbv.application.Locator;
import ch.bbv.cmdlineparser.CmdLineBuilder;
import ch.bbv.dnm.users.TypeFactory;
import ch.bbv.dog.DataObjectManager;
import ch.bbv.dog.LightweightObjectHandlerImpl;
import ch.bbv.dog.persistence.ojb.PersistenceHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The class defines the server application connecting to the database and to
 * the servers. The application is configured to support 2 tiers, CORBA client
 * server and J2EE client server modes through start-up parameters.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Application extends Locator {
  /**
   * Mode when the client directly connects to the database.
   */
  public static final String USE_DIRECT = "direct";

  /**
   * Mode when the client connects to the servers using CORBA middleware.
   */
  public static final String USE_CORBA = "corba";

  /**
   * Mode when the client connects to the servers using J2EE middleware.
   */
  public static final String USE_J2EE = "j2ee";

  /**
   * Mode for the configuration of the object-relation mapping subsystem.
   */
  public static final String USE_REPLACE = "REPLACE";

  /**
   * Mode for the configuration of the object-relation mapping subsystem.
   */
  public static final String USE_SYNCHRONIZE = "SYNCHRONIZE";

  /**
   * Mode for the configuration of the object-relation mapping subsystem.
   */
  public static final String USE_DETACHED = "DETACHED";

  /**
   * Name of the ORB component in the application.
   */
  public static final String ORB_NAME = "CORBA_ORB";

  /**
   * Database alias to connect to the O/R mapping layer tool.
   */
  public static final String USERS_DATABASE = "orm-dnm";

  /**
   * Logger of the application class.
   */
  private static Log log = LogFactory.getLog(Application.class);

  private static String[] args;

  /**
   * Returns the client application singleton instance. The application uses the
   * locator pattern.
   * @return the client application unique instance.
   */
  public static Application getApplication() {
    return (Application) Locator.getInstance();
  }

  /**
   * Entry point the client application. The arguments of the application
   * specify the mode.
   * @param args arguments of the client application.
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
    options.addOption(CmdLineBuilder.createArgOption("s", "synch",
        "defines the O/R synchronization [replace | synchronize | detach]",
        "synch", false, String.class));
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
      String synchronization = line.getOptionValue("s", USE_REPLACE)
          .toUpperCase();
      synchronization = (synchronization == null ? USE_REPLACE
          : synchronization);
      if (log.isInfoEnabled()) {
        log.info(Arrays.asList(args));
      }
      // starts the application in the requested mode
      new Application(mode, "REPLACE");
    } catch (ComponentException e) {
      log.fatal(e);
    } catch (ParseException e) {
      log.fatal(e);
    }
  }
  
  /**
   * Default constructor of the class.
   * @param mode mode of the client application.
   * @param synchronization synchronization mode of the O/R system.
   * @throws ComponentException occured when creating the client application.
   * @pre (mode != null) && (synchronization != null)
   */
  public Application(String mode, String synchronization)
      throws ComponentException {
    assert (mode != null) && (synchronization != null);
    DataObjectManager manager = null;
    ORB orb = null;
    register(this);
    PersistenceHandler.Mode sync = PersistenceHandler.Mode
        .valueOf(synchronization);
    if (mode.equalsIgnoreCase(USE_DIRECT)) {
      log.info("Application started in direct mode");
      manager = new DataObjectManager(new PersistenceHandler(false, sync),
          new LightweightObjectHandlerImpl(), null);
      register(manager);
    } else if (mode.equalsIgnoreCase(USE_CORBA)) {
      log.info("Application started in CORBA mode");
      manager = new DataObjectManager(new PersistenceHandler(false, sync),
          new LightweightObjectHandlerImpl(), null);
      register(manager);
      orb = ORB.init(args, null);
      register(orb, ORB_NAME);
      createCorbaServer("ch.bbv.dnm.users.server.UsersServerCorba");
    } else if (mode.equalsIgnoreCase(USE_J2EE)) {
      log.info("Application started in J2EE mode");
      manager = new DataObjectManager(new PersistenceHandler(true, sync),
          new LightweightObjectHandlerImpl(), null);
      register(manager);
    } 
    TypeFactory factory = new TypeFactory(manager);
    factory.registerAllTypes();
    initialize();
    startup();
    manager.getDataObjectHandler().openDatasource(USERS_DATABASE, false);
    // starts the ORB main loop
    if (mode.equalsIgnoreCase(USE_CORBA)) {
      orb.run();
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
   * Returns the CORBA object request broker of the application.
   * @return the ORB of the application.
   */
  public ORB getOrb() {
    return (ORB) getSubsystem(ORB_NAME);
  }

  /**
   * Creates the CORBA server using reflection to avoid compile dependencies to
   * the CORBA libraries.
   * @param qualifiedName qualified name of the corba wrapper server.
   */
  protected final void createCorbaServer(String qualifiedName) {
    try {
      Class clazz = Class.forName(qualifiedName);
      Method method = clazz.getMethod("create");
      method.invoke(null);
    } catch (ClassNotFoundException e) {
      log.fatal(e);
    } catch (SecurityException e) {
      log.fatal(e);
    } catch (NoSuchMethodException e) {
      log.fatal(e);
    } catch (IllegalArgumentException e) {
      log.fatal(e);
    } catch (IllegalAccessException e) {
      log.fatal(e);
    } catch (InvocationTargetException e) {
      log.fatal(e);
    }
  }
}