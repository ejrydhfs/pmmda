/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.OjbConstants.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import ch.bbv.mda.MetaClassifier;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.TaggedValue;

/**
 * The persistence tag initializator initializes important tags defining
 * characteristics of persistence. The following rules are defined.
 * <ul>
 * <li>If the persistence flag is modified, the column name, column type,
 * column size, primary flag, null flag are set to adequate values.</li>
 * <li>the name of the item is used to generate the column or table name. The
 * type of the item is used to generate the other information.</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OjbTagListener implements PropertyChangeListener {
  /**
   * Called each time the value property of a tag is changed.
   * @param event property change event describing the changes.
   */
  public void propertyChange(PropertyChangeEvent event) {
    TaggedValue item = (TaggedValue) event.getSource();
    String newValue = (String) event.getNewValue();
    if (UmlConstants.PERSISTENCE_TAG.equals(item.getTag())) {
      if (Boolean.parseBoolean(newValue)) {
        enablePersistence(item.getContext());
      } else {
        disablePersistence(item.getContext());
      }
    }
  }

  /**
   * Resets all tags related to persistence if the persistence flag of the item
   * is disabled.
   * @param item item to reset.
   */
  private void disablePersistence(MetaElement item) {
    setValue(item.getTaggedValue(TABLE_NAME_TAG), null);
    setValue(item.getTaggedValue(COLUMN_NAME_TAG), null);
    setValue(item.getTaggedValue(COLUMN_TYPE_TAG), null);
    setValue(item.getTaggedValue(COLUMN_SIZE_TAG), null);
    setValue(item.getTaggedValue(ORDER_BY_TAG), null);
    setValue(item.getTaggedValue(CAN_BE_NULL_TAG), Boolean.FALSE.toString());
    setValue(item.getTaggedValue(IS_PRIMARY_TAG), Boolean.FALSE.toString());
  }

  /**
   * Initializes all tags related to persistence if the persistence flag of the
   * item is enabled.
   * @param item item to initialize.
   */
  private void enablePersistence(MetaElement item) {
    String name = item.getName();
    if (name != null) {
      name = name.toUpperCase();
      setValue(item.getTaggedValue(TABLE_NAME_TAG), name);
      setValue(item.getTaggedValue(COLUMN_NAME_TAG), name);
    }
    if (item instanceof MetaProperty) {
      MetaClassifier classifier = ((MetaProperty) item).getClassifier();
      setValue(item.getTaggedValue(COLUMN_TYPE_TAG), null);
      setValue(item.getTaggedValue(COLUMN_SIZE_TAG), null);
    }
  }

  /**
   * Sets the new value of the tag if it exists.
   * @param tag tag to updated.
   * @param value new value of the tag.
   */
  private void setValue(TaggedValue tag, String value) {
    if (tag != null) {
      tag.setValue(value);
    }
  }
}
