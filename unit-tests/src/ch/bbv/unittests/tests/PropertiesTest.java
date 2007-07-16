package ch.bbv.unittests.tests;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import ch.bbv.application.Application;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectId;
import ch.bbv.unittests.props.*;

public class PropertiesTest {

	@Test
	public void test() {
    	Log log = LogFactory.getLog(PropertiesTest.class);

    	log.info("*** Properties Test ***");
        log.info("    Creating a few test objects");
        
        SimpleAttributes simpleAttributes = new SimpleAttributes();
        simpleAttributes.setBooleanAttribute(true);
        simpleAttributes.setDoubleAttribute(1234.56789d);
        simpleAttributes.setFloatAttribute(9876.54f);
        simpleAttributes.setLongAttribute(123456789L);
        simpleAttributes.setIntAttribute(98765);
        simpleAttributes.setStringAttribute("Hello world");            

        Parent parent = new Parent();
        parent.setParentAttribute(1);
        Item item = new Item();
        item.setItemAttribute(2);
        SubItem subItem = new SubItem();
        subItem.setSubItemAttribute(3);
        parent.setItem(item);
        item.setSubItem(subItem);

        log.info("    Saving the test objects");
        
        DataObjectHandler dataHandler = Application.getApplication().getDataObjectMgr().getDataObjectHandler(); 
        String dataSource = Application.getApplication().getDataObjectMgr().getDatasource();
        
        dataHandler.store(dataSource, SimpleAttributes.class, simpleAttributes);
        dataHandler.store(dataSource, Parent.class, parent);

        log.info("    Successfully created and saved the test objects.");
        log.info("    Reloading the test objects");
        
        SimpleAttributes simpleAttributesLoaded = (SimpleAttributes)dataHandler.retrieve(dataSource, SimpleAttributes.class, simpleAttributes.getId());
        Parent parentLoaded = (Parent)dataHandler.retrieve(dataSource, Parent.class, parent.getId());

        log.info("    Successfully loaded the test objects.");
        log.info("    Compare the created and loaded test objects");

        compareSavedAndLoaded(simpleAttributes, simpleAttributesLoaded, parent, parentLoaded);

        log.info("    Saved and loaded test objects are equal.");
    	log.info("    Modifiy loaded objects...");

    	simpleAttributesLoaded.setBooleanAttribute(false);
        simpleAttributesLoaded.setDoubleAttribute(9876.54321d);
        simpleAttributesLoaded.setFloatAttribute(1234.56f);
        simpleAttributesLoaded.setLongAttribute(987654321L);
        simpleAttributesLoaded.setIntAttribute(12345);
        simpleAttributesLoaded.setStringAttribute("Goodbye cruel world");
    	
        parentLoaded.setParentAttribute(4);
        parentLoaded.getItem().setItemAttribute(5);
        SubItem subItemNew = new SubItem();
        subItemNew.setSubItemAttribute(6);
        subItemNew.setId( DataObjectId.ILLEGAL_ID);
        parentLoaded.getItem().setSubItem(subItemNew);
        
    	log.info("    Saving the modified objects...");

    	dataHandler.store(dataSource, SimpleAttributes.class, simpleAttributesLoaded);
        dataHandler.store(dataSource, Parent.class, parentLoaded);

        log.info("    Successfully saved the modified objects");
		log.info("    Reloading the modified objects...");

		SimpleAttributes simpleAttributesLoaded2 = (SimpleAttributes)dataHandler.retrieve(dataSource, SimpleAttributes.class, simpleAttributesLoaded.getId());
        Parent parentLoaded2 = (Parent)dataHandler.retrieve(dataSource, Parent.class, parentLoaded.getId());

        log.info("    Successfully loaded the test objects");
		log.info("    Compare the modified and loaded test objects...");
		
		compareSavedAndLoaded(simpleAttributesLoaded, simpleAttributesLoaded2, parentLoaded, parentLoaded2);

		log.info("    Saved and loaded test objects are equal");
        log.info("*** Properties test successful :) ***");
    }
	
	private void compareSavedAndLoaded( SimpleAttributes simpleAttributesSaved,
										SimpleAttributes simpleAttributesLoaded,
										Parent parentSaved,
										Parent parentLoaded) {
		
	    assertEquals("Saved and loaded simpleAttributes.getId() differ.", simpleAttributesSaved.getId(), simpleAttributesLoaded.getId());
	    //assertEquals("Saved and loaded simpleAttributes.getTimestamp() differ.", simpleAttributesSaved.getTimestamp(), simpleAttributesLoaded.getTimestamp());
	    assertEquals("Saved and loaded simpleAttributes.isBooleanAttribute() differ.", simpleAttributesSaved.isBooleanAttribute(), simpleAttributesLoaded.isBooleanAttribute());
	    assertEquals("Saved and loaded simpleAttributes.getDoubleAttribute() differ.", simpleAttributesSaved.getDoubleAttribute(), simpleAttributesLoaded.getDoubleAttribute());
	    assertEquals("Saved and loaded simpleAttributes.getFloatAttribute() differ.", simpleAttributesSaved.getFloatAttribute(), simpleAttributesLoaded.getFloatAttribute());
	    assertEquals("Saved and loaded simpleAttributes.getLongAttribute() differ.", simpleAttributesSaved.getLongAttribute(), simpleAttributesLoaded.getLongAttribute());
	    assertEquals("Saved and loaded simpleAttributes.getIntAttribute() differ.", simpleAttributesSaved.getIntAttribute(), simpleAttributesLoaded.getIntAttribute());
	    assertEquals("Saved and loaded simpleAttributes.getStringAttribute() differ.", simpleAttributesSaved.getStringAttribute(), simpleAttributesLoaded.getStringAttribute());

	    assertEquals("Saved and loaded parent.getId() differ.", parentSaved.getId(), parentLoaded.getId());
	    //assertEquals("Saved and loaded parent.getTimestamp() differ.", parentSaved.getTimestamp(), parentLoaded.getTimestamp());
	    assertEquals("Saved and loaded parent.getParentAttribute() differ.", parentSaved.getParentAttribute(), parentLoaded.getParentAttribute());
	    assertEquals("Saved and loaded parent.getItem().getId() differ.", parentSaved.getItem().getId(), parentLoaded.getItem().getId());
	    //assertEquals("Saved and loaded parent.getItem().getTimestamp() differ.", parentSaved.getItem().getTimestamp(), parentLoaded.getItem().getTimestamp());
	    assertEquals("Saved and loaded parent.getItem().getItemAttribute() differ.", parentSaved.getItem().getItemAttribute(), parentLoaded.getItem().getItemAttribute());
	    assertEquals("Saved and loaded Parent.getItem().getSubItem().getId() differ.", parentSaved.getItem().getSubItem().getId(), parentLoaded.getItem().getSubItem().getId());
	    //assertEquals("Saved and loaded Parent.getItem().getSubItem().getTimestamp() differ.", parentSaved.getItem().getSubItem().getTimestamp(), parentLoaded.getItem().getSubItem().getTimestamp());
	    assertEquals("Saved and loaded parent.getItem().getSubItem().getSubItemAttribute() differ.", parentSaved.getItem().getSubItem().getSubItemAttribute(), parentLoaded.getItem().getSubItem().getSubItemAttribute());
	}	
}
