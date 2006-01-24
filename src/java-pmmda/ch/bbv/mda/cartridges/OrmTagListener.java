/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.OrmConstants.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.configuration.CompositeConfiguration;

import ch.bbv.mda.*;
import ch.bbv.mda.generators.CartridgeImpl;
import ch.bbv.utilities.PropertyUtilities;

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
public class OrmTagListener implements PropertyChangeListener {
  private CartridgeImpl cartridge;

  /**
   * Default constructor of the class.
   * @param cartridge cartridge owning the listener.
   */
  public OrmTagListener(CartridgeImpl cartridge) {
    this.cartridge = cartridge;
  }

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
    setValue(item, TABLE_NAME, null);
    setValue(item, COLUMN_NAME, null);
    setValue(item, COLUMN_TYPE, null);
    setValue(item, COLUMN_SIZE, null);
    setValue(item, ORDER_BY, null);
    setValue(item, CAN_BE_NULL, Boolean.FALSE.toString());
    setValue(item, IS_PRIMARY, Boolean.FALSE.toString());
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
      setValue(item, TABLE_NAME, name);
      setValue(item, COLUMN_NAME, name);
    }
    if (item instanceof MetaProperty) {
      translate(item);
    }
  }

  /**
   * Translates the classifier to the associated type. If the classifier is a
   * datatype the translation table of the cartridge is used.
   * @param item element to update.
   */
  private void translate(MetaElement item) {
    String type = null;
    String nullable = null;
    String size = null;
    String path = null;
    MetaClassifier classifier = ((MetaProperty) item).getClassifier();
    if (classifier != null) {
      if (classifier instanceof MetaDatatype) {
        path = "datatypes.datatype";
      } else if (classifier instanceof MetaClass) {
        path = "datatypes.class";
      }
      CompositeConfiguration configuration = cartridge.getConfiguration();
      type = PropertyUtilities.getAttribute(configuration, path, "name",
          classifier.getName(), "type");
      nullable = PropertyUtilities.getAttribute(configuration, path, "name",
          classifier.getName(), "null");
      size = PropertyUtilities.getAttribute(configuration, path, "name",
          classifier.getName(), "default");
    }
    setValue(item, COLUMN_TYPE, type);
    setValue(item, COLUMN_SIZE, size);
    setValue(item, CAN_BE_NULL, Boolean.valueOf(nullable).toString());
  }

  /**
   * Sets the new value of the tag if it exists.
   * @param item item to update.
   * @param tag tag to updated.
   * @param value new value of the tag.
   */
  private void setValue(MetaElement item, String tag, String value) {
    TaggedValue taggedValue = item.getTaggedValue(tag);
    if (taggedValue != null) {
      taggedValue.setValue(value);
    }
  }
}
