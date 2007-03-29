package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

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

	private String description;
	
	/**
	 * fields (properties) contained within this view. They are available after composition of a view is complete.
	 */
	private List<FieldDefinition> fields;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MetaView)) return false;

		MetaView other = (MetaView) o;
		if (!other.getName().equals(this.getName())) return false;

		if (!other.getVisibility().equals(this.getVisibility())) return false;

		if (other.description == null ^ this.description == null) return false;

		if (other.description != null && this.description != null) {
			if (!other.description.equals(this.description)) {
				return false;
			}
		}

		if (other.fields.size() != this.fields.size()) return false;

		for (int i = 0; i < other.fields.size(); i++) {
			if (!other.fields.get(i).equals(this.fields.get(i)))
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
	 * if you want change the name call <code>setName(String s)</code> after
	 * the constructor
	 */
	public MetaView() {
		this("NewView");
	}

	/**
	 * Constructor for a MetaView, which is setting its name.
	 * @param name for the new MetaView.
	 */
	public MetaView(String name) {
		setName(name);
		fields = new ArrayList<FieldDefinition>();
	}

	/**
	 * set the given <code>String</code> as view description
	 * 
	 * @param s
	 *            description of the view as <code>String</code>
	 */
	public void setDescription(String s) {
		super.setDescription(s);
		description = s;
	}

	/**
	 * add the given <code>FieldDefinition</code>
	 * 
	 * @param def
	 *            the attribute and his package as <code>FieldDefinition</code>
	 */
	public void appendField(FieldDefinition def) {
		fields.add(def);
	}

	/**
	 * get all saved attributes of the current view!
	 * <b>This Method is used by the class generator</b>
	 * @return all saved <code>FieldDefinition</code> of the current View
	 * in a <code>List of FieldDefinitions</code> 
	 */
	public List<FieldDefinition> getFields(){
		return fields;
	}
	
	/**
	 * Searches a FieldDefinition with equal properties. If found then that FieldDefinition is returned.
	 * The equal operator is used to determine whether the FieldDefinition match.
	 * @param searchedField to be searched within this MetaView
	 * @return null if not found, otherwise the corresponding FieldDefinition.
	 */
	public FieldDefinition findField(FieldDefinition searchedField) {
		for(FieldDefinition field : fields) {
			if(field.equals(searchedField)) {
				return field;
			}
		}
		return null;
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
			if (this == o) return true;
			if (!(o instanceof FieldDefinition)) return false;
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
		 * @param metaElement is the MetaProperty we want to have within this FieldDefinition and hence we want to add to this MetaView.
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
		 * @return the current field as kind of MetaElement(MetaProperty)
		 */
		public MetaProperty asMetaProperty(){
			return this.metaElement;
		}

	}


	/**
	 * Implements the visitor pattern.
	 * 
	 * @param visitor
	 *            visitor to process.
	 */
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public MetaView clone() {
		MetaView clone = null;
		try {
			clone = (MetaView) super.clone();

			List<FieldDefinition> fieldDefs = new ArrayList<FieldDefinition>(
					this.fields.size() * 2);
			for (FieldDefinition definition : fields) {
				fieldDefs.add(definition.clone());
			}
			clone.fields = fieldDefs;
		
		} catch (CloneNotSupportedException e) {
			// impossible exception because super class implements clonable
			e.printStackTrace();
		}
		return clone;
	}

}
