package ch.bbv.dynamicproperties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import ch.bbv.dynamicproperties.core.ComplexType;
import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.visualization.editor.ComplexEditor;
import ch.bbv.dynamicproperties.visualization.viewer.ComplexViewer;

public class ComplexPropertyTest {

    private static final String BIRTHDATE = "birthdate";

    private static final String SIZE = "size";

    private static final String NAME = "name";

    String typeName = "string";

    String propertyName = "propertyName";

    ComplexType complexType;

    Property property;

    ComplexValue complexValue;

    Property nameProperty;

    Property sizeProperty;

    Property birthdayProperty;

    @org.junit.Before
    public void setUp() {
        complexType = new ComplexType(typeName, ComplexViewer.class, ComplexEditor.class);
        property = complexType.createProperty(propertyName);
        
        nameProperty = new Property(NAME, PropertyInstanceManager.getType(String.class));
        complexType.addProperty(nameProperty);
        sizeProperty = new Property(SIZE, PropertyInstanceManager.getType(Integer.class));
        complexType.addProperty(sizeProperty);
        birthdayProperty = new Property(BIRTHDATE, PropertyInstanceManager.getType(Date.class));
        complexType.addProperty(birthdayProperty);
        
        complexValue = (ComplexValue) property.createInstance();

    }

    @Test
    public void testComplexType() {
        assertNotNull(complexType);
        assertEquals(complexType.getName(), typeName);
        assertEquals(complexType.getPropertyType(NAME), nameProperty);
        assertEquals(complexType.getPropertyType(SIZE), sizeProperty);
        assertEquals(complexType.getPropertyType(BIRTHDATE), birthdayProperty);
    }

    @Test
    public void testComplexProperty() {
        assertNotNull(property);
        assertEquals(property.getName(), propertyName);
        assertEquals(property.getType(), complexType);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testComplexValue() {
        assertEquals(complexValue.getProperty(), property);
        assertEquals(complexValue.getName(), property.getName());
        assertNotNull(complexValue.getEditor());
        assertNotNull(complexValue.getViewer());
        String name = "Ueli";
        int size = 183;
        Date birthdate = new Date("1980/04/15");
        complexValue.setValue(NAME, name);
        complexValue.setValue(BIRTHDATE, birthdate);
        complexValue.setValue(SIZE, size);

        assertEquals(complexValue.getPropertyValue(NAME).getValue(), name);
        assertEquals(complexValue.getPropertyValue(BIRTHDATE).getValue(), birthdate);
        assertEquals(complexValue.getPropertyValue(SIZE).getValue(), size);
        
        ComplexValue copy = (ComplexValue)complexValue.getValue();
        assertEquals(copy.getPropertyValue(NAME).getValue(), name);
        assertEquals(copy.getPropertyValue(BIRTHDATE).getValue(), birthdate);
        assertEquals(copy.getPropertyValue(SIZE).getValue(), size);
        
    }
}
