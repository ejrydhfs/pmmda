package ch.bbv.dynamicproperties;


import org.junit.Test;


import static org.junit.Assert.*;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.SimpleType;
import ch.bbv.dynamicproperties.core.SimpleValue;
import ch.bbv.dynamicproperties.visualization.editor.StringEditor;
import ch.bbv.dynamicproperties.visualization.viewer.StringViewer;

public class SimplePropertyTest {

    
    String typeName = "string";
    String propertyName = "propertyName";
    
    SimpleType simpleType;
    Property property;
    SimpleValue simpleValue;
    
    @org.junit.Before
    public void setUp(){
        simpleType = new SimpleType(typeName, String.class, StringViewer.class, StringEditor.class);
        property = simpleType.createProperty(propertyName);
        simpleValue = (SimpleValue)property.createInstance();
    }
    
    @Test
    public void testSimpleType() {
        assertNotNull(simpleType);
        assertEquals(simpleType.getName(), typeName);
        assertEquals(simpleType.getType(), String.class);
    }
    
    @Test
    public void testSimpleProperty() {
        assertNotNull(property);
        assertEquals(property.getName(), propertyName);
        assertEquals(property.getType(), simpleType);
    }
    
    @Test
    public void testSimpleValue(){
        assertEquals(simpleValue.getProperty(), property);
        assertEquals(simpleValue.getName(), property.getName());
        assertNotNull(simpleValue.getEditor());
        assertNotNull(simpleValue.getViewer());
        String text = "text";
        simpleValue.setValue(text);
        assertEquals(simpleValue.getValue(), text);
        simpleValue.setValue(text+text);
        assertEquals(simpleValue.getValue(), text+text);
        assertNotSame(simpleValue.getValue(), text);
    }
}
