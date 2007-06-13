package ch.bbv.dynamicproperties;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.SimpleType;



public class InfrastructureTest {

    PropertyInstanceManager manager;
    
    @org.junit.Before
    public void setUp(){
        manager = PropertyInstanceManager.instance();
    }
    
    @org.junit.Test
    public void testBasisTypes(){
        assertEquals(((SimpleType)PropertyInstanceManager.getType(Integer.class)).getType(), Integer.class);
        assertEquals(((SimpleType)PropertyInstanceManager.getType(String.class)).getType(), String.class);
        assertEquals(((SimpleType)PropertyInstanceManager.getType(Date.class)).getType(), Date.class);
        assertEquals(((SimpleType)PropertyInstanceManager.getType(Boolean.class)).getType(), Boolean.class);
        assertEquals(((SimpleType)PropertyInstanceManager.getType(Double.class)).getType(), Double.class);
    }
    
    @org.junit.Test
    public void testPropertyStore(){
        String propertyName = "PropertyName";
        Property property = PropertyInstanceManager.getType(Integer.class).createProperty(propertyName);
        manager.addProperty(property);
        assertEquals(manager.getProperty(propertyName), property);
    }
}
