package ch.bbv.dynamicproperties.demo.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.tool.XMLTools;

public class Hibernate {
	public static void main(String[] args) {
		Hibernate mgr = new Hibernate();
		mgr.createAndStoreEvent();
		HibernateUtils.getSessionFactory().close();
	}

	private void createAndStoreEvent() {
		Session session = HibernateUtils.getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();

		/**
		 * Load the XML file containing types and attributes
		 */
		PropertyInstanceManager manager = XMLTools.importAttributeManager(new File(
				System.getProperty("user.dir") + "/test.xml"));

		/**
		 * Create some attribute values
		 */
		PropertyValue stringValue = manager.getProperty("firstname")
				.createInstance();
		stringValue.setValue("Ueli");
		manager.getProperty("firstname").getTranslation().addTranslation("DE",
				"Vorname");

		PropertyValue intValue = manager.getProperty("weight").createInstance();
		intValue.setValue(72);

		PropertyValue dataValue = manager.getProperty("birthday")
				.createInstance();
		dataValue.setValue(new Date("1980/04/15"));

		PropertyValue booleanValue = manager.getProperty("dead")
				.createInstance();
		booleanValue.setValue(false);

		PropertyValue doubleValue = manager.getProperty("size")
				.createInstance();
		doubleValue.setValue(1.83);

		ListValue simpleList = (ListValue) manager.getProperty("names")
				.createInstance();
		for(int i=0;i<10000;i++){
			simpleList.add(simpleList.createListElement(""+System.currentTimeMillis()));
		}
		
		ComplexValue complexValue = (ComplexValue) manager.getProperty(
				"customer").createInstance();

		/**
		 * change a list element
		 */
		simpleList.get(1).setValue("Ueli");

		/**
		 * usage of complex attribute values
		 */
		complexValue.getPropertyValue("firstname").setValue("Ueli Kurmann");
		complexValue.getPropertyValue("birthday").getValue();

		/**
		 * Variante
		 */
		complexValue.setValue("birthday", new Date("1970/1/1"));

		/**
		 * Enumeration
		 */
		PropertyValue fontEnumeration = manager.getProperty("Fonts")
				.createInstance();
		fontEnumeration.setValue("Bold");

		PropertyValue branchEnumeration = manager.getProperty("branchEnum")
				.createInstance();
		branchEnumeration.setValue(new Integer(1));

		/**
		 * Collect attributes in a list
		 */
		List<PropertyValue> values = new ArrayList<PropertyValue>();

		values.add(stringValue);
		values.add(intValue);
		values.add(dataValue);
		values.add(booleanValue);
		values.add(doubleValue);
		values.add(simpleList);
		values.add(complexValue);
		values.add(fontEnumeration);
		values.add(branchEnumeration);

		for (PropertyValue value : values) {
			session.save(value);
		}

		session.getTransaction().commit();

		session = HibernateUtils.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		// Criteria criteria = session.createCriteria(Multilingualism.class);
		for (Object o : session.createCriteria(PropertyValue.class).list()) {
			System.out.println(((PropertyValue) o).getValue());
			System.out.println("+++++++++++++++++++++++++++++++");
		}

		session.getTransaction().commit();
	}
}
