/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $id$
 */
package ch.bbv.application;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The application class implements the start-up mechanism of a software
 * application. Sophisticated mechanisms for various start-up, shutdown and
 * sleep mode are provided. The mechanism request that all active subsystems
 * provide a class extending the interface component. Components registered
 * later are automatically start-up to reach the same state as the application.
 * The mechanism allows configuration update without stopping the application if
 * required. This mechanism is very useful to release all system resources when
 * performing a normal or an immediate shutdown. The application class
 * implements the singleton pattern. The application assumes that all its
 * packages are stored in sub-directories of the main application. Therefore the
 * whole application is stored in one single tree including all its help tools
 * and configuration files. Components can be loaded at runtime. So it is
 * possible to configure the application by specifying the used components for
 * example in a property file. The components are responsible to know and to
 * tell the application all subcomponents which they depend on. With this
 * information the application loads a component and all its dependencies. The
 * component itself can be obtained from the application with the method
 * getComponentWithName. So far default names are given for the components so
 * that there will be only one component of the same type loaded at the same
 * time. The concept of more than one component of the same type with different
 * names is prepared but not used in the current implementation.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class Locator {
  /**
   * The stopped state of the application. See the application startup pattern
   * for a complete description.
   */
  public static final int STOPPED = Component.STOPPED;

  /**
   * The initializing state of the application. See the application startup
   * pattern for a complete description.
   */
  public static final int INITIALIZING = Component.INITIALIZING;

  /**
   * The initialized state of the application. See the application startup
   * pattern for a complete description.
   */
  public static final int INITIALIZED = Component.INITIALIZED;

  /**
   * The starting up state of the application. See the application startup
   * pattern for a complete description.
   */
  public static final int STARTING_UP = Component.STARTING_UP;

  /**
   * The running state of the application. See the application startup pattern
   * for a complete description.
   */
  public static final int RUNNING = Component.RUNNING;

  /**
   * The shutting down immediate state of the application. See the application
   * startup pattern for a complete description.
   */
  public static final int SHUTTING_DOWN_IMMEDIATELY = Component.SHUTTING_DOWN_IMMEDIATELY;

  /**
   * The shutting down normal state of the application. See the startup pattern
   * for a complete description.
   */
  public static final int SHUTTING_DOWN_NORMALLY = Component.SHUTTING_DOWN_NORMALLY;

  /**
   * The application singleton object.
   */
  private static Locator theLocator;

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(Locator.class);

  /**
   * List of all registered components in the application. A component
   * implements the component interface. The key is a string, the value is a
   * component.
   */
  private Map<String, Component> components;

  /**
   * List of all registered subsystems in the application. No constraints exist
   * on a subsystem. The key is a string, the value is an object.
   */
  private Map<String, Object> subsystems;

  /**
   * State of the application.
   */
  private int state;

  /**
   * The debug mode status of the application. This mode is used to simplify
   * debugging policies in the application during development.
   */
  private boolean isDebugOn;

  /**
   * The simulation mode status of the application. This mode is used to
   * simplify development policies when no all subsystems are available during
   * development or testing.
   */
  private boolean isSimulationOn;

  /**
   * Default constructor of the class.
   */
  public Locator() {
    components = new HashMap<String, Component>();
    subsystems = new HashMap<String, Object>();
    isDebugOn = false;
    isSimulationOn = false;
  }

  /**
   * Returns the default application object. The application class implements
   * the singleton pattern. The class is part of the factory startup pattern.
   * @return Returns the singleton application object.
   */
  public static final Locator getInstance() {
    return theLocator;
  }

  /**
   * Factory create pattern for the locator class. An instance of the given
   * subclass is created and registered as locator.
   * @param classname qualified name of the subclass of locator to instantiate.
   * @param params parameters list of the create method. The type of the
   *        parameters must correspond to the ones of the constructor signature.
   */
  public static final void create(String classname, Object[] params) {
    if (getInstance() == null) {
      synchronized (Locator.class) {
        if (getInstance() == null) {
          try {
            log.info("create locator with class " + classname);
            Class clazz = Class.forName(classname);
            Locator locator = null;
            if (params == null) {
              locator = (Locator) clazz.newInstance();
            } else {
              Class[] types = new Class[params.length];
              for (int i = 0; i < types.length; i++) {
                types[i] = params[i].getClass();
              }
              Constructor constructor = clazz.getConstructor(types);
              locator = (Locator) constructor.newInstance(params);
            }
            register(locator);
          } catch (ClassNotFoundException e) {
            log.fatal("Could not retrieve locator class " + classname, e);
          } catch (InstantiationException e) {
            log.fatal("Could not instatantiate locator class " + classname, e);
          } catch (IllegalAccessException e) {
            log.fatal("No access to locator class " + classname, e);
          } catch (SecurityException e) {
            log.fatal("No security access to locator class " + classname, e);
          } catch (NoSuchMethodException e) {
            log.fatal("No constructor with request signature in locator class "
                + classname, e);
          } catch (IllegalArgumentException e) {
            log.fatal("Wrong parameter list in locator class " + classname, e);
          } catch (InvocationTargetException e) {
            log.fatal("Target invocation exception in locator class "
                + classname, e);
          }
        }
      }
    }
  }

  /**
   * Register the unique instance of the application class. The application
   * class the singleton pattern.
   * @param locator locator to register. Subclass instances of locator can be
   *        registered.
   * @pre theLocator == null
   */
  public static final void register(Locator locator) {
    assert theLocator == null;
    synchronized (Locator.class) {
      if (theLocator == null) {
        theLocator = locator;
      }
    }
  }

  /**
   * Returns true if the component is stopped.
   * @return True if stopped otherwise false.
   */
  public boolean isStopped() {
    return state == STOPPED;
  }

  /**
   * Returns true if the component is initialized.
   * @return True if initialized otherwise false.
   */
  public boolean isInitialized() {
    return state == INITIALIZED;
  }

  /**
   * Returns true if the component is running.
   * @return True if running otherwise false.
   */
  public boolean isRunning() {
    return state == RUNNING;
  }

  /**
   * Initializes the application global resources before the startup sequence is
   * started. The application should configure all global configuration items.
   * All basic services are available after this call.
   * @throws ComponentException an error occured in one of the components.
   * @pre isStopped()
   * @post isInitialized()
   */
  protected void initialize() throws ComponentException {
    assert isStopped();
    state = INITIALIZING;

    for (Iterator i = components.values().iterator(); i.hasNext();) {
      ((Component) i.next()).initialize();
    }

    state = INITIALIZED;
    assert isInitialized();
  }

  /**
   * Startups the whole application. All registered components are started.
   * @throws ComponentException an error occured in one of the components.
   * @pre isInitialized()
   * @post isRunning()
   */
  protected void startup() throws ComponentException {
    assert isInitialized();
    state = STARTING_UP;

    for (Iterator i = components.values().iterator(); i.hasNext();) {
      ((Component) i.next()).startup();
    }

    state = RUNNING;
    assert isRunning();
  }

  /**
   * Shutdowns immediately the whole application. All registered components are
   * started. An immediate shutdown is an emergency procedure.
   * @throws ComponentException an error occured in one of the components.
   * @pre isRunning()
   * @post isStopped()
   */
  protected void shutdownImmediate() throws ComponentException {
    assert isRunning();
    state = SHUTTING_DOWN_IMMEDIATELY;

    for (Iterator i = components.values().iterator(); i.hasNext();) {
      ((Component) i.next()).shutdownImmediate();
    }

    state = STOPPED;
    assert isStopped();
  }

  /**
   * Shutdowns the whole application. All registered components are started. A
   * normal shutdown is the standard procedure to stop the application.
   * @throws ComponentException an error occured in one of the components.
   * @pre isRunning()
   * @post isStopped()
   */
  protected void shutdownNormal() throws ComponentException {
    assert isRunning();
    state = SHUTTING_DOWN_NORMALLY;

    for (Iterator i = components.values().iterator(); i.hasNext();) {
      ((Component) i.next()).shutdownNormal();
    }

    state = STOPPED;
    assert isStopped();
  }

  /**
   * Registers a component on the application. If the application is already
   * running, the component is started accordingly. The component is not
   * registered if there is already a component with the same name.
   * @param component component to register
   * @throws ComponentException an error occured in one of the components.
   * @pre (component != null) && !isRegistered(component)
   * @post isRegistered(component)
   */
  public void register(Component component) throws ComponentException {
    assert (component != null) && !isRegistered(component);
    components.put(component.getName(), component);
    switch (state) {
    case INITIALIZING:
    case INITIALIZED:
      component.initialize();
      break;
    case STARTING_UP:
    case RUNNING:
      component.initialize();
      component.startup();
      break;
    }

    assert isRegistered(component);
  }

  /**
   * Unregisters the component from the application. The component is removed
   * from the application.
   * @param component component to remove from the application.
   * @pre isRegistered(component)
   * @post !isRegistered(component)
   */
  public void unregister(Component component) {
    assert (component != null) && isRegistered(component);
    components.remove(component.getName());
    assert !isRegistered(component);
  }

  /**
   * Registers the subsystem under the given name.
   * @param subsystem subsystem to register.
   * @param name name of the subsystem.
   * @pre subsystem != null && name != null
   */
  public void register(Object subsystem, String name) {
    assert (subsystem != null) && (name != null);
    subsystems.put(name, subsystem);
  }

  /**
   * Unregisters the subsystem.
   * @param name name of the subsystem to unregister.
   * @pre subsystem != null
   */
  public void unregister(String name) {
    assert name != null;
    subsystems.remove(name);
  }

  /**
   * Returns true if the component is already registered otherwise false.
   * @param component component which registration should be checked.
   * @return True if registered otherwise false.
   */
  public boolean isRegistered(Component component) {
    assert component != null;
    return components.containsValue(component);
  }

  /**
   * Returns the component with the given name.
   * @param name name of the component to be found.
   * @return Found component otherwise null.
   * @pre name != null
   */
  public Component getComponent(String name) {
    assert name != null;
    return components.get(name);
  }

  /**
   * Returns the subsystem with the given name.
   * @param name name of the component to be found.
   * @return Found component otherwise null.
   * @pre name != null
   */
  public Object getSubsystem(String name) {
    assert name != null;
    return subsystems.get(name);
  }

  /**
   * Enables or disables the debug mode for the whole application and all
   * registered components.
   * @param isDebugOn new debug mode for the application.
   * @see #getDebugMode
   * @pre isStopped()
   * @post getDebugMode() == isSimulationOn
   */
  protected void setDebugMode(boolean isDebugOn) {
    assert isStopped();
    this.isDebugOn = isDebugOn;
    assert getDebugMode() == isDebugOn;
  }

  /**
   * Returns the debug mode of the application.
   * @return The debug mode of the application.
   * @see #setDebugMode
   */
  protected boolean getDebugMode() {
    return isDebugOn;
  }

  /**
   * Enables or disables the simulation mode for the whole application and all
   * registered components.
   * @param isSimulationOn new simulation mode for the application.
   * @see #getSimulationMode
   * @pre isStopped()
   * @post getSimulationMode() == isSimulationOn
   */
  protected void setSimulationMode(boolean isSimulationOn) {
    assert isStopped();
    this.isSimulationOn = isSimulationOn;
    assert getSimulationMode() == isSimulationOn;
  }

  /**
   * Returns the simulation mode of the application.
   * @return The simulation mode of the application.
   * @see #setSimulationMode
   */
  protected boolean getSimulationMode() {
    return isSimulationOn;
  }
}