/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui;

import javax.swing.JPanel;

/**
 * The view allows the relocation of contact to a selected organization. The
 * logic removes the contact from the current organization, adds the contact to
 * the new organization and adds a note to the history to provide an audit trail
 * to the operation. The view provides a left panel with a tree of all loaded
 * contacts and a right panel with a tree of the selected organization and its
 * contacts. Buttons are provided to select the target organizationa and to move
 * contact to it.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class RelocateContactsView extends JPanel {

  /**
   * Serial version identifier of the class.
   */
  private static final long serialVersionUID = -8153917542904210072L;

}
