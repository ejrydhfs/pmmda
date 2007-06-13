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

package ch.bbv.dynamicproperties.pmmda;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.objecteditor.action.DynamicPropertyUpdater;

public class DynamicPropertyFunctor implements DoFunctor {
    public void execute(DataObject dataObject) {
        if (dataObject instanceof DynamicProperty) {
            DynamicPropertyUpdater.update((DynamicProperty) dataObject, PropertyInstanceManager.instance());
        }
    }
}