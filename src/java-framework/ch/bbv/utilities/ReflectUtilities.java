/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
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

  public static final Map<String, Class> PRIMITIVES_TYPES;

  /**
   * Logger of the application class.
   */
  private static Log log = LogFactory.getLog(ReflectUtilities.class);

  static {
    PRIMITIVES_TYPES = new HashMap<String, Class>();
    PRIMITIVES_TYPES.put("boolean", Boolean.TYPE);
    PRIMITIVES_TYPES.put("byte", Byte.TYPE);
    PRIMITIVES_TYPES.put("short", Short.TYPE);
    PRIMITIVES_TYPES.put("int", Integer.TYPE);
    PRIMITIVES_TYPES.put("long", Long.TYPE);
    PRIMITIVES_TYPES.put("float", Float.TYPE);
    PRIMITIVES_TYPES.put("double", Double.TYPE);
    PRIMITIVES_TYPES.put("char", Character.TYPE);
  }
  
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
      returns = executeInherited(bean, name, types, params);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    }
    return returns;
  }

  /**
   * Returns true if the class exists otherwise false.
   * @param qualifiedName qualified class name of the class to retrive.
   * @return flag indicating if the class could be found and loaded or not.
   */
  public static boolean exists(String qualifiedName) {
    boolean exists;
    try {
      Class.forName(qualifiedName);
      exists = true;
    } catch (ClassNotFoundException e) {
      exists = false;
    }
    return exists;
  }

  /**
   * Loads and returns the class having the className. Will load but normal
   * classes and the classes representing primatives.
   * @param className the name of the class to load.
   * @return Class the loaded class
   */
  public static Class loadClass(String className) {
    final String methodName = "ClassUtils.loadClass";
    ExceptionUtils.checkEmpty(methodName, "className", className);
    className = StringUtils.trimToNull(className);

    // get rid of any array notation
    className = StringUtils.replace(className, "[]", "");

    final ClassLoader loader = getClassLoader();
    Class loadedClass = null;
    try {
      // check and see if its a primitive and if so convert it
      if (isPrimitiveType(className)) {
        loadedClass = getPrimitiveClass(className, loader);
      } else {
        loadedClass = loader.loadClass(className);
      }
    } catch (final Throwable e) {
      log.error(e);
    }
    return loadedClass;
  }

  /**
   * Executes a static method on a class with the help of reflection. First it
   * attempts directly declared methods then it attempts inherited metods.
   * @param className class on which the static method should be called.
   * @param name name of the method.
   * @param types types of the method signature.
   * @param params parameters of the call.
   * @return the return result of the called method if sucessful otherwise null.
   * @pre (clazz != null) && (name != null)
   */
  public static Object execute(String className, String name, Class[] types,
      Object[] params) {
    assert ((className != null) && (name != null));
    Object returns = null;
    Class clazz = null;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      log.fatal(e);
    }
    if (clazz != null) {
      try {
        Method method = clazz.getDeclaredMethod(name, types);
        method.invoke(clazz, params);
      } catch (SecurityException e) {
        log.fatal(e);
      } catch (NoSuchMethodException e) {
        returns = executeInherited(className, name, types, params);
      } catch (IllegalArgumentException e) {
        returns = executeInherited(className, name, types, params);
      } catch (IllegalAccessException e) {
        log.fatal(e);
      } catch (InvocationTargetException e) {
        log.fatal(e);
      }
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
    assert (bean != null) && (property != null);
    Object value = null;
    try {
      value = PropertyUtils.getProperty(bean, property);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (NestedNullException e) {
      log.error(e);
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

  /**
   * Executes a static method on a class with the help of reflection. It
   * attempts all metods.
   * @param className class on which the static method should be called.
   * @param name name of the method.
   * @param types types of the method signature.
   * @param params parameters of the call.
   * @return the return result of the called method if sucessful otherwise null.
   * @pre (clazz != null) && (name != null)
   */
  private static Object executeInherited(String className, String name,
      Class[] types, Object[] params) {
    assert ((className != null) && (name != null));
    Object returns = null;
    Class clazz = null;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      log.fatal(e);
    }
    if (clazz != null) {
      try {
        Method method = clazz.getMethod(name, types);
        method.invoke(clazz, params);
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
    }
    return returns;
  }

  /**
   * Gets the appropriate class loader instance.
   * @return the class loader.
   */
  public static final ClassLoader getClassLoader() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      loader = ReflectUtilities.class.getClassLoader();
      Thread.currentThread().setContextClassLoader(loader);
    }
    return loader;
  }

  /**
   * Provides matching of simple wildcards. (i.e. '*.java' etc.)
   * @param path the path to match against.
   * @param pattern the pattern to check if the path matches.
   * @return true if the <code>path</code> matches the given
   *         <code>pattern</code>, false otherwise.
   */
  public static boolean wildcardMatch(String path, String pattern) {
    path = StringUtils.trimToEmpty(path);
    boolean matches = false;
    final String doubleStar = "**/";
    final String slash = "/";
    pattern = StringUtils.replace(pattern, ".", "\\.");
    boolean matchAll = pattern.startsWith(doubleStar);
    if (matchAll) {
      String replacement = ".*/";
      if (path.indexOf(slash) == -1) {
        replacement = ".*";
      }
      pattern = StringUtils.replaceOnce(pattern, doubleStar, replacement);
    }
    pattern = StringUtils.replace(pattern, "*", ".*");
    try {
      matches = path.matches(pattern);
    } catch (final PatternSyntaxException exception) {
      matches = false;
    }
    if (!matchAll) {
      matches = matches
          && StringUtils.countMatches(pattern, slash) == StringUtils
              .countMatches(path, slash);
    }
    return matches;
  }

  /**
   * Returns the type class name for a Java primitive.
   * @param name a <code>String</code> with the name of the type
   * @param loader the loader to use.
   * @return a <code>String</code> with the name of the corresponding
   *         java.lang wrapper class if <code>name</code> is a Java primitive
   *         type; <code>false</code> if not
   */
  protected static final Class getPrimitiveClass(final String name,
      final ClassLoader loader) {
    final String methodName = "ClassUtils.getPrimitiveClass";
    ExceptionUtils.checkEmpty(methodName, "name", name);
    ExceptionUtils.checkNull(methodName, "loader", loader);

    Class primitiveClass = null;
    if (isPrimitiveType(name) && !name.equals("void")) {
      String className = null;
      if ("char".equals(name)) {
        className = "java.lang.Character";
      }
      if ("int".equals(name)) {
        className = "java.lang.Integer";
      }

      className = "java.lang." + StringUtils.capitalize(name);

      try {
        if (StringUtils.isNotEmpty(className)) {
          Field field = loader.loadClass(className).getField("TYPE");
          primitiveClass = (Class) field.get(null);
        }
      } catch (final Exception e) {
        log.error(e);
      }
    }
    return primitiveClass;
  }

  /**
   * <p/> Checks if a given type name is a Java primitive type.
   * </p>
   * @param name a <code>String</code> with the name of the type
   * @return <code>true</code> if <code>name</code> is a Java primitive
   *         type; <code>false</code> if not
   */
  protected static final boolean isPrimitiveType(final String name) {
    return ("void".equals(name) || "char".equals(name) || "byte".equals(name)
        || "short".equals(name) || "int".equals(name) || "long".equals(name)
        || "float".equals(name) || "double".equals(name) || "boolean"
        .equals(name));
  }

}