/* This code was generated automatically with the pmMDA generator. Please do 
   NOT add code outside the marked zones. All changes outside the marked areas 
   will be discarded during the next execution of the MDA generator. 
   $Id$
*/

package ch.bbv.codes.lw;

import ch.bbv.codes.*;

/** 
 * The transformer is responsible to transform a tree of data objects into its
 * lighthweight tree representation.
 * @version $Revision$
 */
public class LwTransformer {
  public static ReferenceCodeDescriptorLw transform(ReferenceCodeDescriptor item) {
    ReferenceCodeDescriptorLw lw = new ReferenceCodeDescriptorLw(item.getId());
    lw.setName(item.getName());
    lw.setHierarchical(item.isHierarchical());
    for (ValueDescriptor i : item.getValues()) {
      lw.addValue(transform(i));
    }
    return lw;
  }
  
  public static ReferenceCodeGroupLw transform(ReferenceCodeGroup item) {
    ReferenceCodeGroupLw lw = new ReferenceCodeGroupLw(item.getId());
    lw.setName(item.getName());
    lw.setDescription(item.getDescription());
    for (ReferenceCodeDescriptor i : item.getCodes()) {
      lw.addCode(transform(i));
    }
    for (ReferenceCodeGroup i : item.getGroups()) {
      lw.addGroup(transform(i));
    }
    return lw;
  }
  
  public static ValueDescriptorLw transform(ValueDescriptor item) {
    ValueDescriptorLw lw = new ValueDescriptorLw(item.getId());
    lw.setCode(item.getCode());
    lw.setOrder(item.getOrder());
    lw.setActive(item.isActive());
    lw.setParentCode(item.getParentCode());
    lw.setKeyName(item.getKeyName());
    return lw;
  }
  
}
