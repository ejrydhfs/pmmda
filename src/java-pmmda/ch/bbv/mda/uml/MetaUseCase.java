/* copyright 2000-2006 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.uml;

import java.util.ArrayList;
import java.util.List;

import ch.bbv.mda.*;
import ch.bbv.mda.MetaClassifier;

/**
 * The MetaUseCase represents a use case according to the uml 2 standard. Currently we just use it as a type node in order
 * to prevent dangling relations to use cases.
 * 
 * @author Adrian Bachofen
 * @version $Revision$
 */
public class MetaUseCase extends MetaClassifier {

  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20060705;

  /**
   * List of ancestors of the use case. An ancestor is a use case by itself.
   */
  private List<MetaUseCase> ancestors;

  public MetaUseCase() {
    ancestors = new ArrayList<MetaUseCase>();
  }
  
  /**
   * Adds an ancestor to the use case.
   * @param ancestor ancestor to add.
   */
  public void addAncestor(MetaUseCase ancestor) {
    ancestors.add(ancestor);
  }

  /**
   * Retrieves all the use cases extended in this use case. In Java the list can
   * contains at most one item.
   * @return list of all extended implemented in the use case.
   */
  public List<MetaUseCase> retrieveAncestors() {
    return ancestors;
  }

  /**
   * Returns true if the use case or one of its ancestors has an ancestor with the
   * given qualified name.
   * @param qualifiedName qualified name of the ancestor.
   * @return true if one ancestor has the requested name otherwise false.
   */
  public boolean hasAncestor(String qualifiedName) {
    assert qualifiedName != null;
    MetaClassifier ancestor = getModel().findClassifier(qualifiedName);
    boolean hasAncestor = false;
    if (ancestor != null && ancestor instanceof MetaUseCase) {
      hasAncestor = hasAncestor((MetaUseCase)ancestor);
    }
    return hasAncestor;
  }

  /**
   * Returns true if the class or one of its ancestors has the given use case as
   * ancestor.
   * @param useCase ancestor use case to infer.
   * @return true if the given use case is an ancestor otherwise false.
   */
  public boolean hasAncestor(MetaUseCase useCase) {
    assert useCase != null;
    List<MetaUseCase> ancestors = retrieveAncestors();
    boolean hasAncestor = ancestors.contains(useCase);
    if (!hasAncestor) {
      for (MetaUseCase ancestor : ancestors) {
        hasAncestor = ancestor.hasAncestor(useCase);
        if (hasAncestor) {
          break;
        }
      }
    }
    return hasAncestor;
  }

  /**
   * Implements the visitor pattern.
   * @param visitor visitor to process.
   */
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
