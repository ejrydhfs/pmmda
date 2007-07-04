/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.mdr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.jmi.model.AggregationKindEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;

import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaDatatype;
import ch.bbv.mda.MetaElement;
import ch.bbv.mda.MetaIndexedProperty;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.Multiplicity;
import ch.bbv.mda.Stereotype;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.UmlConstants;
import ch.bbv.mda.operators.MdaUtilities;
import ch.bbv.mda.persistence.ExternalTransformer;
import ch.bbv.mda.persistence.PersistenceException;

public class Mdr2MdaTransformer extends ExternalTransformer {
	/**
	 * Logger of the class.
	 */
	private static Log log = LogFactory.getLog(Mdr2MdaTransformer.class);

	/**
	 * Maps between the source objects used as keys and the target objects used
	 * as values. Source objects are wrapped MDR entities, target objects are
	 * MDA instances.
	 */
	private Map<Object, MetaElement> elements;

	private MdrUmlModel sourceModel;

	/**
	 * Default constructor of the class.
	 */
	public Mdr2MdaTransformer() {
		elements = new HashMap<Object, MetaElement>();
	}

	/**
	 * Loads the provided file, opens the file containing the XMI model and
	 * creates a MDR model.
	 * 
	 * @param file
	 *            file containing the MDR model.
	 * @return the MDR model defined in the file.
	 * @throws FileNotFoundException
	 *             the given file could not be found.
	 * @throws IOException
	 *             an error occured when reading data.
	 */
	public UmlPackage loadArgoModel(File file) throws FileNotFoundException,
			IOException {
		InputStream source = null;
		ZipInputStream input = null;
		FileInputStream inputStream = new FileInputStream(file);
		if (!file.getName().endsWith(".xmi")) {
			input = new ZipInputStream(inputStream);
			String name = input.getNextEntry().getName();
			while (!name.endsWith(".xmi")) {
				name = input.getNextEntry().getName();
			}
			source = new BufferedInputStream(input);

		} else {
			source = new BufferedInputStream(new FileInputStream(file));
		}

		MdrLoader mdrLoader = new MdrLoader();
		UmlPackage uml = null;
		try {
			mdrLoader.init();
			uml = mdrLoader.read(source);

		} catch (PersistenceException e) {
			e.printStackTrace();
		}

		return uml;
	}

	/**
	 * Synchronize the MDA model with the MDR model.
	 * 
	 * @param source
	 *            MDR model used as source of the synchronization.
	 * @param target
	 *            MDA model to synchronize.
	 */
	public void synchronize(MdrUmlModel source, MetaModel target) {
		this.sourceModel = source;
		target.setName(source.getName());
		extractDatatypes(source, target);
		extractStereotypes(source, target);

		// extract packages
		for (MdrUmlPackage mdrUmlPackage : source.getPackages()) {
			// process root package specially
			if (source.getName().equals(mdrUmlPackage.getName())) {
				for (MdrUmlClass mdrUmlClass : mdrUmlPackage.getClasses()) {
					extractClass(mdrUmlClass, target);
				}
			} else {
				extractPackage(mdrUmlPackage, target);
			}
		}
		extractRelations(target, source);
		postProcessModel(target);
	}

	/**
	 * Extracts the information from the MDR package and overwrites the ones of
	 * the MDA package defined in the given context.
	 * 
	 * @param source
	 *            MDR package used as source of the synchronization.
	 * @param context
	 *            MDA context containing the package to synchronize.
	 */
	protected void extractPackage(MdrUmlPackage source, MetaModel context) {
		String qualifiedName = source.getQualifiedName();

		if (source.getQualifiedName().indexOf(".") == 0) {
			return;
		}

		MetaPackage metaPackage = context.findPackage(qualifiedName);
		if (metaPackage == null) {
			metaPackage = new MetaPackage();

			// TODO: hack beseitigen... (prefix [modelname] vom qualified name)

			if (qualifiedName.length() == 0) {
				return;
			}

			int i = sourceModel.getName().length() + 1;

			if (qualifiedName.length() >= i) {
				qualifiedName = qualifiedName.substring(i);
			}

			// TODO: end hack...

			metaPackage.setName(qualifiedName);
			context.addPackage(metaPackage);
		}

		// extract sterotypes and tagged values
		extractStereotypeAndTags(source, metaPackage);

		// process owned packages
		for (MdrUmlPackage mSubPackage : source.getPackages()) {
			extractPackage(mSubPackage, context);
		}

		// process owned classes
		for (MdrUmlClass mClass : source.getClasses()) {
			extractClass(mClass, metaPackage);
		}
	}

	/**
	 * Extracts the information from the MDR class and overwrites the ones of
	 * the MDA class defined in the given context.
	 * 
	 * @param source
	 *            MDR class used as source of the synchronization.
	 * @param context
	 *            MDA context containing the class to synchronize.
	 */
	protected void extractClass(MdrUmlClass source, MetaPackage context) {
		assert (source != null) && (context != null);
		String name = source.getName();
		if (name == null) {
			if (log.isErrorEnabled()) {
				log.error("Parsed class without name in package "
						+ context.getName());
			}
			return;
		}
		MetaClass metaClass = context.getClass(name);
		if (metaClass == null) {
			metaClass = new MetaClass();
			metaClass.setName(name);
			context.addClass(metaClass);
		}

		elements.put(source.getModelElement(), metaClass);
		metaClass.setAbstract(source.isAbstract());
		metaClass.setFinal(source.isFinal());
		metaClass.setStatic(source.isStatic());

		extractStereotypeAndTags(source, metaClass);

		// set interface
		metaClass.setInterface(source.isInterface());

		// process attributes
		for (MdrUmlAttribute attribute : source.getAttributes()) {
			extractProperty(attribute, metaClass);
		}

		// process inner classes
		for (MdrUmlClass innerClass : source.getInnerClasses()) {
			extractNestedClass(innerClass, metaClass);
		}
	}

	/**
	 * Extracts the information from the MDR class and overwrites the ones of
	 * the MDA nested classes defined in the given context.
	 * 
	 * @param source
	 *            MDR class used as source of the synchronization.
	 * @param context
	 *            MDA context containing the class to synchronize.
	 */
	protected void extractNestedClass(MdrUmlClass source, MetaClass context) {
		assert (source != null) && (context != null);

		String name = source.getName();
		if (name == null) {
			if (log.isErrorEnabled()) {
				log.error("Parsed nested class without name in class "
						+ context.getName());
			}
			return;
		}

		MetaClass metaNestedClass = context.getNestedClass(name);

		if (metaNestedClass == null) {
			metaNestedClass = new MetaClass();
			metaNestedClass.setName(name);
			context.addNestedClass(metaNestedClass);
		}

		elements.put(source.getModelElement(), metaNestedClass);
		metaNestedClass.setAbstract(source.isAbstract());
		extractStereotypeAndTags(source, metaNestedClass);

		if (source.isInterface()) {
			metaNestedClass.setInterface(true);
		}

		// attributes
		for (MdrUmlAttribute attribute : source.getAttributes()) {
			extractProperty(attribute, metaNestedClass);
		}

		// process inner classes
		for (MdrUmlClass innerClass : source.getInnerClasses()) {
			extractNestedClass(innerClass, metaNestedClass);
		}
	}

	/**
	 * Extracts the property from the attribute and synchronizes the target.
	 * 
	 * @param attribute
	 *            attribute containing the data.
	 * @param context
	 *            context in which the property will be updated.
	 */
	private void extractProperty(MdrUmlAttribute attribute, MetaClass context) {
		String name = attribute.getName();
		Multiplicity mMultiplicity = attribute.getMultiplicity();
		MetaProperty property;
		if (mMultiplicity.getUpperBound() == 1) {
			property = new MetaProperty();
			context.addProperty(property);
		} else {
			MetaIndexedProperty metaIndexedProperty = new MetaIndexedProperty();
			property = metaIndexedProperty;
			context.addIndexedProperty(metaIndexedProperty);
		}
		property.setName(name);

		if (attribute.getInitialValue() == null) {
			property.setInitialValue(attribute.getInitialValue());
		}

		if (attribute.getType().getQualifiedName() != null) {
			property.setTypeName(attribute.getType().getQualifiedName());
		} else {
			log.info("Attribute '" + attribute.getName() + "' of class '"
					+ context.getName()
					+ "' has no type associated (set to datatype integer)");
			property.setTypeName("int");
		}

		Multiplicity multiplicity = attribute.getMultiplicity();
		if (multiplicity != null) {
			property.setMultiplicity(multiplicity);
		}
		extractStereotypeAndTags(attribute, property);
	}

	/**
	 * Extracts tags and stereotypes from the MDR element and add them to the
	 * meta element. The visibility of the item is also set.
	 * 
	 * @param source
	 *            MDR element to parse
	 * @param item
	 *            meta item to synchronize
	 */
	private void extractStereotypeAndTags(MdrUmlElement source, MetaElement item) {
		for (MdrUmlStereotype mStereotype : source.getStereotypes()) {
			if (mStereotype != null) {
				MdaUtilities.addStereotype(item, mStereotype.getName());
			}
		}
		item.setVisibility(source.getVisibility());
		for (MdrTaggedValue tag : source.getTaggedValues()) {
			String tagName = tag.getName();
			if (item instanceof MetaProperty) {
				MetaProperty property = (MetaProperty) item;
				if ("transient".equals(tagName)) {
					property.setTransient(booleanValue(tag.getValue()));
				} else if ("volatile".equals(tagName)) {
					property.setVolatile(booleanValue(tag.getValue()));
				}
			}
			if (UmlConstants.DOCUMENTATION_TAG.equals(tagName)) {
				item.setDescription(tag.getValue());
			} else {
				if (tagName == null) {
					log.warn("Tagged Value has no name and has value."
							+ tag.getValue());
					continue;
				}
				TaggedValue taggedValue = item.getTaggedValue(tagName);
				if (taggedValue == null) {
					taggedValue = new TaggedValue(tag.getName(), tag.getValue());
					item.addTaggedValue(taggedValue);
				} else {
					taggedValue.setValue(tag.getValue());
				}
			}
		}
	}

	/**
	 * Extracts all inheritance, implement, composition and aggregation
	 * relations defined in the given package and its children. Generalizations
	 * are inheritance relations and abstractions are implement relations in the
	 * MDR terminology. The relations are mapped to the model of modern
	 * programming languages. Aggregations with a upper cardinality of 1 are
	 * mapped to simple properties, all others are mapped to indexed properties.
	 * 
	 * @param target
	 *            meta model to complete with the defined relations.
	 * @param source
	 *            package, which relations should be extracted.
	 * @pre (target != null) && (mPackage != null)
	 */

	private void extractRelations(MetaModel target, MdrUmlModel source) {
		assert (target != null) && (source != null);

		for (MdrUmlGeneralization generalization : source.getGeneralizations()) {
			MetaClass superClass = (MetaClass) elements.get(generalization
					.getParent());
			MetaClass metaClass = (MetaClass) elements.get(generalization
					.getChild());

			if ((superClass != null) && (metaClass != null)) {
				metaClass.addAncestor(superClass);
			}
		}

		for (MdrUmlAbstraction abstraction : source.getAbstractions()) {
			if ((abstraction.getSuppliers().size() == 0)
					|| (abstraction.getClients().size() == 0)) {
				if (log.isErrorEnabled()) {
					log.error("Association (MAbstraction) without end "
							+ abstraction.getName() + " in package "
							+ source.getName());
				}
				continue;
			}
			ModelElement supplier = (ModelElement) abstraction.getSuppliers()
					.iterator().next();
			ModelElement client = (ModelElement) abstraction.getClients()
					.iterator().next();
			MetaClass metaClass = (MetaClass) elements.get(client);
			MetaClass superClass = (MetaClass) elements.get(supplier);
			if ((superClass != null) && (metaClass != null)) {
				metaClass.addAncestor(superClass);
			}
		}

		for (MdrUmlAssociation association : source.getAssociations()) {
			AssociationEnd e1 = association.getConnection(0);
			AssociationEnd e2 = association.getConnection(1);
			if ((e1 == null) || (e2 == null)) {
				if (log.isErrorEnabled()) {
					log.error("Association (UmlAssociation) without end(s) "
							+ association.getName() + " in package "
							+ source.getName());
				}
				continue;
			}

			MetaClass owner = (MetaClass) elements.get(e1.getParticipant());
			MetaClass owned = (MetaClass) elements.get(e2.getParticipant());

			String name = e2.getName();
			if ((name == null) || (name.length() == 0)) {
				name = association.getName();
			}
			if (owner == null) {
				if (log.isErrorEnabled()) {
					log.error("No owner for association "
							+ association.getName() + " in package "
							+ source.getName());
				}
				continue;
			}

			Multiplicity multiplicity = MdrUtilities.convertMultiplicity(e2
					.getMultiplicity());
			MetaProperty property = null;
			if (((multiplicity != null) && (multiplicity.getUpperBound() == Multiplicity.N_MULTIPLICITY))
					|| (multiplicity == null)) {
				property = new MetaIndexedProperty();
				owner.addIndexedProperty((MetaIndexedProperty) property);
			} else {
				property = new MetaProperty();
				owner.addProperty(property);
			}
			property.setName(name);
			if (owned != null) {
				property.setTypeName(owned.getQualifiedName());
			}

			// TODO: really shared?
			if (e1.getAggregation().toString().equals(
					AggregationKindEnum.SHARED.toString())) {
				property.setAggregation(true);
			}
			if (e1.getAggregation().toString().equals(
					AggregationKindEnum.COMPOSITE.toString())) {
				property.setComposition(true);
			}

			extractStereotypeAndTags(association, property);

			if (multiplicity != null) {
				property.setMultiplicity(multiplicity);
			}

			org.omg.uml.foundation.core.TaggedValue tag = MdrUtilities.getTag(
					association.getModelElement(), "transient");
			if (tag != null) {
				property.setTransient(booleanValue(tag.getDataValue()
						.iterator().next().toString()));
			}
			tag = MdrUtilities
					.getTag(association.getModelElement(), "volatile");
			if (tag != null) {
				property.setVolatile(booleanValue(tag.getDataValue().iterator()
						.next().toString()));
			}
		}

	}

	/**
	 * Helper method to extract the datatypes out of the model.
	 * 
	 * @param source
	 *            the MDR source model
	 * @param target
	 *            the PMMDA target model
	 */
	private void extractDatatypes(MdrUmlModel source, MetaModel target) {
		for (MdrUmlDataType mDatatype : source.getDataTypes()) {
			String name = mDatatype.getName();
			if (target.findDatatype(name) == null) {
				MetaDatatype datatype = new MetaDatatype();
				datatype.setName(mDatatype.getName());
				extractStereotypeAndTags(mDatatype, datatype);
				target.addDatatype(datatype);
			}
		}
	}

	/**
	 * Helper method to extract the stereotypes out of the model.
	 * 
	 * @param source
	 *            the MDR source model
	 * @param target
	 *            the PMMDA target model
	 */
	private void extractStereotypes(MdrUmlModel source, MetaModel target) {
		for (MdrUmlStereotype mStereotype : source.getStereotypes()) {
			String name = mStereotype.getName();
			if ((name != null) && (name.trim().length() > 0)) {
				Stereotype stereotype = target.findStereotype(name);
				if (stereotype == null) {
					stereotype = new Stereotype();
					stereotype.setName(name);
					target.addStereotype(stereotype);
				}
			}
		}
	}
}