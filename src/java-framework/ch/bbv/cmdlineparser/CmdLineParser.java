/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 */
package ch.bbv.cmdlineparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * The command line parser is responsible for the parsing of command line
 * strings. It uses the internally stored command line language definition to
 * parse and check the input data. The class exports also a protocol, which can
 * be used to input a command line through a user interface.
 * </p>
 * <p>
 * The command line parser is owner of a command language and optionally of a
 * parsed command line input.
 * </p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class CmdLineParser {
  /**
   * List of all found command options parsed after a request.
   */
  private List options;

  /**
   * Description of the command language the parser should interpret.
   */
  private List optionDescriptions;

  /**
   * Constructs a new command line parser. No default language is defined. The
   * user is requested to input the definition of its input language with the
   * provided functions.
   */
  public CmdLineParser() {
    options = new ArrayList();
    optionDescriptions = new ArrayList();
  }

  /**
   * Adds a new command line option descriptor in the command line parser. The
   * list of option descriptors is the language parsed.
   * @param optionDescr command line option description to add.
   * @pre optionDescr != null
   */
  public void add(CmdLineOptionDescr optionDescr) {
    assert optionDescr != null : "pre";
    optionDescriptions.add(optionDescr);
  }

  /**
   * Parses an array of strings. Each string is a token read from an input
   * character stream. A list of command options with their associated values is
   * generated and stored in the command line parser. If an syntax error is
   * detected, the parsing process is stopped. Before the parsing is started the
   * previous result is discarded. At the end of the parsing process all
   * undefined mandatory options with default values are added. For each pair of
   * string we first find out which command option ID the first string matches,
   * we match the second string as the value of the found command option. For
   * each pair a new command option object is created and added in the list of
   * parsed options.
   * @param strings is an array of tokens to be parsed.
   * @pre strings != null
   */
  public void parse(String[] strings) {
    assert strings != null : "pre";
    reset();
    int i = 0;
    while (i < strings.length) {
      CmdLineOptionDescr optionDescr = getOptionDescriptionWithId(strings[i]);
      if (optionDescr != null) {
        CmdLineOption option = getOptionWithDescription(optionDescr);
        if (option == null) {
          option = new CmdLineOption();
          option.setCmdLineOptionDescr(optionDescr);
          option.addValue(strings[i]);
          options.add(option);
        }
        i++;
        if (optionDescr.hasValues()) {
          // The option has a value.
          if ((i < strings.length)
              && optionDescr.isLegalOptionValue(strings[i])) {
            option.addValue(strings[i]);
            i++;
          } else {
            option.setErrorCode(CmdLineOption.MISSING_VALUE);
          }
        }
      } else {
        boolean isUnixLikeOption = handleUnixLikeOptionWith(strings[i]);
        if (!isUnixLikeOption) {
          CmdLineOption option = new CmdLineOption();
          option.addValue(strings[i]);
          option.setErrorCode(CmdLineOption.ILLEGAL_OPTION);
          options.add(option);
        }
        i++;
      }
    }
    generateAllDefaultOptions();
  }

  /**
   * Resets the parser and removes all parsed options.
   */
  public void reset() {
    options.clear();
  }

  /**
   * Returns all parsed command options managed in the parser.
   * @return All command line options currently defined.
   */
  public Iterator getAllOptions() {
    return options.iterator();
  }

  /**
   * Returns all command option descriptions managed in the parser.
   * @return All command line option descriptions currently defined.
   */
  public Iterator getAllOptionDescriptions() {
    return optionDescriptions.iterator();
  }

  /**
   * Adds an option to the list of parsed options.
   * @param option option to add to the list.
   * @pre option != null
   */
  public void add(CmdLineOption option) {
    assert option != null : "pre";
    options.add(option);
  }

  /**
   * Removes the given option from the list of parsed options.
   * @param option option to remove from the list.
   * @pre option != null
   */
  public void remove(CmdLineOption option) {
    assert option != null : "pre";
    options.remove(option);
  }

  /**
   * Returns the command line option with the given command identifier.
   * @param cmdId identifier of the searched command option description.
   * @return The found command option or null if not found.
   * @pre cmdId != null
   */
  public CmdLineOption getOptionWithId(String cmdId) {
    assert cmdId != null : "pre";
    CmdLineOption option = null;
    CmdLineOptionDescr cmdLineOptionDescr;
    for (Iterator i = options.iterator(); i.hasNext();) {
      option = (CmdLineOption) i.next();
      cmdLineOptionDescr = option.cmdLineOptionDescr();
      if ((cmdLineOptionDescr != null) && cmdLineOptionDescr.isOptionId(cmdId)) {
        break;
      }
      option = null;
    }
    return option;
  }

  /**
   * Returns all options which were wrongly defined on the command line.
   * @return Iterator of all erroneous command line options with their
   *         associated error code.
   */
  public Iterator getAllErroneousOptions() {
    ArrayList result = new ArrayList();
    int size = options.size();
    CmdLineOption option;
    for (int i = 0; i < size; i++) {
      option = (CmdLineOption) options.get(i);
      if (option.getErrorCode() != CmdLineOption.OK) {
        result.add(option);
      }
    }
    return result.iterator();
  }

  /**
   * Returns the option description of all missing options. An option is missing
   * if it is mandatory and not defined on the command line.
   * @return Iterator of all command line descriptions of the missing options.
   */
  public Iterator getDescriptionOfAllMissingOptions() {
    int size = optionDescriptions.size();
    ArrayList result = new ArrayList();
    CmdLineOptionDescr description;
    for (int i = 0; i < size; i++) {
      description = (CmdLineOptionDescr) optionDescriptions.get(i);
      if (description.isMandatory()
          && (getOptionWithId(description.getCmdId()) == null)) {
        result.add(description);
      }
    }
    return result.iterator();
  }

  /**
   * Generates all options with default values and add them to the parsed
   * command line input. It is assumed that only mandatory options have default
   * values as specified in the command line description.
   */
  void generateAllDefaultOptions() {
    Iterator e = optionDescriptions.iterator();
    while (e.hasNext()) {
      CmdLineOptionDescr description = (CmdLineOptionDescr) e.next();
      if (description.hasDefaultValue()) {
        CmdLineOption option = getOptionWithDescription(description);
        if (option != null) {
          option = new CmdLineOption();
          option.setCmdLineOptionDescr(description);
          option.addValue(description.getDefaultValue());
          options.add(option);
        }
      }
    }
  }

  /**
   * Returns the command line description with the given command identifier.
   * @param cmdId identifier of the searched command option description.
   * @return The found command option description or null if not found.
   * @pre cmdId != null
   */
  private CmdLineOptionDescr getOptionDescriptionWithId(String cmdId) {
    assert (cmdId != null);
    CmdLineOptionDescr cmdLineOptionDescr;
    for (Iterator i = optionDescriptions.iterator(); i.hasNext();) {
      cmdLineOptionDescr = (CmdLineOptionDescr) i.next();
      if (cmdLineOptionDescr.isOptionId(cmdId)) {
        return cmdLineOptionDescr;
      }
    }
    return null;
  }

  /**
   * Handles a command line option with a UNIX like syntax where the option and
   * its value are concatenated into one string. If the syntax is correct the
   * option value is added.
   * @param cmd command line option to handle.
   * @return True if the parsing was sucessfull otherwise false.
   * @pre cmd != null
   */
  private boolean handleUnixLikeOptionWith(String cmd) {
    assert (cmd != null);
    CmdLineOptionDescr optionDescr;
    for (Iterator i = optionDescriptions.iterator(); i.hasNext();) {
      optionDescr = (CmdLineOptionDescr) i.next();
      if (cmd.regionMatches(optionDescr.isCaseSensitive(), 0, optionDescr
          .getCmdId(), 0, optionDescr.getMinimumLength())) {
        CmdLineOption option = new CmdLineOption();
        option.setCmdLineOptionDescr(optionDescr);
        option.addValue(cmd.substring(0, optionDescr.getMinimumLength()));
        options.add(option);
        return true;
      }
    }
    return false;
  }

  /**
   * Returns true if at least one command option description uses the UNIX like
   * syntax style.
   * @return True if at least one option uses UNIX like syntax otherwise false.
   */
  protected boolean hasUnixLikeOptions() {
    for (Iterator i = optionDescriptions.iterator(); i.hasNext();) {
      if (((CmdLineOptionDescr) i.next()).isUnixLike()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the command line value associated with the given option
   * description. If none is found returns null.
   * @param optionDescr the option description of the searched command option.
   * @return the found command line option otherwise null
   * @pre optionDescr != null
   */
  private CmdLineOption getOptionWithDescription(CmdLineOptionDescr optionDescr) {
    assert (optionDescr != null);
    CmdLineOption option = null;
    for (Iterator i = options.iterator(); i.hasNext();) {
      option = (CmdLineOption) i.next();
      CmdLineOptionDescr currentDescr = option.cmdLineOptionDescr();
      if (currentDescr == optionDescr) {
        break;
      }
      option = null;
    }
    return option;
  }
}