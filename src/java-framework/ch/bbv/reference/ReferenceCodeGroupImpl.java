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
public class ReferenceCodeGroupImpl implements ReferenceCodeGroup {
	/** List of reference code groups owned in this group.
	 */
	private List groups;
	
	/** List of reference code descriptors of this group.
	 */
	private List descriptors;
	
	/** Name of the group.
	 */
	private String name;
	
	/** Description of the group.
	 */
	private String description;
	
	public ReferenceCodeGroupImpl() {
		groups = new ArrayList();
		descriptors = new ArrayList();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public List getGroup() {
		return groups;
	}
	public void setGroup(List groups) {
		this.groups = groups;
	}
	
	public ReferenceCodeGroup getGroup(int index) {
		return (ReferenceCodeGroup)groups.get(index); 
	}
	
	public void setGroup(int index, ReferenceCodeGroup group) {
		groups.set(index, group);
	}

	public void addGroup(ReferenceCodeGroup group) {
		groups.add(group);	
	}
	
	public List getDescriptor() {
		return descriptors;
	}
	public void setDescriptor(List descriptors) {
		this.descriptors = descriptors;
	}
	
	public ReferenceCodeDescriptor get(int index) {
		return (ReferenceCodeDescriptor)descriptors.get(index);
	}
	public void setDescriptor(int index, ReferenceCodeDescriptor descriptor) {
		descriptors.set(index, descriptor);
	}

}
