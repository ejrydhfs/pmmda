/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The identifier validator provides rules to evaluate regular identifier in
 * programming languages.
 * <ul>
 * <li>Simple identifiers composed with a letter, underscore followed with a
 * sequence of letters, underscores and digits</li>
 * <li>Java identifiers composed with a letter, underscore or a dollar followed
 * with a sequence of letters, underscores, dollars and digits</li>
 * <li>Package names composed of a simple identifier followed with a composite
 * sequence of a dot and a simple identifier</li>
 * </ul>
 * @author MarcelBaumann
 * @version $Revision$
 */
public class IdentifierValidator {
  /**
   * Regular expression for a simple identifier.
   */
  public static final String SIMPLE_IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";

  /**
   * Regular expression for a Java simple identifier.
   */
  public static final String JAVA_IDENTIFIER = "[a-zA-Z_\\$][a-zA-Z0-9_\\$]*";

  /**
   * Regular expression for a qualified package identifier.
   */
  public static final String PACKAGE_IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*[\\.[a-zA-Z_][a-zA-Z0-9_]*]*";

  private static Pattern identifier = Pattern.compile(SIMPLE_IDENTIFIER);

  private static Pattern identifierJava = Pattern.compile(JAVA_IDENTIFIER);

  private static Pattern identifierPackage = Pattern
      .compile(PACKAGE_IDENTIFIER);

  public static boolean checkIdentifier(String text) {
    return match(text, identifier);
  }

  public static boolean checkJavaIdentifier(String text) {
    return match(text, identifierJava);
  }

  public static boolean checkPackageIdentifier(String text) {
    return match(text, identifierPackage);
  }

  private static boolean match(String text, Pattern pattern) {
    Matcher matcher = pattern.matcher(text);
    return matcher.matches();
  }
}
