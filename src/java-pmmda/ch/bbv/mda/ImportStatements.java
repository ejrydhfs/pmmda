/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

import ch.bbv.mda.operators.ModelOperators;

/**
 * The import statement contains all classes imported in a class file to realize
 * the services of the class. The imported classes are infered from the
 * interfaces the class implements, the ones it inherits from and all the types
 * used to define fields, parameters and return types. The class provides
 * services to handle the star notation to imply all classes of a package and
 * define the number of imports for a package before the star notation is used.
 * The class can also be used for other languages than Java as long as they do
 * not defined a package called "java.lang".
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ImportStatements {
  /**
   * The wildcard for specifying all classes of a package.
   */
  public static final String ALL_CLASSES = "*";

  /**
   * The default package always implicitely available in the Java environment.
   */
  public static final String JAVA_LANG = "java.lang";

  /**
   * List of the imported packages. Each entry of the map is again the list of
   * its classes imported in the class file.
   */
  private Map<String, Map<String, String>> packages;

  /**
   * Name of the package where the item defining the import statements is
   * located.
   */
  private String context;

  /**
   * Default constructor of the class.
   */
  public ImportStatements() {
    this(null);
  }

  /**
   * Constructor of the class.
   * @param context name of the package containing the class which import
   *          statements are computed
   * @pre context != null
   */
  public ImportStatements(String context) {
    assert context != null;
    this.packages = new HashMap<String, Map<String, String>>();
    this.context = context;
  }

  /**
   * Sets the package context of the item defining the import statements.
   * @param context name of the package containing the class which import
   *          statements are computed
   * @pre context != null
   */
  public void setPackageContext(String context) {
    assert context != null;
    this.context = context;
  }

  /**
   * Returns the names of all imported packages.
   * @return list of imported packages
   */
  public Collection<String> getPackageNames() {
    return new ArrayList<String>(packages.keySet());
  }

  /**
   * Returns the list of all imported classes of the named package.
   * @param packageName name of the package which classes are inquired
   * @return list of all imported classes
   * @pre packageName != null
   */
  public Collection<String> getClassNames(String packageName) {
    assert packageName != null;
    Map<String, String> classes = packages.get(packageName);
    Collection<String> classNames;
    if (classes != null) {
      classNames = classes.values();
    } else {
      classNames = Collections.emptyList();
    }
    return classNames;
  }

  /**
   * Returns the number of imported classes of this package.
   * @return number of imported classes
   * @param packageName name of the package which classes are inquired
   * @pre packageName != null
   * @post $return >= 0
   */
  public int getNrOfClasses(String packageName) {
    assert packageName != null;
    int nrOfClasses = 0;
    Map<String, String> classes = packages.get(packageName);
    if (classes != null) {
      if (classes.containsValue(ALL_CLASSES)) {
        nrOfClasses = Integer.MAX_VALUE;
      } else {
        nrOfClasses = classes.size();
      }
    }
    return nrOfClasses;
  }

  /**
   * Adds an imported class to the list of imported classes.
   * @param clazz class to add
   * @pre clazz != null
   */
  public void addClass(MetaClass clazz) {
    assert clazz != null;
    addClassName(clazz.getQualifiedName());
  }

  /**
   * Adds the collection of class to the list of imported classes.
   * @param classes classes to add
   * @pre classes != null
   */
  public void addClasses(Collection<MetaClass> classes) {
    assert classes != null;
    for (MetaClass clazz : classes) {
      addClass(clazz);
    }
  }
  
  /**
   * Adds a class to the list of imported classes. The package of the class is
   * added if not already defined.
   * @param qualifiedName not qualified name of the classs
   * @pre (qualifiedName != null) && (qualifiedName.length() > 0)
   */
  public void addClassName(String qualifiedName) {
    assert (qualifiedName != null) && (qualifiedName.length() > 0);
    String name = ModelOperators.getContextName(qualifiedName);
    if (!isRelevant(name)) {
      return;
    }

    Map<String, String> classes = packages.get(name);
    if (classes == null) {
      classes = new HashMap<String, String>();
      packages.put(name, classes);
    }
    name = ModelOperators.getComponentName(qualifiedName);
    classes.put(name, name);
  }

  /**
   * Adds a package and all its classes to the list of import statements.
   * @param packageName qualified name of the package to add.
   * @pre (packageName != null) && (packageName.length() > 0)
   */
  public void addPackageName(String packageName) {
    assert (packageName != null) && (packageName.length() > 0);
    StringBuilder buffer = new StringBuilder();
    buffer.append(packageName).append(".*");
    addClassName(buffer.toString());
  }

  /**
   * Returns the list of import statements lexicographically sorted.
   * @param limit the limit value specifies the threshold when wildcard should
   *          be used. If the value is -1 no check is performed
   * @return the list of import statements
   * @pre (limit > 0) || (limit == -1)
   */
  public List<String> getImportStatements(int limit) {
    assert (limit > 0) || (limit == -1);
    List<String> imports = new ArrayList<String>();
    StringBuilder buffer = new StringBuilder();
    for (String name : packages.keySet()) {
      buffer.setLength(0);
      buffer.append(name).append(".");
      int length = buffer.length();
      Set<String> classes = packages.get(name).keySet();
      if (((limit != -1) && (classes.size() >= limit))
          || classes.contains(ALL_CLASSES)) {
        buffer.append("*");
        imports.add(buffer.toString());
      } else {
        for (String classname : classes) {
          buffer.append(classname);
          imports.add(buffer.toString());
          buffer.setLength(length);
        }
      }
    }
    Collections.sort(imports);
    return imports;
  }

  /**
   * Returns true if the package name is relevant for the import statements. To
   * be relevant the package must neither be the default package or the package
   * of the class which import statements should be infered.
   * @param packageName name of the package to check
   * @return true if the package is relevant otherwise false
   */
  private boolean isRelevant(String packageName) {
    return (packageName != null)
        && (!packageName.equals(context) && !packageName.equals(JAVA_LANG));
  }
}
