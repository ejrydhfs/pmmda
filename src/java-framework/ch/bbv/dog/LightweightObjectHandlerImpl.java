/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.dog;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * The implementation of the lightweight object handler implementation provides
 * all services realized with the help of bean utilities.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class LightweightObjectHandlerImpl implements LightweightObjectHandler {

  private Map typesbyLw;

  private Map typesbyBo;

  /**
   * Default constructor of the class.
   */
  public LightweightObjectHandlerImpl() {
    typesbyLw = new HashMap();
    typesbyBo = new HashMap();
  }

  public void synchronize(Object lightweight, Object object) {
    if (lightweight == object) {
      return;
    }
    PropertyDescriptor[] descriptors = PropertyUtils
        .getPropertyDescriptors(lightweight);
    for (int i = 0; i < descriptors.length; i++) {
      PropertyDescriptor descriptor = descriptors[i];
      if (Arrays.binarySearch(LightweightObject.SYSTEM_SIMPLE_PROPERTIES,
          descriptor.getName()) >= 0) {
        continue;
      }
      if (descriptor instanceof IndexedPropertyDescriptor) {
        // not handled in the framework
      } else if (descriptor instanceof MappedPropertyDescriptor) {
        // not handled in the framework
      } else {
        String property = descriptor.getName();
        try {
          Class clazz = descriptor.getPropertyType();
          if (containsInterface(clazz.getInterfaces(),
              java.util.Collection.class)
              || containsInterface(clazz.getInterfaces(), java.util.Map.class)) {
            continue;
          }
          PropertyUtils.setProperty(lightweight, property, PropertyUtils
              .getProperty(object, property));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public LightweightObject create(DataObject object) {
    DataObjectType type = (DataObjectType) typesbyLw.get(object.getClass());
    LightweightObject lightweight = type.createLwInstance();
    synchronize(lightweight, object);
    return lightweight;
  }

  public LightweightObject createGraph(DataObject boGraph) {
    LightweightObject lwGraph = null;
    DataObjectType type = getType(boGraph);
    if (type != null) {
      lwGraph = type.invokeTransformer(boGraph);
    }
    return lwGraph;
  }

  public void register(DataObjectType type) {
    typesbyLw.put(type.getLwClass(), type);
    typesbyBo.put(type.getBoClass(), type);
  }

  public DataObjectType getType(LightweightObject object) {
    return (DataObjectType) typesbyLw.get(object.getClass());
  }

  /**
   * Returns the type associated with the given data object.
   * @param object object which type is requested.
   * @return the requested type if found otherwise null.
   * @pre object != null
   */
  protected final DataObjectType getType(DataObject object) {
    return (DataObjectType) typesbyBo.get(object.getClass());
  }

  /**
   * Returns true if the class is member of the list of interfaces.
   * @param interfaces list of interfaces to check.
   * @param clazz class which membership should be check.
   * @return true if the class is member of the list otherwise false.
   * @pre (interfaces != null) && (clazz != null)
   */
  protected final boolean containsInterface(Class[] interfaces, Class clazz) {
    boolean containsInterface = false;
    for (int i = 0; i < interfaces.length; i++) {
      if (clazz == interfaces[i]) {
        containsInterface = true;
        break;
      }
    }
    return containsInterface;
  }
}