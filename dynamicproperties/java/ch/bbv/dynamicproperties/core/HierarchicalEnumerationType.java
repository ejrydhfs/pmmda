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

import ch.bbv.dynamicproperties.visualization.IEditor;
import ch.bbv.dynamicproperties.visualization.IViewer;

public class HierarchicalEnumerationType extends EnumerationType {

    private List<HierarchicalEnumerationElement> rootElements;

    public HierarchicalEnumerationType(String name, Class<? extends IViewer> viewer, Class<? extends IEditor> editor) {
        super(name, viewer, editor);
        rootElements = new ArrayList<HierarchicalEnumerationElement>();
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public HierarchicalEnumerationType() {
        super();
    }

    @Override
    public void addEnum(Integer key, EnumerationElement enumeration) {
        enumeration.setCode(key);
        rootElements.add((HierarchicalEnumerationElement) enumeration);

    }

    @Override
    public void addEnum(EnumerationElement enumeration) {
        rootElements.add((HierarchicalEnumerationElement) enumeration);
    }

    @Override
    public void addEnum(String shortDescription, String description) {
        addEnum(0, 0, shortDescription, description, true, null, null);

    }

    @Override
    public void addEnum(int code, int sortOrder, String shortDescription, String description, boolean active,
            Date fromDate, Date toDate) {
        addEnum(new HierarchicalEnumerationElement(code, sortOrder, shortDescription, description, active, fromDate,
                toDate, null));
    }

    @Override
    public HierarchicalEnumerationElement getEnum(Integer code) {
        for (HierarchicalEnumerationElement rootElement : rootElements) {
            HierarchicalEnumerationElement result = rootElement.getEnumerationElement(code);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<EnumerationElement> getEnumerations() {
        return new ArrayList<EnumerationElement>(rootElements);
    }

    @Override
    public Integer getKey(String enumeration) {
        return null;
    }

    public List<HierarchicalEnumerationElement> getRoots() {
        return rootElements;
    }
}
