/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.reference;

import java.io.Serializable;
import java.util.List;

/**
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface ReferenceCodeGroup extends Serializable {
	/** Returns the name of the reference code.
	 * @return name of the reference code.
	 */
	String getName();
	
	void setName(String name);
	
	String getDescription();
	
	void setDescription(String description);
	
	List getGroup();
	void setGroup(List groups);
	ReferenceCodeGroup getGroup(int index);
	void setGroup(int index, ReferenceCodeGroup group);
	void addGroup(ReferenceCodeGroup group);
	
	List getDescriptor();
	void setDescriptor(List descriptors);
	ReferenceCodeDescriptor get(int index);
	void setDescriptor(int index, ReferenceCodeDescriptor descriptor);
}
