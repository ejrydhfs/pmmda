/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.cartridges;

import static ch.bbv.mda.cartridges.BoConstants.*;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.dog.validation.Validator;
import ch.bbv.mda.*;
import ch.bbv.mda.generators.Cartridge;
import ch.bbv.mda.generators.validation.DogTagRule;
import ch.bbv.mda.generators.validation.TagRule;
import ch.bbv.mda.operators.FileOperators;
import ch.bbv.mda.operators.MdaUtilities;
import ch.bbv.mda.operators.ModelOperators;

/**
 * The data object cartridge is responsible for the generation of all data
 * object types and the associated helper classes. The code generator is able to
 * distinguish between simple properties, indexed properties and reference
 * codes. The generated code for each type of fields can be modified in the
 * velocity script without any source code changes. The following constructs are
 * supported.
 * <ul>
 * <li>The data object with its constructors, getters and setters for all
 * properties is generated. The code is well indented and fully documented
 * following the JavaDoc conventions. Each data object has a default constructor
 * and an optional specialized one. The getter and setter naming conventions are
 * defined in the package documentation of MDA models -.</li>
 * <li>A property is identified as a reference code if it implements the
 * reference code or hierarchical reference code interface.</li>
 * <li>A lightweight representation of each data object having the lightweight
 * flag set is generated. Lighweight objects are used for navigation and display
 * purposes when it would be too expensive to send the complete graph of data
 * object instances. Typical usage is displaying a navigation tree in the user
 * interface. The getter and setter naming conventions are the same as the ones
 * of the data objects.</li>
 * <li>A reference service is generated for all the reference codes defined in
 * one package. Convenience methods to access each type of code are provided. If
 * no reference code exists in the package no service will be generated.</li>
 * <li>Helper classes are generated to provide a complete implementation of the
 * visitor pattern on all data object classes defined in the package. The
 * generated visitor can visit data objects defined in other packages but do not
 * generate specialized methods for their descendents.</li>
 * </ul>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class BoCartridge extends JavaCartridge implements Cartridge {
  /**
   * The prefix of the cartridge.
   */
  public static final String PREFIX = "bo";

  /**
   * The name of the generated programming language.
   */
  public static final String LANGUAGE = "java";

  /**
   * Log instance for all instatnces of the class.
   */
  private static Log log = LogFactory.getLog(BoCartridge.class);

  /**
   * List of stereotypes managed in the cartride.
   */
  private static List<Stereotype> stereotypes;

  /**
   * The actual package being processed in the cartridge.
   */
  private MetaPackage packages;

  /**
   * Flag indicating if the package contains reference code declarations.
   */
  private boolean hasCodes;

  /**
   * Flag indicating if the package contains data object declarations.
   */
  private boolean hasDataObjects;

  /**
   * Root directory of the generated source files.
   */
  private File root;

  /**
   * Constructor of the class. All data object related code generator templates
   * are loaded.
   */
  public BoCartridge() {
    super("Data Object Graph Cartridge", PREFIX, new VersionId(0, 3, 0));
    stereotypes = new ArrayList<Stereotype>();
    stereotypes.add(new Stereotype(DATA_OBJECT_STEREOTYPE));
    stereotypes.add(new Stereotype(REFERENCE_CODE_STEREOTYPE));
  }

  @Override
  public void registerRules(Validator validator) {
    TagRule tagRule = new DogTagRule();
    tagRule.register(validator);
  }

  @Override
  public void preProcessDatatype(MetaDatatype datatype) {
    datatype.addAttribute(BO_TYPE_JAVA, translateDatatype(datatype, LANGUAGE,
        false));
  }

  /**
   * Computes the data object cartridge relevant attributes for the meta
   * package. The following attributes are defined.
   * <ul>
   * <dt>IF_PACKAGE</dt>
   * <dd>Name of the package containing the interfaces of data objects. The
   * name is the aggregation of the base package name, the interface extension
   * and the prefix and postfix for the interface package.</dd>
   * <dt>BO_PACKAGE</dt>
   * <dd>Name of the package containing the implementation of the data objects.
   * The name is the aggregation of the base package name, the interface
   * extension and the prefix and postfix for the data object package.</dd>
   * <dt>LW_PACKAGE</dt>
   * <dd>Name of the package containing the lightweight representation of data
   * objects. The name is the aggregation of the base package name, the
   * interface extension and the prefix and postfix for the lightweight
   * representation package.</dd>
   * </ul>
   * @param packages the package, which attributes should be computed.
   * @pre packages != null
   */
  @Override
  public void preProcessPackage(MetaPackage packages) {
    assert (packages != null);
    this.packages = packages;
    String name = packages.getQualifiedName();
    packages.addAttribute(IF_PACKAGE, expandIdentifier(null, name,
        IF_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(BO_PACKAGE, expandIdentifier(null, name,
        BO_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(LW_PACKAGE, expandIdentifier(null, name,
        LW_PACKAGE_POSTFIX_KEY));
    packages.addAttribute(BO_LIGHTWEIGHT, Boolean
        .toString(hasLightweight(packages)));
  }

  /**
   * Computes the data cartridge relevant attributes for the meta class. The
   * following attributes are defined.
   * <ul>
   * <dt>IF_CLASS</dt>
   * <dd>Name of the class representing the interface of the data object. The
   * name is the aggregation of the class name and the class prefix and postfix.
   * </dd>
   * <dt>BO_CLASS</dt>
   * <dd>Name of the class representing the data object implementation. The
   * name is the IF_CLASS and the data object class postfix.</dd>
   * <dt>LW_CLASS</dt>
   * <dd>Name of the class representing the data object lightweight
   * implementation. The name is the IF_CLASS and the data object lightweight
   * class postfix.</dd>
   * </ul>
   * @param clazz the class, which attributes should be computed
   * @pre clazz != null
   */
  @Override
  public void preProcessClass(MetaClass clazz) {
    assert clazz != null;
    String name = clazz.getName();
    clazz.addAttribute(IF_CLASS, expandIdentifier(IF_CLASS_PREFIX_KEY, name,
        IF_CLASS_POSTFIX_KEY));
    clazz.addAttribute(BO_CLASS, expandIdentifier(BO_CLASS_PREFIX_KEY, name,
        BO_CLASS_POSTFIX_KEY));
    clazz.addAttribute(LW_CLASS, expandIdentifier(LW_CLASS_PREFIX_KEY, name,
        LW_CLASS_POSTFIX_KEY));
    clazz.addAttribute(BO_HAS_INTERFACE, getContext().getProperty(
        BO_GENERATE_INTERFACE_KEY));
    clazz.addAttribute(BO_LIGHTWEIGHT, Boolean.toString(hasLightweight(clazz)));
    boolean changeable = false;
    boolean vetoable = false;
    for (MetaProperty property : clazz.getProperties()) {
      MetaProperty link = getNavigationProperty(property);
      if (link != null) {
        link.addAttribute(USED_FOR_NAVIGATION, Boolean.toString(true));
      }
      changeable |= property.retrieveIsChangeable();
      vetoable |= property.retrieveIsVetoable();
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      MetaProperty link = getNavigationProperty(property);
      if (link != null) {
        link.addAttribute(USED_FOR_NAVIGATION, Boolean.toString(true));
      }
      changeable |= property.retrieveIsChangeable();
      vetoable |= property.retrieveIsVetoable();
      preProcessClassifier(property, INTERFACE_CLASS, true);
      preProcessClassifier(property, CONTAINER_CLASS, false);
      preProcessClassifier(property, KEY_CLASS, false);
    }
    preProcessBean(clazz, changeable, vetoable);
  }

  @Override
  public void processPackage(MetaPackage packages) {
    assert packages != null;
    this.packages = packages;
    String rootDirectory = packages.valueOfTag(ROOT);
    if (rootDirectory != null) {
      rootDirectory = getContext().getProperty(rootDirectory);
      if (rootDirectory == null) {
        log.error(packages.valueOfTag(ROOT)
            + " logical root directory undefined");
      }
    }
    if (rootDirectory == null) {
      rootDirectory = getContext().getProperty(ROOT_COMMON_FOLDER_KEY);
    }
    root = this.mkdirs(rootDirectory);
    hasDataObjects = false;
    hasCodes = false;
    if (!packages.booleanValueOfTag(GENERATE)) {
      return;
    }
    for (MetaClass clazz : packages.getClasses()) {
      hasDataObjects |= clazz.hasStereotype(DATA_OBJECT_STEREOTYPE);
      hasCodes |= clazz.hasStereotype(REFERENCE_CODE_STEREOTYPE);
    }
  }

  @Override
  public void processClass(MetaClass clazz) {
    assert clazz != null;
    // class is only emitted if the class is relevant and the package should be
    // generated
    if (!clazz.retrievePackage().booleanValueOfTag(GENERATE)) {
      return;
    }
    VelocityContext context = createContext(clazz);
    try {
      Map<String, List<String>> blocks = extractUserCode(packages
          .getAttribute(BO_PACKAGE), clazz.getAttribute(BO_CLASS));
      context.put(CODE_BLOCKS, blocks);
      if (clazz.hasStereotype(DATA_OBJECT_STEREOTYPE)) {
        processJavaTemplate(root, packages.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), retrieveTemplate("bo"), context);
        if (hasLightweight(clazz)) {
          blocks = extractUserCode(packages.getAttribute(LW_PACKAGE), clazz
              .getAttribute(LW_CLASS));
          context.put(CODE_BLOCKS, blocks);
          processJavaTemplate(root, packages.getAttribute(LW_PACKAGE), clazz
              .getAttribute(LW_CLASS), retrieveTemplate("lw"), context);
        }
      } else if (clazz.hasStereotype(REFERENCE_CODE_STEREOTYPE)) {
        processJavaTemplate(root, packages.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), retrieveTemplate("code"), context);
      } else if (clazz.booleanValueOfTag(BO_GENERATE)) {
        processJavaTemplate(root, packages.getAttribute(BO_PACKAGE), clazz
            .getAttribute(BO_CLASS), retrieveTemplate("class"), context);
      }
    } catch (IOException e) {
      log.error(e);
    }
  }

  @Override
  public void postProcessPackage(MetaPackage metaPackage) {
    if (metaPackage.booleanValueOfTag(GENERATE)) {
      VelocityContext context = createContext(metaPackage);
      boolean cyclicVisitor = Boolean.parseBoolean(getContext().getProperty(
          CYCLIC_WRAPPER_KEY));
      try {
        if (hasDataObjects) {
          String packages = metaPackage.getAttribute(IF_PACKAGE);
          processJavaTemplate(root, packages, "Visitor",
              retrieveTemplate("visitor-if-bo"), context);
          processJavaTemplate(root, packages, "VisitorFunctor",
              retrieveTemplate("visitor-functor-bo"), context);
          if (cyclicVisitor) {
            processJavaTemplate(root, packages, "CyclicVisitor",
                retrieveTemplate("visitor-cyclic-bo"), context);
          }
          processJavaTemplate(root, metaPackage.getAttribute(BO_PACKAGE),
              "TypeFactory", retrieveTemplate("type-factory"), context);
        }
        if (metaPackage.booleanValueOfAttribute(BO_LIGHTWEIGHT)) {
          String packages = metaPackage.getAttribute(LW_PACKAGE);
          processJavaTemplate(root, packages, "LwVisitor",
              retrieveTemplate("visitor-if-lw"), context);
          processJavaTemplate(root, packages, "LwVisitorFunctor",
              retrieveTemplate("visitor-functor-lw"), context);
          processJavaTemplate(root, packages, "LwTransformer",
              retrieveTemplate("transformer-lw"), context);
          if (cyclicVisitor) {
            processJavaTemplate(root, packages, "LwCyclicVisitor",
                retrieveTemplate("visitor-cyclic-lw"), context);

          }
        }
      } catch (IOException e) {
        log.error(e);
      }
    }
  }

  /**
   * Returns true if the property is navigable in the lightweight
   * representation. The conditions are that the property is lightweight,
   * navigable and that the navigation property is also lightweight.
   * @param property property to infer
   * @return true if the property is navigable in the lightweight model
   *         otherwise false
   * @pre property != null
   */
  public boolean isLightweightNavigable(MetaProperty property) {
    if (property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
      MetaProperty link = getNavigationProperty(property);
      return (link != null)
          && link.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT);
    }
    return false;
  }

  /**
   * Returns the property used to implement the back link.
   * @param property property to infer
   * @return the back link property if defined otherwise null
   * @pre property != null
   */
  public MetaProperty getNavigationProperty(MetaProperty property) {
    assert property != null;
    MetaProperty link = null;
    String navigation = property.valueOfTag(BoConstants.NAVIGABLE_RELATION);
    if ((navigation != null) && (navigation.trim().length() > 0)) {
      MetaClassifier classifier = property.getClassifier();
      if (classifier instanceof MetaClass) {
        MetaClass clazz = (MetaClass) classifier;
        link = clazz.findProperty(navigation);
      }
    }
    return link;
  }

  /**
   * Returns the Java type name of the type of the property. The translation of
   * datatypes is handled.
   * @param property property to infer.
   * @return the component name of the property type.
   */
  public String getDatatype(MetaProperty property) {
    String qualifiedName = property.getClassifier().getAttribute(BO_TYPE_JAVA);
    if (qualifiedName == null) {
      qualifiedName = property.getTypeName();
    }
    return ModelOperators.getComponentName(qualifiedName);
  }

  /**
   * Computes the transitive closure of all data objects referenced from all
   * data objects defined in the package.
   * @param packages packages which transitive closure should be computed
   * @return the list of data objects accesible from the root data objects
   */
  public Set<MetaClass> computeTransitiveClosure(MetaPackage packages) {
    Set<MetaClass> classes = new HashSet<MetaClass>();
    for (MetaClass clazz : packages.getClasses()) {
      computeTransitiveClosure(clazz, classes);
    }
    return classes;
  }

  /**
   * Computes the transitive closure of all data objects referenced from the
   * given data object.
   * @param clazz which transitive closure should be computed
   * @param classes set in which the transitive closure of classes is added
   */
  public void computeTransitiveClosure(MetaClass clazz, Set<MetaClass> classes) {
    if ((clazz.findStereotype(DATA_OBJECT_STEREOTYPE) != null)
        && !classes.contains(clazz)) {
      classes.add(clazz);
      for (MetaProperty property : clazz.getProperties()) {
        MetaClassifier classifier = property.getClassifier();
        if ((classifier != null) && (classifier instanceof MetaClass)) {
          MetaClass type = (MetaClass) classifier;
          if (classifier.findStereotype(DATA_OBJECT_STEREOTYPE) != null) {
            classes.add(type);
            computeTransitiveClosure(type, classes);
          }
        }
      }
      for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
        MetaClassifier classifier = property.getClassifier();
        if ((classifier != null) && (classifier instanceof MetaClass)) {
          MetaClass type = (MetaClass) classifier;
          if (classifier.findStereotype(DATA_OBJECT_STEREOTYPE) != null) {
            classes.add(type);
            computeTransitiveClosure(type, classes);
          }
        }
      }
    }
  }

  /**
   * Generates the import statements necessary for the class defining the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *          artifact.
   * @return list of all import statements for the class.
   * @pre (clazz != null) && (packageName != null)
   */
  public ImportStatements retrieveImportStatementsForBo(MetaClass clazz,
      String packageName) {
    assert (clazz != null) && (packageName != null);
    return retrieveImportStatements(packageName, clazz, null, BO_CLASS);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *          artifact.
   * @return list of all import statements for the class.
   * @pre (clazz != null) && (packageName != null)
   */
  public ImportStatements retrieveImportStatementsForIf(MetaClass clazz,
      String packageName) {
    assert (clazz != null) && (packageName != null);
    return retrieveImportStatements(packageName, clazz, null, IF_CLASS);
  }

  /**
   * Generates the import statements necessary for the class definint the data
   * object type.
   * @param clazz class for which the import statements should be retrieved.
   * @param packageName qualified name of the package containing the generated
   *          artifact.
   * @return list of all import statements for the class.
   * @pre (clazz != null) && (packageName != null)
   */
  public ImportStatements retrieveImportStatementsForLw(MetaClass clazz,
      String packageName) {
    assert (clazz != null) && (packageName != null);
    return retrieveImportStatements(packageName, clazz, BO_LIGHTWEIGHT,
        LW_CLASS);
  }

  /**
   * Generates the import statements necessary for the class. The import
   * statement should be only used if the classes belong to another package.
   * @param packageName qualified name of the package containing the generated
   *          artifact.
   * @param clazz class for which the import statements should be retrieved.
   * @param key key of the tag or attribute constraining the properties, which
   *          dependencies should be imported.
   * @param kind kind of type to be retrieved from the data object type of the
   *          property.
   * @return list of all import statements for the class.
   */
  private ImportStatements retrieveImportStatements(String packageName,
      MetaClass clazz, String key, String kind) {
    ImportStatements imports = new ImportStatements(packageName);
    for (MetaClass item : clazz.retrieveAncestors()) {
      addImportStatement(imports, clazz, item, kind);
    }
    for (MetaClass item : clazz.retrieveInterfaces()) {
      addImportStatement(imports, clazz, item, kind);
    }
    for (MetaProperty property : clazz.getProperties()) {
      if ((key == null)
          || (property.booleanValueOfAttribute(key) || property
              .booleanValueOfTag(key))) {
        MetaClass type = clazz.getModel().findClass(property.getTypeName());
        if (type != null) {
          addImportStatement(imports, clazz, type, kind);
        } else {
          addImportStatement(imports, clazz, property.getTypeName());
        }
      }
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      if ((key == null)
          || (property.booleanValueOfAttribute(key) || property
              .booleanValueOfTag(key))) {
        MetaClass type = clazz.getModel().findClass(property.getTypeName());
        if (type != null) {
          addImportStatement(imports, clazz, type, kind);
        } else {
          addImportStatement(imports, clazz, property.getTypeName());
        }
        imports.addClassName("java.util.Collection");
        imports.addClassName("java.util.Iterator");
        addImportStatement(imports, clazz, property.valueOfTag(INTERFACE_CLASS));
        addImportStatement(imports, clazz, property.valueOfTag(CONTAINER_CLASS));
        if (isOrdered(property)) {
          type = clazz.getModel().findClass(property.valueOfTag(KEY_CLASS));
          if (type != null) {
            imports.addClass(type);
          }
        }
      }
    }
    return imports;
  }

  /**
   * Adds the import statement for the type entity. If the attribute is defined
   * it is used otherwise the qualified type name is used.
   * @param statements statements to add to.
   * @param clazz
   * @param type
   * @param attribute
   */
  private void addImportStatement(ImportStatements statements, MetaClass clazz,
      MetaClass type, String attribute) {
    String typeName = type.getAttribute(attribute);
    if (typeName == null) {
      addImportStatement(statements, clazz, type.getQualifiedName());
    } else {
      statements.addClassName(typeName);
    }
  }

  /**
   * Adds the qualified type name to the import statements.
   * @param statements statements to add.
   * @param clazz clazz to which the statements should be added.
   * @param qualifiedName qualified name of the class to import.
   */
  private void addImportStatement(ImportStatements statements, MetaClass clazz,
      String qualifiedName) {
    if (MdaUtilities.isStandardItem(qualifiedName)) {
      statements.addClassName(qualifiedName);
    } else {
      MetaModel model = clazz.getModel();
      MetaDatatype datatype = model.findDatatype(qualifiedName);
      if (datatype != null) {
        statements.addClassName(datatype.getAttribute(BO_TYPE_JAVA));
      } else {
        MetaClass type = model.findClass(qualifiedName);
        if (type != null) {
          statements.addClass(type);
        } else {
          if (log.isErrorEnabled()) {
            Object[] parameters = { qualifiedName, clazz.getQualifiedName() };
            log
                .error(MessageFormat
                    .format(
                        "Non standard class {0} used in class {1} is not defined in model",
                        parameters));
          }
        }
      }
    }
  }

  private boolean hasLightweight(MetaPackage metaPackage) {
    boolean hasLightweight = false;
    for (MetaClass clazz : metaPackage.getClasses()) {
      hasLightweight = hasLightweight(clazz);
      if (hasLightweight) {
        break;
      }
    }
    return hasLightweight;
  }

  /**
   * Returns true if the meta class has lightweight properties.
   * @param clazz class to check if it has lightweight properties.
   * @return true if the class has lightweights items otherwise false.
   * @pre clazz != null
   */
  private boolean hasLightweight(MetaClass clazz) {
    assert clazz != null;
    boolean hasLightweight = false;
    for (MetaProperty property : clazz.getProperties()) {
      hasLightweight = property.booleanValueOfTag(LIGHTWEIGHT);
      if (hasLightweight) {
        break;
      }
    }
    if (!hasLightweight) {
      for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
        hasLightweight = property.booleanValueOfTag(LIGHTWEIGHT);
        if (hasLightweight) {
          break;
        }
      }
    }
    if (!hasLightweight) {
      for (MetaClass ancestor : clazz.retrieveAncestors()) {
        hasLightweight = hasLightweight(ancestor);
        if (hasLightweight) {
          break;
        }
      }
    }
    return hasLightweight;
  }

  /**
   * Extracts all blocks of declarations defined by the developer. These blocks
   * are stored in the class. The file containing the source code is inferred
   * from the global properties.
   * @param packageName name of the package containing the Java class.
   * @param classname name of the Java class.
   * @return the map of retrieved user code blocks indexed with the block name.
   * @throws IOException if error was encountered when reading the file.
   */
  private Map<String, List<String>> extractUserCode(String packageName,
      String classname) throws IOException {
    Map<String, List<String>> blocks = new HashMap<String, List<String>>();
    File source = FileOperators.createFile(root, packageName, classname
        + JAVA_EXTENSION);
    blocks.put(IMPORT_BLOCK, FileOperators.extractSourceCode(source,
        USER_IMPORT_BEGIN, USER_IMPORT_END));
    blocks.put(DECLARATION_BLOCK, FileOperators.extractSourceCode(source,
        USER_DECLARATION_BEGIN, USER_DECLARATION_END));
    blocks.put(CONSTRUCTOR_BLOCK, FileOperators.extractSourceCode(source,
        USER_CONSTRUCTORS_BEGIN, USER_CONSTRUCTORS_END));
    blocks.put(DEFINITION_BLOCK, FileOperators.extractSourceCode(source,
        USER_CODE_BEGIN, USER_CODE_END));
    blocks.put(PRIVATE_DEFINITION_BLOCK, FileOperators.extractSourceCode(
        source, USER_PRIVATE_CODE_BEGIN, USER_PRIVATE_CODE_END));
    return blocks;
  }

  /**
   * Retrieves the velocity template with the given name.
   * @param name name of the template to retrieve.
   * @return the template if found otherwise null.
   */
  private Template retrieveTemplate(String name) {
    return super.retrieveTemplate(name, getContext().getProperty(LANGUAGE_KEY));
  }

  /**
   * Preprocesses the classifier of an element.
   * @param element element to preprocess.
   * @param tag tag which type should be infer against the data type translation
   *          table associated with the cartridge.
   * @param interfaces flag indicating if the translation is for an interface or
   *          a concrete class.
   */
  private void preProcessClassifier(MetaElement element, String tag,
      boolean interfaces) {
    String qualifiedName = element.valueOfTag(tag);
    if (qualifiedName != null) {
      MetaDatatype datatype = packages.getModel().findDatatype(qualifiedName);
      if (datatype != null) {
        qualifiedName = translateDatatype(datatype, LANGUAGE, interfaces);
      }
    }
    qualifiedName = ModelOperators.getComponentName(qualifiedName);
    element.addAttribute(tag, qualifiedName);
  }

  /**
   * Propagates the vetoable and changeable information through the class
   * inheritance hierarchy.
   * @param clazz class to process.
   * @param changeable flag indicating if the class has a changeable property.
   * @param vetoable flag indicating if the class has a vetoable property.
   */
  private void preProcessBean(MetaClass clazz, boolean changeable,
      boolean vetoable) {
    clazz.addAttribute(BO_CHANGEABLE, Boolean.toString(clazz
        .booleanValueOfAttribute(BO_CHANGEABLE)
        || changeable));
    clazz.addAttribute(BO_VETOABLE, Boolean.toString(clazz
        .booleanValueOfAttribute(BO_VETOABLE)
        || vetoable));
    if (changeable || vetoable) {
      for (MetaClass ancestor : clazz.retrieveAncestors()) {
        if (ancestor.hasStereotype(DATA_OBJECT_STEREOTYPE)) {
          preProcessBean(ancestor, changeable, vetoable);
        }
      }
    }
  }
}