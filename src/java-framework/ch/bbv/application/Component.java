/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application;


/** The component class implements the interface to a package as seen from 
 * the main application. The class defines a set of methods implementing
 * a state machine of the subsystem state and methods to fire transitions. This
 * behavior allows the application to treat all packages in an uniform way.
 * @author Marcel Baumann
 * @version $Revision$
*/
public interface Component {
  /** The stopped state of the component. See the application startup pattern
   * for a complete description.
   */
  int STOPPED = 0;

  /** The initializing state of the component. See the application startup
   * pattern for a complete description.
   */
  int INITIALIZING = 1;

  /** The component is initialized. See the application startup pattern for
   * a complete description.
   */
  int INITIALIZED = 2;

  /** The starting up state of the component. See the application startup
   * pattern for a complete description.
   */
  int STARTING_UP = 3;

  /** The running state of the component. See the application startup pattern
   * for a complete description.
   */
  int RUNNING = 4;

  /** The shutting down immediate state of the component. See the application
   * startup pattern for a complete description.
   */
  int SHUTTING_DOWN_NORMALLY = 5;

  /** The shutting down normal state of the component. See the startup
   * pattern for a complete description.
   */
  int SHUTTING_DOWN_IMMEDIATELY = 6;

  /** Installs the component onto the system. This method is called the
   * very first time the application is started.
   * @throws ComponentException is thrown if the component encounters an
   * non recoverable error during this phase.
   * @pre isStopped()
   * @post isStopped()
   */
  void install() throws ComponentException;

  /** Initializes the component. After initializing the component and its
   * subsystems are ready for starting up phase.
   * @throws ComponentException is thrown if the component encounters an
   * non recoverable error during this phase.
   * @pre isStopped()
   * @post isInitialized()
   */
  void initialize() throws ComponentException;

  /** Startups the component. After starting up the component and its
   * subsystems are ready and fully functional. If necessary the component
   * is first initialized.
   * @throws ComponentException is thrown if the component encounters an
   * non recoverable error during this phase.
   * @pre isInitialized() || isStopped()
   * @post isRunning()
   */
  void startup() throws ComponentException;

  /** Shutdowns normally the component. After the operation all resources are
   * released and the component can be deleted.
   * @throws ComponentException is thrown if the component encounters an
   * non recoverable error during this phase.
   * @pre isRunning() || isStopped()
   * @post isStopped()
   */
  void shutdownNormal() throws ComponentException;

  /** Shutdowns immediately the component. The operation should be fast. It is
   * allowed not to clean up all resources.
   * @throws ComponentException is thrown if the component encounters an
   * non recoverable error during this phase.
   * @pre isRunning() || isStopped()
   * @post isStopped()
   */
  void shutdownImmediate() throws ComponentException;

  /** Returns the debug mode of the component.
   * @return Debug mode of the component.
   */
  boolean getDebugMode();

  /** Returns the simulation mode of the component.
   * @return Simulation mode of the component.
   */
  boolean getSimulationMode();

  /** Returns true if the component is stopped.
   * @return True if stopped otherwise false.
   */
  boolean isStopped();

  /** Returns true if the component is initialized.
   * @return True if initialized otherwise false.
   */
  boolean isInitialized();

  /** Returns true if the component is running.
   * @return True if running otherwise false.
   */
  boolean isRunning();

  /** Returns the name of the component.
   * @return Name of the component.
   */
  String getName();
}
