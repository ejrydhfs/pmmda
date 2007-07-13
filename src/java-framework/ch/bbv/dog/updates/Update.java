/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.updates;

import ch.bbv.dog.DataObject;

/**  
 * Interface for all updates made on {@link DataObject data objects}.
 * <p>
 * There are several types of updates, which implement this interface.<p>
 * <table>
 *   <tr>
 *     <th>Class</th>
 *     <th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>{@link ItemAdditionUpdate}</td>
 *     <td>Represents an item which has been added to an indexed property.</td>
 *   </tr>
 *   <tr>
 *     <td>{@link ItemInsertionUpdate}</td>
 *     <td>Represents an item which has been inserted into an indexed property.</td>
 *   </tr>
 *   <tr>
 *     <td>{@link ItemRemovalUpdate}</td>
 *     <td>Represents an item which has been removed from an indexed property.</td>
 *   </tr>
 *   <tr>
 *     <td>{@link PrimitiveFieldUpdate}</td>
 *     <td>Represents an update of the value of a single property which stores primitive values.</td>
 *   </tr>
 *   <tr>
 *     <td>{@link SingleFieldUpdate}</td>
 *     <td>
 *       Represents an update of the value of a single property which is of type
 *       {@code DataObject}.
 *     </td>
 *   </tr>
 * </table>
 * @version $Revision$
 */
public interface Update {
	/**
	 * Executes this update on a specified data object.
	 * @param dataObject the data object which should be modified by this update
	 */
	void execute(DataObject dataObject);
}