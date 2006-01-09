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

public class RoseLocatorVisitor implements RoseVisitor {

  private Boolean found = false;
  private String searchCriteria = "";
  private String searchType = "";
  private RoseNode result = null;
  private RoseNode closestMatch = null;
  private String closestQualifiedName = "";
  
  public RoseLocatorVisitor(String qualifiedName, String roseString) {
    setSearchCriteria(qualifiedName, roseString);
  }
  
  public void setSearchCriteria(String qualifiedName, String roseString) {
    assert(roseString.equals(RoseStrings.CLASS_CATEGORY) || roseString.equals(RoseStrings.CLASS) || roseString.equals(RoseStrings.CLASSATTRIBUTE));
    found = false;
    searchCriteria = qualifiedName;
    searchType = roseString;
    result = null;
    closestMatch = null;
    closestQualifiedName = "";
  }
  
  public String getSearchCriteria() {
    return searchCriteria;
  }
  
  public String getSearchType() {
    return searchType;
  }

  public RoseNode getResult() {
    return result;
  }

  public RoseNode getBestMatch() {
    return closestMatch;
  }
  
  public String getBestMatchingQualifiedName() {
    return closestQualifiedName;
  }
  
  public void visitList(RoseNode roseNode)
  {
  }

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
        if(Util.getName(currentValue).equals(RoseStrings.LOGICAL_VIEW)) break;
        temp = Util.getName(currentValue) + "." + temp;
      }
    }
    return temp;
  }

}
