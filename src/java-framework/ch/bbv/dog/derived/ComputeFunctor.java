/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog.derived;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
class ComputeFunctor implements DoFunctor {
	private DerivedAttributeMgr manager;
	
	public ComputeFunctor(DerivedAttributeMgr manager) {
		assert(manager != null);
		this.manager = manager;
	}
	
	public void execute(DataObject object) {
		manager.computeDerivedProperties(object);
	}

}
