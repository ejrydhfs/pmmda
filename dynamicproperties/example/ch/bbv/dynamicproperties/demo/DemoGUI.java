package ch.bbv.dynamicproperties.demo;

import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.tool.XMLTools;
import ch.bbv.dynamicproperties.visualization.tool.SwingTools;

public class DemoGUI {

	public DemoGUI() {
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {

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
		simpleList.add(simpleList.createListElement("Adam"));
		simpleList.add(simpleList.createListElement("Eva"));

		ComplexValue complexValue = (ComplexValue) manager.getProperty(
				"customer").createInstance();

		/**
		 * iterate over the list
		 */
		for (PropertyValue value : simpleList) {
			System.out.println(value.getValue());
		}

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

		/**
		 * Display attributes
		 */
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		frame.setVisible(true);
		frame.add(SwingTools.createViewerPanel(values), SwingTools
				.createConstraints(0, 0));
		frame.add(new JLabel(" "), SwingTools.createConstraints(0, 1));
		frame.add(SwingTools.createEditorPanel(values), SwingTools
				.createConstraints(0, 2));
		frame.pack();
	}

	public static void main(String[] args) {
		new DemoGUI();
	}
}
