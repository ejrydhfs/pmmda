package ch.bbv.dynamicproperties.demo.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.tool.XMLTools;

public class DynamicPropertiesTest {
	public static void main(String[] args) {
		DynamicPropertiesTest mgr = new DynamicPropertiesTest();
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
		
		
		int numOf =  20;
		List<Property> properties = new ArrayList<Property>();
		for(int i = 0; i < numOf; i++){
			Property property = new Property("field_"+i, PropertyInstanceManager.getType(String.class), "","");
			properties.add(property);
		}
		
		/**
		 * number of rows
		 */
		for(int i = 0; i< 10000; i++){
			Container container = new Container();
			for(Property property:properties){
				PropertyValue value = property.createInstance();
				value.setValue(String.valueOf(System.currentTimeMillis()));
				container.add(value);
			}
			session.save(container);
		}
		session.getTransaction().commit();
	}
}
