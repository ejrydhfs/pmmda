/*
 * copyright 2000-2004 Marcel Baumann Licensed under the Apache License, Version
 * 2.0 (the "Licence); you may not use this file except in compliance with the
 * License. $Id$
 */
package ch.bbv.mda.generators;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.application.VersionId;
import ch.bbv.mda.*;

/**
 * The OEX cartridge generates all the artifacts for the explorer framework to
 * create a user interface to manipulate data object graphs.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class OexCartridge extends CartridgeImpl {
  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String RICH_CLIENT_PACKAGE_POSTFIX_KEY = "oex-cartridge.rich-client-package-extension";

  /**
   * Key for a property of the cartridge used to retrieve user values.
   */
  public static final String ROOT_RICH_CLIENT_KEY = "oex-cartridge.rich-client-folder";

  /**
   * Tag name defining the class of the property view used to display the class.
   */
  public static final String VIEW_CLASS_TAG = "oex-view-class";

  /**
   * Tag name indicating if the class is visible in the explorer or not.
   */
  public static final String IS_VISIBLE_TAG = "oex-visible";

  /**
   * Tag name of the package containing the rich client source code artifacts.
   */
  public static final String OEX_RICH_CLIENT_PACKAGE = "oex-rich-client-package";

  /**
   * Log instance for the class.
   */
  private static Log log = LogFactory.getLog(OexCartridge.class);

  /**
   * velocity template to generate user interface types for the rich client.
   */
  private Template javaRichClientTypeTemplate;

  /**
   * Default constructor of the class.
   */
  public OexCartridge() {
    super("Object Explorer Cartridge", "oex", new VersionId(0, 3, 0));

    CartridgeFactory factory = CartridgeFactory.getInstance();
    addCartridge(factory.getCartridge("dog"));
  }

  public void initialize(Properties properties) {
    super.initialize(properties);
    addUndefinedProperty(properties, RICH_CLIENT_PACKAGE_POSTFIX_KEY, "");
    try {
      javaRichClientTypeTemplate = Velocity.getTemplate("oex-typeFactory.vm");
    } catch (ParseErrorException pee) {
      log.equals(pee);
      pee.printStackTrace();
    } catch (ResourceNotFoundException rnfe) {
      log.equals(rnfe);
      rnfe.printStackTrace();
    } catch (Exception exception) {
      log.equals(exception);
      exception.printStackTrace();
    }
  }

  /**
   * Returns a list of visible fields of the class. Visible fields are displayed
   * in the explorer user interface.
   * @param clazz class to analyze.
   * @return the list of visible fields of the class.
   */
  public Collection getVisibleFields(MetaClass clazz) {
    Map fields = new HashMap();
    MetaModel model = (MetaModel) clazz.getRoot();
    for (Iterator i = clazz.getIndexedProperties().iterator(); i.hasNext();) {
      MetaIndexedProperty object = (MetaIndexedProperty) i.next();
      String className = object.getTypeName();
      if (className == null) {
        continue;
      }

      MetaClass objectClass = model.findClass(className);
      if ((objectClass == null)
          || !BogCartridge.DATA_OBJECT_STEREOTYPE.equals(objectClass
              .getStereotype())) {
        continue;
      }

      for (Iterator j = objectClass.getProperties().iterator(); j.hasNext();) {
        MetaProperty property = (MetaProperty) j.next();
        if (property.booleanValueOfTag(IS_VISIBLE_TAG)) {
          fields.put(property.getName(), property);
        }
      }
      for (Iterator j = objectClass.getIndexedProperties().iterator(); j
          .hasNext();) {
        MetaIndexedProperty property = (MetaIndexedProperty) j.next();
        if (property.booleanValueOfTag(IS_VISIBLE_TAG)) {
          fields.put(property.getName(), property);
        }
      }
    }
    return fields.values();
  }

  public List getTaggedValueTypes(Class clazz) {
    List types = new ArrayList();
    if (clazz.equals(MetaClass.class)) {
      types.add(new TaggedValueType(VIEW_CLASS_TAG, "", String.class, this));
    } else if (clazz.equals(MetaProperty.class)) {
      types
          .add(new TaggedValueType(IS_VISIBLE_TAG, "false", Boolean.TYPE, this));
    } else if (clazz.equals(MetaIndexedProperty.class)) {
      types
          .add(new TaggedValueType(IS_VISIBLE_TAG, "false", Boolean.TYPE, this));
    }
    return types;
  }

  public List getObsoleteTaggedValueTypes(Class clazz) {
    List types = null;
    if (clazz == MetaClass.class) {
      types = new ArrayList();
      types.add(new TaggedValueType("oex-is-root", null, Boolean.class, this));
    }
    return types;
  }

  public TaggedValue getTaggedValueWithElementDependentDefault(
      TaggedValueType taggedValueType, MetaElement element) {
    TaggedValue value = new TaggedValue(taggedValueType);
    updateTaggedValueWithElementDependentDefault(value, element);
    return value;
  }

  public void updateTaggedValueWithElementDependentDefault(
      TaggedValue taggedValue, MetaElement element) {
    if (taggedValue.getType().getTag().equals(VIEW_CLASS_TAG)) {
      String name = element.getName();
      if (name != null) {
        taggedValue.setValue(name + "View");
      }
    }
  }

  public void preProcessPackage(MetaPackage metaPackage) {
    Properties context = getContext();
    StringBuffer buffer = new StringBuffer();
    String qualifiedName = metaPackage.qualifiedName();
    buffer.append(qualifiedName).append(
        context.getProperty(RICH_CLIENT_PACKAGE_POSTFIX_KEY));
    metaPackage.addAttribute(OEX_RICH_CLIENT_PACKAGE, buffer.toString());
    super.preProcessPackage(metaPackage);
  }

  public void processModel(MetaModel model) {
  }

  public void postProcessPackage(MetaPackage metaPackage) {
    File sourceRoot = getRoot(ROOT_RICH_CLIENT_KEY,
        BogCartridge.ROOT_CLIENT_FOLDER_KEY);
    if (metaPackage.booleanValueOfTag(BogCartridge.GENERATE_TAG)) {
      VelocityContext context = createContext(metaPackage);
      log
          .info("OEX Catridge processing package "
              + metaPackage.qualifiedName());
      try {
        PrintWriter writer = createJavaWriter(sourceRoot, metaPackage
            .getAttribute(OEX_RICH_CLIENT_PACKAGE), "TypeFactory");
        writer.print(processTemplate(javaRichClientTypeTemplate, context));
        writer.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }
}