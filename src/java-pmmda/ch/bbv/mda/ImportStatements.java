/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.*;

/** The import statement contains all classes imported in a class file to
 * realize the services of the class. The class provides services to handle
 * the star notation to imply all classes of a package and define the number of
 * imports for a package before the star notation is used.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class ImportStatements {
	/** The wildcard for specifying all classes of a package.
	 */
  public static final String ALL_CLASSES = "*";
  
  /** The default package always implicitely available in the Java environment.
   */
  public static final String JAVA_LANG = "java.lang";

  /** List of the imported packages. Each entry of the map is again the list
   * of its classes imported in the class file.
   */
  private Map packageNames;

  /** Name of the package where the item defining the import statements is
   * located.
   */
  private String context;

  /** Default constructor of the class.
   */
  public ImportStatements() {
    this(null);
  }

  /** Constructor of the class.
   * @param context name of the package containing the class which import
   * statements are computed.
   */
  public ImportStatements(String context) {
    this.packageNames = new HashMap();
    this.context = context;
  }

  /** Set the package context of the item defining the import statements.
   * @param context name of the package containing the class which import
   * statements are computed.
   */
  public void setPackageContext(String context) {
    this.context = context;
  }

  /** Returns the list of imported packages.
   * @return list of imported packages.
   */
  public Collection getPackageNames() {
    return new ArrayList(packageNames.keySet());
  }

  /** Returns the list of all imported classes of the named package.
   * @param packageName name of the package which classes are inquired.
   * @return list of all imported classes.
   * @pre pacakgeName != null
   */
  public Collection getClassNames(String packageName) {
    Map classes = (Map) packageNames.get(packageName);
    Collection classNames;
    if (classes != null) {
      classNames = classes.values();
    } else {
      classNames = Collections.EMPTY_LIST;
    }
    return classNames;
  }

  /** Returns the number of imported classes of this package.
   * @return number of imported classes.
   * @param packageName name of the package which classes are inquired.
   * @pre pacakgeName != null
   * @post $return >= 0
   */
  public int getNrOfClasses(String packageName) {
    int nrOfClasses = 0;
    Map classes = (Map) packageNames.get(packageName);
    if (classes != null) {
      if (classes.containsValue(ALL_CLASSES)) {
        nrOfClasses = Integer.MAX_VALUE;
      } else {
        nrOfClasses = classes.size();
      }
    }
    return nrOfClasses;
  }

	/** Adds an imported class to the list of classes.
	 * @param clazz class to add.
	 * @pre clazz != null
	 */
  public void addClass(MetaClass clazz) {
    assert (clazz != null);
    addClassName(clazz.qualifiedName());
  }

  /** Adds a class to the list of imported classes.
   * @param qualifiedName not qualified name of the classs.
   * @pre qualifiedName != null && qualifiedName.length() > 0
   */
  public void addClassName(String qualifiedName) {
    String name = MetaClass.getPackageName(qualifiedName);
    if (!isRelevant(name)) {
      return;
    }

    Map classes = (Map) packageNames.get(name);
    if (classes == null) {
      classes = new HashMap();
      packageNames.put(name, classes);
    }
    name = MetaClass.getClassName(qualifiedName);
    classes.put(name, name);
  }

	/** Adds a package and all its classes to the list of import statements.
	 * @param packageName qualified name of the package to add.
	 */
  public void addPackageName(String packageName) {
    StringBuffer buffer = new StringBuffer();
    if (!isRelevant(packageName)) {
      return;
    }
    buffer.append(packageName).append(".*");
    addClassName(buffer.toString());
  }

	/** Returns the list of import statements.
	 * @param limit the limit value specifies the threshold when wildcard should
	 * be used.
	 * @return the list of import statements.
	 */
  public List getImportStatements(int limit) {
    List imports = new ArrayList();
    Iterator names = packageNames.keySet().iterator();
    StringBuffer buffer = new StringBuffer();
    while (names.hasNext()) {
      String name = (String) names.next();
      buffer.setLength(0);
      buffer.append(name).append(".");

      int length = buffer.length();
      Set classes = ((Map) packageNames.get(name)).keySet();
      if (classes.size() >= limit) {
        buffer.append("*");
        imports.add(buffer.toString());
      } else {
        Iterator classnames = classes.iterator();
        while (classnames.hasNext()) {
          buffer.append((String) classnames.next());
          imports.add(buffer.toString());
          buffer.setLength(length);
        }
      }
    }
    Collections.sort(imports);
    return imports;
  }

	/** Returns true if the package name is relevant for the import statements.
	 * To be relevant the package must neither be the default package or the
	 * package of the class which import statements should be infered.
	 * @param packageName name of the package to check.
	 * @return true if the package is relevant otherwise false.
	 */ 
  private boolean isRelevant(String packageName) {
    return (packageName != null) &&
    (!packageName.equals(context) && !packageName.equals(JAVA_LANG));
  }
}
