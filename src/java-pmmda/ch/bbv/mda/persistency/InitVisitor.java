/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistency;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.Visitor;

/**
 * @author AdrianBachofen
 *
 * Visitor initializing all MetaModel objects. Is used to init the objects
 * after deserialization or loading from xml with XStream.
 */
public class InitVisitor implements Visitor {

	  public void visit(MetaModel item){
	  	item.init();
	  }
	
	  public void visit(MetaPackage item){
	  	item.init();
	  }
	
	  public void visit(MetaClass item){
	  	item.init();
	  }
	
	  public void visit(MetaProperty item){
	  	item.init();
	  }
	
	  public void visit(MetaIndexedProperty item){
	  	item.init();
	  }
	
	  public void visit(TaggedValue item){
	  }
}
