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

package ch.bbv.dynamicproperties.core;

import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * This interface provides helper methods to work with properties
 * 
 * @author Ueli Kurmannn
 * 
 */

public interface DynamicProperty extends PropertyChangeListener {

    /**
     * Returns the Property value with the given name
     * 
     * @param name
     *            the name of the property
     */
    public PropertyValue getProperty(String name);

    /**
     * Returns the list of dynamic properties
     * 
     * @return the list of dynamic properties
     */
    public List<PropertyValue> getProperties();

}
