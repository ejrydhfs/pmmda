/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ch.bbv.utilities.ReflectUtilities;

/**
 * The data object type defines the type structure of a data object type. The
 * classes defining such a type are declared in the data object type. Each data
 * object type contains the following information.
 * <ul>
 * <li>The class implementing the data object type.</li>
 * <li>The class implementing the lightweight data object type.</li>
 * <li>The class implementing the iterator for data object representation.
 * </li>
 * <li>The class implementing the iterator for lightweight representation.
 * </li>
 * <li>The transformer class to convert a graph of data object into their
 * lightweight representation.</li>
 * <li>A flag indicating if the data object is a root or not. Root classes are
 * supported in the SOA architecture of the BOG framework and CRUD operations
 * are provided for them.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class DataObjectType {
  /**
   * Name of the visitor as generated.
   */
  public static final String BO_VISITOR_IF_NAME = ".Visitor";

  /**
   * Name of the lightweight visitor as generated.
   */
  public static final String LW_VISITOR_IF_NAME = ".LwVisitor";

  /**
   * Name of the accept method in the visitor pattern as generated.
   */
  public static final String ACCEPT_METHOD_NAME = "accept";

  /**
   * Name of the transform method in the transformer class as generated.
   */
  public static final String TRANSFORM_METHOD_NAME = "transform";

  /**
   * The class of the data object instances.
   */
  private Class boClass;

  /**
   * The class of the lightweight data object instances.
   */
  private Class lwClass;

  /**
   * The class of the interface of data object instances if defined.
   */
  private Class ifClass;

  /**
   * The class of the visitor defined in the visit method in the data object
   * class.
   */
  private Class boVisitorClass;

  /**
   * The class of the visitor defined in the visit method in the lightweight
   * data object class.
   */
  private Class lwVisitorClass;

  /**
   * The class of the transformer class.
   */
  private Class lwTransformerClass;

  /**
   * Flag indicating if instances of the data object type are root objects in
   * data object subgraphs.
   */
  private boolean root;

  /**
   * Constructor of the class.
   * @param lwClass lightweight class describing the data object type.
   * @param boClass data object class describing the data object type.
   * @param ifClass interface class describing the data object type.
   * @param visitorClass visitor class of the data object.
   * @param lwVisitorClass visitor class of the lightweight data object.
   * @param lwTransformerClass transformer class.
   */
  public DataObjectType(Class lwClass, Class boClass, Class ifClass,
      Class visitorClass, Class lwVisitorClass, Class lwTransformerClass) {
    assert ((boClass != null) && (lwClass != null) && (visitorClass != null));
    this.lwClass = lwClass;
    this.boClass = boClass;
    this.ifClass = ifClass;
    this.lwVisitorClass = lwVisitorClass;
    this.boVisitorClass = visitorClass;
    this.lwTransformerClass = lwTransformerClass;
  }

  /**
   * Returns the data object class of the type.
   * @return the requested class.
   */
  public Class getBoClass() {
    return boClass;
  }

  /**
   * Returns the lightweight data object class of the type.
   * @return the requested class.
   */
  public Class getLwClass() {
    return lwClass;
  }

  /**
   * Returns the interface data object class of the type.
   * @return the requested class.
   */
  public Class getIfClass() {
    return ifClass;
  }

  /**
   * Returns the visitor class of the type.
   * @return the requested class.
   */
  public Class getBoVisitorClass() {
    return boVisitorClass;
  }

  /**
   * Returns the lightweight visitor class of the type.
   * @return the requested class.
   */
  public Class getLwVisitorClass() {
    return lwVisitorClass;
  }

  /**
   * Returns the lightweight transformer class of the type.
   * @return the requested transformer class.
   */
  public Class getLwTransformer() {
    return lwTransformerClass;
  }

  /**
   * Returns the flag defining if the type is a potential root in graphs.
   * @return requested flag value.
   * @see #setRoot
   */
  public boolean isRoot() {
    return root;
  }

  /**
   * Sets the flag defining if the type is a potential root in graphs.
   * @param root new value of the flag.
   * @see #isRoot
   */
  public void setRoot(boolean root) {
    this.root = root;
  }

  /**
   * Returns an instance of the requested class.
   * @return the instance of the requested class.
   */
  public DataObject createBoInstance() {
    return (DataObject) ReflectUtilities.createInstance(boClass);
  }

  /**
   * Returns an instance of the requested class.
   * @return the instance of the requested class.
   */
  public LightweightObject createLwInstance() {
    return (LightweightObject) ReflectUtilities.createInstance(lwClass);
  }

  /**
   * Returns an instance of the requested class.
   * @return the instance of the requested class.
   */
  public AbstractVisitorFunctor createVisitorInstance() {
    return (AbstractVisitorFunctor) ReflectUtilities
        .createInstance(boVisitorClass);
  }

  /**
   * Returns an instance of the requested class.
   * @return the instance of the requested class.
   */
  public AbstractVisitorFunctor createLwVisitorInstance() {
    return (AbstractVisitorFunctor) ReflectUtilities
        .createInstance(lwVisitorClass);
  }

  /**
   * Invokes the appropriate method of the transformer class for the data object
   * and returns the created lightweight data object graph.
   * @param boGraph data object graph to transform.
   * @return the created lightweight representation of the data object graph.
   */
  public LightweightObject invokeTransformer(DataObject boGraph) {
    LightweightObject lwGraph = null;
    try {
      Class[] types = { boGraph.getClass() };
      Method method = lwTransformerClass.getDeclaredMethod(
          TRANSFORM_METHOD_NAME, types);
      Object[] params = { boGraph };
      lwGraph = (LightweightObject) method.invoke(null, params);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return lwGraph;
  }

  /**
   * Invokes the visitor on the lightweight data object.
   * @param object lightweight object to which the visitor should be applied.
   * @param visitor visitor to execute.
   */
  public static void invokeVisitor(LightweightObject object,
      AbstractVisitorFunctor visitor) {
    assert ((object != null) && (visitor != null));
    invokeVisitor(object.getClass(), object, visitor);
  }

  /**
   * Invokes the visitor on the lightweight data class of the given type.
   * @param clazz class of the data object instance.
   * @param object lightweight object to which the visitor should be applied.
   * @param visitor visitor to execute.
   */
  public static void invokeVisitor(Class clazz, LightweightObject object,
      AbstractVisitorFunctor visitor) {
    assert ((clazz != null) && (object != null) && (visitor != null));
    invokeVisitorOn(clazz, object, visitor, LW_VISITOR_IF_NAME);
  }

  /**
   * Invokes the visitor on the data object.
   * @param object data object to which the visitor should be applied.
   * @param visitor visitor to execute.
   */
  public static void invokeVisitor(DataObject object,
      AbstractVisitorFunctor visitor) {
    assert ((object != null) && (visitor != null));
    invokeVisitor(object.getClass(), object, visitor);
  }

  /**
   * Invokes the visitor on the data class of the given type.
   * @param clazz class of the data object instance.
   * @param object data object to which the visitor should be applied.
   * @param visitor visitor to execute.
   */
  public static void invokeVisitor(Class clazz, DataObject object,
      AbstractVisitorFunctor visitor) {
    assert ((clazz != null) && (object != null) && (visitor != null));
    invokeVisitorOn(clazz, object, visitor, BO_VISITOR_IF_NAME);
  }

  /**
   * Invokes the visitor on the given bean.
   * @param clazz class of the bean to visit.
   * @param object bean to visit.
   * @param visitor visitor object to invoke on the bean.
   * @param visitorIfName name of the visitor interface.
   */
  protected static void invokeVisitorOn(Class clazz, Object object,
      AbstractVisitorFunctor visitor, String visitorIfName) {
    try {
      Class[] types = new Class[1];
      Class visitorClass = visitor.getClass();
      Class[] interfaces = visitorClass.getInterfaces();
      for (int i = 0; i < interfaces.length; i++) {
        if (interfaces[i].getName().endsWith(visitorIfName)) {
          types[0] = interfaces[i];
          break;
        }
      }

      Method method = clazz.getDeclaredMethod(ACCEPT_METHOD_NAME, types);
      Object[] params = { visitor };
      method.invoke(object, params);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}