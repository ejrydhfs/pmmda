/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.cmdlineparser;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CmdLineBuilder {
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

  public static Option createJavaProperty() {
    OptionBuilder.withArgName("property=value");
    OptionBuilder.withLongOpt(null);
    OptionBuilder.isRequired(false);
    OptionBuilder.hasArg();
    OptionBuilder.withValueSeparator();
    OptionBuilder.withDescription("use value for given property");
    return createOption("D");
  }
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
