/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.documentor.doclets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.mda.*;

import com.sun.javadoc.*;

/**
 * The interface handler synchronizes the source code tree with the UML model.
 * The handler assumes that interfaces are part of frameworks and should be
 * defined in the model.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class InterfaceHandler {
	/** Logger for all instances of the class.
	 */
	private static Log log = LogFactory.getLog(InterfaceHandler.class);

	/** UML/MDA model to synchronize with.
	 */
	private MetaModel model;
	
	/** Constructor of the class.
	 * @param model model to synchronize with.
	 */
	public InterfaceHandler(MetaModel model) {
		this.model = model;
	}

	/** Synchronize the interface with its model representation.
	 * @param interfaces interface to synchronize.
	 */	
	public void synchronize(ClassDoc interfaces) {
		assert(interfaces != null && interfaces.isInterface());
		MetaClass clazz = model.findClass(interfaces.qualifiedTypeName());
		if (clazz == null) {
			if (log.isWarnEnabled()) {
				log.warn("interface not defined in model " + interfaces.qualifiedTypeName());
			}
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("synchronize interface with model "+ clazz.getName());
		}
		FieldDoc[] fields = interfaces.fields();
		for (int i = 0; i < fields.length; i++) {
			synchronizeConstant(fields[i], clazz);
		}
		removeUndefinedConstants(fields, clazz);
		
		MethodDoc[] methods = interfaces.methods();
		for (int i = 0; i < methods.length; i++) {
		  // TODO implement
		}
	}
	
	/** Synchronizes the model with the JavaDoc data The field name, description, 
	 * and  type are synchronized. The field can only be a constant. 
	 * @param field field to synchronize.
	 * @param clazz clazz to synchronize
	 * @pre (field != null) && (clazz != null)
	 */
	private void synchronizeConstant(FieldDoc field, MetaClass clazz) {
		assert((field != null) && (clazz != null));
		MetaProperty property = clazz.findProperty(field.name());
	}
	
	/** Removes the constants defined in the model which are no more part of the
	 * source code.
	 * @param fields list of fields define in the source code.
	 * @param clazz class to clean.
	 */
	private void removeUndefinedConstants(FieldDoc[] fields, MetaClass clazz) {
	}
	
	private void synchronizeMethod(MethodDoc method, MetaClass clazz) {
		MetaMethod function = new MetaMethod();
		function.setName(method.name());
		function.setVisibility(getVisibility(method));
		function.setDescription(method.commentText());
		function.setAbstract(method.isAbstract());
		MetaReturn returns = new MetaReturn();
		returns.setName(method.returnType().qualifiedTypeName());
		function.setReturn(returns);
		Parameter[] parameters = method.parameters();
		for (int i = 0; i < parameters.length; i++) {
		}
	}
	
	VisibilityCode getVisibility(ProgramElementDoc element) {
		VisibilityCode code = null;
		if (element.isPublic()) {
			code = VisibilityCode.getCode(VisibilityCode.PUBLIC_VISIBILITY_CODE);
		} else if (element.isProtected()) {
			code = VisibilityCode.getCode(VisibilityCode.PROTECTED_VISIBILITY_CODE);
		} else if (element.isPackagePrivate()) {
			code = VisibilityCode.getCode(VisibilityCode.PACKAGE_VISIBILITY_CODE);
		} else if (element.isPrivate()) {
			code = VisibilityCode.getCode(VisibilityCode.PRIVATE_VISIBILITY_CODE);
		}
		return code;
	}
}
