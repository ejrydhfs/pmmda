/* copyright 2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.rose;

import java.util.StringTokenizer;

import org.eclipse.emf.importer.rose.builder.RoseStrings;
import org.eclipse.emf.importer.rose.builder.RoseVisitor;
import org.eclipse.emf.importer.rose.parser.RoseNode;
import org.eclipse.emf.importer.rose.parser.Util;

/**
 * The RoseLocatorVisitor searches for a qualified name within a rose model.
 * The qualified name is the identification of a certain rose element. Currently
 * RoseStrings.CLASS_CATEGORY/CLASS/CLASSATTRIBUTE are supported. If the qualified name of
 * the searched rose element is found within the model then the found property is set to true and
 * the result will be different from null. If not found then the closestMatch and closestQualifiedName
 * are filled.
 * 
 * Examples:
 *  searchCriteria: ch.bbv.Test, RoseStrings.CLASS
 *  If a class "Test" with searched qualified name is within the model then result is that node and found is true.
 *  If a package "ch.bbv" is available but no class "test" then the closest match attributes will contain that category.
 *  If no package with name "ch" is within the model then the closest match attributes will be null. 
 * @author AdrianBachofen
 *
 */
public class RoseLocatorVisitor implements RoseVisitor {

  private Boolean found = false;
  private String searchCriteria = "";
  private String searchType = "";
  private RoseNode result = null;
  private RoseNode closestMatch = null;
  private String closestQualifiedName = "";
  
  /**
   * Constructor of the RoseLocatorVisitor.
   * @param qualifiedName String to be searched within the model. The name is case sensitive. A trailing '.' will not be considered.
   * @param roseString Type of model element to be searched (CLASS_CATEGORY, CLASS, CLASSATTRIBUTE).
   */
  public RoseLocatorVisitor(String qualifiedName, String roseString) {
    setSearchCriteria(qualifiedName, roseString);
  }
  
  /**
   * Sets a new search criteria for the RoseLocatorVisitor.
   * @param qualifiedName String to be searched within the model. The name is case sensitive. A trailing '.' will not be considered.
   * @param roseString Type of model element to be searched (CLASS_CATEGORY, CLASS, CLASSATTRIBUTE).
   */
  public void setSearchCriteria(String qualifiedName, String roseString) {
    assert(roseString.equals(RoseStrings.CLASS_CATEGORY) || roseString.equals(RoseStrings.CLASS) || roseString.equals(RoseStrings.CLASSATTRIBUTE));
    found = false;
    searchCriteria = qualifiedName;
    searchType = roseString;
    result = null;
    closestMatch = null;
    closestQualifiedName = "";
  }
  
  /**
   * Returns the search criteria set by the user.
   * @return qualified name.
   */
  public String getSearchCriteria() {
    return searchCriteria;
  }
  
  /**
   * Returns the type of RoseNode to be searched.
   * @return type.
   */
  public String getSearchType() {
    return searchType;
  }

  /**
   * Returns the matching RoseNode. If no match then it is null.
   * @return RoseNode or null.
   */
  public RoseNode getResult() {
    return result;
  }

  /**
   * Returns the best match for the RoseNode. This means that as many name element of the qualified name match.
   * Example: SearchCriteria: ch.bbv.Test, RoseStrings.CLASS. A class 'ch.bbv.T' within the model will not be returned. Then 'ch.bbv' will be closest match.
   *          A package 'ch.b' will not be returned. The package 'ch' will be returned. 
   * @return null or best matching node.
   */
  public RoseNode getBestMatch() {
    return closestMatch;
  }
  
  /**
   * Closest matching qualified name. If none found then it is empty.
   * @return empty or best match.
   */
  public String getBestMatchingQualifiedName() {
    return closestQualifiedName;
  }
  
  /**
   * Visit function typically used by RoseWalker. 
   */
  public void visitList(RoseNode roseNode)
  {
  }

  /**
   * Visit function typically used by RoseWalker. 
   */
  public void visitObject(RoseNode roseNode)
  {
    if(found) return;
    
    String roseNodeValue = roseNode.getValue();
    String objectKey = roseNode.getKey();
    String objectType = Util.getType(roseNodeValue);
    String objectName = Util.getName(roseNodeValue);

    //System.out.println("---> visitObject,,, objectKey - objectType - objectName: " + objectKey + ", " + objectType + ", " + objectName);

    visitObject(roseNode, roseNodeValue, objectKey, objectType, objectName);
  }

  protected void visitObject(RoseNode roseNode, String roseNodeValue, String objectKey, String objectType, String objectName)
  {
    if (objectKey.equals("") && objectType.equals(RoseStrings.CLASS_CATEGORY))
    {
      visitClassCategory(roseNode, roseNodeValue, objectKey, objectName);
    }
    else if (objectType.equals(RoseStrings.CLASS))
    {
      visitClass(roseNode, roseNodeValue, objectKey, objectName);
    }
    else if (objectType.equals(RoseStrings.CLASSATTRIBUTE) && (!roseNode.isDerived() || !"reference".equals(roseNode.getStereotype())))
    {
      visitClassattribute(roseNode, roseNodeValue, objectKey, objectName);
    }
  }
  
  protected void visitClassCategory(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName) {
    doMatch(roseNode);
  }

  protected void visitClass(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName) {
    doMatch(roseNode);
  }

  protected void visitClassattribute(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName) {
    doMatch(roseNode);
  }

  protected void doMatch(RoseNode roseNode) {
    String qualifiedName = this.getQualifiedName(roseNode);
    if(qualifiedName.equals(searchCriteria)) {
      found = true;
      result = roseNode;
      return;
    }
    String c = compareQualifiedNames(searchCriteria, qualifiedName);
    if(c.length() > closestQualifiedName.length() && c.equals(qualifiedName)) {
      closestQualifiedName = c;
      closestMatch = roseNode;
    }
  }
  
  /**
   * Tests how many name elements of the current Name match those of searchCriteria.
   * Example: SearchCriteria: "ch.bbv.abc", CurrentName: "ch.bbv" -> result will be "ch.bbv".
   * Example: SearchCriteria: "ch.bbv.abc", CurrentName: "de.bbv" -> result will be "".
   * Example: SearchCriteria: "ch.bbv.abc", CurrentName: "ch.abc" -> result will be "ch".
   */
  protected String compareQualifiedNames(String searchCriteria, String currentName) {
    assert(searchCriteria != null && currentName != null);
    if(searchCriteria.equals(currentName)) return searchCriteria;
    StringTokenizer s = new StringTokenizer(searchCriteria, ".");
    StringTokenizer c = new StringTokenizer(currentName, ".");
    String closestMatch = "";
    while (s.hasMoreTokens() && c.hasMoreTokens())
    {
      String t1 = s.nextToken();
      String t2 = c.nextToken();
      if(t1.equals(t2)) {
        closestMatch += t1 + ".";
      } else {
        break;
      }
    }
    if(closestMatch.length() > 0) return closestMatch.substring(0, closestMatch.length()-1);
    return closestMatch;
  }
  
  /**
   * Returns the qualified name of the passed roseNode.
   * @param roseNode to determine the qualified name of.
   * @return the qualified name of the roseNode.
   */
  protected String getQualifiedName(RoseNode roseNode) {
    String temp = Util.getName(roseNode.getValue());
    while(roseNode.getParent() != null) {
      roseNode = roseNode.getParent();
      String currentValue = roseNode.getValue(); 
      if(Util.getType(currentValue).equals(RoseStrings.CLASS_CATEGORY) || Util.getType(currentValue).equals(RoseStrings.CLASS)) {
        if(Util.getName(currentValue).equals(RoseStrings.LOGICAL_VIEW) || Util.getName(currentValue).equals(RoseStrings.USE_CASE_VIEW)) break;
        temp = Util.getName(currentValue) + "." + temp;
      }
    }
    return temp;
  }

}
