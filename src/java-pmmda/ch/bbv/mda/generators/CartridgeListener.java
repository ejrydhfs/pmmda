/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators;

import java.io.File;

import ch.bbv.mda.MetaElement;

/**
 * The cartridge listener defines an interface for interested parties requesting
 * progress information when cartridges are executed against a model.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface CartridgeListener {
  
  /**
   * Signals the start of the processing for the given element.
   * @param cartridge cartridge processing the element.
   * @param element the element being processed.
   */
  void processingStarted(Cartridge cartridge, MetaElement element);

  /**
   * Signals the start of the processing for the given element.
   * @param cartridge cartridge processing the element.
   * @param element the element being processed.
   */
  void processingCompleted(Cartridge cartridge, MetaElement element);

  /**
   * Signals the creation of a file.
   * @param cartridge cartridge creating the file.
   * @param file file being created.
   */
  void fileCreated(Cartridge cartridge, File file);
}
