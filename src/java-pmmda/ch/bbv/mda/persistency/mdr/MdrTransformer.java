/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.persistency.mdr;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.xml.sax.SAXException;

import ru.novosoft.uml.xmi.XMIReader;

/**
 * The MDR transformer defines rules how to transform a MDR element into the
 * corresponding MDA representation. Special is the mapping of aggregations and
 * compositions to indexed properties to reflect the capabilities of modern
 * object oriented programming languages.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class MdrTransformer {
  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(MdrTransformer.class);

  private MDRepository repository;

  public void readModel(String xmiFile) throws SAXException,
      ParserConfigurationException {
    String uri = new File(xmiFile).toURI().toString();
    repository = MDRManager.getDefault().getDefaultRepository();
    XMIReader xmiReader = new XMIReader();
  }

}