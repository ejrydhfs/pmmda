/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistency;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;

/**
 * The persistency policy defines the set of functions needed to realize a
 * persistency handler. A persistency policy manipulates its current persistency
 * scheme. The policy provides the fine grained methods to manipulate a 
 * persistent scheme.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface PersistencyPolicy {
	/** Returns true if the package exists in the scheme.
	 * @param qualifiedName qualified name of the package to check. 
	 * @return true if the package exists otherwise false.
	 */
	boolean existsPackage(String qualifiedName);
	
	/** Creates a new package based on the given prototype.
	 * @param metaPackage prototype used to create the new package.
	 */
	void create(MetaPackage metaPackage);
	
	/** Retrieves the package with the given name.
	 * @param qualifiedName qualified name of the package to retrieve.
	 * @return the requested package if existing otherwise null.
	 */
	MetaPackage getPackage(String qualifiedName);
	
	/** Deletes the package with the given name.
	 * @param qualifiedName qualified name of the package to delete.
	 */
	void deletePackage(String qualifiedName);
	
	/** Updates the existing package of the scheme with the given prototype.
	 * If the package does not exist no update is performed.
	 * @param metaPackage prototype used to update the package.
	 */
	void update(MetaPackage metaPackage);

	/** Returns true if the class exists in the scheme.
	 * @param qualifiedName qualified name of the class to check. 
	 * @return true if the class exists otherwise false.
	 */
	boolean existsClass(String qualifiedName);
	void create(MetaClass metaClass);
	MetaClass getClass(String qualifiedName);
	void deleteClass(String qualifiedName);
	void update(MetaClass metaClass);

	/** Returns true if the property exists in the scheme.
	 * @param qualifiedName qualified name of the property to check. 
	 * @return true if the property exists otherwise false.
	 */
	boolean existsProperty(String qualifiedName);
	void create(MetaProperty metaProperty);
	MetaProperty getProperty(String qualifiedName);
	void deleteProperty(String qualifiedName);
	void update(MetaProperty metaProperty);
}
