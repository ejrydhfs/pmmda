/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.application;

import java.awt.Image;
import java.util.Iterator;
import java.util.List;

import ch.bbv.explorer.about.Contributor;

/**
 * The project information records descriptive information about an open source
 * software project.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ProjectInfo {
  /**
   * The project name.
   */
  private String name;

  /**
   * The project version.
   */
  private VersionId versionId;

  /**
   * The project copyright statement.
   */
  private String copyright;

  /**
   * Further info about the project (typically a URL for the project's web
   * page).
   */
  private String info;

  /**
   * An optional project logo.
   */
  private Image logo;

  /**
   * The name of the licence.
   */
  private String licenceName;

  /**
   * The licence text.
   */
  private String licenceText;

  /**
   * A list of contributors.
   */
  private List contributors;

  /**
   * A list of libraries used by the project.
   */
  private List libraries;

  /**
   * Default constructor of the class.
   */
  public ProjectInfo() {
    // default constructor
  }

  /**
   * Constructs a project info object.
   * @param name the name of the project.
   * @param versionId the version.
   * @param info other info (usually a URL).
   * @param logo the project logo.
   * @param copyright a copyright statement.
   * @param licenceName the name of the licence that applies to the project.
   * @param licenceText the text of the licence that applies to the project.
   */
  public ProjectInfo(String name, VersionId versionId, String info, Image logo,
      String copyright, String licenceName, String licenceText) {
    this.name = name;
    this.versionId = versionId;
    this.info = info;
    this.logo = logo;
    this.copyright = copyright;
    this.licenceName = licenceName;
    this.licenceText = licenceText;
  }

  /**
   * Returns the project name.
   * @return the project name.
   * @see #setName
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the project name.
   * @param name the project name.
   * @see #getName
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the project version.
   * @return the project version.
   * @see #setVersion
   */
  public VersionId getVersion() {
    return versionId;
  }

  /**
   * Sets the project version.
   * @param versionId the project version.
   * @see #getVersion
   */
  public void setVersion(VersionId versionId) {
    this.versionId = versionId;
  }

  /**
   * Returns other information about the project (typically a URL for the
   * project home page).
   * @return the project info.
   * @see #setInfo
   */
  public String getInfo() {
    return this.info;
  }

  /**
   * Sets the project info (typically a URL).
   * @param info the project info.
   * @see #getInfo
   */
  public void setInfo(String info) {
    this.info = info;
  }

  /**
   * Returns the logo.
   * @return the project logo.
   * @see #setLogo
   */
  public Image getLogo() {
    return this.logo;
  }

  /**
   * Sets the project logo.
   * @param logo the project logo.
   * @see #getLogo
   */
  public void setLogo(Image logo) {
    this.logo = logo;
  }

  /**
   * Returns the copyright statement.
   * @return the copyright statement.
   * @see #setCopyright
   */
  public String getCopyright() {
    return this.copyright;
  }

  /**
   * Sets the project copyright statement.
   * @param copyright the project copyright statement.
   * @see #getCopyright
   */
  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  /**
   * Returns the licence name.
   * @return the licence name.
   * @see #setLicenceName
   */
  public String getLicenceName() {
    return this.licenceName;
  }

  /**
   * Sets the project licence name.
   * @param licenceName the licence name.
   * @see #getLicenceName
   */
  public void setLicenceName(String licenceName) {
    this.licenceName = licenceName;
  }

  /**
   * Returns the licence text.
   * @return the licence text.
   * @see #setLicenceText
   */
  public String getLicenceText() {
    return this.licenceText;
  }

  /**
   * Sets the project licence text.
   * @param licenceText the licence text.
   * @see #getLicenceText
   */
  public void setLicenceText(String licenceText) {
    this.licenceText = licenceText;
  }

  /**
   * Returns the list of contributors for the project.
   * @return the list of contributors.
   * @see #setContributors
   */
  public List getContributors() {
    return this.contributors;
  }

  /**
   * Sets the list of contributors.
   * @param contributors the list of contributors.
   * @see #getContributors
   */
  public void setContributors(List contributors) {
    this.contributors = contributors;
  }

  /**
   * Returns a list of libraries used by the project.
   * @return the list of libraries.
   * @see #setLibraries
   */
  public List getLibraries() {
    return this.libraries;
  }

  /**
   * Sets the list of libraries.
   * @param libraries the list of libraries.
   * @see #getLibraries
   */
  public void setLibraries(List libraries) {
    this.libraries = libraries;
  }

  /**
   * Returns a string describing the project.
   * @return a string describing the project.
   */
public String toString() {
    String result = getName() + " version " + getVersion() + ".\n";
    result = result + getCopyright() + ".\n";
    result = result + "\n";
    result = result + "For terms of use, see the licence below.\n";
    result = result + "\n";
    result = result + "FURTHER INFORMATION:";
    result = result + getInfo();
    result = result + "\n";
    result = result + "CONTRIBUTORS:";

    List contributors = getContributors();
    if (contributors != null) {
      for (Iterator i = getContributors().iterator(); i.hasNext();) {
        Contributor contributor = (Contributor) i.next();
        result = result + contributor.getName() + " (" + contributor.getEmail()
            + ").";
      }
    } else {
      result = result + "None";
    }

    result = result + "\n";
    result = result + "OTHER LIBRARIES USED BY " + getName() + ":";

    List libraries = getLibraries();
    if (libraries != null) {
      for (Iterator i = getLibraries().iterator(); i.hasNext();) {
        Library lib = (Library) i.next();
        result = result + lib.getName() + " " + lib.getVersion() + " ("
            + lib.getInfo() + ").";
      }
    } else {
      result = result + "None";
    }
    result = result + "\n";
    result = result + getName() + " LICENCE TERMS:";
    result = result + "\n";
    result = result + getLicenceText();

    return result;
  }}