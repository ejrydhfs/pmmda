/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.dog.derived;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import ch.bbv.dog.AbstractVisitorFunctor;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DoFunctor;

/**
 * The derived attribue manager is responsible to compute the new value of all
 * registered derived properties. Each time a property is modified it sends a
 * property change event. The manager is triggered and calls all functors
 * registered for the modified property. Registered functors on new derived
 * property are only applied to newly registered data objects to guarantee
 * consistency for the user working on them. Updated functors on already
 * observed properties are used upon next change of the property. Services are
 * provided to force the calculation of all derived attributes of a single data
 * object or a data object tree. The traversal of the tree is realized with the
 * help of the visitor pattern.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DerivedAttributeMgr implements PropertyChangeListener {
  /**
   * List of all registered classes with at least one derived attribute. The key
   * is the class. The values is a map containing of the derived properties. the
   * key is the property name and the value is a list of functors. More formally
   * we have <code>classes = Map(clazz, Map(property, List(functor))).</code>
   */
  private Map classes;

  /**
   * List of the visitor with functor class associated with each registered data
   * object class. The visitor is used to register the manager on all
   * interesting properties and to compute all derived properties of a data
   * object graph.
   */
  private Map visitorClasses;

  /**
   * Buffer containing at moste one instance of a visitor with functor class for
   * a register visitor class.
   */
  private Map visitors;

  /**
   * Default constructor of the class.
   */
  public DerivedAttributeMgr() {
    classes = new HashMap();
    visitorClasses = new HashMap();
    visitors = new HashMap();
  }

  public void register(Class clazz, Class visitorWithFunctor) {
    assert ((AbstractVisitorFunctor.class).isAssignableFrom(visitorWithFunctor));
    visitorClasses.put(clazz, visitorWithFunctor);
  }

  /**
   * Registers the functor for the property of the given class.
   * @param clazz class owning the property, which triggers the functor.
   * @param property property, which change triggers the functor.
   * @param functor functor to execute upon change.
   */
  public void register(Class clazz, String property, DoFunctor functor) {
    assert (clazz != null && property != null && functor != null);
    Map properties = getProperties(clazz);
    if (properties == null) {
      properties = new HashMap();
      classes.put(clazz, properties);
    }

    List list = (List) properties.get(property);
    if (list == null) {
      list = new ArrayList();
      list.add(functor);
      properties.put(property, list);
    }
  }

  /**
   * Unregisters the functor associated to the class and property.
   * @param clazz class owning the property, which triggers the functor.
   * @param property property, which change triggers the functor.
   * @param functor functor to execute upon change.
   */
  public void unregister(Class clazz, String property, DoFunctor functor) {
    assert (clazz != null && property != null && functor != null);
    Map properties = (Map) classes.get(clazz);
    if (properties != null) {
      List functors = (List) properties.get(property);
      if (functors != null) {
        functors.remove(functor);
        if (functors.isEmpty()) {
          properties.remove(property);
          if (properties.isEmpty()) {
            classes.remove(clazz);
          }
        }
      }
    }
  }

  /**
   * Registers the data object and its owned items to process all derived
   * attributes each time a change happens. All items are traversed with a
   * visitor and the manager register itself as listener.
   * @param object data object to register.
   */
  public void register(DataObject object) {

  }

  /**
   * Unregisters the data object and its owned items. After this call no derived
   * attributes are computed when a change happens. All items are traversed with
   * a visitor and the manager unregister itself as listener.
   * @param object data object to unregister.
   */
  public void unregister(DataObject object) {

  }

  /**
   * All functors registered for the property identified in the event are
   * executed.
   * @param event property change event indentifying the changed property.
   */
  public void propertyChange(PropertyChangeEvent event) {
    List list = getFunctors(event.getSource().getClass(), event
        .getPropertyName());
    if (list != null) {
      for (Iterator i = list.iterator(); i.hasNext();) {
        DoFunctor functor = (DoFunctor) i.next();
        functor.execute((DataObject) event.getSource());
      }
    }
  }

  /**
   * Executes all the functors associated with the given data object.
   * @param object data object, which derived properties should be recomputed.
   */
  public void computeDerivedProperties(DataObject object) {
    Map properties = getProperties(object.getClass());
    if (properties != null) {
      for (Iterator i = properties.values().iterator(); i.hasNext();) {
        for (Iterator j = ((List) i.next()).iterator(); j.hasNext();) {
          DoFunctor functor = (DoFunctor) j.next();
          functor.execute(object);
        }
      }
    }
  }

  /**
   * Traverses the data object graph and compute the derived attributes of each
   * node in the tree.
   * @param root the root of the data tree to traverse.
   */
  public void computeRecursiveDerivedProperties(DataObject root) {
  }

  /**
   * Returns all functors associated with the class and property.
   * @param clazz class owning the property, which triggers the functor.
   * @param property property, which change triggers the functor.
   * @return list of functors.
   */
  public List getFunctors(Class clazz, String property) {
    List functors = null;
    Map properties = (Map) classes.get(clazz);
    if (properties != null) {
      functors = (List) properties.get(property);
    }
    return Collections.unmodifiableList(functors);
  }

  protected Map getProperties(Class clazz) {
    return (Map) classes.get(clazz);
  }
}