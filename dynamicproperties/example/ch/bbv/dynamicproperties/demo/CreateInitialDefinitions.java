package ch.bbv.dynamicproperties.demo;

import java.io.File;
import java.util.Date;
import java.util.Locale;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.EnumerationType;
import ch.bbv.dynamicproperties.core.HierarchicalEnumerationElement;
import ch.bbv.dynamicproperties.core.HierarchicalEnumerationType;
import ch.bbv.dynamicproperties.core.LinearEnumerationType;
import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.core.SimpleValue;
import ch.bbv.dynamicproperties.tool.XMLTools;
import ch.bbv.dynamicproperties.visualization.editor.ComplexEditor;
import ch.bbv.dynamicproperties.visualization.editor.EnumEditor;
import ch.bbv.dynamicproperties.visualization.editor.HierarchicalEnumEditor;
import ch.bbv.dynamicproperties.visualization.editor.IntegerEditor;
import ch.bbv.dynamicproperties.visualization.editor.TableEditor;
import ch.bbv.dynamicproperties.visualization.viewer.ComplexViewer;
import ch.bbv.dynamicproperties.visualization.viewer.EnumViewer;
import ch.bbv.dynamicproperties.visualization.viewer.ListViewer;
import ch.bbv.dynamicproperties.visualization.viewer.StringViewer;

public class CreateInitialDefinitions {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		test2();
	}
	
	public static void test1() {

		PropertyInstanceManager manager = PropertyInstanceManager.instance();

		/**
		 * List Types
		 */
		manager.addType(new ListType("stringList", PropertyInstanceManager.getType(String.class), ListViewer.class,
				TableEditor.class));
		manager.addType(new ListType("intList", PropertyInstanceManager.getType(Integer.class), ListViewer.class,
				TableEditor.class));

		/**
		 * Simple Attributes
		 */
		manager.addProperty(new Property("firstname", PropertyInstanceManager.getType(String.class), "", ""));
		manager.getProperty("firstname").getTranslation().addTranslation(Locale.GERMAN, "Vorname");

		manager.addProperty(new Property("birthday", PropertyInstanceManager.getType(Date.class), "", new Date(
				"15/05/80")));

		manager.addProperty(new Property("size", PropertyInstanceManager.getType(Double.class), "", new Double(0)));
		manager
				.addProperty(new Property("dead", PropertyInstanceManager.getType(Boolean.class), "",
						new Boolean(false)));
		manager.addProperty(new Property("weight", PropertyInstanceManager.getType(Integer.class), "", new Integer(0)));

		/**
		 * Complex Type
		 */
		ComplexType personType = new ComplexType("Person", ComplexViewer.class, ComplexEditor.class);
		personType.addProperty(manager.getProperty("firstname"));
		personType.addProperty(manager.getProperty("birthday"));
		personType.addProperty(manager.getProperty("dead"));
		manager.addType(personType);

		/**
		 * Create Enumeration
		 */
		EnumerationType enumeration = new LinearEnumerationType("FontEnum", EnumViewer.class, EnumEditor.class);
		enumeration.addEnum("Bold", "Bold Font");
		enumeration.addEnum("Normal", "Normal Font");
		enumeration.addEnum("Italic", "Italic Font");
		manager.addType(enumeration);

		/**
		 * Create Hierarchical Enumeration Type
		 */
		EnumerationType hierarchicalEnumeration = new HierarchicalEnumerationType("FilialenEnum", EnumViewer.class,
				HierarchicalEnumEditor.class);

		HierarchicalEnumerationElement countryCH = new HierarchicalEnumerationElement(1, 1, "ch", "Schweiz", true,
				null, null, null);
		HierarchicalEnumerationElement countryDE = new HierarchicalEnumerationElement(2, 2, "de", "Deutschland", true,
				null, null, null);
		hierarchicalEnumeration.addEnum(countryCH);
		hierarchicalEnumeration.addEnum(countryDE);

		HierarchicalEnumerationElement townLucerne = new HierarchicalEnumerationElement(3, 3, "Lucerne",
				"Luzern/Schweiz", true, null, null, null);
		HierarchicalEnumerationElement townBerne = new HierarchicalEnumerationElement(4, 4, "Berne", "Bern/Schweiz",
				true, null, null, null);
		countryCH.addEnumerationElement(townLucerne);
		countryCH.addEnumerationElement(townBerne);

		HierarchicalEnumerationElement qMatthof = new HierarchicalEnumerationElement(13, 3, "Matthof",
				"Quartier in Luzern", true, null, null, null);
		HierarchicalEnumerationElement qMaihof = new HierarchicalEnumerationElement(14, 4, "Maihof",
				"Quartier in Luzern", true, null, null, null);
		townLucerne.addEnumerationElement(qMatthof);
		townLucerne.addEnumerationElement(qMaihof);

		HierarchicalEnumerationElement townBerlin = new HierarchicalEnumerationElement(5, 5, "Berlin",
				"Berlin/Deutschland", true, null, null, null);
		HierarchicalEnumerationElement townHamburg = new HierarchicalEnumerationElement(6, 6, "Hamburg",
				"Hamburg/Deutschland", true, null, null, null);
		countryDE.addEnumerationElement(townBerlin);
		countryDE.addEnumerationElement(townHamburg);

		manager.addProperty(new Property("branchEnum", hierarchicalEnumeration, "", 0));

		/**
		 * Enumeration Attribute
		 */
		manager.addProperty(new Property("Fonts", enumeration, "", 0));

		/**
		 * Complex Attribute
		 */
		manager.addProperty(new Property("customer", manager.getPropertyType("Person"), "", null));

		/**
		 * List Attribute
		 */
		manager.addProperty(new Property("names", manager.getPropertyType("stringList"), "", ""));

		XMLTools.exportAttributeManager(manager, new File(System.getProperty("user.dir") + "/test.xml"));
	}

	public static void test2() {
		SimpleType intType = new SimpleType("int", Integer.class, StringViewer.class, IntegerEditor.class);
		Property pName = new Property("name", PropertyInstanceManager.getType(String.class));
		SimpleValue pNameValue = (SimpleValue)pName.createInstance();
		pNameValue.setValue("Gosling");
		
		PropertyInstanceManager manager = PropertyInstanceManager.instance();
		
		ListType intList = new ListType("intList", manager.getType(Integer.class));
		Property numbersProperty = intList.createProperty("numbers", "", 0);
		ListValue numbers = (ListValue)numbersProperty.createInstance();
		numbers.add(1);
		numbers.add(2);
		for(PropertyValue value:numbers){
			System.out.println(value.getValue());
		}
		
		ComplexType complexType = new ComplexType("contact");
		complexType.addProperty(new Property("name", manager.getType(String.class)));
		complexType.addProperty(new Property("birthdate", manager.getType(Date.class)));
		
		Property contactProperty = complexType.createProperty("myContact", "", null);
		
		ComplexValue myContact = (ComplexValue)contactProperty.createInstance();
		myContact.setValue("name", "Gosling");
		myContact.setValue("birthdate",new Date("15/05/80"));
		
		myContact.getPropertyValue("name");
		myContact.getPropertyValue("birthdate");
		
		
		
	}
}
