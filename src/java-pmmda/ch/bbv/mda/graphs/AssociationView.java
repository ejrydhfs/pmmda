/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.graphs;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;

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
      GraphConstants.setLineBegin(attributes, GraphConstants.ARROW_NONE);
      GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_SIMPLE);
      GraphConstants.setEndSize(attributes, 10);
      GraphConstants.setDashPattern(attributes, new float[] { 3, 3 });
    } else if (edge.isRealizes()) {
      GraphConstants.setLineBegin(attributes, GraphConstants.ARROW_NONE);
      GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_SIMPLE);
      GraphConstants.setEndSize(attributes, 10);
      GraphConstants.setDashPattern(attributes, new float[] { 3, 3 });
    } else {
      // aggregation, composition
      if (edge.isStartNavigable() && edge.isEndNavigable()) {
        GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_NONE);
        GraphConstants
            .setLineBegin(attributes, GraphConstants.ARROW_NONE);
      } else if (edge.isStartNavigable()) {
        GraphConstants.setLineBegin(attributes,
            GraphConstants.ARROW_SIMPLE);
        GraphConstants.setBeginSize(attributes, 10);
      } else {
        GraphConstants
            .setLineEnd(attributes, GraphConstants.ARROW_SIMPLE);
        GraphConstants.setEndSize(attributes, 10);
      }

      if (edge.isAggregate() || edge.isComposite()) {
        GraphConstants.setLineEnd(attributes,
            GraphConstants.ARROW_DIAMOND);
        GraphConstants.setEndSize(attributes, 16);

        if (edge.isAggregate()) {
          GraphConstants.setEndFill(attributes, true);
        } else {
          GraphConstants.setEndFill(attributes, false);
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
