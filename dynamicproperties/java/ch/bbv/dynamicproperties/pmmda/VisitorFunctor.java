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

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;
import ch.bbv.dynamicproperties.PropertiesOfAClass;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.Multilingualism;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.core.PropertyValue;

/**
 * The visitor with functor class implements the visitor pattern for the graph
 * of all data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public class VisitorFunctor implements Visitor, AbstractVisitorFunctor {
    /**
     * Functor of the visitor.
     */
    private DoFunctor functor;

    /**
     * The current depth of the visitor when traversing a graph.
     */
    private int depth;

    /**
     * Default constructor of the class.
     */
    public VisitorFunctor() {
    }

    /**
     * Constructor of the class, which initializes the functor.
     * 
     * @param functor
     *            functor of the visitor
     */
    public VisitorFunctor(DoFunctor functor) {
        this();
        this.functor = functor;
    }

    public void reset() {
        depth = 0;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean shouldVisit(DataObject object) {
        assert object != null;
        return true;
    }

    public Object getFunctor() {
        return functor;
    }

    public void setFunctor(Object functor) {
        this.functor = (DoFunctor) functor;
    }

    public void visit(Property item) {
        functor.execute(item);
    }

    public void visit(PropertyType item) {
        functor.execute(item);
    }

    public void visit(PropertyValue item) {
        functor.execute(item);
    }

    public void visit(PropertyInstanceManager item) {
        functor.execute(item);
    }

    public void visit(PropertiesOfAClass item) {
        functor.execute(item);
    }

    public void visit(Multilingualism item) {
        functor.execute(item);
    }
}
