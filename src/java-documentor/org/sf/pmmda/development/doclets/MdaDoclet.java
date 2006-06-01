/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package org.sf.pmmda.development.doclets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;

/**
 * The MDA doclet synchronizes a Java source tree with a MDA UML model. The
 * expected MDA model conventions are the ones defined in the pmMDA tool.
 * @author marcelbaumann
 * @version $Revision$
 */
public class MdaDoclet extends Doclet {
  /**
   * Tag name to define the model to synchronize with the read source code.
   */
  public static final String MODEL_TAG = "-model";

  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(MdaDoclet.class);

  public MdaDoclet() {
  }

  public static boolean start(RootDoc root) {
    ClassDoc[] classes = root.classes();
    if (log.isDebugEnabled()) {
      for (int i = 0; i < classes.length; i++) {
        log.debug(classes[i]);
      }
    }
    return true;
  }

  public static boolean validOptions(String[][] options,
      DocErrorReporter reporter) {
    boolean foundModelTag = false;
    for (int i = 0; i < options.length; i++) {
      String[] option = options[i];
      if (MODEL_TAG.equals(option[0])) {
        if (foundModelTag) {
          foundModelTag = false;
          break;
        }
        foundModelTag = true;
      }
    }
    if (!foundModelTag) {
      reporter
          .printError("Usage: -model <model-to-synchronize> (missing or multiple)");
    }
    return true;
  }

  public static int optionLength(String option) {
    int length = 0;
    if (option.equals(MODEL_TAG)) {
      length = 2;
    }
    return length;
  }
}