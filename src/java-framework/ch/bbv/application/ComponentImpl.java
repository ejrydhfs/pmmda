/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application;


/** The default component provides a default implementation of the component
 * interface. If you need to implement your own component interface take a look
 * to this class before starting.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ComponentImpl implements Component {
  /** Name of the component. The name should be unique in the set of all
   * registered components of an application. Otherwise the post-office
   * pattern will not work reliable.
   */
  private String name;

  /** State of the component. The state machine is described in the application
   * startup and shutdown pattern.
   */
  private int state;

  /** Constructor of the class. All members are initialized to their default 
   * value.
   * @param name name of the component.
   * @pre name != null && name.length() > 0
   * @post name.equals(getName())
   */
  public ComponentImpl(String name) {
    this.name = name;
    state = STOPPED;
  }

  public void install() throws ComponentException {
    assert isStopped();
    assert isStopped();
  }

  public void initialize() throws ComponentException {
    assert isStopped();
    state = INITIALIZED;
    assert isInitialized();
  }

  public void startup() throws ComponentException {
    assert isInitialized() || isStopped();
    if (state == STOPPED) {
      initialize();
    }
    state = RUNNING;
    assert isRunning();
  }

  public void shutdownNormal() throws ComponentException {
    assert isRunning() || isStopped();
    state = STOPPED;
    assert isStopped();
  }

  public void shutdownImmediate() throws ComponentException {
    assert isRunning() || isStopped();
    state = STOPPED;
    assert isStopped();
  }

  public boolean getDebugMode() {
    return Locator.getInstance().getDebugMode();
  }

  public boolean getSimulationMode() {
    return Locator.getInstance().getSimulationMode();
  }

  public boolean isStopped() {
    return state == STOPPED;
  }

  public boolean isInitialized() {
    return state == INITIALIZED;
  }

  public boolean isRunning() {
    return state == RUNNING;
  }

  public String getName() {
    return name;
  }
}
