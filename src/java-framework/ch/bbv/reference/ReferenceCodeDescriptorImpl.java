/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ReferenceCodeDescriptorImpl implements ReferenceCodeDescriptor {
	private String name;
	private Integer defaultValue;
	private String description;
	private Class referenceCodeClass;
	private String referenceCodeClassName;
	private boolean isHierarchical;
	private List codes;
	
	/** Constructor of the class.
	 * 
	 */
	public ReferenceCodeDescriptorImpl() {
		codes = new ArrayList();
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Class getReferenceCodeClass() {
		return referenceCodeClass;
	}

	public void setReferenceCodeClass(Class clazz) {
		this.referenceCodeClass = clazz;
	}
	
	public String getReferenceCodeClassName() {
		return referenceCodeClassName;
	}
	
	public void setReferenceCodeClassName(String referenceCodeClassName) {
		this.referenceCodeClassName = referenceCodeClassName;
	}

	public boolean isHierarchical() {
		return isHierarchical;
	}

	public void setIsHierarchical(boolean isHierarchical) {
		this.isHierarchical = isHierarchical;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;	
	}
	
	public List getCodes() {
		return codes;
	}
}
