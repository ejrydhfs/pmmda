/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

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
   * List of all views defined in the package.
   */
  private List<MetaView> views;

  /**
   * Default constructor of the class.
   */
  public MetaPackage() {
    classes = new ArrayList<MetaClass>();
    views = new ArrayList<MetaView>();
  }

  /**
   * Returns the class with the given index.
   * @param index index of the requested class.
   * @return the requested class.
   * @see #addClass(int, MetaClass)
   */
  public MetaClass getClass(int index) {
    return classes.get(index);
  }

  /**
   * Adds the class in the list of classes.
   * @param clazz class to add.
   * @pre clazz != null
   */
  public void addClass(MetaClass clazz) {
    assert clazz != null;
    classes.add(clazz);
    clazz.setContext(this);
  }

  /**
   * Adds the class in the list of classes.
   * @param index position where the class should be inserted in the list.
   * @param clazz class to add.
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
   * @return list of all classes.
   * @see #setClasses(List)
   */
  public List<MetaClass> getClasses() {
    return classes;
  }

  /**
   * Sets the list of classes of the package.
   * @param classes new list of classes.
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
   * @param clazz to be removed.
   */
  public void removeClass(MetaClass clazz) {
    if (classes.contains(clazz)) {
      classes.remove(clazz);
      clazz.setContext(null);
    }
  }

  /**
   * Returns the view with the given index.
   * @param index index of the requested view.
   * @return the requested view.
   */
  public MetaView getView(int index) {
    return views.get(index);
  }

  /**
   * Adds the view in the list of views of the package.
   * @param view view to add.
   * @pre view != null
   */
  public void addView(MetaView view) {
    assert view != null;
    views.add(view);
    view.setContext(this);
  }

  /**
   * Adds the view in the list of views of the package.
   * @param index position where the view should be inserted in the list.
   * @param view to be added.
   * @pre view != null
   */
  public void addView(int index, MetaView view) {
    assert view != null;
    views.add(index, view);
    view.setContext(this);
  }

  /**
   * Returns a list of all views of the package.
   * @return list of all views.
   * @see #setViews(List)
   */
  public List<MetaView> getViews() {
    return views;
  }

  /**
   * Set the list of views of the package.
   * @param views new list of classes.
   * @see #getViews()
   */
  public void setViews(List<MetaView> views) {
    removeAllViews();
    views.addAll(views);
    for (MetaView view : views) {
      view.setContext(this);
    }
  }

  /**
   * Remove all views of the package.
   */
  public void removeAllViews() {
    for (MetaView view : views) {
      view.setContext(null);
    }
    views.clear();
  }

  /**
   * Removes a view from the package.
   * @param view to be removed.
   */
  public void removeView(MetaView view) {
    if (views.contains(view)) {
      views.remove(view);
      view.setContext(null);
    }
  }

  /**
   * Implements the visitor pattern.
   * @param visitor visitor to process.
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
  }

  /**
   * Returns the class with the given name.
   * @param name simple name of the class to find.
   * @return the requested class if found otherwise null.
   * @pre name != null
   */
  public MetaClass findClass(String name) {
    assert name != null;
    return (MetaClass) findElement(classes, name);
  }
}