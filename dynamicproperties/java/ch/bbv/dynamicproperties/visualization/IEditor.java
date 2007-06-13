/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.visualization;

import javax.swing.JComponent;

/**
 * @author UeliKurmann
 */
public interface IEditor {

    public JComponent getComponent();

    /**
     * Returns the value
     * 
     * @return
     */
    public Object getValue();

    /**
     * Sets the value
     * 
     * @param value
     */
    public void setValue(Object value);

    public void putProperty(String name, Object value);

}
