/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.generators;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;

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

  public static final String CONFIGURATION_KEY = "configuration";
  public static final String CARTRIDGE_KEY = "cartridge";
  public static final String MODEL_KEY = "model";
  public static final String PACKAGES_KEY = "packages";
  public static final String PACKAGE_KEY = "package";
  public static final String CLASSES_KEY = "classes";
  public static final String CLASS_KEY = "class";
  public static final String PROPERTIES_KEY = "properties";
  public static final String AGGREGATIONS_KEY = "aggregations";

  /**
   * logger for the catridge class.
   */
  private static Log log = LogFactory.getLog(CartridgeImpl.class);

  /**
   * List of all cartridges the cartridge is dependant of.
   */
  private List cartridges;

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
   * Constructor fo the class.
   * @param name name of the cartridge.
   * @param abbreviation abbreviation of the cartridge.
   * @param version version of the catridge.
   */
  public CartridgeImpl(String name, String abbreviation, VersionId version) {
    this.name = name;
    this.abbreviation = abbreviation;
    this.version = version;
    cartridges = new ArrayList();
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

  public List getUsedCartridges() {
    return Collections.unmodifiableList(cartridges);
  }

  public boolean checkPathProperties() {
    return true;
  }

  public List getAllProperties() {
    return Collections.EMPTY_LIST;
  }

  public List getAllStereotypes() {
    return Collections.EMPTY_LIST;
  }

  public void registerRules(Validator validator) {
  }

  public void preProcessModel(MetaModel model) {
  }

  public void processModel(MetaModel model) {
  }

  public void postProcessModel(MetaModel model) {
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
  }

  public List getTaggedValueTypes(Class clazz) {
    return null;
  }

  public List getObsoleteTaggedValueTypes(Class clazz) {
    return Collections.EMPTY_LIST;
  }

  public Properties getContext() {
    return properties;
  }

  /**
   * Adds a cartridge being used in the cartridge to create the context.
   * @param cartridge cartridge being used.
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
   */
  protected void processJavaTemplate(File root, String packageName,
      String classname, Template template, VelocityContext context)
      throws IOException {
    PrintWriter writer = createJavaWriter(root, packageName, classname);
    writer.print(processTemplate(template, context));
    writer.close();
  }

  /**
   * Process a velocity template for an artifact.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param filename name of the file containing the artifact.
   * @param template velocity template to execute.
   * @param context velocity context of the template.
   * @throws IOException An exception occured when trying to create the file.
   */
  protected void processTemplate(File root, String packageName,
      String filename, Template template, VelocityContext context)
      throws IOException {
    PrintWriter writer = createWriter(root, packageName, filename);
    writer.print(processTemplate(template, context));
    writer.close();
  }

  /**
   * Creates a print writer for a Java class artifact.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param classname name of the Java class.
   * @return the requested print writer.
   * @throws IOException An exception occured when trying to create the file.
   */
  protected PrintWriter createJavaWriter(File root, String packageName,
      String classname) throws IOException {
    return createWriter(root, packageName, classname + JAVA_EXTENSION);
  }

  /**
   * Creates a print writer for an artifact.
   * @param root root directory where the artifact should be stored.
   * @param packageName qualified package name of the artifact.
   * @param filename name of the file containing the artifact.
   * @return the requested print writer.
   * @throws IOException An exception occured when trying to create the file.
   */
  protected PrintWriter createWriter(File root, String packageName,
      String filename) throws IOException {
    File file = null;
    try {
      file = createFile(root, packageName, filename);
      return new PrintWriter(new BufferedOutputStream(
          new FileOutputStream(file)));
    } catch (IOException e) {
      log.debug("Could not open print writer with file " + file);
      throw e;
    }
  }

  /**
   * Creates a file in the directory associated with the package. The root
   * defines the start of source tree. The name of the file is the name of the
   * class.
   * @param root root directory of the source tree.
   * @param packageName name of the package.
   * @param filename name of the file.
   * @return the created file if sucessfull otherwise null.
   * @pre (root != null && root.isDirectory()) && (packageName != null &&
   *      packageName.length() > 0) (className != null && className.length() >
   *      0)
   */
  protected File createFile(File root, String packageName, String filename) {
    assert (((root != null) && root.isDirectory()) && ((filename != null) && (filename
        .length() > 0)));

    File file = null;
    File directory;
    if (packageName != null) {
      directory = new File(root, packageName.replace('.', '/'));
    } else {
      directory = root;
    }
    if (directory.isDirectory() || directory.mkdirs()) {
      file = new File(directory, filename);
    }
    if (file != null) {
      if (log.isDebugEnabled()) {
        log.debug("file created " + file.getAbsolutePath());
      }
    } else {
      if (log.isErrorEnabled()) {
        log.error("file not created " + root.getAbsolutePath() + "/"
            + packageName + "/" + filename);
      }
    }
    return file;
  }

  /**
   * Creates a new print writer for the given file.
   * @param file file for which a print writer should be created.
   * @return the new print writer.
   * @throws FileNotFoundException the creation of the print writer generated an
   *         error.
   * @pre file != null && file.canWrite()
   */
  protected PrintWriter createPrintWriter(File file)
      throws FileNotFoundException {
    assert (file != null);
    // assert ((file != null) && file.canWrite());
    return new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
  }

  /**
   * Processes the given velocity template with the given context.
   * @param template template to process.
   * @param context context of the template to process.
   * @return the result of the template processing as string.
   * @pre template != null && context != null
   */
  protected String processTemplate(Template template, VelocityContext context) {
    assert ((template != null) && (context != null));

    StringWriter writer = new StringWriter();
    try {
      template.merge(context, writer);
    } catch (MethodInvocationException mie) {
      mie.printStackTrace();
      log.error(mie);
    } catch (ParseErrorException pee) {
      pee.printStackTrace();
      log.error(pee);
    } catch (ResourceNotFoundException rnfe) {
      rnfe.printStackTrace();
      log.error(rnfe);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e);
    }

    return writer.toString();
  }

  /**
   * Returns all fields of the given class and of all its ancestors in the given
   * lists based on their type.
   * @param clazz meta class, which fields should be generated.
   * @param properties properties of the class and its direct and indirect
   *        ancestors.
   * @param indexedProperties indexed properties of the class and its direct and
   *        indirect ancestors.
   * @param references references of the class and its direct and indirect
   *        ancestors.
   */
  protected void getFieldsFor(MetaClass clazz, List properties,
      List indexedProperties, List references) {
    for (Iterator i = clazz.getProperties().iterator(); i.hasNext();) {
      MetaProperty property = (MetaProperty) i.next();
      if (property.retrieveIsReferenceCode()) {
        references.add(property);
      } else {
        properties.add(property);
      }
    }
    indexedProperties.addAll(clazz.getIndexedProperties());

    for (Iterator i = clazz.retrieveAncestors().iterator(); i.hasNext();) {
      MetaClass ancestor = (MetaClass) i.next();
      getFieldsFor(ancestor, properties, indexedProperties, references);
    }
  }

  /**
   * Creates the context for a model.
   * @param model model which context should be created.
   * @return the requested velocity context.
   */
  protected VelocityContext createContext(MetaModel model) {
    VelocityContext context = new VelocityContext();
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
    context.put(CARTRIDGE_KEY, this);
    context.put(CONFIGURATION_KEY, getContext());
    context.put(MODEL_KEY, metaPackage.getRoot());
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
    context.put(CARTRIDGE_KEY, this);
    context.put(CONFIGURATION_KEY, getContext());
    context.put(MODEL_KEY, clazz.getRoot());
    context.put(PACKAGE_KEY, clazz.getContext());
    context.put(CLASS_KEY, clazz);

    List properties = new ArrayList();
    List indexedProperties = new ArrayList();
    List references = new ArrayList();
    getFieldsFor(clazz, properties, indexedProperties, references);
    context.put(PROPERTIES_KEY, properties);
    context.put(AGGREGATIONS_KEY, indexedProperties);
    
    context.put("references", references);
    context.put("imports", clazz.getImports());
    context.put("nestedClasses", clazz.getClasses());
    return context;
  }

  /**
   * Processes the given file and extracts all user defined statements. These
   * statements are grouped in import statements, declarations statements,
   * custom constructors, and user defined methods. These code segments are
   * protected with special comments.
   * @param file file containing the source code of the meta class.
   * @param clazz class owning the user defined statements.
   * @pre file != null && clazz != null
   */
  protected void extractUserStatements(File file, MetaClass clazz) {
    assert ((file != null) && (clazz != null));

    List statements;
    if (file.exists() && file.canRead()) {
      statements = extractBlock(file, USER_IMPORT_BEGIN, USER_IMPORT_END);
      clazz.setImports(statements);
      statements = extractBlock(file, USER_DECLARATION_BEGIN,
          USER_DECLARATION_END);
      clazz.setDeclarations(statements);
      statements = extractBlock(file, USER_CONSTRUCTORS_BEGIN,
          USER_CONSTRUCTORS_END);
      clazz.setConstructors(statements);
      statements = extractBlock(file, USER_CODE_BEGIN, USER_CODE_END);
      clazz.setDefinitions(statements);
    }
  }

  /**
   * Reads the file to find the start comment, extract the user code until the
   * end comment is reached. The start and end comments are not part of the list
   * of statements.
   * @param file file to process.
   * @param startComment comment identifying the start of the user code block.
   * @param endComment comment identifying the end of the user code block.
   * @return list of read lines if found otherwise null.
   * @pre file != null && startComment != null && endComment != null
   */
  protected List extractBlock(File file, String startComment, String endComment) {
    assert ((file != null) && (startComment != null) && (endComment != null));
    return null;
  }

  /**
   * Adds a property with a default value to the properties if it is missing.
   * @param properties properties set to complete.
   * @param property property name to add if not defined.
   * @param value value of the property to add if not defined.
   * @post properties.getProperty(property) != null
   */
  protected void addUndefinedProperty(Properties properties, String property,
      String value) {
    String propertyValue = properties.getProperty(property);
    if (propertyValue == null) {
      properties.setProperty(property, value);
    }
  }

  /**
   * Returns the root file defined in the property with the given key.
   * @param key key of the property containing the root directory.
   * @param defaultKey default key of the property containing the root
   *        directory.
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
}