/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda;

import java.util.List;
import java.util.Iterator;

/**
 * @author AdrianBachofen Defines a filter for tagged values. Either a tagged
 *         value has to be present (same type and tag) or it has to match
 *         entirely (same type, tag, and value). The non empty rule is used to
 *         select tagged values of same type, tag and a non empty value. This
 *         filter can be used to construct tree views with portions of the model
 * @see FilterTreeConstructor
 */
public class TaggedValueFilter {

  /**
   * This filter rule defines a tagged value with the same type and name has to
   * be present.
   */
  public static final int MUST_BE_PRESENT = 0;

  /**
   * This filter rule defines a tagged value with the same type, name, and value
   * has to be present.
   */
  public static final int NON_EMPTY = 1;

  /**
   * This filter rule defines a tagged value with the same type, name, and value
   * different to null or "".
   */
  public static final int MATCH = 2;

  /**
   * This filter rule defines a tagged value, which is equivalent to the default
   * within its type.
   */
  public static final int DEFAULT = 3;

  /**
   * This filter rule defines a tagged value with the same type, name, and value
   * equal to null or "".
   */
  public static final int UNASSIGNED = 4;

  private TaggedValue taggedValue;

  private int filterRule;

  public TaggedValueFilter(TaggedValue taggedValue, int filterRule) {
    this.taggedValue = taggedValue;
    this.filterRule = filterRule;
  }

  public boolean filterRuleFullfilled(MetaElement element) {
    boolean fullfilled = false;
    List tags = element.getTags();
    for (Iterator i = tags.iterator(); i.hasNext();) {
      if (filterRule == MATCH) {
        fullfilled |= processMatchRule((TaggedValue) i.next());
      } else if (filterRule == MUST_BE_PRESENT) {
        fullfilled |= processPresentRule((TaggedValue) i.next());
      } else if (filterRule == NON_EMPTY) {
        fullfilled |= processNonEmptyRule((TaggedValue) i.next());
      } else if (filterRule == UNASSIGNED) {
        fullfilled |= processUnassignedRule((TaggedValue) i.next());
      } else if (filterRule == DEFAULT) {
        fullfilled |= processDefaultRule((TaggedValue) i.next());
      }
    }
    return fullfilled;
  }

  protected boolean processMatchRule(TaggedValue tv) {
    return processPresentRule(tv)
        && equalStrings(tv.getValue(), taggedValue.getValue());
  }

  protected boolean processPresentRule(TaggedValue tv) {
    return tv.getTag().equals(taggedValue.getTag())
        && equalsTaggedValueType(tv.getType(), taggedValue.getType());
  }

  protected boolean processNonEmptyRule(TaggedValue tv) {
    return processPresentRule(tv) && tv.getValue() != null
        && !tv.getValue().equals("");
  }

  protected boolean processUnassignedRule(TaggedValue tv) {
    return processPresentRule(tv)
        && (tv.getValue() == null || tv.getValue().equals(""));
  }

  protected boolean processDefaultRule(TaggedValue tv) {
    return processPresentRule(tv)
        && equalStrings(tv.getValue(), taggedValue.getType().getDefaultValue());
  }

  private boolean equalStrings(String s1, String s2) {
    if (s1 == null && s2 == null)
      return true;
    if (s1 != null && s2 != null && s1.equals(s2))
      return true;
    return false;
  }

  private boolean equalsTaggedValueType(TagDefinition tvt1,
      TagDefinition tvt2) {
    if (tvt1 == null && tvt2 == null)
      return true;
    if (tvt1 != null && tvt2 != null) {
      if (tvt1.getDefaultValue().equals(tvt2.getDefaultValue())
          && tvt1.getTag().equals(tvt2.getTag())
          && tvt1.getType().equals(tvt2.getType()))
        return true;
    }
    return false;
  }
}