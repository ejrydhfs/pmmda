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
import java.util.Date;

import ch.bbv.dog.DataObjectImpl;

public class EnumerationElement extends DataObjectImpl implements Serializable {

    /**
     * Code of the reference code instance.
     * 
     * @serial
     */
    private int code;

    /**
     * Sort order of the reference code instance in the set of related codes.
     * 
     * @serial
     */
    private int sortOrder;

    /**
     * Flag indicating if the code is active or not.
     * 
     * @serial
     */
    private boolean active;

    /**
     * Description of the reference code instance.
     * 
     * @serial
     */
    private String description;

    /**
     * Short description reference code instance.
     * 
     * @serial
     */
    private String shortDescription;

    /**
     * Start of the validity of the code instance.
     */
    private Date fromDate;

    /**
     * End of the validity of the code instance.
     */
    private Date toDate;

    private Multilingualism translation;

    /**
     * Constructor initializes the immutable instance with all property values.
     * 
     * @param code
     *            unique identifier for the code of this type.
     * @param sortOrder
     *            sort order of the code instance.
     * @param shortDescription
     *            short description of the code.
     * @param description
     *            description of the code.
     * @param active
     *            flag indicating if the code is active or not.
     * @param fromDate
     *            the start of the interval when the code is active.
     * @param toDate
     *            the end of the interval when the code is active.
     */
    public EnumerationElement(int code, int sortOrder, String shortDescription, String description, boolean active,
            Date fromDate, Date toDate) {
        this.code = code;
        this.sortOrder = sortOrder;
        this.shortDescription = shortDescription;
        this.description = description;
        this.active = active;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.translation = new Multilingualism();
    }

    /**
     * Default Constructor used by Hibernate
     * 
     */
    public EnumerationElement() {

    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Multilingualism getTranslation() {
        return translation;
    }

    public String toString() {
        return getShortDescription();
    }
}
