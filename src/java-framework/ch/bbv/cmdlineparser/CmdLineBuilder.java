/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.cmdlineparser;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * The command line builder provides factory methods to construct command line
 * arguments for the Jakarta command line inteface <em>CLI</em> package.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CmdLineBuilder {
  /**
   * Creates an command line option without arguments.
   * @param shortName short name of the option.
   * @param longName long name of the option.
   * @param description description of the option for the online help.
   * @param required flag indicating if the option is mandatory or not.
   * @return the newly created command line option.
   */
  public static Option createNoArgOption(String shortName, String longName,
      String description, boolean required) {
    OptionBuilder.withArgName(null);
    OptionBuilder.withLongOpt(longName);
    OptionBuilder.isRequired(required);
    OptionBuilder.hasArg(false);
    OptionBuilder.withDescription(description);
    OptionBuilder.withType(null);
    return createOption(shortName);
  }

  /**
   * Creates an command line option with arguments.
   * @param shortName short name of the option.
   * @param longName long name of the option.
   * @param description description of the option for the online help.
   * @param argName the name of the argument of the option.
   * @param required flag indicating if the option is mandatory or not.
   * @param type type of the argument values.
   * @return the newly created command line option.
   */
  public static Option createArgOption(String shortName, String longName,
      String description, String argName, boolean required, Object type) {
    OptionBuilder.withArgName(argName);
    OptionBuilder.withLongOpt(longName);
    OptionBuilder.isRequired(required);
    OptionBuilder.hasArg(true);
    OptionBuilder.withDescription(description);
    OptionBuilder.withType(type);
    return createOption(shortName);
  }

  /**
   * Creates a commnad line option following the conventions of Java command
   * line properties.
   * @return the newly created command line option.
   */
  public static Option createJavaProperty() {
    OptionBuilder.withArgName("property=value");
    OptionBuilder.withLongOpt(null);
    OptionBuilder.isRequired(false);
    OptionBuilder.hasArg();
    OptionBuilder.withValueSeparator();
    OptionBuilder.withDescription("use value for given property");
    return createOption("D");
  }

  /**
   * Creates a command line option once the option builder was initialized.
   * @param shortName short name of the option.
   * @return the newly created command line option.
   */
  private static Option createOption(String shortName) {
    Option option;
    if (shortName != null) {
      option = OptionBuilder.create(shortName);
    } else {
      option = OptionBuilder.create();

    }
    return option;
  }
}
