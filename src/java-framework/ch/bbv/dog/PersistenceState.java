/**
 * 
 */
package ch.bbv.dog;

/**
 * @author ZlatkoFranjcic
 *
 */
public enum PersistenceState {
	/**
	* Transient and newly created.
	*/
	New,

	/**
	* Persistent and unchanged. 
	*/
	Unchanged,

	/**
	* Persistent and changed
	*/
	Modified,

	/**
	* Persistent and to delete in database
	*/
	Deleted,

	/**
	* Transient and deleted
	*/
	Destroyed	  
}
