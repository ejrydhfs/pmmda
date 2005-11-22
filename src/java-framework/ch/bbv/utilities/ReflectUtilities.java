/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A set of convenience methods when using reflection mechanisms. Methods to
 * create new instances with constructors and factory creation methods are
 * provided.
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
   * Executes the method on the bean with the help of reflection. First it
   * attempts directly declared methods then it attempts inherited metods.
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
      log.fatal(e);
    } catch (NoSuchMethodException e) {
      returns = executeInherited(bean, name, types, params);
    } catch (IllegalArgumentException e) {
      executeInherited(bean, name, types, params);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    }
    return returns;
  }

  /**
   * Creates a new instance of the class using the default constructor.
   * @param classname qualified name of the class.
   * @return the created instance if sucessful otherwise null.
   * @pre clazz != null
   */
  public static Object newInstance(String classname) {
    assert (classname != null);
    Object object = null;
    try {
      Class clazz = Class.forName(classname);
      object = clazz.newInstance();
    } catch (InstantiationException e) {
      log.fatal(e);
    } catch (IllegalAccessException e) {
      log.fatal(e);
    } catch (ClassNotFoundException e) {
      log.fatal(e);
    }
    return object;

  }

  /**
   * Creates a new instance of the class using the default constructor.
   * @param clazz class of the instance to create.
   * @return the created instance if sucessful otherwise null.
   * @pre clazz != null
   */
  public static Object newInstance(Class clazz) {
    assert (clazz != null);
    Object object = null;
    try {
      object = clazz.newInstance();
    } catch (InstantiationException e) {
      log.fatal(e);
    } catch (IllegalAccessException e) {
      log.fatal(e);
    }
    return object;
  }

  /**
   * Creates a new instance of the class using the constructor with the provided
   * signature.
   * @param clazz class of the instance to create.
   * @param types types of the parameters of the static method to call.
   * @param objects parameters of the static method to call.
   * @return the created instance if sucessfull otherwise null.
   * @pre clazz != null
   */
  public static Object newInstance(Class clazz, Class[] types, Object[] objects) {
    assert (clazz != null);
    Object instance = null;
    try {
      Constructor constructor = clazz.getConstructor(types);
      instance = constructor.newInstance(objects);
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
    } catch (InstantiationException e) {
      log.fatal(e);
    }
    return instance;
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
   * @param clazz class to instantiate.
   * @return the created instance if sucessfull otherwise null.
   * @pre qualifiedName != null
   */
  public static Object createInstance(Class clazz) {
    return createInstance(clazz, null, null);
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
    } catch (InvocationTargetException e) {
      log.fatal(e);
    } catch (NoSuchMethodException e) {
      log.fatal(e);
    } catch (IllegalAccessException e) {
      log.fatal(e);
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
    assert (bean != null) && (property != null);
    try {
      PropertyUtils.setProperty(bean, property, value);
    } catch (InvocationTargetException e) {
      log.fatal(e);
    } catch (NoSuchMethodException e) {
      log.fatal(e);
    } catch (IllegalAccessException e) {
      log.fatal(e);
    }
  }

  /**
   * Returns the path to the location from where the class was loaded. The
   * result is only valid for classes not loaded from the system class loader.
   * @param clazz class which load location should be infered.
   * @return the load location path of the class.
   */
  public String getLoadpath(Class clazz) {
    return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
  }

  private static Object executeInherited(Object bean, String name,
      Class[] types, Object[] params) {
    assert ((bean != null) && (name != null));
    Object returns = null;
    try {
      Method method = bean.getClass().getMethod(name, types);
      returns = method.invoke(bean, params);
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
    return returns;
  }
}