/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ReferenceCodeGroupImpl implements ReferenceCodeGroup {
  /**
   * Serial version identifier coded as back compatibility date.
   */
  private static final long serialVersionUID = 20041201;

  /**
   * List of reference code groups owned in this group.
   */
  private List<ReferenceCodeGroup> groups;

  /**
   * List of reference code descriptors of this group.
   */
  private List<ReferenceCodeDescriptor> descriptors;

  /**
   * Name of the group.
   */
  private String name;

  /**
   * Description of the group.
   */
  private String description;

  public ReferenceCodeGroupImpl() {
    groups = new ArrayList<ReferenceCodeGroup>();
    descriptors = new ArrayList<ReferenceCodeDescriptor>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ReferenceCodeGroup> getGroup() {
    return groups;
  }

  public void setGroup(List<ReferenceCodeGroup> groups) {
    this.groups = groups;
  }

  public ReferenceCodeGroup getGroup(int index) {
    return groups.get(index);
  }

  public void setGroup(int index, ReferenceCodeGroup group) {
    groups.set(index, group);
  }

  public void addGroup(ReferenceCodeGroup group) {
    groups.add(group);
  }

  public List<ReferenceCodeDescriptor> getDescriptor() {
    return descriptors;
  }

  public void setDescriptor(List<ReferenceCodeDescriptor> descriptors) {
    this.descriptors = descriptors;
  }

  public ReferenceCodeDescriptor get(int index) {
    return descriptors.get(index);
  }

  public void setDescriptor(int index, ReferenceCodeDescriptor descriptor) {
    descriptors.set(index, descriptor);
  }

}
