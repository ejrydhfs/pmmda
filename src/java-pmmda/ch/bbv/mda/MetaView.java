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

	private List<FieldDefinition> fields;
	
	private List<FieldDefinition> savedFields;

	public boolean hasMoreFields(List<FieldDefinition> defs) {
		return this.fields.size() > defs.size();
	}

	public boolean hasOtherFields(List<FieldDefinition> defs) {
		for (int i = 0; i < fields.size(); i++) {
			FieldDefinition cur = fields.get(i);
			if (!defs.contains(cur)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the delta between the fields of the current instance and the
	 * input parameter defs. The return list contains all elements which are in
	 * the current list but not in the list given by argument.
	 * @param defs list of field definitions to determine the difference to
	 * @return List of field definitions not passed by defs parameter
	 */
	public List<FieldDefinition> getDelta(List<FieldDefinition> defs) {
		if (defs == null) return this.fields;
		List<FieldDefinition> delta = new ArrayList<FieldDefinition>();
		for (FieldDefinition definition : this.fields) {
			if (!defs.contains(definition)) {
				delta.add(definition);
			}
		}
		return delta;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MetaView)) return false;

		MetaView other = (MetaView) o;
		if (!other.getName().equals(this.getName())) return false;

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
	 * its strong required call this method after each view creation<br>
	 * the method will be clear the internal buffer of selected attributes,
	 * which are used by the view
	 */
	public void clearFields() {
		savedFields = new ArrayList<FieldDefinition>(fields.size());
		for (FieldDefinition current : fields) {
			savedFields.add(current.clone());
		}
		fields.clear();
	}
	
	/**
	 * get all saved attributes of the current view!
	 * <b>This Method is used by the class generator</b>
	 * @return all saved <code>FieldDefinition</code> of the current View
	 * in a <code>List of FieldDefinitions</code> 
	 */
	public List<FieldDefinition> getFields(){
		return this.savedFields == null ? this.fields : this.savedFields;
	}
	
	/**
	 * get all selected attributes of the current view.
	 * This method is for internal use in MetaViewView, internal
	 * we can't work with getFields() because of refreshing problems
	 * in the viewtree.
	 * @return all <code>FieldDefinition</code> in a
	 *         <code>List of FieldDefinitions</code>
	 */
	public List<FieldDefinition> getFieldBuffer() {
		return fields;
	}

	/**
	 * this class capsulate easy creation and access the full qualified names of
	 * the selected view attributes<br>
	 * One <code>FieldDefintion</code> represents on attribute and whose
	 * class/package
	 */
	public static class FieldDefinition {

		private StringBuilder builder;

		private String attribute;

		private String pack;

		private String clazz;
		
		private MetaProperty metaElement;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof FieldDefinition)) return false;
			FieldDefinition other = (FieldDefinition) o;
			return other.attribute.equals(this.attribute) && other.pack.equals(this.pack) && other.clazz.equals(clazz);
		}

		@Override
		public int hashCode() {
			return attribute.hashCode() + pack.hashCode() + clazz.hashCode();
		}

		/**
		 * create a new <code>FieldDefinition</code> for given attribute
		 * 
		 * @param attribute
		 *            the selected attribute as <code>String</code>
		 * @param pack
		 *            the package and class of the attribute as
		 *            <code>String</code>
		 * @param clazz
		 * 			  the class name as <code>String</code>
		 * 
		 * @param metaElement this attribute contains the all tagged values and is used by the
		 * view generator.
		 */
		public FieldDefinition(String attribute, String pack, String clazz, MetaProperty metaElement) {
			this.attribute = attribute;
			this.pack = pack;
			this.clazz = clazz;
			this.metaElement = metaElement;
			builder = new StringBuilder(100);
			builder.append(pack).append(clazz).append(".").append(attribute);
		}

		/**
		 * getting the attribute represents this <code>FieldDefinition</code>
		 * 
		 * @return the attribute name as <code>String</code>
		 */
		public String getAttribute() {
			return this.attribute;
		}

		/**
		 * getting the class/package of the attribute represents this
		 * <code>FieldDefinition</code>
		 * 
		 * @return the package with class name as <code>String</code>
		 */
		public String getPackage() {
			return this.pack;
		}

		public String getClassName() {
			return this.clazz;
		}

		/**
		 * returns getFullQualifiedName()
		 * 
		 * @see getFullQualifiedName()
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
			return builder.toString();
		}

		@Override
		public FieldDefinition clone() {
			FieldDefinition clone = new FieldDefinition(this.attribute,this.pack, this.clazz, this.metaElement.copy());
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
