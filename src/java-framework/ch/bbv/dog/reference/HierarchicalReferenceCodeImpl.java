/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The class provides a default implementation for the hierarchical reference
 * code interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class HierarchicalReferenceCodeImpl extends ReferenceCodeImpl implements
    HierarchicalReferenceCode {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * List of children of the hierarchical reference code.
   */
  private List<HierarchicalReferenceCode> children;

  /**
   * Code of the parent owning the hierarchical reference code.
   */
  private Integer parentCode;

  /**
   * Constructor of the class.
   */
  public HierarchicalReferenceCodeImpl() {
    children = new ArrayList<HierarchicalReferenceCode>();
  }

  /**
   * Constructor of the class.
   * @param code code of the hierarchical reference code.
   * @param description description of the code.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param active flag indicating if the code is active or not.
   * @param parentCode code of the parent of the code or null if not defined.
   */
  public HierarchicalReferenceCodeImpl(int code, short sortOrder,
      String description, String shortDescription, boolean active,
      Integer parentCode) {
    this(code, sortOrder, description, shortDescription, active, parentCode,
        null, null);
  }

  /**
   * Constructor of the class.
   * @param code code of the hierarchical reference code.
   * @param description description of the code.
   * @param sortOrder sort order of the code instance.
   * @param shortDescription short description of the code.
   * @param active flag indicating if the code is active or not.
   * @param parentCode code of the parent of the code or null if not defined.
   * @param fromDate the start of the interval when the code is active.
   * @param toDate the end of the interval when the code is active.
   */
  public HierarchicalReferenceCodeImpl(int code, short sortOrder,
      String description, String shortDescription, boolean active,
      Integer parentCode, Date fromDate, Date toDate) {
    super(code, sortOrder, description, shortDescription, active, fromDate,
        toDate);
    children = new ArrayList<HierarchicalReferenceCode>();
    this.parentCode = parentCode;
  }

  public Integer getParentCode() {
    return parentCode;
  }

  public List getChildren() {
    return children;
  }

  /**
   * Sets all children of the code instance. The children are sorted based on
   * their order information.
   * @param children children to set to the code instance.
   * @see #getChildren
   * @pre children != null
   */
  public void setChildren(List<HierarchicalReferenceCode> children) {
    children.clear();
    children.addAll(children);
  }
}
