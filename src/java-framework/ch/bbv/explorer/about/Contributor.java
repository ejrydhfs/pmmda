/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.explorer.about;


/**
 * <p>The contributor class describes a contributor to a software project.
 * The class implements the Java bean convention.</p>
 * @author Marcel Baumann
 * @version $Revision$
 */
public class Contributor {
  /** The name of the contributor as displayed.
   */
  private String name;

  /** The e-mail address of the contributor as displayed.
   */
  private String email;

	/** The role of the contributor in the project.
	 */
	private String role;
	
  /** Creates a new contributor.
   * @param name the displayed name.
   * @param email the displayed e-mail address.
   * @param role the role of the contributor in the project.
   */
  public Contributor(String name, String email, String role) {
    this.name = name;
    this.email = email;
    this.role = role;
  }

  /** Returns the contributor's name.
   * @return the contributor's name.
   */
  public String getName() {
    return name;
  }

  /** Returns the contributor's e-mail address.
   * @return the contributor's e-mail address.
   */
  public String getEmail() {
    return email;
  }
  
  /** Returns the role of the contributor in the project.
   * @return role of the contributor.
   */
  public String getRole() {
  	return role;
  }
}
