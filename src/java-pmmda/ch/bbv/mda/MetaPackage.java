/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;
import ch.bbv.mda.uml.*;

/**
 * The meta package models a package object as defined in the UML standard. It
 * provides additional services to retrieve tagged values associated with it.
 * These values are used to map the independent application model to the
 * platform specific architecture. The following tagged values are defined.
 * <ul>
 * <dt>shouldBeGenerated</dt>
 * <dd>Boolean flag indicating if the package should be mapped to the platform
 * or not. The default value is false.</dd>
 * <dt>ifPackage</dt>
 * <dd>The package in which the interfaces of the data object interfaces should
 * be generated. If null no interfaces will be generated. The default value is
 * null.</dd>
 * <dt>boPackage</dt>
 * <dd>The package in which the data object classes will be generated. If null
 * no classes will be generated. The default value is null.</dd>
 * <dt>lwPackage</dt>
 * <dd>The package in which the lightweight object classes will be generated.
 * If null no lightweight classes will be generated. The default value is null.
 * </dd>
 * </ul>
 * 
 * @author Marcel Baumann
 * @version $Revision$
 */
public class MetaPackage extends MetaElement {
	/**
	 * Serial version identifier coded as back compatibility date.
	 */
	private static final long serialVersionUID = 20041201;

	/**
	 * List of all classes defined in the package.
	 */
	private List<MetaClass> classes;

	/**
	 * List of all views associated to this package.
	 */
	private List<MetaView> views;

	/**
	 * List of all use cases defined in the package.
	 */
	private List<MetaUseCase> useCases;

	/**
	 * Default constructor of the class.
	 */
	public MetaPackage() {
		classes = new ArrayList<MetaClass>();
		views = new ArrayList<MetaView>();
		useCases = new ArrayList<MetaUseCase>();
	}

	/**
	 * Returns the class with the given index.
	 * 
	 * @param index
	 *          index of the requested class
	 * @return the requested class
	 * @see #addClass(int, MetaClass)
	 */
	public MetaClass getClass(int index) {
		return classes.get(index);
	}

	/**
	 * Adds the class in the list of classes.
	 * 
	 * @param clazz
	 *          class to add
	 * @pre clazz != null
	 */
	public void addClass(MetaClass clazz) {
		assert clazz != null;
		classes.add(clazz);
		clazz.setContext(this);
	}

	/**
	 * Adds the class in the list of classes.
	 * 
	 * @param index
	 *          position where the class should be inserted in the list
	 * @param clazz
	 *          class to add
	 * @pre clazz != null
	 * @see #getClass(int)
	 */
	public void addClass(int index, MetaClass clazz) {
		assert clazz != null;
		classes.add(index, clazz);
		clazz.setContext(this);
	}

	/**
	 * Returns a list of all classes defined in the object.
	 * 
	 * @return list of all classes
	 * @see #setClasses(List)
	 */
	public List<MetaClass> getClasses() {
		return classes;
	}

	/**
	 * Sets the list of classes of the package.
	 * 
	 * @param classes
	 *          new list of classes
	 * @see #getClasses()
	 */
	public void setClasses(List<MetaClass> classes) {
		removeAllClasses();
		classes.addAll(classes);
		for (MetaClass clazz : classes) {
			clazz.setContext(this);
		}
	}

	/**
	 * Remove all classes of the package.
	 */
	public void removeAllClasses() {
		for (MetaClass clazz : classes) {
			clazz.setContext(null);
		}
		classes.clear();
	}

	/**
	 * Removes a class from the package.
	 * 
	 * @param clazz
	 *          to be removed
	 */
	public void removeClass(MetaClass clazz) {
		if (classes.contains(clazz)) {
			classes.remove(clazz);
			clazz.setContext(null);
		}
	}

	/**
	 * Returns the view with the given index.
	 * 
	 * @param index
	 *          index of the requested view
	 * @return the requested view
	 * @see #addView(int, MetaView)
	 */
	public MetaView getView(int index) {
		return views.get(index);
	}

	/**
	 * Adds the view to the list of views. The view properties are registered with the properties of the model. Corresponding tagged values are set.
	 * 
	 * @param view
	 *          view to add
	 * @pre view != null
	 */
	public void addView(MetaView view) {
		assert view != null;
		views.add(view);
		view.setContext(this);
		view.addToModel();
	}

	/**
	 * Adds the view to the list of views. The view properties are registered with the properties of the model. Corresponding tagged values are set.
	 * 
	 * @param index
	 *          position where the view should be inserted in the list
	 * @param view
	 *          view to add
	 * @pre view != null
	 * @see #getView(int)
	 */
	public void addView(int index, MetaView view) {
		assert view != null;
		views.add(index, view);
		view.setContext(this);
		view.addToModel();
	}

	/**
	 * Returns a list of all views defined in the object.
	 * 
	 * @return list of all views
	 * @see #setViews(List)
	 */
	public List<MetaView> getViews() {
		return views;
	}

	/**
	 * Sets the list of views of the package. The view properties are registered with the properties of the model. Corresponding tagged values are set.
	 * 
	 * @param views
	 *          new list of views
	 * @see #getViews()
	 */
	public void setViews(List<MetaView> views) {
		removeAllViews();
		this.views.addAll(views);
		for (MetaView view : views) {
			view.setContext(this);
			view.addToModel();
		}
	}

	/**
	 * Remove all views of the package. Information about the views are removed from the tagged values of the properties contained within the views.
	 */
	public void removeAllViews() {
		for (MetaView view : views) {
			// A view stores its context within tagged values of the properties.
			// Removing a view from its package therofe invalidates that content. We
			// hence remove the corresponding content from the tagged values of the
			// properties.
			view.removeFromModel();
			view.setContext(null);
		}
		views.clear();
	}

	/**
	 * Removes a view from the package. Information about the view is removed from the tagged values of the properties contained within the view.
	 * 
	 * @param view
	 *          to be removed
	 */
	public void removeView(MetaView view) {
		if (views.contains(view)) {
			// A view stores its context within tagged values of the properties.
			// Removing a view from its package therofe invalidates that content. We
			// hence remove the corresponding content from the tagged values of the
			// properties.
			view.removeFromModel();
			views.remove(view);
			view.setContext(null);
		}
	}
	
	/**
	 * Returns the view with passed qualified name. Null if not contained within this package.
	 * @param qualifiedName of the MetaView to be found.
	 * @return MetaView if found, null if none available.
	 */
	public MetaView findView(String qualifiedName) {
		for(MetaView view : views) {
			if(view.getQualifiedName().equals(qualifiedName)) return view;
		}
		return null;
	}

	/**
	 * Returns the use case with the given index.
	 * 
	 * @param index
	 *          index of the requested use case
	 * @return the requested use case
	 * @see #addUseCase(int, MetaUseCase)
	 */
	public MetaUseCase getUseCase(int index) {
		return useCases.get(index);
	}

	/**
	 * Adds the use case in the list of useCases.
	 * 
	 * @param useCase
	 *          use case to add
	 * @pre useCase != null
	 */
	public void addUseCase(MetaUseCase useCase) {
		assert useCase != null;
		useCases.add(useCase);
		useCase.setContext(this);
	}

	/**
	 * Adds the use casae in the list of useCases.
	 * 
	 * @param index
	 *          position where the use case should be inserted in the list
	 * @param useCase
	 *          use case to add
	 * @pre useCase != null
	 * @see #getUseCase(int)
	 */
	public void addUseCase(int index, MetaUseCase useCase) {
		assert useCase != null;
		useCases.add(index, useCase);
		useCase.setContext(this);
	}

	/**
	 * Returns a list of all use cases defined in the object.
	 * 
	 * @return list of all use cases
	 * @see #setUseCases(List)
	 */
	public List<MetaUseCase> getUseCases() {
		return useCases;
	}

	/**
	 * Sets the list of use cases of the package.
	 * 
	 * @param useCases
	 *          new list of use cases
	 * @see #getUseCases()
	 */
	public void setUseCases(List<MetaUseCase> useCases) {
		removeAllUseCases();
		useCases.addAll(useCases);
		for (MetaUseCase useCase : useCases) {
			useCase.setContext(this);
		}
	}

	/**
	 * Remove all use cases of the package.
	 */
	public void removeAllUseCases() {
		for (MetaUseCase useCase : useCases) {
			useCase.setContext(null);
		}
		useCases.clear();
	}

	/**
	 * Removes a use case from the package.
	 * 
	 * @param useCase
	 *          to be removed
	 */
	public void removeUseCase(MetaUseCase useCase) {
		if (useCases.contains(useCase)) {
			useCases.remove(useCase);
			useCase.setContext(null);
		}
	}

	/**
	 * Implements the visitor pattern.
	 * 
	 * @param visitor
	 *          visitor to process
	 */
	public void accept(Visitor visitor) {
		assert visitor != null;
		visitor.visit(this);
		for (MetaClass clazz : classes) {
			clazz.accept(visitor);
		}
		for (MetaView view : views) {
			view.accept(visitor);
		}
		for (MetaUseCase useCase : useCases) {
			useCase.accept(visitor);
		}
	}

	/**
	 * Returns the class with the given name.
	 * 
	 * @param name
	 *          simple name of the class to find
	 * @return the requested class if found otherwise null
	 * @pre name != null
	 */
	public MetaClass getClass(String name) {
		assert name != null;
		return (MetaClass) findElement(classes, name);
	}

	/**
	 * Returns the use case with the given name.
	 * 
	 * @param name
	 *          simple name of the use case to find
	 * @return the requested use case if found otherwise null
	 * @pre name != null
	 */
	public MetaUseCase getUseCase(String name) {
		assert name != null;
		return (MetaUseCase) findElement(useCases, name);
	}
}