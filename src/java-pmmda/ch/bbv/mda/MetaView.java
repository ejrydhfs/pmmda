/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 * 
 * History
 * 
 * ?            Bruno Persi       Initial Version
 * 2007/07/10   Ueli Kurmann      Cleanup
 * 
 */

package ch.bbv.mda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.bbv.dog.DataObject;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.ui.ViewConstants;
import ch.persi.xml.XmlNode;

/**
 * this meta element represents an view definition
 * 
 * @author Bruno Persi
 * @author Ueli Kurmann
 * 
 */
public class MetaView extends MetaElement {

  /**
   * serialUID, change this value on each check-in/changing this class
   */
  private static final long serialVersionUID = 7526472295622776147L;

  /**
   * fields (properties) contained within this view. They are available after
   * composition of a view is complete.
   */
  private List<MetaProperty> fields;

  /**
   * List of all inner views contained within this view.
   */
  private List<MetaView> views;
  
  /**
   * List of root objects. Root objects are used to navigate to a property
   */
  private List<Class<? extends MetaClassifier>> rootObjects;

  /**
   * Maps meta property to paths. a path is a "Point" separated string of properties
   */
  private Map<MetaProperty, RootDataObject> paths;
  
  /**
   * creates a new view with default name <code>View</code><br>
   * if you want change the name call <code>setName(String s)</code> after the
   * constructor
   */
  public MetaView() {
    this("NewView");
  }

  /**
   * Constructor for a MetaView, which is setting its name.
   * 
   * @param name
   *          for the new MetaView.
   */
  public MetaView(String name) {
    setName(name);
    TaggedValue author = new TaggedValue("author", "pmMDA");
    this.addTaggedValue(author);
    
    fields = new ArrayList<MetaProperty>();
    views = new ArrayList<MetaView>();
    paths = new HashMap<MetaProperty, RootDataObject>();
    
    /**
     * FIXME: uk: correct? What about multiple views? Move to static constructor?
     */
    List<TagDefinition> taggedValueTypes = new ArrayList<TagDefinition>();
    taggedValueTypes.add(new TagDefinition(ViewConstants.VIEW_TAG, "", String.class));
    
    CartridgeFactory.getInstance().getItemFactory().registerTypes(MetaPackage.class, taggedValueTypes);
    CartridgeFactory.getInstance().getItemFactory().registerTypes(MetaProperty.class, taggedValueTypes);
    CartridgeFactory.getInstance().getItemFactory().registerTypes(MetaIndexedProperty.class, taggedValueTypes);
  }

  /**
   * Implements the visitor pattern.
   * 
   * @param visitor
   *          visitor to process.
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  /**
   * Adds this view to the MetaModel. For every FieldDefinition contained within
   * this view a tagged value entry is made in its corresponding property. If
   * there is already an entry it is updated to the current values of the
   * MetaView.
   */
  public void addToModel() {
    addToPackage(getQualifiedName(), getModel(), (MetaPackage)getContext());
    addToProperties(fields, getQualifiedName(), getModel());
  }

  /**
   * Adds information about this view to package this view belongs to. If there
   * is already an entry then it is updated with current information about this
   * view.
   * 
   * @param qualifiedName
   *          of this view. This is necessary because in case of a new view, it
   *          is not yet set at the time we use this method.
   * @param rootModel
   *          MetaModel this View is belonging to. Necessary to process new
   *          views not having yet a model assigned.
   * @param context MetaPackage this view is belonging to. One cannot obtain the view context, because in case of a new view that context is still null.
   */
  public void addToPackage(String qualifiedName, MetaModel rootModel, MetaPackage context) {
    
    if (context == null){
      return;
    }
    
    TaggedValue taggedValue = context.getTaggedValue(ViewConstants.VIEW_TAG);
    if (taggedValue == null) {
      taggedValue = new TaggedValue(ViewConstants.VIEW_TAG, new XmlNode(
          ViewConstants.VIEW_TAG).asXml());
      context.addTaggedValue(taggedValue);
    }
    
    XmlNode xmlDef = new XmlNode();
    xmlDef.parse(taggedValue.getValue());
    List<XmlNode> nodes = xmlDef.getAllAsList(ViewConstants.VIEW_NODE);
    for (XmlNode viewNode : nodes) {
      if (viewNode.getAttribute(ViewConstants.NAME).equals(qualifiedName)) {
        viewNode.remove();
        break;
      }
    }
    
    XmlNode viewNode = xmlDef.append(ViewConstants.VIEW_NODE);
    viewNode.setAttribute(ViewConstants.NAME, qualifiedName);
    viewNode.setAttribute(ViewConstants.DESCRIPTION, getDescription());
    viewNode.setAttribute(ViewConstants.VISIBILITY, Integer
        .toString(getVisibility().getCode()));
    for (MetaView view : views) {
      XmlNode referenceNode = viewNode.append(ViewConstants.REFERENCE);
      referenceNode.setValue(view.getQualifiedName());
    }
    taggedValue.setValue(xmlDef.asXml());
  }

  /**
   * Adds information about this view to passed properties. If there is already
   * an entry then it is updated with current information about this view.
   * 
   * @param propertiesToBeAdded
   *          list of properties where we want to register this view, meaning
   *          creating an entry in the view tagged value.
   * @param qualifiedName
   *          of this view. This is necessary because in case of a new view, it
   *          is not yet set at the time we use this method.
   * @param rootModel
   *          MetaModel this View is belonging to. Necessary to process new
   *          views not having yet a model assigned.
   */
  public void addToProperties(List<MetaProperty> propertiesToBeAdded,
      String qualifiedName, MetaModel rootModel) {
    for (MetaProperty property : propertiesToBeAdded) {
      property = (MetaProperty) rootModel.findElement(property.getQualifiedName());
      TaggedValue taggedValue = property.getTaggedValue(ViewConstants.VIEW_TAG);
      if (taggedValue == null || taggedValue.getValue().equals("")) {
        taggedValue = new TaggedValue(ViewConstants.VIEW_TAG, new XmlNode(ViewConstants.VIEW_TAG).asXml());
        property.addTaggedValue(taggedValue);
      }
      XmlNode xmlDef = new XmlNode();
      xmlDef.parse(taggedValue.getValue());
      List<XmlNode> nodes = xmlDef.getAllAsList(ViewConstants.VIEW_NODE);
      for (XmlNode viewNode : nodes) {
        if (viewNode.getAttribute(ViewConstants.NAME).equals(qualifiedName)) {
          viewNode.remove();
          break;
        }
      }
      XmlNode viewNode = xmlDef.append(ViewConstants.VIEW_NODE);
      viewNode.setAttribute(ViewConstants.NAME, qualifiedName);
      taggedValue.setValue(xmlDef.asXml());
    }
  }

  /**
   * Adds the view to the list of views at given index.
   * 
   * @param index
   *          position where the view should be inserted in the list
   * @param view
   *          view to add
   * @pre view != null
   * @see #addView(MetaView)
   * @see #getView(int)
   */
  public void addView(int index, MetaView view) {
    assert view != null;
    views.add(index, view);
  }

  /**
   * Adds the view to the list of views. This is more a reference to a view than
   * a containment. An inner view's context will still be its package.
   * 
   * @param view
   *          view to add
   * @pre view != null
   */
  public void addView(MetaView view) {
    assert view != null;
    views.add(view);
  }

  /**
   * add the given <code>FieldDefinition</code>
   * 
   * @param def
   *          the attribute and his package as <code>FieldDefinition</code>
   */
  public void appendField(MetaProperty def) {
    fields.add(def);
  }

  @Override
  public MetaView clone() {
    MetaView clone = null;
    clone = (MetaView) super.clone();

    List<MetaProperty> fieldDefs = new ArrayList<MetaProperty>(
        this.fields.size() * 2);
    for (MetaProperty definition : fields) {
      fieldDefs.add(definition.copy());
    }
    clone.fields = fieldDefs;

    List<MetaView> clonedViews = new ArrayList<MetaView>();
    for (MetaView view : views) {
      clonedViews.add(view);
    }
    clone.views = clonedViews;
    return clone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof MetaView))
      return false;

    MetaView other = (MetaView) o;
    if (!other.getName().equals(this.getName()))
      return false;

    if (!other.getVisibility().equals(this.getVisibility()))
      return false;

    if (other.getDescription() == null ^ this.getDescription() == null)
      return false;

    if (other.getDescription() != null && this.getDescription() != null) {
      if (!other.getDescription().equals(this.getDescription())) {
        return false;
      }
    }

    if (other.getStereotypes().size() != this.getStereotypes().size())
      return false;

    for (int i = 0; i < other.getStereotypes().size(); i++) {
      if (!other.getStereotypes().get(i).equals(this.getStereotypes().get(i)))
        return false;
    }

    if (other.fields.size() != this.fields.size())
      return false;

    for (int i = 0; i < other.fields.size(); i++) {
      if (!other.fields.get(i).equals(this.fields.get(i)))
        return false;
    }

    if (other.views.size() != this.views.size())
      return false;

    for (int i = 0; i < other.views.size(); i++) {
      if (!other.views.get(i).equals(this.views.get(i)))
        return false;
    }

    return true;
  }

  /**
   * Searches a FieldDefinition with equal properties. If found then that
   * FieldDefinition is returned. The equal operator is used to determine
   * whether the FieldDefinition match.
   * 
   * @param searchedField
   *          to be searched within this MetaView
   * @return null if not found, otherwise the corresponding FieldDefinition.
   */
  public MetaProperty findField(MetaProperty searchedField) {
    for (MetaProperty field : fields) {
      if (field.equals(searchedField)) {
        return field;
      }
    }
    return null;
  }

  /**
   * Searches passed view within referenced views of this view. Equality operator is used to determin whether passed view is referenced.
   * @see #getViews()
   * @param searchedView MetaView to be looked up.
   * @return MetaView if found or null otherwise.
   */
  public MetaView findView(MetaView searchedView) {
    for (MetaView view : views) {
      if (view.equals(searchedView)) {
        return view;
      }
    }
    return null;
  }

  /**
   * get all saved attributes of the current view! <b>This Method is used by the
   * class generator</b>
   * 
   * @return all saved <code>FieldDefinition</code> of the current View in a
   *         <code>List of FieldDefinitions</code>
   */
  public List<MetaProperty> getFields() {
    return fields;
  }

  /**
   * Returns the view with the given index.
   * 
   * @param index
   *          index of the requested view
   * @return the requested view
   * @see #addView(int, MetaView)
   */
  public MetaView getView(int index) {
    return views.get(index);
  }

  /**
   * Returns a list of all views defined in the object.
   * 
   * @return list of all views
   * @see #setViews(List)
   */
  public List<MetaView> getViews() {
    return views;
  }

  @Override
  public int hashCode() {
    int hashCode = this.getName() == null ? 0 : this.getName().hashCode();
    hashCode += this.getDescription() == null ? 0 : this.getDescription().hashCode();

    if (fields != null) {
      for (MetaProperty def : fields) {
        hashCode += def.hashCode();
      }
    }
    return hashCode == 0 ? "View".hashCode() : hashCode;
  }

  /**
   * Remove all referenced views from this view.
   */
  public void removeAllViews() {
    views.clear();
  }

  /**
   * Removes this view from all its properties. Entries in the tagged values of
   * its properties will be removed. The properties within the FieldDefinition
   * will remain.
   */
  public void removeFromModel() {
    removeFromPackage(getQualifiedName(), getModel(), (MetaPackage)getContext());
    removeFromProperties(fields, getQualifiedName(), getModel());
  }

  /**
   * removes information about this view from its package.
   * 
   * @param qualifiedName
   *          of this view. This is necessary because in case of a new view, it
   *          is not yet set at the time we use this method.
   * @param rootModel
   *          MetaModel this View is belonging to. Necessary to process new
   *          views not having yet a model assigned.
   * @param context MetaPackage this view is contained in. Necessary because in case of new views the context might not be set yet.
   */
  public void removeFromPackage(String qualifiedName, MetaModel rootModel, MetaPackage context) {
    if (context == null) return;
    TaggedValue taggedValue = context.getTaggedValue(ViewConstants.VIEW_TAG);
    if (taggedValue != null) {
      XmlNode xmlDef = new XmlNode();
      xmlDef.parse(taggedValue.getValue());
      List<XmlNode> nodes = xmlDef.getAllAsList(ViewConstants.VIEW_NODE);
      boolean removedOne = false;
      for (XmlNode viewNode : nodes) {
        if (viewNode.getAttribute(ViewConstants.NAME).equals(qualifiedName)) {
          viewNode.remove();
          removedOne = true;
          break;
        }
      }
      // If it was the only view in this package then the tagged value is
      // not longer required
      if (nodes.size() == 1 && removedOne) {
        context.removeTaggedValue(ViewConstants.VIEW_TAG);
      } else {
        taggedValue.setValue(xmlDef.asXml());
      }
    }
  }

  /**
   * removes information about this view from passed properties.
   * 
   * @param propertiesToBeRemoved
   *          list of FieldDefinitions to remove this view from
   * @param qualifiedName
   *          of this view. This is necessary because in case of a new view, it
   *          is not yet set at the time we use this method.
   * @param rootModel
   *          MetaModel this View is belonging to. Necessary to process new
   *          views not having yet a model assigned.
   */
  public void removeFromProperties(List<MetaProperty> propertiesToBeRemoved,
      String qualifiedName, MetaModel rootModel) {
    // now remove our view content from properties within list
    // (propertiesToBeRemoved)
    for (MetaProperty fieldDefinition : propertiesToBeRemoved) {
      MetaProperty property = (MetaProperty) rootModel
          .findElement(fieldDefinition.getQualifiedName());
      TaggedValue taggedValue = property.getTaggedValue(ViewConstants.VIEW_TAG);
      if (taggedValue != null) {
        XmlNode xmlDef = new XmlNode();
        xmlDef.parse(taggedValue.getValue());
        List<XmlNode> nodes = xmlDef.getAllAsList(ViewConstants.VIEW_NODE);
        boolean removedOne = false;
        for (XmlNode viewNode : nodes) {
          if (viewNode.getAttribute(ViewConstants.NAME).equals(qualifiedName)) {
            viewNode.remove();
            removedOne = true;
            break;
          }
        }
        // if the field are used only in one view, the tagged value is
        // not longer required
        if (nodes.size() == 1 && removedOne) {
          property.removeTaggedValue(ViewConstants.VIEW_TAG);
        } else {
          taggedValue.setValue(xmlDef.asXml());
        }
      }
    }
  }

  /**
   * Removes a referenced view from this view.
   * 
   * @param view
   *          to be removed
   */
  public void removeView(MetaView view) {
    if (views.contains(view)) {
      views.remove(view);
    }
  }

  /**
   * Sets the list of views of the view.
   * 
   * @param views
   *          new list of views
   * @see #addView(MetaView)
   * @see #getViews()
   */
  public void setViews(List<MetaView> views) {
    removeAllViews();
    this.views.addAll(views);
  }
  
  
  public class RootDataObject{
    private Class<? extends DataObject> rootObjectType;
    private String alias;
    
    public Class<? extends DataObject> getRootObjectType(){
      return rootObjectType;
    }
    
    public void setRootObjectType(Class<? extends DataObject> rootObjectType){
      this.rootObjectType = rootObjectType;
    }
    
    public String getAlias(){
      return alias;
    }
    
    public void setAlias(String alias){
      this.alias = alias;
    } 
  }
}
  
