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

package ch.bbv.dynamicproperties.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ch.bbv.dynamicproperties.PropertyInstanceManager;

import com.thoughtworks.xstream.XStream;

public class XMLTools {

    public static String readFile(File file) {
        try {
            return IOUtils.toString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(File file, String string) {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            IOUtils.write(string, outputStream);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(string);
            fileWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyInstanceManager importAttributeManager(File file) {
        XStream xstream = new XStream();
        return (PropertyInstanceManager) xstream.fromXML(readFile(file));
    }

    public static void exportAttributeManager(PropertyInstanceManager manager, File file) {
        XStream xstream = new XStream();
        writeFile(file, xstream.toXML(manager));
    }
}
