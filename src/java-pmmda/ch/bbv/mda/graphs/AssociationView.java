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
 * The view to display a UML association in a class diagram.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class AssociationView extends EdgeView {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * The renderer for the associations to display.
   */
  static AssociationRenderer renderer = new AssociationRenderer();

  /**
   * Constructor of the class.
   * @param edge edge of the association.
   */
  public AssociationView(AssociationEdge edge) {
    super(edge);
    setAttributes(createAttributes());
  }

  /**
   * Creates the graphical attributes of the associaton.
   * @return the map of graphical attributes discribing the association.
   */
  private AttributeMap createAttributes() {
    AttributeMap style = getAttributes();
    AssociationEdge edge = (AssociationEdge) cell;

    if (edge.isInheritance()) {
      GraphConstants.setLineBegin(style, GraphConstants.ARROW_NONE);
      GraphConstants.setLineEnd(style, GraphConstants.ARROW_TECHNICAL);
      GraphConstants.setEndSize(style, 15);
      GraphConstants.setDashPattern(style, new float[] { 3, 3 });
    } else if (edge.isRealizes()) {
      GraphConstants.setLineBegin(style, GraphConstants.ARROW_NONE);
      GraphConstants.setLineEnd(style, GraphConstants.ARROW_TECHNICAL);
      GraphConstants.setEndSize(style, 15);
      GraphConstants.setDashPattern(style, new float[] { 3, 3 });
    } else {
      // aggregation, composition
      if (edge.isStartNavigable() && edge.isEndNavigable()) {
        GraphConstants.setLineEnd(style, GraphConstants.ARROW_NONE);
        GraphConstants.setLineBegin(style, GraphConstants.ARROW_NONE);
      } else if (edge.isStartNavigable()) {
        GraphConstants.setLineBegin(style, GraphConstants.ARROW_SIMPLE);
        GraphConstants.setBeginSize(style, 10);
      } else {
        GraphConstants.setLineEnd(style, GraphConstants.ARROW_SIMPLE);
        GraphConstants.setEndSize(style, 10);
      }
      if (edge.isAggregate() || edge.isComposite()) {
        GraphConstants.setLineEnd(style, GraphConstants.ARROW_DIAMOND);
        GraphConstants.setEndSize(style, 16);
        if (edge.isAggregate()) {
          GraphConstants.setEndFill(style, true);
        } else {
          GraphConstants.setEndFill(style, false);
        }
      }
    }
    return style;
  }

  static class AssociationRenderer extends EdgeRenderer {
    /**
     * Serial version identifier coded as back compatibility date.
     */
    private static final long serialVersionUID = 20041201;
  }
}
