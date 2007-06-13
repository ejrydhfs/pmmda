package ch.bbv.dynamicproperties.demo.hibernate;

import java.io.File;
import java.util.Date;
import java.util.Random;

import org.hibernate.Session;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.tool.XMLTools;

public class LotOfDynamicProperties {
	public static void main(String[] args) {
		LotOfDynamicProperties mgr = new LotOfDynamicProperties();
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
		
		
		Random random = new Random();
		
		for(int i = 0; i< 10; i++){
			Container container = new Container();
			
			// simple properties (5-25 simple string properties)
			int numOf =  random.nextInt(20)+5;
			for(int simpleProperties = 0; simpleProperties < numOf; simpleProperties++){
				Property property = new Property(String.valueOf(System.currentTimeMillis()), manager.getType(String.class), "","");
				PropertyValue value = property.createInstance();
				value.setValue(String.valueOf(System.currentTimeMillis()));
				container.add(value);
			}
			
			// list properties (5-25 list properties)
			numOf =  random.nextInt(20)+5;
			for(int listProperties = 0; listProperties < numOf; listProperties++){
				Property property = new Property(String.valueOf(System.currentTimeMillis()), manager.getPropertyType("intList"), "","");
				ListValue list = (ListValue)property.createInstance();
				int numOfListElements =  random.nextInt(50)+5;
				for(int j = 0; j < numOfListElements; j++){
					list.add(list.createListElement(random.nextInt(8192)));
				}
				container.add(list);
			}
			
			// complex properties
			numOf =  random.nextInt(20)+5;
			for(int listProperties = 0; listProperties < numOf; listProperties++){
				ComplexValue complexValue = (ComplexValue) manager.getProperty("customer").createInstance();
				complexValue.getPropertyValue("firstname").setValue("Ueli Kurmann");
				complexValue.setValue("birthday", new Date("1970/1/1"));
				container.add(complexValue);
			}
			session.save(container);
		}
		session.getTransaction().commit();
	}
}
