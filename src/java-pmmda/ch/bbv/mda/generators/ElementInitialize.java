/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.generators;

import ch.bbv.mda.*;

/**
 * The initializor provides an interface to initialize elements.
 * @author marcelbaumann
 * @version $Revision$
 */
public interface ElementInitialize {
  /**
   * Called when a item shouuld be initialized.
   * @param item item to initialize.
   */
  void initialize(MetaModel item);

  /**
   * Called when a item shouuld be initialized.
   * @param item item to initialize.
   */
  void initialize(MetaPackage item);

  /**
   * Called when a item shouuld be initialized.
   * @param item item to initialize.
   */
  void initialize(MetaClass item);

  /**
   * Called when a item shouuld be initialized.
   * @param item item to initialize.
   */
  void initialize(MetaProperty item);

  /**
   * Called when a item shouuld be initialized.
   * @param item item to initialize.
   */
  void initialize(MetaIndexedProperty item);
}
