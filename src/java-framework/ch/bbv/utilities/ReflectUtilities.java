/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A set of convenience methods when using reflection mechanisms.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class ReflectUtilities {
  /**
   * Name of the create method used to instantiate an object using the factory
   * pattern.
   */
  public static final String CREATE_METHOD = "create";

  /**
   * Logger of the application class.
   */
  private static Log log = LogFactory.getLog(ReflectUtilities.class);

  /**
   * Default private constructor to implement static method pattern.
   */
  private ReflectUtilities() {
    // private constructor of static class
  }

  /**
   * Executes the method on the bean with the help of reflection. First it attempts directly declared methods
   * then it attempts inherited metods.
   * @param bean bean on which the method should be called.
   * @param name name of the method.
   * @param types types of the method signature.
   * @param params parameters of the call.
   * @return the return result of the called method if sucessful otherwise null.
   * @pre (bean != null) && (name != null)
   */
  public static Object execute(Object bean, String name, Class[] types,
      Object[] params) {
    assert ((bean != null) && (name != null));
    Object returns = null;
    try {
      Method method = bean.getClass().getDeclaredMethod(name, types);
      returns = method.invoke(bean, params);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      returns = executeInherited(bean, name, types, params);
    } catch (IllegalArgumentException e) {
      executeInherited(bean, name, types, params);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return returns;
  }

  private static Object executeInherited(Object bean, String name, Class[] types,
      Object[] params){
    assert ((bean != null) && (name != null));
    Object returns = null;
    try {
      Method method = bean.getClass().getMethod(name, types);
      returns = method.invoke(bean, params);
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
    return returns;
  }
  
  /**
   * Creates a new instance of the class using the default constructor.
   * @param clazz class of the instance to create.
   * @return the created instance if sucessful otherwise null.
   * @pre clazz != null
   */
  public static Object createInstance(Class clazz) {
    assert (clazz != null);
    Object object = null;
    try {
      object = clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return object;
  }

  /**
   * Creates a new instance through the create method of a factory class.
   * @param qualifiedName qualified name of the class to instantiate.
   * @return the created instance if sucessfull otherwise null.
   * @pre qualifiedName != null
   */
  public static Object createInstance(String qualifiedName) {
    return createInstance(qualifiedName, null, null);
  }

  /**
   * Creates a new instance through the create method of a factory class.
   * @param qualifiedName qualified name of the class to instantiate.
   * @param types types of the parameters of the static method to call.
   * @param objects parameters of the static method to call.
   * @return the created instance if sucessfull otherwise null.
   * @pre qualifiedName != null
   */
  public static Object createInstance(String qualifiedName, Class[] types,
      Object[] objects) {
    assert (qualifiedName != null);
    Object instance = null;
    try {
      Class clazz = Class.forName(qualifiedName);
      if (clazz != null) {
        instance = createInstance(clazz, types, objects);
      }
    } catch (ClassNotFoundException e) {
      log.fatal(e);
    } catch (SecurityException e) {
      log.fatal(e);
    }
    return instance;
  }

  /**
   * Creates a new instance through the create method of a factory class.
   * @param clazz class to instantiate.
   * @param types types of the parameters of the static method to call.
   * @param objects parameters of the static method to call.
   * @return the created instance if sucessfull otherwise null.
   * @pre qualifiedName != null
   */
  public static Object createInstance(Class clazz, Class[] types,
      Object[] objects) {
    Object instance = null;
    try {
      Method method = clazz.getMethod(CREATE_METHOD, types);
      instance = method.invoke(null, objects);
    } catch (SecurityException e) {
      log.fatal(e);
    } catch (NoSuchMethodException e) {
      log.fatal(e);
    } catch (IllegalArgumentException e) {
      log.fatal(e);
    } catch (IllegalAccessException e) {
      log.fatal(e);
    } catch (InvocationTargetException e) {
      log.fatal(e);
    }
    return instance;

  }

  /**
   * Gets the property of a data object described in the node type.
   * @param bean object which property should be retrieved.
   * @param property name of the property to acess.
   * @return the value of the requested property.
   * @pre (bean != null) && (property != null)
   * @see #setProperty(Object, String, Object)
   */
  public static Object getProperty(Object bean, String property) {
    assert ((bean != null) && (property != null));

    Object value = null;
    try {
      value = PropertyUtils.getProperty(bean, property);
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }

    return value;
  }

  /**
   * Sets the property of a data object described in the node type.
   * @param bean object which property should be set.
   * @param property name of the property to acess.
   * @param value new value of the property.
   * @pre (bean != null) && (property != null)
   * @see #getProperty(Object, String)
   */
  public static void setProperty(Object bean, String property, Object value) {
    assert ((bean != null) && (property != null));
    try {
      PropertyUtils.setProperty(bean, property, value);
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    } catch (NoSuchMethodException nsmi) {
      nsmi.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    }
  }
}