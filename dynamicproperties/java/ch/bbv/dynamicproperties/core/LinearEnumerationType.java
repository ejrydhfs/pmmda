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

public class LinearEnumerationType extends EnumerationType {

    private List<EnumerationElement> enumerations;

    /**
     * Constructor
     * 
     * @param name
     *            the name
     * @param viewer
     *            the viewer
     * @param editor
     *            the editor
     */
    public LinearEnumerationType(String name, Class<? extends IViewer> viewer, Class<? extends IEditor> editor) {
        super(name, viewer, editor);
        enumerations = new ArrayList<EnumerationElement>();
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public LinearEnumerationType() {
        super();
    }

    /**
     * Returns the maximal code of all enumerations and increments the result by
     * one
     * 
     * @return
     */
    private synchronized int getNextCode() {
        int max = 0;
        for (EnumerationElement element : enumerations) {
            max = Math.max(max, element.getCode());
        }
        return max + 1;
    }

    /**
     * Tests if a code is already present in the list
     * 
     * @param code
     * @return
     */
    private synchronized boolean containsKey(int code) {
        for (EnumerationElement element : enumerations) {
            if (element.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void addEnum(Integer key, EnumerationElement enumeration) {
        if (!containsKey(key)) {
            enumeration.setCode(key);
            enumerations.add(enumeration);
        } else {
            throw new RuntimeException("Key does already exists");
        }
    }

    @Override
    public synchronized void addEnum(EnumerationElement enumeration) {
        int code = 1;
        if (!enumerations.isEmpty()) {
            code = getNextCode();
        }
        enumeration.setCode(code);
        addEnum(code, enumeration);
    }

    @Override
    public void addEnum(String shortDescription, String description) {
        addEnum(0, 0, shortDescription, description, true, null, null);
    }

    @Override
    public void addEnum(int code, int sortOrder, String shortDescription, String description, boolean active,
            Date fromDate, Date toDate) {
        addEnum(new EnumerationElement(code, sortOrder, shortDescription, description, active, fromDate, toDate));
    }

    @Override
    public EnumerationElement getEnum(Integer key) {
        return enumerations.get(key);
    }

    @Override
    public Integer getKey(String enumeration) {
        // TODO: redesign
        for (EnumerationElement element : enumerations) {
            if (element.getShortDescription().equals(enumeration)) {
                return element.getCode();
            }
        }
        return 0;
    }

    @Override
    public List<EnumerationElement> getEnumerations() {
        return enumerations;
    }

    @Override
    public PropertyValue createPropertyValue(Property attribute) {
        return new EnumerationValue(attribute);
    }
}
