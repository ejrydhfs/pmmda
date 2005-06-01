/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
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
  private Map<Class, List<ReferenceCode>> referenceCodes;

  /**
   * Contains all version numbers of reference code types. Each version is
   * stored with its class as key.
   */
  private Map<Class, String> codeVersions;

  /**
   * Contains all reference codes managed through a known reference code
   * manager.
   */
  private Map<Class, ReferenceCodeMgr> referenceCodeMgrs;

  /**
   * List of reference code retrievers
   */
  private List<ReferenceCodeRetriever> retrievers;

  /**
   * Constructor of the class.
   */
  public ReferenceCodeMgrImpl() {
    referenceCodes = new HashMap<Class, List<ReferenceCode>>();
    codeVersions = new HashMap<Class, String>();
    referenceCodeMgrs = new HashMap<Class, ReferenceCodeMgr>();
    retrievers = new ArrayList<ReferenceCodeRetriever>();
  }

  public List<HierarchicalReferenceCode> getAllRoots(Class clazz) {
    assert (clazz != null);
    return getRootsFor(transform(getAllReferenceCodes(clazz)));
  }

  public List<HierarchicalReferenceCode> getRoots(Class clazz) {
    assert (clazz != null);
    return getRootsFor(transform(getReferenceCodes(clazz)));
  }

  public List<ReferenceCode> getAllReferenceCodes(Class clazz) {
    assert (clazz != null);

    ReferenceCodeMgr referenceCodeMgr = getReferenceCodeMgr(clazz);
    List<ReferenceCode> codes = null;
    if (referenceCodeMgr != null) {
      if (referenceCodeMgr == this) {
        codes = getAllCodes(clazz);
      } else {
        codes = referenceCodeMgr.getAllReferenceCodes(clazz);
      }
    }

    return codes;
  }

  public List<ReferenceCode> getReferenceCodes(Class clazz) {
    assert (clazz != null);

    ReferenceCodeMgr referenceCodeMgr = getReferenceCodeMgr(clazz);
    List<ReferenceCode> codes = null;
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

  public List<Class> getReferenceCodeClasses() {
    return new ArrayList<Class>(referenceCodes.keySet());
  }

  public List<Class> getAllReferenceCodeClasses() {
    List<Class> classes = getReferenceCodeClasses();
    for (ReferenceCodeMgr manager : referenceCodeMgrs.values()) {
      classes.addAll(manager.getReferenceCodeClasses());
    }
    return classes;
  }

  public String getVersion(Class clazz) {
    String version = codeVersions.get(clazz);
    if (version == null) {
      ReferenceCodeMgr manager = referenceCodeMgrs.get(clazz);
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

    for (Class clazz : referenceCodeMgr.getReferenceCodeClasses()) {
      registerReferenceCode(clazz, referenceCodeMgr);
    }
  }

  public void registerReferenceCode(Class clazz, List<ReferenceCode> codes,
      String version) {
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
      referenceCodeMgr = referenceCodeMgrs.get(clazz);
    }
    return referenceCodeMgr;
  }

  /**
   * Returns all active codes of the type as a list.
   * @param clazz class of the reference codes to retrieve.
   * @return list of all active regular reference codes.
   */
  protected List<ReferenceCode> getActiveCodes(Class clazz) {
    assert (clazz != null);

    List<ReferenceCode> codes = referenceCodes.get(clazz);
    if (codes == null) {
      return null;
    }

    List<ReferenceCode> list = new ArrayList<ReferenceCode>();

    for (ReferenceCode code : codes) {
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
  protected List<HierarchicalReferenceCode> getRootsFor(
      List<HierarchicalReferenceCode> codes) {
    assert (codes != null);
    List<HierarchicalReferenceCode> roots = null;
    for (HierarchicalReferenceCode code : codes) {
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
  protected List<ReferenceCode> getAllCodes(Class clazz) {
    assert (clazz != null);

    List<ReferenceCode> codes = referenceCodes.get(clazz);
    if (codes == null) {
      return null;
    }

    List<ReferenceCode> list = new ArrayList<ReferenceCode>(codes);
    Collections.sort(list, codeComparator);

    return list;
  }

  private List<HierarchicalReferenceCode> transform(List<ReferenceCode> codes) {
    List<HierarchicalReferenceCode> list = new ArrayList<HierarchicalReferenceCode>();
    for (ReferenceCode code : codes) {
      if (code instanceof HierarchicalReferenceCode) {
        list.add((HierarchicalReferenceCode) code);
      }
    }
    return list;
  }

  /**
   * Comparator used to sort regular code groups.
   * @author Marcel Baumann
   * @version $Revision$
   */
  private static class CodeComparator implements Comparator<ReferenceCode> {
    /**
     * Compares two regular reference code.
     * @param left left item to compare.
     * @param right right item to compare.
     * @return value of the comparaison.
     * @see java.util.Comparator
     */
    public int compare(ReferenceCode left, ReferenceCode right) {
      return (left.getSortOrder() - right.getSortOrder());
    }
  }
}