/* This code was generated automatically with the MDA generator Version 0.2.
   Please do NOT add code outside the marked zones. All changes outside the 
   marked area will be discarded during the next execution of the MDA generator. 
*/


package ch.bbv.codes.lw;

import java.util.Iterator;
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
    lw.setHierarchical(item.getHierarchical());
    for (Iterator i = item.getValues().iterator(); i.hasNext();) {
      lw.addValue(transform((ValueDescriptor)i.next()));
    }
    return lw;
  }
  
  public static ReferenceCodeGroupLw transform(ReferenceCodeGroup item) {
    ReferenceCodeGroupLw lw = new ReferenceCodeGroupLw(item.getId());
    lw.setName(item.getName());
    lw.setDescription(item.getDescription());
    for (Iterator i = item.getCodes().iterator(); i.hasNext();) {
      lw.addCode(transform((ReferenceCodeDescriptor)i.next()));
    }
    for (Iterator i = item.getGroups().iterator(); i.hasNext();) {
      lw.addGroup(transform((ReferenceCodeGroup)i.next()));
    }
    return lw;
  }
  
  public static ValueDescriptorLw transform(ValueDescriptor item) {
    ValueDescriptorLw lw = new ValueDescriptorLw(item.getId());
    lw.setCode(item.getCode());
    lw.setOrder(item.getOrder());
    lw.setActive(item.getActive());
    lw.setParentCode(item.getParentCode());
    lw.setKeyName(item.getKeyName());
    return lw;
  }
  
}
