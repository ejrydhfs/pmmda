/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License.
 * $Id$ 
 */
package ch.bbv.cmdlineparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** <p>The command line option contains all option values read for the 
 * associated command line description. The parser verifies the correctness 
 * of the inputs. The command line description of the option is 
 * available. If the option is a default one automatically generated, the
 * text is the one defined in the option description.</p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class CmdLineOption {

	/** No error was detected during the parsing of the command line option 
	 * values. 
	 */
	public static final int OK = 1;

	/** Duplicate for this option was read but the description of the option does 
	 * not allow multiple values. 
	 */
	public static final int ILLEGAL_DUPLICATE = 2;
	
	/** A value was read for this option which is not element of the set of allowed 
	 * values. 
	 */
	public static final int ILLEGAL_VALUE = 3;

	/** A option was read but the associated value is not defined for it. 
	 */
	public static final int MISSING_VALUE = 4;
	
	/** An option identifier was read which does not belongs to the language defined. 
	 */
	public static final int ILLEGAL_OPTION = 5;

	/** Reference to the command line description. 
	 */
	private CmdLineOptionDescr cmdLineOptionDescr;
	
	/** All option values as text associated with the option. 
	 */
	private List options;
	
	/** Error code discovered during the parsing of the option. If no error was 
	 * found, the value is ok. 
	 */
	private int errorCode;

	/** Default constructor of the class. All members are initialized to their 
	 * default value.
	 */
	public CmdLineOption() {
		options = new ArrayList();
		errorCode = OK;
	}

	/** Adds a new option value in the list containing all values of the option. 
	 * @param text string of the read option 
   * @pre text != null
	 */
	public void addValue(String text) {
    assert text != null : "pre";
		options.add(text);
	}

	/** Sets the line option description for the actual values of the read line 
	 * option. 
	 * @param optionDescr command line option description object 
   * @pre optionDescr != null
	 */
	public void setCmdLineOptionDescr(CmdLineOptionDescr optionDescr) {
    assert optionDescr != null : "pre";
		cmdLineOptionDescr = optionDescr;
	}

	/** Sets the parsing error code of the option. All error codes are defined
	 * as static members of the class. 
	 * @param errorCode new error code.
	 * @see #getErrorCode()
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/** Returns the error code of the parsed command line option.
	 * @return error code value
	 * @see #setErrorCode(int)
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/** Returns the list of all option values read for this option. The option
   * is identified through its identifier.
	 * @return Iterator of all read option values. 
	 */
	public Iterator values() {
		return options.iterator();
	}

	/** Returns the first read value read for this option. This method is 
   * often used if you know that the associated option description allows 
	 * only an unique value.
	 * @return returns the first option value of the command option.
	 */
	public String getFirstValue() {
		String result = null;
		if (!options.isEmpty()) {
			result = (String)options.get(0);
		}
		return result;
	}

	/** Returns the command line description associated with the option object.
	 * @return associated command line description. If the parsed	option 
   * identifier is not defined in the language, null is returned. 
	 */
	public CmdLineOptionDescr cmdLineOptionDescr() {
		return cmdLineOptionDescr;
	}
}
