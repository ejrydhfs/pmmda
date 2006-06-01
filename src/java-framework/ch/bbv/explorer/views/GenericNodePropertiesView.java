/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.explorer.views;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectType;
import ch.bbv.explorer.NodePropertiesView;
import ch.bbv.explorer.UiNodeType;
import ch.bbv.explorer.properties.Messages;
import ch.bbv.utilities.TextUtilities;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * The generic node properties view displays all the properties of a data object
 * type. Their values can be edited. The properties are inferred through
 * reflection. The view handles simple, indexed and mapped properties. The view
 * has knowledge how to handle strings, natural numbers, decimal numbers,
 * booleans and dates. The following conventions exist.
 * <ul>
 * <li>String properties are displayed as text lines.</li>
 * <li>Natural numbers and decimal numbers are displayed as a text input field
 * with a format filter to allow only legal values. Overflows are only detected
 * during the internal conversion and not during user input.</li>
 * <li>Boolean properties are displayed as check boxes.</li>
 * <li>Date are displayed with a custom calendar components.</li>
 * </ul>
 * The formatted text field features are used to check if the inputed data have
 * the expected format. The class is well behaved in case the bean is null.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class GenericNodePropertiesView extends NodePropertiesView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(GenericNodePropertiesView.class);

  /**
   * List of all views for simple properties.
   */
  private List<PrimitivePropertyView> primitivePropertyViews;

  /**
   * List of all views for simple data object properties.
   */
  private List<DataObjectPropertyView> simplePropertyViews;

  /**
   * List of all views for indexed properties.
   */
  private List<IndexedPropertyView> indexedPropertyViews;

  /**
   * List of all views for mapped properties.
   */
  private List<MappedPropertyView> mappedPropertyViews;

  /**
   * Class of the instance displayed in the generic properties view.
   */
  private Class type;

  /**
   * Default constructor of the class.
   */
  public GenericNodePropertiesView() {
    primitivePropertyViews = new ArrayList<PrimitivePropertyView>();
    simplePropertyViews = new ArrayList<DataObjectPropertyView>();
    indexedPropertyViews = new ArrayList<IndexedPropertyView>();
    mappedPropertyViews = new ArrayList<MappedPropertyView>();
  }

  @Override
  public void populateView() {
    DataObject bean = getModel().getDataObject();
    if (type == null) {
      // first time the dialog is populated, the visible fields are infered
      // and the associated user interface items are created.
      type = bean.getClass();
      if (log.isDebugEnabled()) {
        log.debug("Creating generic node properties view " + type.getName());
      }
      retrieveProperties(bean);
      initialize();
    }
    for (PropertyView view : primitivePropertyViews) {
      view.setModel(bean);
    }
    for (DataObjectPropertyView view : simplePropertyViews) {
      view.setModel(bean);
    }
    for (IndexedPropertyView view : indexedPropertyViews) {
      view.setModel(bean);
    }
    for (MappedPropertyView view : mappedPropertyViews) {
      view.setModel(bean);
    }
  }

  @Override
  public void commitChanges() {
    for (PropertyView view : primitivePropertyViews) {
      view.update();
    }
  }

  @Override
  public void cancelChanges() {
    // no operations
  }

  @Override
  public void setViewEditable(boolean editable) {
    for (PropertyView view : primitivePropertyViews) {
      view.setEditable(editable);
    }
  }

  /**
   * Initializes the form layout of the generic view.
   */
  protected void initialize() {
    FormLayout layout = new FormLayout("right:pref, 3dlu, pref", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    generateRows(builder, Messages.nodeViewSimplePropertiesName,
        primitivePropertyViews);
    generateRows(builder, Messages.nodeViewDataObjectsName, simplePropertyViews);
    generateRows(builder, Messages.nodeViewIndexedPropertiesName,
        indexedPropertyViews);
    generateRows(builder, Messages.nodeViewMappedPropertiesName,
        mappedPropertyViews);
    JPanel panel = builder.getPanel();
    add(panel);
  }

  /**
   * Generates the rows for the form layout of the generic view.
   * @param builder form builder to use
   * @param seperator the human readable seperators for a sub form
   * @param properties properties to display in the form
   */
  protected void generateRows(DefaultFormBuilder builder, String seperator,
      List< ? extends PropertyView> properties) {
    int size = properties.size();
    if (size > 0) {
      builder.appendSeparator(seperator);
      for (int i = 0; i < size; i++) {
        PropertyView view = properties.get(i);
        builder.append(view.getLabel(), view.getComponent());
      }
    }
  }

  /**
   * Retrieves the properties of the data object. The following rules are
   * defined.
   * <ul>
   * <li>Indexed property descriptors are only added if the type of their
   * elements is not a data object type. Indexed properties with data objects
   * are always displayed in the navigator.</li>
   * <li>Mapped property descriptors are only added if the type of their
   * elements is not a data object type. Mapped properties with data objects are
   * always displayed in the navigator.</li>
   * <li>Simple properties are displayed if they contains at most one item or
   * if their elements are not data objects.</li>
   * </ul>
   * @param object data object which properties should be infered
   */
  protected void retrieveProperties(DataObject object) {
    type = object.getClass();
    PropertyDescriptor[] descriptors = PropertyUtils
        .getPropertyDescriptors(object);
    for (int i = 0; i < descriptors.length; i++) {
      PropertyDescriptor descriptor = descriptors[i];
      if (descriptor instanceof IndexedPropertyDescriptor) {
        continue;
      } else if (descriptor instanceof MappedPropertyDescriptor) {
        MappedPropertyView propertyView = new MappedPropertyView(descriptor
            .getName(), descriptor.getPropertyType(), TextUtilities
            .fromCamelCase(descriptor.getDisplayName()));
        mappedPropertyViews.add(propertyView);
      } else {
        if (Arrays.binarySearch(DataObject.SYSTEM_SIMPLE_PROPERTIES, descriptor
            .getName()) < 0) {
          Class clazz = descriptor.getPropertyType();
          if (containsInterface(clazz.getInterfaces(),
              java.util.Collection.class)
              || containsInterface(clazz.getInterfaces(), java.util.Map.class)) {
            // handle indexed or mapped properties with multiple items.
            if (getNode().getNodeType().getRelation(descriptor.getName()) == null) {
              IndexedPropertyView propertyView = new IndexedPropertyView(
                  descriptor.getName(), descriptor.getPropertyType(),
                  TextUtilities.fromCamelCase(descriptor.getDisplayName()));
              indexedPropertyViews.add(propertyView);
            }
          } else {
            // handle simple properties with at most one item.
            if (containsInterface(clazz.getInterfaces(), DataObject.class)) {
              UiNodeType nodeType = getModel().getNodeType().getFactory()
                  .getNodeTypeForBo(clazz);
              if (nodeType != null) {
                // only registered data object types are added
                DataObjectType type = nodeType.getDataObjectType();
                DataObjectPropertyView view = new DataObjectPropertyView(
                    descriptor.getName(), clazz, TextUtilities
                        .fromCamelCase(descriptor.getDisplayName()), type);
                simplePropertyViews.add(view);
              } else {
                if (log.isDebugEnabled()) {
                  log.debug("Data object simple property is not available "
                      + type.getName() + ":" + descriptor.getName());
                }
              }
            } else {
              PrimitivePropertyView view = new PrimitivePropertyView(descriptor
                  .getName(), clazz, TextUtilities.fromCamelCase(descriptor
                  .getDisplayName()));
              primitivePropertyViews.add(view);
            }
          }
        }
      }
    }
  }

  /**
   * Returns true if the given class is contained in the interface list.
   * @param interfaces list of interfaces to check
   * @param clazz search interface class definition
   * @return true if the interface was found otherwise false
   * @pre (interfaces != null) && (clazz != null)
   */
  protected boolean containsInterface(Class[] interfaces, Class clazz) {
    assert (interfaces != null) && (clazz != null);
    for (int i = 0; i < interfaces.length; i++) {
      if (clazz == interfaces[i]) {
        return true;
      }
    }
    return false;
  }
}