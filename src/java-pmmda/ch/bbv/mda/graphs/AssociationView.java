/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class AssociationView extends EdgeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  static AssociationRenderer renderer = new AssociationRenderer();

  /**
   * Constructor of the class.
   * @param edge
   */
  public AssociationView(AssociationEdge edge) {
    super(edge);
    setAttributes(createAttributes());
  }

  private AttributeMap createAttributes() {
    AttributeMap attributes = getAttributes();
    AssociationEdge edge = (AssociationEdge) cell;

    if (edge.isInheritance()) {
      UMLGraphConstants.setLineBegin(attributes, UMLGraphConstants.ARROW_NONE);
      UMLGraphConstants.setLineEnd(attributes, UMLGraphConstants.ARROW_SIMPLE);
      UMLGraphConstants.setEndSize(attributes, 10);
      UMLGraphConstants.setDashPattern(attributes, new float[] { 3, 3 });
    } else if (edge.isRealizes()) {
      UMLGraphConstants.setLineBegin(attributes, UMLGraphConstants.ARROW_NONE);
      UMLGraphConstants.setLineEnd(attributes, UMLGraphConstants.ARROW_SIMPLE);
      UMLGraphConstants.setEndSize(attributes, 10);
      UMLGraphConstants.setDashPattern(attributes, new float[] { 3, 3 });
    } else {
      // aggregation, composition
      if (edge.isStartNavigable() && edge.isEndNavigable()) {
        UMLGraphConstants.setLineEnd(attributes, UMLGraphConstants.ARROW_NONE);
        UMLGraphConstants
            .setLineBegin(attributes, UMLGraphConstants.ARROW_NONE);
      } else if (edge.isStartNavigable()) {
        UMLGraphConstants.setLineBegin(attributes,
            UMLGraphConstants.ARROW_SIMPLE);
        UMLGraphConstants.setBeginSize(attributes, 10);
      } else {
        UMLGraphConstants
            .setLineEnd(attributes, UMLGraphConstants.ARROW_SIMPLE);
        UMLGraphConstants.setEndSize(attributes, 10);
      }

      if (edge.isAggregate() || edge.isComposite()) {
        UMLGraphConstants.setLineEnd(attributes,
            UMLGraphConstants.ARROW_DIAMOND);
        UMLGraphConstants.setEndSize(attributes, 16);

        if (edge.isAggregate()) {
          UMLGraphConstants.setEndFill(attributes, true);
        } else {
          UMLGraphConstants.setEndFill(attributes, false);
        }
      }
    }
    return attributes;
  }

  static class AssociationRenderer extends EdgeRenderer {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;
  }
}
