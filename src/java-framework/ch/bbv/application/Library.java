/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application;

/**
 * A simple class representing a library in a software project.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class Library {
  /**
   * The name of the library
   */
  private String name;

  /** The version number should follow the Apache conventions. */
  private VersionId versionId;

  /** The licence. */
  private String licence;

  /** Information about the library. */
  private String info;

  /**
   * Creates a new library reference.
   * @param name the name.
   * @param versionId the version.
   * @param licence the licence.
   * @param info the web address or other info.
   */
  public Library(String name, VersionId versionId, String licence, String info) {
    this.name = name;
    this.versionId = versionId;
    this.licence = licence;
    this.info = info;
  }

  /**
   * Constructs a library reference from a ProjectInfo object.
   * @param project information about a project.
   */
  public Library(ProjectInfo project) {
    this.name = project.getName();
    this.versionId = project.getVersion();
    this.licence = project.getLicenceName();
    this.info = project.getInfo();
  }

  /**
   * Returns the library name.
   * @return the library name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the library version.
   * @return the library version.
   */
  public VersionId getVersion() {
    return versionId;
  }

  /**
   * Returns the licence text.
   * @return the licence text.
   */
  public String getLicence() {
    return this.licence;
  }

  /**
   * Returns the project info for the library.
   * @return the project info.
   */
  public String getInfo() {
    return this.info;
  }
}