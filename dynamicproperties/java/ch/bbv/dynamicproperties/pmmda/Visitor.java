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
import ch.bbv.dynamicproperties.PropertiesOfAClass;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.Multilingualism;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.core.PropertyValue;

/* %% USER DEFINED DECLARATIONS START %% */
/* %% USER DEFINED DECLARATIONS END %% */

/**
 * The visitor class implements the visitor pattern for the graph of all data
 * object classes for all root classes defined in the package. The list of all
 * relevant classes is generated from the MDA model.
 */
public interface Visitor {

    /* %% USER DEFINED DEFINITIONS START %% */
    /* %% USER DEFINED DEFINITIONS END %% */

    /**
     * Returns the current depth in the graph when traversing it with the
     * visitor. The depth is a positive integer with a minimal value of 1.
     * 
     * @return the current depth of the visitor in the graph
     * @see #setDepth(int)
     */
    int getDepth();

    /**
     * Sets the current depth in the graph when traversing it with the visitor.
     * The depth is a positive integer with a minimal value of 1.
     * 
     * @param depth
     *            current depth of the visitor in the traversed graph
     * @see #getDepth()
     */
    void setDepth(int depth);

    /**
     * Resets the visitor so that it can be used to traverse a graph.
     */
    void reset();

    /**
     * Returns true if the object should be visited otherwise false. This method
     * is used to detect cycles.
     * 
     * @param object
     *            data object to visit
     * @pre object != null
     */
    boolean shouldVisit(DataObject object);

    /**
     * Visits the class Property as part of the visitor pattern.
     * 
     * @param item
     *            item to visit
     */
    void visit(Property item);

    /**
     * Visits the class PropertyType as part of the visitor pattern.
     * 
     * @param item
     *            item to visit
     */
    void visit(PropertyType item);

    /**
     * Visits the class PropertyValue as part of the visitor pattern.
     * 
     * @param item
     *            item to visit
     */
    void visit(PropertyValue item);

    /**
     * Visits the class PropertyInstanceManager as part of the visitor pattern.
     * 
     * @param item
     *            item to visit
     */
    void visit(PropertyInstanceManager item);

    /**
     * Visits the class PropertiesOfAClass as part of the visitor pattern.
     * 
     * @param item
     *            item to visit
     */
    void visit(PropertiesOfAClass item);

    void visit(Multilingualism item);

}
