/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.List;

/**
 * The reference code manager handles a set of reference codes either directly 
 * or through another reference code manager. Access methods provide access to
 * the codes. The manager provides only read operations and not update 
 * functions. Reference codes are data domain enumeration values and should
 * never be modified in the program. All codes are preloaded at startup time
 * to provide responsiveness in the application. The clients do not notice any
 * difference between reference code types and canonical enumeration types. 
 * @author Marcel Baumann
 * @version $Revision$
 */
public interface ReferenceCodeMgr {
	/** Returns all active roots of a hierarchical reference code list.
	 * @param clazz class of the requested reference codes.
	 * @return The list of all active root hierarchical reference codes.
	 * @pre clazz != null
	 */
	List getRoots(Class clazz);

	/** Returns all roots of a hierarchical reference code list.
	 * @param clazz class of the requested reference codes.
	 * @return The list of all root hierarchical reference codes.
	 * @pre clazz != null
	 */
	List getAllRoots(Class clazz);
	
	/** Returns the list of all reference codes of the given class.
	 * @param clazz class of the requested reference codes.
	 * @return list of all reference codes of the given class. If the class is not
	 * not known returns null.
	 * @pre clazz != null
	 */
	List getAllReferenceCodes(Class clazz);
		
	/** Returns the list of all active reference codes of the given class.
	 * @param clazz class of the requested reference codes.
	 * @return list of all active reference codes of the given class. If the class
	 * is not known returns null.
	 * @pre clazz != null
	 */
	List getReferenceCodes(Class clazz);
		
	/** Returns the reference code of the given class with the defined code.
	 * @param clazz class of the requested reference codes.
	 * @param code code of the requested reference code.
	 * @return the requested reference code if found otherwise null.
	 * @pre (clazz != null) && (code != null)
	 */
	ReferenceCode getReferenceCode(Class clazz, Integer code);

	/** Returns all references codes classes managed by the manager.
	 * @return list of reference code classes managed in the manager.
	 */
	List getReferenceCodeClasses();

	/** Returns all references codes classes available through the manager.
	 * @return list of reference code classes available in the manager.
	 */
	List getAllReferenceCodeClasses();
	
	/** Returns the version of the reference code type.
	 * @param clazz class of the reference code type.
	 * @return version of the reference code.
	 */
	String getVersion(Class clazz);
	
	/** Registers the class of codes and the reference manager managing them.
	 * @param clazz class of the requested reference codes.
	 * @param referenceCodeMgr reference code manager handling the given set of
	 * codes.
	 * @pre (clazz != null) && (referenceCodeMgr != null)
	 */	
	void registerReferenceCode(Class clazz, ReferenceCodeMgr referenceCodeMgr);
	
	/** Registers all the reference code classes managed in the given reference 
	 * code manager.
	 * @param referenceCodeMgr reference code manager managing the reference codes
	 * to register.
	 * @pre referenceCodeMgr != null
	 */
	void registerReferenceCodes(ReferenceCodeMgr referenceCodeMgr);

	/** Register the class of codes, the list of code values and its version.
	 * @param clazz clazz of the reference code.
	 * @param codes list of codes of the reference code.
	 * @param version version of the reference code type.
	 */
	void registerReferenceCode(Class clazz, List codes, String version);
	
	/** Returns the root of the given hierarchical reference code.
	 * @param code code which root is looked for.
	 * @return the root of the given hierarchical reference code.
	 * @pre code != null
	 */
	HierarchicalReferenceCode getRootFor(HierarchicalReferenceCode code);

	/** Returns the parent of the given hierarchical reference code.
	 * @param code the code which parent is requested.
	 * @return the parent code if defined or null if it the code is root.
	 * @pre code != null
	 */	
	HierarchicalReferenceCode getParentFor(HierarchicalReferenceCode code);
	
	/** Adds a reference code retriever to the manager. The addition order is 
	 * the one used to cascade the retrieval and store requests.
	 * @param retriever retriever to add to the manager.
	 */
	void addRetriever(ReferenceCodeRetriever retriever);
}
