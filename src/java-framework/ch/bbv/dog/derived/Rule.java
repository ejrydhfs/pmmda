/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.dog.derived;

import java.util.List;

/**
 * The rule interface defines the contract for all rules applied on data
 * objects.
 * @author MarcelBaumann
 * @version $Revision$
 */
public interface Rule {
	/** Returns the name of the rule.
	 * @return the name of the rule.
	 */
  String getName();
  
  /** Returns the description of the rule.
   * @return the description of the rule.
   */
  String getDescription();
  
  /** Evaluates the rule in the given context. If an error is encountered an
   * error message can be added in the list.
   * @param bean bean on which the rule is evaluated.
   * @param property property on which the rule is evaluated.
   * @param messages list of errors messages where messages can be added.
   * @return true if no error was encountered otherwise false. 
   */
  boolean evaluate(Object bean, String property, List messages);
}
