/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistence.nsuml;

import java.util.Collection;
import java.util.Iterator;

/**
 * The filter iterator provides a selective iterator over a collection. Only
 * items compatible with the filter class are returned.
 * @param <T> class which instances should be filtered.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class FilterIterator<T> implements Iterator<T> {
  /**
   * Iterator used to iterate over the collection.
   */
  private Iterator<Object> iterator;

  /**
   * Class used to filter the items of the collection.
   */
  private Class<T> filter;

  /**
   * The next element of the iterator. This buffer is necessary to implement the
   * has next method.
   */
  private T data;

  /**
   * Constructor of the class.
   * @param collection collection on which the iterator will be applied.
   * @param filter the filter criterium.
   * @pre (collection != null) && (filter != null)
   */
  public FilterIterator(Collection collection, Class<T> filter) {
    assert (collection != null) && (filter != null);
    iterator = collection.iterator();
    this.filter = filter;
    next();
  }

  /**
   * Unsupported operation for filter iterator.
   * @see java.util.Iterator#remove()
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns true if the iterator has a next item.
   * @return true if a next item exists otherwise false.
   * @see java.util.Iterator#hasNext()
   */
  public boolean hasNext() {
    return data != null;
  }

  /**
   * Returns the next object of the iterator.
   * @return the next object of the iterator if defined otherwise null.
   * @see java.util.Iterator#next()
   */
  public T next() {
    T value = data;
    data = null;
    while (iterator.hasNext()) {
      data = (T)iterator.next();
      if (filter.isAssignableFrom(data.getClass())) {
        break;
      }
      data = null;
    }
    return value;
  }
}