/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.derived;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The property iterator traverses all property values defined through a 
 * relative path following the bean utility syntax. The syntax was extended to
 * traverse complete collections. The wildcard character is used to define that
 * a collection must be traverse. The wildcard can be used for a collection, an
 * array or a map as usual in bean utility.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class PropertyIterator implements Iterator {
  public static final String PATTERN = "[*]";

  /** Logger of the class.
   */
  private static Log log = LogFactory.getLog(PropertyIterator.class);
  
  /** List of path elements containing at the end a collection to traverse.
   */
  private String[] pathElements;
  
  /** The list of objects being the bean used to evaluate the associated path
   * element. Object with index i is the bean of the path element with the 
   * same index. 
   */
  private Object[] objects;
 
 	/** The iterator associated with the path element with the same index.
 	 */
  private Iterator[] iterators;
  
  /** The last part of the path pointing to a property.
   */
  private String endPath;
  private Object nextObject;

	/** Constructor of the class.
	 * @param bean bean root of the path used to retrieve values.
	 * @param path path to the properties to retrieve.
	 */
  public PropertyIterator(Object bean, String path) {
    pathElements = extractCollections(path);
    objects = new Object[pathElements.length];
    iterators = new Iterator[pathElements.length];
    objects[0] = bean;
    iterators[0] = getIterator(0);
    nextObject = null;
  }

  public boolean hasNext() {
  	try {
  		if (nextObject != null) {
				nextObject = nextObject();
  		}
		} catch (NoSuchElementException e) {
			nextObject = null;
		}
		return nextObject != null;
  }

  public Object next() throws NoSuchElementException {
  	Object result = null;
  	if (nextObject != null) {
  		result = nextObject;
  		nextObject = null;
  	} else {
  		result = nextObject();
  	}
    return result;
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  /** Returns the next object of a set of nested iterators. If the iterator is
   * null or has no more objects the chain is traversed and next object is
   * retrieved.
   * @return the requested next object.
   * @throws NoSuchElementException iterator has no more elements.
   * @pre (objects[0] != null) && (iterators[0] != null)
   */
  private Object nextObject() throws NoSuchElementException {
    Object object = null;
    int i = iterators.length - 1;
    while ((i >= 0) && ((iterators[i] == null) || !iterators[i].hasNext())) {
      i--;
    }
    if (i < 0) {
    	// no more elements are available.
      throw new NoSuchElementException("No more elements");
    }
    while (i < (iterators.length - 1)) {
    	if (iterators[i].hasNext()) {
	      objects[i + 1] = iterators[i].next();
	      i++;
	      iterators[i] = getIterator(i);
	      if (iterators[i] == null) {
	        throw new NoSuchElementException("Illegal path expression");
	      }
			} else {
				// the iterator has no more elements, retrieve the next elements of
				// higher iterator, if none exists no more elements are available.
				i--;
				if (i < 0) {
				  throw new NoSuchElementException("No more elements");
				}
			}
    }
    try {
      object = BeanUtils.getProperty(objects[i], pathElements[i]);
      if ((object != null) && (endPath != null)) {
        BeanUtils.getProperty(object, endPath);
      }
    } catch (IllegalAccessException e) {
      log.error(e);
      throw new NoSuchElementException("Illegal path expression");
    } catch (InvocationTargetException e) {
      log.error(e);
      throw new NoSuchElementException("Illegal path expression");
    } catch (NoSuchMethodException e) {
      log.error(e);
      throw new NoSuchElementException("Illegal path expression");
    }
    return object;
  }

  private Iterator getIterator(int index) {
    Iterator iterator = null;
    try {
      Object collection = BeanUtils.getProperty(objects[index],
          pathElements[index]);
      if (collection instanceof Map) {
        collection = ((Map) collection).values();
      }
      iterator = ((Collection) collection).iterator();
    } catch (IllegalAccessException e) {
      log.error(e);
    } catch (InvocationTargetException e) {
      log.error(e);
    } catch (NoSuchMethodException e) {
      log.error(e);
    }
    return iterator;
  }

  private String[] extractCollections(String path) {
    List elements = new ArrayList();
    int fromIndex = 0;
    int indexOf = path.indexOf(PATTERN);
    while (indexOf != -1) {
      elements.add(path.substring(fromIndex, indexOf));
      fromIndex = indexOf + PATTERN.length();
    }
    if (fromIndex < path.length()) {
      endPath = path.substring(fromIndex);
    }
    return (String[]) elements.toArray(new String[elements.size()]);
  }
}
