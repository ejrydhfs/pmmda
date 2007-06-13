package ch.bbv.dynamicproperties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import ch.bbv.dynamicproperties.core.ListType;
import ch.bbv.dynamicproperties.core.ListValue;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.visualization.editor.IntegerEditor;
import ch.bbv.dynamicproperties.visualization.editor.ListEditor;
import ch.bbv.dynamicproperties.visualization.viewer.ListViewer;
import ch.bbv.dynamicproperties.visualization.viewer.StringViewer;

public class ListPropertyTest {

    String elementTypeName = "int";
    String typeName = "intList";
    String propertyName = "propertyName";
    
    ListType listType;
    SimpleType elementType;
    Property property;
    ListValue listValue;
    
    @org.junit.Before
    public void setUp(){
        elementType = new SimpleType(elementTypeName, Integer.class, StringViewer.class, IntegerEditor.class );
        listType = new ListType(typeName, elementType, ListViewer.class, ListEditor.class);
        property = listType.createProperty(propertyName);
        listValue = (ListValue)property.createInstance();
    }
    
    @Test
    public void testListType() {
        assertNotNull(elementType);
        assertNotNull(listType);
        
        assertEquals(listType.getElementType(), elementType);
        assertEquals(elementType.getName(), elementTypeName);
        assertEquals(elementType.getType(), Integer.class);
    }
    
    @Test
    public void testListProperty() {
        assertNotNull(property);
        assertEquals(property.getName(), propertyName);
        assertEquals(property.getType(), listType);
    }
    
    @Test
    public void testListValue(){
        assertNotNull(listValue);
        assertEquals(listValue.getProperty(), property);
        assertEquals(listValue.getName(), property.getName());
        assertNotNull(listValue.getEditor());
        assertNotNull(listValue.getViewer());
        
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<100; i++){
            listValue.add(i);
            list.add(i);
        }
        
        for(int i=0;i<list.size(); i++){
            assertEquals(listValue.get(i).getValue(), list.get(i));
        }
        
        for(int i=40;i<60;i++){
            listValue.remove(i);
            list.remove(i);
        }
        
        for(int i=0;i<list.size(); i++){
            assertEquals(listValue.get(i).getValue(), list.get(i));
        }
    }
}
