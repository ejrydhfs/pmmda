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

package ch.bbv.dynamicproperties;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.pmmda.Visitor;

public class PropertiesOfAClass extends DataObjectImpl {
    List<Property> properties;

    public PropertiesOfAClass() {
        properties = new ArrayList<Property>();
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public List<Property> getProperties() {
        return properties;
    }

    /**
     * 
     * @param visitor
     */
    public void accept(Visitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    private void traverse(Visitor visitor) {
        for (Property property : properties) {
            property.accept(visitor);
        }
    }
}
