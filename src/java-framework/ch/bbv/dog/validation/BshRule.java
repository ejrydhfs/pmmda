/* copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.validation;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bsh.EvalError;
import bsh.Interpreter;
import ch.bbv.dog.persistence.ojb.PersistenceHandler;

/**
 * The beanshell validation rule implements validation rules which logic is
 * defined as a beanshell script. The application developer can modify the rule
 * without having to recompile the application.
 * @author marcelbaumann
 * @version $Revision$
 */
public class BshRule extends ValidationRuleImpl implements ValidationRule {
  /**
   * Logger of the instances of the class.
   */
  private static Log log = LogFactory.getLog(PersistenceHandler.class);

  private Interpreter interpreter;

  private String filename;

  /**
   * Constructor of the class.
   * @param immediate flag indicating if the rule is immediate or not.
   * @param severity the severity level of the rule.
   * @param filename name of the file containing the beanshell script.
   */
  public BshRule(boolean immediate, int severity, String filename) {
    super(immediate, severity);
    interpreter = new Interpreter();
  }

  @Override
  public boolean execute(Object bean, String property, List results) {
    return process(bean, property, null, null, results);
  }

  @Override
  public boolean execute(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    return process(bean, property, oldValue, newValue, results);
  }

  private boolean process(Object bean, String property, Object oldValue,
      Object newValue, List results) {
    try {
      interpreter.set("bean", bean);
      interpreter.set("property", property);
      interpreter.set("oldValue", oldValue);
      interpreter.set("newValue", newValue);
      interpreter.set("results", results);
      interpreter.source(filename);
    } catch (IOException e) {
      log.error(e);
    } catch (EvalError e) {
      if (log.isErrorEnabled()) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(e.getErrorSourceFile()).append(" : ").append(
            e.getErrorLineNumber()).append(" ").append(e.getErrorText());
        log.error(buffer.toString(), e);
      }
    }
    return true;
  }
}