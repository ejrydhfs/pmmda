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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HierarchicalEnumerationElement extends EnumerationElement {

    private HierarchicalEnumerationElement parent;

    private List<HierarchicalEnumerationElement> children;

    public HierarchicalEnumerationElement(int code, int sortOrder, String shortDescription, String description,
            boolean active, Date fromDate, Date toDate, HierarchicalEnumerationElement parent) {
        super(code, sortOrder, shortDescription, description, active, fromDate, toDate);
        this.parent = parent;
        this.children = new ArrayList<HierarchicalEnumerationElement>();
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public HierarchicalEnumerationElement() {
        super();
    }

    public void addEnumerationElement(HierarchicalEnumerationElement child) {
        children.add(child);
    }

    public EnumerationElement getParent() {
        return parent;
    }

    public void setParent(HierarchicalEnumerationElement parent) {
        this.parent = parent;
    }

    public List<HierarchicalEnumerationElement> getChildren() {
        return children;
    }

    public HierarchicalEnumerationElement getEnumerationElement(int code) {
        if (getCode() == code) {
            return this;
        }
        for (HierarchicalEnumerationElement element : children) {
            HierarchicalEnumerationElement result = element.getEnumerationElement(code);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
