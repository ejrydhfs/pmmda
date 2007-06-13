/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.visualization.editor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.swing.JComponent;

import ch.bbv.dynamicproperties.visualization.IEditor;

public abstract class DefaultEditor implements IEditor {

    private Hashtable<String, Object> properties;

    public DefaultEditor() {
        properties = new Hashtable<String, Object>();
    }

    public void putProperty(String name, Object value) {
        properties.put(name, value);
    }

    /**
     * 
     * @param component
     */
    public void setProperties(JComponent component) {
        for (String key : properties.keySet()) {
            Object parameter = properties.get(key);
            try {
                Method method = JComponent.class.getMethod(createMethodName(key), new Class[] { parameter.getClass() });
                method.invoke(component, new Object[] { parameter });
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private String createMethodName(String name) {
        return "set" + firstLetterUpperCase(name);
    }

    private String firstLetterUpperCase(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return name;
    }

}
