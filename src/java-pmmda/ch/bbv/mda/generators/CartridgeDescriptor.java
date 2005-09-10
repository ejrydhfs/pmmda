/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators;

import ch.bbv.application.VersionId;

/**
 * The cartridge descriptor defines the capabilities of a loaded cartridge.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CartridgeDescriptor {
  /**
   * Qualified name of the class implementing the described cartridge.
   */
  private String classname;

  /**
   * Class implementing the described cartridge.
   */
  private transient Class cartridgeClass;

  /**
   * Flag indicating if the cartridge should be loaded or not.
   */
  private transient boolean loaded;

  /**
   * Flag indicating if the cartridge is active or not.
   */
  private transient boolean active;

  /**
   * Instance of the cartridge being described.
   */
  private transient Cartridge cartridge;

  /**
   * Returns the classname of the cartridge.
   * @return qualified classname of the cartridge class.
   * @see #setClassname(String)
   */
  public String getClassname() {
    return classname;
  }

  /**
   * Sets the classname of the cartridge.
   * @param classname qualified classname of the cartridge class.
   * @see #getClassname()
   */
  public void setClassname(String classname) {
    this.classname = classname;
  }

  /**
   * Returns the class implementing the described cartridge.
   * @return the class implementing the cartridge or null if not set.
   * @see #setCartridgeClass(Class)
   */
  public Class getCartridgeClass() {
    return cartridgeClass;
  }

  /**
   * Sets the class implementing the described cartridge.
   * @param cartridgeClass class implementing the described cartridge.
   * @see #getCartridgeClass()
   */
  public void setCartridgeClass(Class cartridgeClass) {
    this.cartridgeClass = cartridgeClass;
  }

  /**
   * Returns the class implementing the described cartridge.
   * @return the class implementing the cartridge or null if not set.
   * @see #setCartridge(Cartridge)
   */
  public Cartridge getCartridge() {
    return cartridge;
  }

  /**
   * Sets the class implementing the described cartridge.
   * @param cartridge class implementing the described cartridge.
   * @see #getCartridge()
   */
  public void setCartridge(Cartridge cartridge) {
    this.cartridge = cartridge;
  }

  /**
   * Returns the version of the described cartridge.
   * @return the version if defined otherwise null.
   */
  public VersionId getVersion() {
    return (cartridge != null) ? cartridge.getVersion() : null;
  }

  /**
   * Returns the name of the described cartridge.
   * @return the name of the cartridge if defined otherwise null.
   */
  public String getName() {
    return (cartridge != null) ? cartridge.getName() : null;
  }

  /**
   * Returns the prefix of the cartridge as displayed in the user interface.
   * @return prefix of the cartridge if defined otherwise null.
   */
  public String getPrefix() {
    return (cartridge != null) ? cartridge.getPrefix() : null;
  }

  /**
   * Returns the is loaded flag.
   * @return flag indicating if the cartridge is loaded or not.
   * @see #setLoaded(Boolean)
   */
  public Boolean getLoaded() {
    return loaded;
  }

  /**
   * Sets the loaded flag to its new value.
   * @param loaded new value of the flag.
   * @see #getLoaded()
   */
  public void setLoaded(Boolean loaded) {
    this.loaded = loaded;
  }

  /**
   * Returns the is active flag.
   * @return flag indicating if the cartridge is active or not.
   * @see #setActive(Boolean)
   */
  public Boolean getActive() {
    return active;
  }

  /**
   * Sets the active flag to its new value.
   * @param active new value of the flag.
   * @see #getActive()
   */
  public void setActive(Boolean active) {
    this.active = active;
  }
}