/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog;

import java.io.Serializable;

/**
 * The data object identifier class defines the identifier for any data object.
 * Instances of this class are immutable.
 * @author MarcelBaumann
 * @version $Revision$
 */
public final class DataObjectId implements Serializable {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Defines the value of an illegal identifier.
   */
  public static final Integer ILLEGAL_ID = null;

  /**
   * Class of the data object which identity is represented.
   */
  private Class clazz;

  /**
   * Identifier of the data object.
   */
  private Integer id;

  /**
   * Constructor of the class.
   * @param clazz class of the data object.
   * @param id identifier of the data object.
   * @pre (clazz != null) && (id != null)
   */
  public DataObjectId(Class clazz, Integer id) {
    assert ((clazz != null) && (id != null));
    this.clazz = clazz;
    this.id = id;
  }

  /**
   * Constructor of the class.
   * @param object data object which identity should be created.
   * @pre (object != null) && (object.getId() != null)
   */
  public DataObjectId(HasId object) {
    this(object.getClass(), object.getId());
  }

  /**
   * Returns the identifier of the object.
   * @return the unique identifier of the object of this data object type.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Returns the class of the object.
   * @return the class of the data object.
   */
  public Class getBoClass() {
    return clazz;
  }

  /**
   * @inheritDoc Object#equals(Object)
   */
  public boolean equals(Object object) {
    boolean equals = false;
    if (this == object) {
      equals = true;
    } else if (object instanceof DataObjectId) {
      DataObjectId objectId = (DataObjectId) object;
      equals = id.equals(objectId.getId())
          && clazz.equals(objectId.getBoClass());
    }
    return equals;
  }

  /**
   * @inheritDoc Object#hashCode
   */
  public int hashCode() {
	if(id == null){
	  return super.hashCode();
	}
    return id.hashCode() ^ clazz.hashCode();
  }
}