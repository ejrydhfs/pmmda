/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.ui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.application.ComponentException;
import ch.bbv.application.Locator;
import ch.bbv.cmdlineparser.CmdLineBuilder;
import ch.bbv.dog.reference.ReferenceCodeUtilities;
import ch.bbv.pim.AddressBook;
import ch.bbv.pim.Country;
import ch.bbv.pim.Language;
import ch.bbv.pim.formats.*;

/**
 * The personal information manager provides a command line to import contact
 * information from a file.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class Pim extends Locator {
  /**
   * Logger instance of the class.
   */
  private static Log log = LogFactory.getLog(Pim.class);

  /**
   * The command line arguments of the application.
   */
  private String[] arguments;

  /**
   * @param args command line arguments of the application
   */
  public static void main(String[] args) {
    try {
      System.out.println("Hello PIM");
      new Pim(args);
    } catch (ComponentException e) {
      log.fatal(e);
    } catch (IOException e) {
      log.error(e);
    } catch (Exception e) {
      log.fatal(e);
    }
  }

  /**
   * Convenience method returning the locator instace as an application.
   * @return the singleton locator as application instance.
   */
  public static Pim getApplication() {
    return (Pim) Locator.getInstance();
  }

  /**
   * Constructor of the class.
   * @param args command line arguments.
   * @throws ComponentException occured when creating and starting the
   *           application.
   * @throws IOException if a file operation encounters a problem.
   * @throws Exception if a component creation is unsucessful.
   */
  public Pim(String[] args) throws ComponentException, IOException, Exception {
    arguments = args;
    register(this);
    FormatManager manager = new FormatManager();
    manager.register(new LdifEmitter(), ".ldif");
    manager.register(new VCardEmitter(), ".vcf");
    manager.register(new LdifParser(), ".ldif");
    manager.register(new VCardParser(), ".vcf");

    register(manager);
    initialize();
    processCommandLine();
  }

  public FormatManager getFormatManager() {
    return (FormatManager) getApplication().getComponent(FormatManager.NAME);
  }

  /**
   * Processes the command line and executes the defined actions.
   * @throws IOException if a file operation encounters a problem.
   */
  private void processCommandLine() throws IOException {
    Options options = new Options();
    OptionGroup group = new OptionGroup();
    options.addOption(CmdLineBuilder.createNoArgOption("h", "help",
        "display the list of available commands", false));
    group.addOption(CmdLineBuilder.createNoArgOption("v", "verbose",
        "set the logging level to verbose", false));
    group.addOption(CmdLineBuilder.createNoArgOption("q", "quiet",
        "set the logging level to quiet", false));
    options.addOptionGroup(group);
    options
        .addOption(CmdLineBuilder.createArgOption("i", "import",
            "import the contacts from the given file", "file", false,
            String.class));
    options
        .addOption(CmdLineBuilder.createArgOption("e", "export",
            "export the contacts from the given file", "file", false,
            String.class));
    options.addOption(CmdLineBuilder.createArgOption("l", "language",
        "default language for contacts without this data", "language", false,
        String.class));
    options.addOption(CmdLineBuilder.createArgOption("c", "country",
        "default country for contacts without this data", "country", false,
        String.class));
    options.addOption(CmdLineBuilder.createJavaProperty());
    Parser parser = new PosixParser();
    try {
      CommandLine line = parser.parse(options, arguments);
      if (line.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("PIM", options);
      }
      if (line.hasOption("v")) {
        log.info("logging level set to verbose");
      }
      if (line.hasOption("q")) {
        log.info("logging level set to quiet");
      }
      Language language = Language.GERMAN;
      Country country = Country.SWITZERLAND;
      if (line.hasOption("l")) {
        String value = line.getOptionValue("l");
        if (value != null) {
          language = (Language) ReferenceCodeUtilities.find(
              Language.getCodes(), value);
        }
      }
      if (line.hasOption("c")) {
        String value = line.getOptionValue("c");
        if (value != null) {
          country = (Country) ReferenceCodeUtilities.find(Country.getCodes(),
              value);
        }
      }
      for (ContactParser item : getFormatManager().getParsers()) {
        item.setDefaultLanguage(language);
        item.setDefaultCountry(country);
      }
      AddressBook book = new AddressBook();
      if (line.hasOption("i")) {
        String filename = line.getOptionValue("i");
        if (filename != null) {
          getFormatManager().getParser(filename).synchronize(book,
              new File(filename));
        }
      }
      if (line.hasOption("e")) {
        String filename = line.getOptionValue("e");
        if (filename != null) {
          getFormatManager().getEmitter(filename)
              .emit(book, new File(filename));
        }
      }
    } catch (ParseException e) {
      log.error("parse arguments exception", e);
    }
  }

}
