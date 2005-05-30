/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import org.jgraph.graph.DefaultEdge;

/**
 * An association edge is an edge in an UML diagram. An edge models an 
 * inheritance relation, a realize relation, an aggregation relation or a
 * composition relation. Some of these relations have an associated model 
 * object of the MDA model.
 * Currently no sublcassing is used to distinguish the relations because
 * they do not contain semantic value at the view level.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class AssociationEdge extends DefaultEdge {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * Defines the different types of edges supported in the view.
   * @author MarcelBaumann
   * @version $Revision$
   */
  public enum Type {
    inheritance, realizes, aggregation, composition
  };

  /** 
   * Type of the edge.
   */
  private Type type;

  /**
   * Constructor of the class.
   * @param type type of the association.
   * @param name name of the association as displayed.
   */
  public AssociationEdge(Type type, String name) {
    super(name);
    this.type = type;
  }

  /**
   * Returns true if the association represents inheritance.
   * @return True if the check is sucessful otherwise false.
   */
  public boolean isInheritance() {
    return Type.inheritance == type;
  }

  /**
   * Returns true if the association represents realization.
   * @return True if the check is sucessful otherwise false.
   */
  public boolean isRealizes() {
    return Type.realizes == type;
  }

  /**
   * Returns true if the association represents aggregation.
   * @return True if the check is sucessful otherwise false.
   */
  public boolean isAggregate() {
    return Type.aggregation == type;
  }

  /**
   * Returns true if the association represents composition.
   * @return True if the check is sucessful otherwise false.
   */
  public boolean isComposite() {
    return Type.composition == type;
  }
  
  /**
   * Returns true if the association is navigable from the start node.
   * @return True if the check is sucessful otherwise false.
   */
  public boolean isStartNavigable() {
    return false;
  }

  /**
   * Returns true if the association is navigable from the end node.
   * @return True if the check is sucessful otherwise false.
   */
  public boolean isEndNavigable() {
    return true;
  }
}
