package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.ui.ViewConstants;
import ch.persi.xml.XmlNode;

/**
 * this meta element represents an view definition
 * 
 * @author Bruno Persi
 * 
 */
public class MetaView extends MetaElement {

	/**
	 * serialUID, change this value on each check-in/changeing this class
	 */
	private static final long serialVersionUID = 7526472295622776147L;

	/**
	 * Description of this view.
	 */
	private String description;

	/**
	 * fields (properties) contained within this view. They are available after
	 * composition of a view is complete.
	 */
	private List<FieldDefinition> fields;

	/**
	 * List of all inner views contained within this view.
	 */
	private List<MetaView> views;

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

		if (other.description == null ^ this.description == null)
			return false;

		if (other.description != null && this.description != null) {
			if (!other.description.equals(this.description)) {
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

	@Override
	public int hashCode() {
		int hashCode = this.getName() == null ? 0 : this.getName().hashCode();
		hashCode += this.description == null ? 0 : this.description.hashCode();

		if (fields != null) {
			for (FieldDefinition def : fields) {
				hashCode += def.hashCode();
			}
		}
		return hashCode == 0 ? "View".hashCode() : hashCode;
	}

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
		fields = new ArrayList<FieldDefinition>();
		TaggedValue author = new TaggedValue("author", "pmMDA");
		this.addTaggedValue(author);
		views = new ArrayList<MetaView>();
		List<TagDefinition> taggedValueTypes = new ArrayList<TagDefinition>();
		taggedValueTypes.add(new TagDefinition(ViewConstants.VIEW_TAG, "", String.class));
		CartridgeFactory.getInstance().getItemFactory().registerTypes(MetaPackage.class, taggedValueTypes);
		CartridgeFactory.getInstance().getItemFactory().registerTypes(MetaProperty.class, taggedValueTypes);
		CartridgeFactory.getInstance().getItemFactory().registerTypes(MetaIndexedProperty.class, taggedValueTypes);
	}

	/**
	 * set the given <code>String</code> as view description
	 * 
	 * @param s
	 *          description of the view as <code>String</code>
	 */
	public void setDescription(String s) {
		super.setDescription(s);
		description = s;
	}

	/**
	 * add the given <code>FieldDefinition</code>
	 * 
	 * @param def
	 *          the attribute and his package as <code>FieldDefinition</code>
	 */
	public void appendField(FieldDefinition def) {
		fields.add(def);
	}

	/**
	 * get all saved attributes of the current view! <b>This Method is used by the
	 * class generator</b>
	 * 
	 * @return all saved <code>FieldDefinition</code> of the current View in a
	 *         <code>List of FieldDefinitions</code>
	 */
	public List<FieldDefinition> getFields() {
		return fields;
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
	public FieldDefinition findField(FieldDefinition searchedField) {
		for (FieldDefinition field : fields) {
			if (field.equals(searchedField)) {
				return field;
			}
		}
		return null;
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
	 * Returns a list of all views defined in the object.
	 * 
	 * @return list of all views
	 * @see #setViews(List)
	 */
	public List<MetaView> getViews() {
		return views;
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

	/**
	 * Remove all referenced views from this view.
	 */
	public void removeAllViews() {
		views.clear();
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
	 * Removes this view from all its properties. Entires in the tagged values of
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
	public void removeFromProperties(List<FieldDefinition> propertiesToBeRemoved,
			String qualifiedName, MetaModel rootModel) {
		// now remove our view content from properties within list
		// (propertiesToBeRemoved)
		for (FieldDefinition fieldDefinition : propertiesToBeRemoved) {
			MetaProperty property = (MetaProperty) rootModel
					.findElement(fieldDefinition.asMetaProperty().getQualifiedName());
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
	 * Adds this view to the MetaModel. For every FieldDefinition contained within
	 * this view a tagged value entrie is made in its corresponding property. If
	 * there is already an entry it is updated to the current vlaues of the
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
		if (context == null)
			return;
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
	public void addToProperties(List<FieldDefinition> propertiesToBeAdded,
			String qualifiedName, MetaModel rootModel) {
		for (MetaView.FieldDefinition newField : propertiesToBeAdded) {
			MetaProperty property = newField.asMetaProperty();
			property = (MetaProperty) rootModel.findElement(property
					.getQualifiedName());
			TaggedValue taggedValue = property.getTaggedValue(ViewConstants.VIEW_TAG);
			if (taggedValue == null) {
				taggedValue = new TaggedValue(ViewConstants.VIEW_TAG, new XmlNode(
						ViewConstants.VIEW_TAG).asXml());
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
	 * this class capsulate easy creation and access the full qualified names of
	 * the selected view attributes<br>
	 * One <code>FieldDefintion</code> represents on attribute and whose
	 * class/package
	 */
	public static class FieldDefinition {

		private MetaProperty metaElement;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof FieldDefinition))
				return false;
			FieldDefinition other = (FieldDefinition) o;
			return other.metaElement.equals(this.metaElement);
		}

		@Override
		public int hashCode() {
			return metaElement.hashCode();
		}

		/**
		 * create a new <code>FieldDefinition</code> for given attribute
		 * 
		 * @param metaElement
		 *          is the MetaProperty we want to have within this FieldDefinition
		 *          and hence we want to add to this MetaView.
		 */

		public FieldDefinition(MetaProperty metaElement) {
			this.metaElement = metaElement;
		}

		/**
		 * returns getFullyQualifiedName()
		 * 
		 * @see #getFullyQualifiedName()
		 */
		@Override
		public String toString() {
			return getFullyQualifiedName();
		}

		/**
		 * getting the fully qualified attribute name
		 * 
		 * @return the fully qualified attribute name as <code>String</code>
		 */
		public String getFullyQualifiedName() {
			return metaElement.getQualifiedName();
		}

		@Override
		public FieldDefinition clone() {
			FieldDefinition clone = new FieldDefinition(this.metaElement.copy());
			return clone;
		}

		/**
		 * returns the current Field as MetaElement
		 * 
		 * @return the current field as kind of MetaElement(MetaProperty)
		 */
		public MetaProperty asMetaProperty() {
			return this.metaElement;
		}

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

	@Override
	public MetaView clone() {
		MetaView clone = null;
		clone = (MetaView) super.clone();

		List<FieldDefinition> fieldDefs = new ArrayList<FieldDefinition>(
				this.fields.size() * 2);
		for (FieldDefinition definition : fields) {
			fieldDefs.add(definition.clone());
		}
		clone.fields = fieldDefs;

		List<MetaView> clonedViews = new ArrayList<MetaView>();
		for (MetaView view : views) {
			clonedViews.add(view);
		}
		clone.views = clonedViews;
		return clone;
	}

}
