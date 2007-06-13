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

import java.io.Serializable;

public class EnumerationValue extends PropertyValue {

    private Integer enumKey;

    /**
     * Constructor
     * 
     * @param attribute
     */
    public EnumerationValue(Property attribute) {
        super();
        if (attribute.getType() instanceof EnumerationType) {
            setProperty(attribute);
        } else {
            throw new RuntimeException("Wrong type...");
        }
        enumKey = 0;
    }

    public EnumerationValue() {
        super();
    }

    /**
     * Returns the Enumeration Type of the attribute
     * 
     * @return the Enumeration Type of the attribute
     */
    private EnumerationType getEnumType() {
        return (EnumerationType) getProperty().getType();
    }

    @Override
    public Serializable getValue() {
        return getEnumType().getEnum(enumKey);
    }

    @Override
    public void setValue(Serializable value) {
        if (value instanceof Integer) {
            enumKey = (Integer) value;
        } else if (value instanceof String) {
            enumKey = getEnumType().getKey((String) value);
        } else if (value instanceof EnumerationElement) {
            enumKey = ((EnumerationElement) value).getCode();
        } else {
            throw new RuntimeException("Wrong type...");
        }
    }

}
