/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.reference;

/**
 * <p>The reference code creator provides services to create the database 
 * representation for a set of reference codes defining all values of a
 * reference code descriptor. The following functions are provided.</p>
 * <ul>
 *   <li>Creates the insert statements needed to fill the database table
 *     of a reference code set with all defined code values.</li>
 *   <li>Extracts the code values from a database table and populate a
 *     reference code descriptor with all found values.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ReferenceCodesCreator {
	public StringBuffer createDbInsertStmts(ReferenceCodeDescriptor descriptor) {
		return null;
	}
	
	public void extractDbReferenceCodes(ReferenceCodeDescriptor descriptor) {
	}
}
