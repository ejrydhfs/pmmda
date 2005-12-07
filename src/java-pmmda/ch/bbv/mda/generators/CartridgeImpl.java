/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.generators;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.operators.ModelOperators;
import ch.bbv.utilities.PropertyUtilities;

/**
 * The default cartridge implementation provides a set of utility methods to
 * simplify the definition of specific meta data handlers. The cartridge has
 * knowledge about the following constructs.
 * <ul>
 * <li>The cartridge walks through packages, classes, properties and indexed
 * properties. It knows the difference between class, abstract class and
 * interfaces.</li>
 * <li>The cartridge distinguishes between regular , reference code and
 * primitive type properties.</li>
 * <li>The cartridge explores the model to find all ancestors of a class or an
 * interface.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public abstract class CartridgeImpl implements Cartridge {
  /**
   * Extension for Java source file.
   */
  public static final String JAVA_EXTENSION = ".java";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String CONFIGURATION_KEY = "configuration";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String CARTRIDGE_KEY = "cartridge";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String UTILITIES_KEY = "utilities";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String MODEL_KEY = "model";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String PACKAGE_KEY = "package";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String CLASSES_KEY = "classes";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String CLASS_KEY = "class";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String PROPERTIES_KEY = "properties";

  /**
   * Velocity context key to identify the object stored in the context.
   */
  public static final String AGGREGATIONS_KEY = "aggregations";

  /**
   * logger for the catridge class.
   */
  private static Log log = LogFactory.getLog(CartridgeImpl.class);

  /**
   * List of all cartridges the cartridge is dependant of.
   */
  private List<Cartridge> cartridges;

  /**
   * Properties for the code generation.
   */
  private Properties properties;

  /**
   * Name of the cartridge.
   */
  private String name;

  /**
   * Version of the cartridge.
   */
  private VersionId version;

  /**
   * Abbreviation of the cartridge.
   */
  private String abbreviation;

  /**
   * The configuration of the cartridge based on a set of properties files.
   */
  private CompositeConfiguration configuration;

  /**
   * Operators for the model.
   */
  private ModelOperators modelOperators;

  /**
   * Constructor fo the class.
   * @param name name of the cartridge.
   * @param abbreviation abbreviation of the cartridge.
   * @param version version of the catridge.
   */
  public CartridgeImpl(String name, String abbreviation, VersionId version) {
    this.name = name;
    this.abbreviation = abbreviation;
    this.version = version;
    this.cartridges = new ArrayList<Cartridge>();
    this.configuration = new CompositeConfiguration();
    this.modelOperators = new ModelOperators();
  }

  public String getName() {
    return name;
  }

  public VersionId getVersion() {
    return version;
  }

  public String getPrefix() {
    return abbreviation;
  }

  public List<Cartridge> getUsedCartridges() {
    return Collections.unmodifiableList(cartridges);
  }

  public boolean checkPathProperties() {
    return true;
  }

  public List getAllProperties() {
    return Collections.EMPTY_LIST;
  }

  public List<StereotypeType> getAllStereotypes() {
    return Collections.emptyList();
  }

  public void registerRules(Validator validator) {
  }

  public void preProcessModel(MetaModel model) {
  }

  public void processModel(MetaModel model) {
  }

  public void postProcessModel(MetaModel model) {
  }

  public void preProcessDatatype(MetaDatatype datatype) {
  }

  public void preProcessPackage(MetaPackage metaPackage) {
  }

  public void processPackage(MetaPackage metaPackage) {
  }

  public void postProcessPackage(MetaPackage metaPackage) {
  }

  public void preProcessClass(MetaClass clazz) {
  }

  public void processClass(MetaClass clazz) {
  }

  public void initialize(Properties properties) {
    this.properties = properties;
    this.configuration.addConfiguration(ConfigurationConverter
        .getConfiguration(properties));
    try {
      StringBuilder buffer = new StringBuilder();
      buffer.append(configuration.getProperty("file.resource.loader.path"))
          .append("/").append(getPrefix()).append("/").append("mappings.xml");
      configuration.addConfiguration(new XMLConfiguration(buffer.toString()));
      addDependencies();
      addUndefinedProperties();
    } catch (ConfigurationException e) {
      log.error("Configuration file not found", e);
    }
  }

  public List<TagDefinition> getTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    StringBuilder buffer = new StringBuilder();
    buffer.append("configuration.tags.tag");
    for (int i = 0;; i++) {
      String tag = PropertyUtilities.getAttributeAt(configuration, buffer, i,
          "name");
      if (tag == null) {
        break;
      }
      String type = PropertyUtilities.getAttributeAt(configuration, buffer, i,
          "type");
      String context = PropertyUtilities.getAttributeAt(configuration, buffer,
          i, "context");
      String defaultValue = PropertyUtilities.getAttributeAt(configuration,
          buffer, i, "default-value");
      if (clazz == convertContext(context)) {
        types.add(new TagDefinition(tag, defaultValue, convertType(type)));
      }
    }
    return types;
  }

  public List<TagDefinition> getObsoleteTaggedValueTypes(Class clazz) {
    List<TagDefinition> types = new ArrayList<TagDefinition>();
    StringBuilder buffer = new StringBuilder();
    buffer.append("configuration.obsolete-tags.obsolete-tag");
    for (int i = 0;; i++) {
      String tag = PropertyUtilities.getAttributeAt(configuration, buffer, i,
          "name");
      if (tag == null) {
        break;
      }
      String type = PropertyUtilities.getAttributeAt(configuration, buffer, i,
          "type");
      String context = PropertyUtilities.getAttributeAt(configuration, buffer,
          i, "context");
      if (clazz == convertContext(context)) {
        types.add(new TagDefinition(tag, convertType(type)));
      }
    }
    return types;
  }

  public Properties getContext() {
    return properties;
  }

  public ModelOperators getModelOperators() {
    return modelOperators;
  }

  /**
   * Returns the configuration of cartridge.
   * @return the properties of the configuration of the cartridge.
   */
  public CompositeConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Adds a cartridge being used in the cartridge to create the context.
   * @param cartridge cartridge being used.
   * @pre cartridge != null
   */
  protected void addCartridge(Cartridge cartridge) {
    cartridges.add(cartridge);
  }

  /**
   * Process a velocity template for a Java artifact.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param classname name of the file containing the artifact.
   * @param template velocity template to execute.
   * @param context velocity context of the template.
   * @throws IOException An exception occured when trying to create the file.
   * @pre (root != null) && (packageName != null) && (classname != null) &&
   *      (template != null) && (context != null)
   */
  protected void processJavaTemplate(File root, String packageName,
      String classname, Template template, VelocityContext context)
      throws IOException {
    processTemplate(root, packageName, classname + JAVA_EXTENSION, template,
        context);
  }

  /**
   * Processes a velocity template for an artifact. The new artifact is only
   * created if it is different from the old one to minimize spurious updates.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param filename name of the file containing the artifact.
   * @param template velocity template to execute.
   * @param context velocity context of the template.
   * @throws IOException An exception occured when trying to create the file.
   * @pre (root != null) && (packageName != null) && (filename != null) &&
   *      (template != null) && (context != null)
   */
  protected void processTemplate(File root, String packageName,
      String filename, Template template, VelocityContext context)
      throws IOException {
    File tempFile = File.createTempFile("pmMDA", null, root);
    tempFile.deleteOnExit();
    PrintWriter writer = new PrintWriter(new BufferedOutputStream(
        new FileOutputStream(tempFile)));
    writer.print(processTemplate(template, context));
    writer.close();
    File file = FileOperators.createFile(root, packageName, filename);
    if (!FileOperators.equals(tempFile, file)) {
      FileOperators.copyFile(tempFile, file);
    }
    tempFile.delete();
  }

  /**
   * Creates a print writer for a Java class artifact.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param classname name of the Java class.
   * @return the requested print writer.
   * @throws IOException An exception occured when trying to create the file.
   * @pre (root != null) && (packageName != null) && (classname != null)
   */
  protected PrintWriter createJavaWriter(File root, String packageName,
      String classname) throws IOException {
    return FileOperators.createWriter(root, packageName, classname
        + JAVA_EXTENSION);
  }

  /**
   * Retrieves the template with the given name and language extension.
   * @param name name of the template to retrieve.
   * @param language language handled in the template.
   * @return the requested template if found otherwise null.
   * @pre (name != null) && (language != null)
   */
  protected Template retrieveTemplate(String name, String language) {
    Template template = null;
    StringBuilder buffer = new StringBuilder();
    buffer.append(getPrefix()).append("/").append(name.toLowerCase());
    if (language != null) {
      buffer.append("-").append(language);
    }
    buffer.append(".vm");
    try {
      template = Velocity.getTemplate(buffer.toString());
    } catch (ParseErrorException e) {
      log.error(e);
    } catch (ResourceNotFoundException e) {
      log.error("Couldn not find template " + buffer.toString(), e);
    } catch (Exception e) {
      log.error(e);
    }
    return template;
  }

  /**
   * Processes the given velocity template with the given context.
   * @param template template to process.
   * @param context context of the template to process.
   * @return the result of the template processing as string.
   * @pre template != null && context != null
   */
  protected String processTemplate(Template template, VelocityContext context) {
    assert (template != null) && (context != null);
    StringWriter writer = new StringWriter();
    try {
      template.merge(context, writer);
    } catch (MethodInvocationException e) {
      log.error(e);
    } catch (ParseErrorException e) {
      log.error(e);
    } catch (ResourceNotFoundException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }

    return writer.toString();
  }

  /**
   * Creates the context for a model.
   * @param model model which context should be created.
   * @return the requested velocity context.
   */
  protected VelocityContext createContext(MetaModel model) {
    VelocityContext context = new VelocityContext();
    context.put(UTILITIES_KEY, modelOperators);
    context.put(CARTRIDGE_KEY, this);
    context.put(CONFIGURATION_KEY, getContext());
    context.put(MODEL_KEY, model);
    return context;
  }

  /**
   * Creates the context for a package.
   * @param metaPackage package to process.
   * @return the requested velocity context.
   */
  protected VelocityContext createContext(MetaPackage metaPackage) {
    VelocityContext context = new VelocityContext();
    context.put(UTILITIES_KEY, modelOperators);
    context.put(CARTRIDGE_KEY, this);
    context.put(CONFIGURATION_KEY, getContext());
    context.put(MODEL_KEY, metaPackage.getModel());
    context.put(PACKAGE_KEY, metaPackage);
    context.put(CLASSES_KEY, metaPackage.getClasses());
    return context;
  }

  /**
   * Generates the context for a class, all its properties, imports, and own
   * context. All properties are eligible.
   * @param clazz class to process.
   * @return velocity velocity context for the meta class.
   */
  protected VelocityContext createContext(MetaClass clazz) {
    VelocityContext context = new VelocityContext();
    context.put(UTILITIES_KEY, modelOperators);
    context.put(CARTRIDGE_KEY, this);
    context.put(CONFIGURATION_KEY, getContext());
    context.put(MODEL_KEY, clazz.getModel());
    context.put(PACKAGE_KEY, clazz.getContext());
    context.put(CLASS_KEY, clazz);
    context.put(PROPERTIES_KEY, modelOperators.getTransitiveFieldsFor(clazz));
    context.put(AGGREGATIONS_KEY, modelOperators
        .getTransitiveIndexedFieldsFor(clazz));
    context.put("nestedClasses", clazz.getNestedClasses());
    return context;
  }

  /**
   * Adds a property with a default value to the properties if it is missing.
   * @param property property name to add if not defined.
   * @param value value of the property to add if not defined.
   * @deprecated
   * @pre (properties != null) && (property != null)
   * @post properties.getProperty(property) != null
   */
  protected void addUndefinedProperty(String property, String value) {
    String propertyValue = properties.getProperty(property);
    if (propertyValue == null) {
      properties.setProperty(property, value);
    }
  }

  /**
   * Returns the root file defined in the property with the given key.
   * @param key key of the property containing the root directory.
   * @param defaultKey default key of the property containing the root
   *          directory.
   * @return The root file if defined otherwise null.
   */
  protected final File getRoot(String key, String defaultKey) {
    Properties properties = getContext();
    String value = properties.getProperty(key);
    File root;
    if ((value == null) || (value.trim().length() == 0)) {
      value = properties.getProperty(defaultKey);
    }
    if ((value == null) || (value.trim().length() == 0)) {
      root = null;
    } else {
      root = new File(value);
    }
    return root;
  }

  /**
   * Expands an identifier with its prefix and postfix.
   * @param prefixKey key of the prefix defined in the context.
   * @param identifier identifier to expand.
   * @param postfixKey key of the prefix defined in the context.
   * @return expanded identifier.
   */
  protected String expandIdentifier(String prefixKey, String identifier,
      String postfixKey) {
    Properties context = getContext();
    StringBuilder buffer = new StringBuilder();
    if (prefixKey != null) {
      buffer.append(context.getProperty(prefixKey));
    }
    buffer.append(identifier);
    if (postfixKey != null) {
      buffer.append(context.getProperty(postfixKey));
    }
    return buffer.toString();
  }

  /**
   * Returns the mapping for the type to the target language. A mapping exists
   * only for datatypes.
   * @param type type to map to a language construct.
   * @param language language to map to.
   * @param concrete flag indicating if the implementation class should be
   *          retrieve or not.
   * @return the requested mapping if found otherwise the qualified name of the
   *         type.
   */
  protected String translateDatatype(MetaDatatype type, String language,
      boolean concrete) {
    String typename = type.qualifiedName();
    StringBuilder buffer = new StringBuilder();
    buffer.append("datatypes.datatype");
    for (int i = 0;; i++) {
      String name = PropertyUtilities.getAttributeAt(configuration, buffer, i,
          "name");
      if (name == null) {
        break;
      }
      if (typename.equals(name)) {
        typename = PropertyUtilities.getAttributeAt(configuration, buffer, i,
            "type");
        break;
      }
    }
    return typename;
  }

  /**
   * Adds all properties defined in the configuration file of the cartridge and
   * not already defined in the application property file.
   */
  private void addUndefinedProperties() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("configuration.properties.property");
    for (int i = 0;; i++) {
      String property = PropertyUtilities.getAttributeAt(configuration, buffer,
          i, "name");
      if (property == null) {
        break;
      }
      String value = PropertyUtilities.getAttributeAt(configuration, buffer, i,
          "default-value");
      String propertyValue = properties.getProperty(property);
      if (propertyValue == null) {
        properties.setProperty(property, value);
      }
    }
  }

  private void addDependencies() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("configuration.uses.cartridge");
    CartridgeFactory factory = CartridgeFactory.getInstance();
    for (int i = 0;; i++) {
      String prefix = PropertyUtilities.getAttributeAt(configuration, buffer,
          i, "name");
      if (prefix == null) {
        break;
      }
      addCartridge(factory.getCartridge(prefix));
    }

  }

  private Class convertType(String type) {
    Class clazz = null;
    if ("boolean".equals(type)) {
      clazz = Boolean.TYPE;
    } else if ("int".equals(type)) {
      clazz = Integer.TYPE;
    } else {
      try {
        clazz = Class.forName(type);
      } catch (ClassNotFoundException e) {
        log.error("Class not found for name " + type, e);
      }
    }
    assert (clazz != null);
    return clazz;
  }

  private Class convertContext(String context) {
    Class clazz = null;
    if ("model".equals(context)) {
      clazz = MetaModel.class;
    } else if ("package".equals(context)) {
      clazz = MetaPackage.class;
    } else if ("class".equals(context)) {
      clazz = MetaClass.class;
    } else if ("property".equals(context)) {
      clazz = MetaProperty.class;
    } else if ("indexed-property".equals(context)) {
      clazz = MetaIndexedProperty.class;
    }
    return clazz;
  }
}