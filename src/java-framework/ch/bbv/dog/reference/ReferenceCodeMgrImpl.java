/* copyright 2000-2004 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence);
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.dog.reference;

import java.util.*;

/**
 * The class implements a canonical form of the reference code manager
 * interface.
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ReferenceCodeMgrImpl implements ReferenceCodeMgr {
  /**
   * Comparator used to sort the list of codes.
   */
  private static CodeComparator codeComparator;

  /**
   * initializes the comparator object used to sort the codes.
   */
  static {
    codeComparator = new CodeComparator();
  }

  /**
   * Contains all reference codes managed in this server. Each set of reference
   * codes is stored with its class as key.
   */
  private Map referenceCodes;

  /**
   * Contains all version numbers of reference code types. Each version is
   * stored with its class as key.
   */
  private Map codeVersions;

  /**
   * Contains all reference codes managed through a known reference code
   * manager.
   */
  private Map referenceCodeMgrs;

  /**
   * List of reference code retrievers
   */
  private List retrievers;

  /**
   * Constructor of the class.
   */
  public ReferenceCodeMgrImpl() {
    referenceCodes = new HashMap();
    codeVersions = new HashMap();
    referenceCodeMgrs = new HashMap();
    retrievers = new ArrayList();
  }

  public List getAllRoots(Class clazz) {
    assert (clazz != null);
    return getRootsFor(getAllReferenceCodes(clazz));
  }

  public List getRoots(Class clazz) {
    assert (clazz != null);
    return getRootsFor(getReferenceCodes(clazz));
  }

  public List getAllReferenceCodes(Class clazz) {
    assert (clazz != null);

    ReferenceCodeMgr referenceCodeMgr = getReferenceCodeMgr(clazz);
    List codes = null;
    if (referenceCodeMgr != null) {
      if (referenceCodeMgr == this) {
        codes = getAllCodes(clazz);
      } else {
        codes = referenceCodeMgr.getAllReferenceCodes(clazz);
      }
    }

    return codes;
  }

  public List getReferenceCodes(Class clazz) {
    assert (clazz != null);

    ReferenceCodeMgr referenceCodeMgr = getReferenceCodeMgr(clazz);
    List codes = null;
    if (referenceCodeMgr != null) {
      if (referenceCodeMgr == this) {
        codes = getActiveCodes(clazz);
      } else {
        codes = referenceCodeMgr.getReferenceCodes(clazz);
      }
    }

    return codes;
  }

  public ReferenceCode getReferenceCode(Class clazz, Integer code) {
    assert ((clazz != null) && (code != null));

    ReferenceCodeMgr referenceCodeMgr = getReferenceCodeMgr(clazz);
    ReferenceCode referenceCode = null;
    if (referenceCodeMgr != null) {
      if (referenceCodeMgr == this) {
      } else {
        referenceCode = referenceCodeMgr.getReferenceCode(clazz, code);
      }
    }

    return referenceCode;
  }

  public List getReferenceCodeClasses() {
    return new ArrayList(referenceCodes.values());
  }

  public List getAllReferenceCodeClasses() {
    List classes = getReferenceCodeClasses();
    Iterator managers = referenceCodeMgrs.values().iterator();
    while (managers.hasNext()) {
      ReferenceCodeMgr manager = (ReferenceCodeMgr) managers.next();
      classes.addAll(manager.getReferenceCodeClasses());
    }
    return classes;
  }

  public String getVersion(Class clazz) {
    String version = (String) codeVersions.get(clazz);
    if (version == null) {
      ReferenceCodeMgr manager = (ReferenceCodeMgr) referenceCodeMgrs
          .get(clazz);
      if (manager != null) {
        version = manager.getVersion(clazz);
      }
    }
    return version;
  }

  public void registerReferenceCode(Class clazz, ReferenceCodeMgr referenceMgr) {
    assert ((clazz != null) && (referenceMgr != null));
    referenceCodeMgrs.put(clazz, referenceMgr);
  }

  public void registerReferenceCodes(ReferenceCodeMgr referenceCodeMgr) {
    assert (referenceCodeMgr != null);

    Iterator classes = referenceCodeMgr.getReferenceCodeClasses().iterator();
    while (classes.hasNext()) {
      registerReferenceCode((Class) classes.next(), referenceCodeMgr);
    }
  }

  public void registerReferenceCode(Class clazz, List codes, String version) {
    assert ((clazz != null) && (codes != null));
    referenceCodes.put(clazz, codes);
    codeVersions.put(clazz, version);
  }

  public HierarchicalReferenceCode getRootFor(HierarchicalReferenceCode code) {
    assert (code != null);

    Class clazz = code.getClass();
    HierarchicalReferenceCode root = code;
    while (code.getParentCode() != null) {
      root = (HierarchicalReferenceCode) getReferenceCode(clazz, code
          .getParentCode());
    }

    return root;
  }

  public HierarchicalReferenceCode getParentFor(HierarchicalReferenceCode code) {
    assert (code != null);

    Class clazz = code.getClass();
    return (HierarchicalReferenceCode) getReferenceCode(clazz, code
        .getParentCode());
  }

  public void addRetriever(ReferenceCodeRetriever retriever) {
    retrievers.add(retriever);
  }

  /**
   * Returns the reference code manager for the given class.
   * @param clazz reference code type class which manager is requested.
   * @return the requested manager if found otherwise null.
   */
  protected ReferenceCodeMgr getReferenceCodeMgr(Class clazz) {
    assert (clazz != null);

    ReferenceCodeMgr referenceCodeMgr = (ReferenceCodeMgr) referenceCodes
        .get(clazz);
    if (referenceCodeMgr == null) {
      referenceCodeMgr = (ReferenceCodeMgr) referenceCodeMgrs.get(clazz);
    }

    return referenceCodeMgr;
  }

  /**
   * Returns all active codes of the type as a list.
   * @param clazz class of the reference codes to retrieve.
   * @return list of all active regular reference codes.
   */
  protected List getActiveCodes(Class clazz) {
    assert (clazz != null);

    HashMap codes = (HashMap) referenceCodes.get(clazz);
    if (codes == null) {
      return null;
    }

    List list = new ArrayList();

    for (Iterator i = codes.values().iterator(); i.hasNext();) {
      ReferenceCode code = (ReferenceCode) i.next();

      if (code.getActive()) {
        list.add(code);
      }
    }

    Collections.sort(list, codeComparator);

    return list;
  }

  /**
   * Returns the set of roots for the list of hierarchical reference codes.
   * @param codes list of codes which roots are searched.
   * @return the list of root hierarchical reference codes.
   */
  protected List getRootsFor(List codes) {
    assert (codes != null);

    List roots = null;
    for (Iterator i = codes.iterator(); i.hasNext();) {
      HierarchicalReferenceCode code = (HierarchicalReferenceCode) i.next();
      if (code.getParentCode() != null) {
        roots.add(code);
      }
    }

    return roots;
  }

  /**
   * Returns all codes of the type as a list.
   * @param clazz class of the reference codes to retrieve.
   * @return list of all regular reference codes.
   */
  protected List getAllCodes(Class clazz) {
    assert (clazz != null);

    HashMap codes = (HashMap) referenceCodes.get(clazz);
    if (codes == null) {
      return null;
    }

    List list = new ArrayList(codes.values());
    Collections.sort(list, codeComparator);

    return list;
  }

  /**
   * Comparator used to sort regular code groups.
   */
  protected static class CodeComparator implements Comparator {
    /**
     * Compares two regular reference code.
     * @param left left item to compare.
     * @param right right item to compare.
     * @return value of the comparaison.
     * @see java.util.Comparator
     */
    public int compare(Object left, Object right) {
      return (((ReferenceCode) left).getSortOrder() - ((ReferenceCode) right)
          .getSortOrder());
    }
  }
}