/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.reference;

import java.util.List;

/**
 * The reference code retriever provides an interface to retrieve reference
 * code descriptors and code values from various sources. Multiple retrievers
 * can be available. A priority order should be define to insure optimal 
 * gathering of reference code values. 
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface ReferenceCodeRetriever {
	/** Returns the updated reference code information of the given reference
	 * code descriptor.
	 * @param info description of the requested reference code.
	 * @return updated reference code descriptor.
	 */
	ReferenceCodeInfo retrieveReferenceCode(ReferenceCodeInfo info);
	
	/** Returns a list of reference code descriptors of the given list of 
	 * reference code descriptors. If the list is null returns all the
	 * reference code descriptors the retriever has access to.
	 * @param infos list of descriptors of the requested reference codes.
	 * @return udpated reference code descriptors.
	 */
	List retrieveReferenceCodes(List infos);
	
	/** Stores the list of reference code descriptors if the retriever support 
	 * caching.
	 * @param infos list of descriptors of the reference codes to store.
	 */
	void storeReferenceCodes(List infos);
}
