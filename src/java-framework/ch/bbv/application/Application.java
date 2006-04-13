/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.application;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.reference.ReferenceCodeMgr;

public class Application extends Locator {
  /**
   * Subsystem name of the reference code manager instance.
   */
  public static final String REFERENCE_CODE_MANAGER_NAME = "ReferenceCodeManager";

  /**
   * Subsystem name of the data object manger instance.
   */
  public static final String DATA_OBJECT_MANAGER_NAME = "DataObjectManager";

  /**
   * The data object manager of the application.
   */
  private DataObjectMgr dataObjectMgr;

  /**
   * The reference code manager of the application
   */
  private ReferenceCodeMgr referenceCodeMgr;

  public static Application getApplication() {
    return (Application) Locator.getInstance();
  }
  /**
   * Returns the data object manager of the application.
   * @return the data object code manager
   * @see #setDataObjectMgr(DataObjectMgr)
   */
  public DataObjectMgr getDataObjectMgr() {
    return dataObjectMgr;
  }

  /**
   * Returns the reference code manager of the application.
   * @return the reference code manager
   * @see #setReferenceCodeMgr(ReferenceCodeMgr)
   */
  public ReferenceCodeMgr getReferenceCodeMgr() {
    return referenceCodeMgr;
  }

  /**
   * Sets the data object manager of the application.
   * @param dataObjectMgr the data object manager
   * @see #getDataObjectMgr()
   */
  protected void setDataObjectMgr(DataObjectMgr dataObjectMgr) {
    this.dataObjectMgr = dataObjectMgr;
    register(dataObjectMgr, DATA_OBJECT_MANAGER_NAME);
  }

  /**
   * Sets the reference code manager of the application.
   * @param referenceCodeMgr the reference code manager
   * @see #getReferenceCodeMgr()
   */
  protected void setReferenceCodeMgr(ReferenceCodeMgr referenceCodeMgr) {
    this.referenceCodeMgr = referenceCodeMgr;
    register(referenceCodeMgr, REFERENCE_CODE_MANAGER_NAME);
  }
}
