/* copyright 2000-2006 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import ch.bbv.explorer.ExplorerNode;

/**
 * Compares MetaElements:
 * 
 * Order: model < package < class < datatype < classifier < stereotype < property < indexed property < member < method < return < tag definition < parameter < exception < element
 * If same type then name is compared according to String.compareTo 
 * @author adrianbachofen
 *
 */
public class MetaElementComparator implements Comparator {
	
	static ArrayList<Class> clazzes = new ArrayList<Class>();

	static Class[] classOrder = new Class[]{MetaModel.class, MetaPackage.class, MetaClass.class, MetaDatatype.class, MetaClassifier.class,
																					Stereotype.class, MetaProperty.class, MetaIndexedProperty.class, MetaMember.class,
																					MetaMethod.class, MetaReturn.class, TagDefinition.class, MetaParameter.class, MetaException.class, MetaElement.class};

	static {
		clazzes.addAll(Arrays.asList(classOrder));
	}
	
	//@Override
	public int compare(Object o1, Object o2) {
		if(o1 == null && o2 == null) return 0;
		if(o1 == null) return 1;
		if(o2 == null) return -1;
		
		if(o1 instanceof ExplorerNode && o2 instanceof ExplorerNode) {
			ExplorerNode en1 = (ExplorerNode)o1;
			ExplorerNode en2 = (ExplorerNode)o2;
			
			if(en1.getDataObject() == null && en2.getDataObject() == null) return 0;
			if(en1.getDataObject() == null) return 1;
			if(en2.getDataObject() == null) return -1;
			
			Integer classOrder1 = clazzes.indexOf(en1.getDataObject().getClass());
			Integer classOrder2 = clazzes.indexOf(en2.getDataObject().getClass());
			int compared = classOrder1.compareTo(classOrder2);
			
			if(compared != 0) return compared;
			
			// if MetaElements then use name as further criteria. If not MetaElement then there is no further comparing aspect.
			if(en1.getDataObject() instanceof MetaElement && en2.getDataObject() instanceof MetaElement) { 
				MetaElement me1 = (MetaElement)en1.getDataObject();
				MetaElement me2 = (MetaElement)en2.getDataObject();
				if(me1.getName() == null && me2.getName() == null) return 0;
				if(me1.getName() == null) return 1;
				if(me2.getName() == null) return -1;
				return me1.getName().compareToIgnoreCase(me2.getName());
			}
			return en1.getDataObject().getClass().getName().compareTo(en2.getDataObject().getClass().getName());
		}
		return o1.getClass().getName().compareTo(o2.getClass().getName());
  }

}
