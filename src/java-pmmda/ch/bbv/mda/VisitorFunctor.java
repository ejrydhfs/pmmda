/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda;

/**
 * The visitor functor is the standard functor implementation for the MDA
 * visitor.
 * @author marcelbaumann
 * @version $Revision$
 */
public abstract class VisitorFunctor implements Visitor {
	public void visit(MetaModel item) {
    execute(item);
	}

	public void visit(MetaPackage item) {
		execute(item);
	}

	public void visit(MetaClass item) {
		execute(item);
	}

	public void visit(MetaProperty item) {
		execute(item);
	}

	public void visit(MetaIndexedProperty item) {
		execute(item);
	}

	public void visit(TaggedValue item) {
		execute(item);
	}

	/** Executes the functor method on the item.
	 * @param item item to process.
	 */	
	protected abstract void execute(Object item);
}
