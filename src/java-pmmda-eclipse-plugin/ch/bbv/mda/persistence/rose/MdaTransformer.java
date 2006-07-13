/* copyright 2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.rose;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.importer.rose.builder.RoseWalker;
import org.eclipse.emf.importer.rose.parser.RoseNode;
import org.eclipse.emf.importer.rose.builder.RoseStrings;

import ch.bbv.mda.MetaModel;

/**
 * The MDA transformer defines rules how to transform a MDA element into the
 * corresponding Rose representation.
 * @author adrianbachofen
 * @version $Revision$
 */

public class MdaTransformer {
    private static Log log = LogFactory.getLog(MdaTransformer.class);
    
    public MdaTransformer(){
    }
    
    public void synchronize(MetaModel source, RoseNode target) {
      RoseMdaVisitor roseMdaVisitor = new RoseMdaVisitor(source);
      RoseWalker roseWalker = new RoseWalker(target);
      roseWalker.traverse(roseMdaVisitor);
      roseMdaVisitor.deferredQuiduUpdate();
      roseMdaVisitor.processNewElements(target);
      roseMdaVisitor.deleteElements(target);
    }
    
    public RoseNode createVersionTree() {
      RoseNode node = new RoseNode("", "Petal", RoseNode.OBJECT);
      node.addNode(new RoseNode("version", "45", RoseNode.STRING));
      node.addNode(new RoseNode("_written", "\"pmMDA\"", RoseNode.STRING));
      node.addNode(new RoseNode("charSet", "0", RoseNode.STRING));
      return node;
    }
    
    public RoseNode createModelTree() {
      return new RoseNode("", RoseStrings.DESIGN+" \""+RoseStrings.LOGICAL_VIEW+"\"", RoseNode.OBJECT);
    }
    
}
