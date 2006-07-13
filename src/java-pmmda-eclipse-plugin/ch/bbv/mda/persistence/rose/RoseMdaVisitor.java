/* copyright 2005 Adrian Bachofen
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.mda.persistence.rose;

import java.util.*;

import org.eclipse.emf.importer.rose.builder.RoseStrings;
import org.eclipse.emf.importer.rose.builder.RoseVisitor;
import org.eclipse.emf.importer.rose.builder.RoseWalker;
import org.eclipse.emf.importer.rose.parser.RoseNode;
import org.eclipse.emf.importer.rose.parser.Util;

import ch.bbv.mda.*;
import ch.bbv.mda.uml.*;
import ch.bbv.mda.generators.CartridgeFactory;
import ch.bbv.mda.generators.ItemFactory;
import ch.bbv.mda.persistence.eclipse.RosePersistenceHandler;

public class RoseMdaVisitor implements RoseVisitor {

  protected MetaModel model;
  protected Map packageTable = new HashMap();
  protected Map classTable = new HashMap();
  protected Map propertyTable = new HashMap();
  protected ArrayList<Object[]> inheritanceTable = new ArrayList<Object[]>(); // holds new inheritance relations
  protected ArrayList<Object[]> realizeTable = new ArrayList<Object[]>(); // holds new realize relations
  private List<ItemToDelete> itemsToDelete;
  protected Map quidTable = new HashMap(); // holds quids of classes - used for type references (qualified name : quid with surrounding "")
  protected ArrayList<Object[]> updateQuiduLaterTable = new ArrayList<Object[]>(); // holds qualifiedTypeNames and roseNodes. The type names cannot be obtained at the time processed. Hence we have to process them at the end.
  protected ItemFactory itemFactory = CartridgeFactory.getInstance().getItemFactory();
  
  public RoseMdaVisitor(MetaModel source) {
    model = source;
    itemsToDelete = new ArrayList<ItemToDelete>();
  }
  
  public void visitList(RoseNode roseNode)
  {
  }

  public void visitObject(RoseNode roseNode)
  {
    String roseNodeValue = roseNode.getValue();
    String objectKey = roseNode.getKey();
    String objectType = Util.getType(roseNodeValue);
    String objectName = Util.getName(roseNodeValue);

    //System.out.println("---> visitObject,,, objectKey - objectType - objectName: " + objectKey + ", " + objectType + ", " + objectName);

    // Get the parent object.
    //
    RoseNode currentNode = roseNode;
    Object parent = currentNode.getNode();
    while (parent == null)
    {
      currentNode = currentNode.getParent();
      if(currentNode == null) break;
      parent = currentNode.getNode();
    }

    visitObject(roseNode, roseNodeValue, objectKey, objectType, objectName, parent);
  }

  protected void visitObject(RoseNode roseNode, String roseNodeValue, String objectKey, String objectType, String objectName, Object parent)
  {
    if (objectKey.equals("") && objectType.equals(RoseStrings.DESIGN))
    {
      visitDesign(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectKey.equals("") && objectType.equals(RoseStrings.CLASS_CATEGORY))
    {
      visitClassCategory(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.CLASS))
    {
      visitClass(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals("UseCase"))
    {
      visitUseCase(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.OPERATION))
    {
      visitOperation(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.PARAMETER))
    {
      visitParameter(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.INHERITANCE_RELATIONSHIP) || objectType.equals(RoseStrings.REALIZE_RELATIONSHIP))
    {
      visitInheritanceRelationship(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.CLASSATTRIBUTE) && (!roseNode.isDerived() || !"reference".equals(roseNode.getStereotype())))
    {
      visitClassattribute(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.ASSOCIATION))
    {
      visitAssociation(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
    else if (objectType.equals(RoseStrings.ROLE) && !objectName.startsWith("/"))
    {
      visitRole(roseNode, roseNodeValue, objectKey, objectName, parent);
    }
  }

  protected RoseNode visitDesign(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // set default values for pmMDA related tagged values
    RoseNode properties = roseNode.findNodeWithKey(RoseStrings.PROPERTIES);
    if(properties == null) {
      properties = new RoseNode(RoseStrings.PROPERTIES, RoseStrings.UPROPERTIES, RoseNode.OBJECT);
      roseNode.addNode(properties);
    }
    MetaPackage metaPackage = new MetaPackage();
    itemFactory.initialize(metaPackage, MetaPackage.class);
    processDefaultTaggedValues(properties, RoseStrings.DEFAULT_CATEGORY, metaPackage);
    
    MetaClass metaClass = new MetaClass();
    itemFactory.initialize(metaClass, MetaClass.class);
    processDefaultTaggedValues(properties, RoseStrings.DEFAULT_CLASS, metaClass);
    
    MetaIndexedProperty metaIndexedProperty = new MetaIndexedProperty();
    itemFactory.initialize(metaIndexedProperty, MetaIndexedProperty.class);
    processDefaultTaggedValues(properties, "default__Association", metaIndexedProperty);
    
    MetaProperty metaProperty = new MetaProperty();
    itemFactory.initialize(metaProperty, MetaProperty.class);
    processDefaultTaggedValues(properties, RoseStrings.DEFAULT_ATTRIBUTE, metaProperty);

    return roseNode;
  }
  
  /**
   * Processes one kind of default tagged values. It adds if not existing, it changes if already there, and
   * it removes unused default tagged values.
   * @param properties where the default tagged values are located
   * @param defaultType string to describe the kind of default tagged values (e.g. RoseStrings.DEFAULT_CATEGORY).
   * @param metaElement containing the default tagged values.
   */
  protected void processDefaultTaggedValues(RoseNode properties, String defaultType, MetaElement metaElement) {
    RoseNode defaultCategory = findTaggedValue(properties, RoseStrings.ATTRIBUTES, defaultType);
    if(defaultCategory == null) {
      RoseNode attrSet = properties.findNodeWithKey(RoseStrings.ATTRIBUTES);
      if(attrSet == null || !attrSet.getValue().equals(RoseStrings.ATTRIBUTE_SET)){
        attrSet = new RoseNode(RoseStrings.ATTRIBUTES, RoseStrings.ATTRIBUTE_SET, RoseNode.LIST);
        properties.addNode(attrSet);
      }
      defaultCategory = createDefaultTaggedValueAttribute(defaultType);
      attrSet.addNode(defaultCategory);
      RoseNode value = defaultCategory.findNodeWithKey(RoseStrings.VALUE); 
      // now add the tagged values
      for(TaggedValue tag : metaElement.getTags()) {
        if(!(tag.getTag().equals(UmlConstants.PERSISTENCE_TAG) && defaultType.equals(RoseStrings.DEFAULT_CLASS))) {
          value.addNode(createTaggedValue(tag));
        }
      }
    } else { // if there is already a default category attribute set then only add missing tagged values
      RoseNode value = defaultCategory.findNodeWithKey(RoseStrings.VALUE); 
      for(TaggedValue tag : metaElement.getTags()) {
        RoseNode tv = findTaggedValue(defaultCategory, RoseStrings.VALUE, tag.getTag());
        if(tv == null) {
          if(!(tag.getTag().equals(UmlConstants.PERSISTENCE_TAG) && defaultType.equals(RoseStrings.DEFAULT_CLASS))) {
            value.addNode(createTaggedValue(tag));
          }
        } else {
          RoseNode v = tv.findNodeWithKey(RoseStrings.VALUE);
          setValueOfTaggedValue(v, tag.getValue());
        }
      }

      // now remove unused tags
      List tags = value.getNodes();
      for (int i = 0; i < tags.size(); i++)
      {
        RoseNode tv = (RoseNode)tags.get(i);
        RoseNode name = tv.findNodeWithKey(RoseStrings.NAME); // there is no need to check for correct tool, because all tagged values in here belong to pmMDA.
        TaggedValue tag = metaElement.getTaggedValue(Util.getName(name.getValue()));
        if(tag == null || (tag.getTag().equals(UmlConstants.PERSISTENCE_TAG) && defaultType.equals(RoseStrings.DEFAULT_CLASS))) {
          itemsToDelete.add(new ItemToDelete(tv, value));
        }
      }
    }
  }

  protected RoseNode visitClassCategory(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // find package in model
    MetaPackage element = null;
    String qualifiedName = getQualifiedName(roseNode);
    for(MetaPackage item : model.getPackages()) {
      if(item.getName().equals(qualifiedName)) {
        element = item;
        break;
      }
    }
    // if found then synchronize package
    if(element != null) {
      updateRosePackage(element, roseNode);
      packageTable.put(element, roseNode);
    }
    // if not in MetaModel -> delete it in Rose Model
    else {
      parent = roseNode.getParent();
      if(parent != null && parent instanceof RoseNode) {
        itemsToDelete.add(new ItemToDelete(roseNode, (RoseNode)parent));
      }
    }
    return roseNode;
  }
  
  protected void updateRosePackage(MetaPackage metaPackage, RoseNode rosePackage) {
    MetaElement defaultMetaElement = new MetaPackage();
    itemFactory.initialize(defaultMetaElement, MetaPackage.class);
    updateGeneralRoseElements(rosePackage, metaPackage, defaultMetaElement);
  }

  protected RoseNode visitClass(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // find classifier in model
    MetaClassifier element = findClassifier(roseNode);
    // if found then synchronize package
    if(element != null) {
      updateRoseClass(element, roseNode);
      classTable.put(element, roseNode);
      putQuid(roseNode, element.getQualifiedName());
      updateRoseInheritanceRelations(element, roseNode);
    }
    // if not in MetaModel -> delete it in Rose Model
    else {
      parent = roseNode.getParent();
      if(parent != null && parent instanceof RoseNode) {
        itemsToDelete.add(new ItemToDelete(roseNode, (RoseNode)parent));
      }
    }
    return roseNode;
  }
  
  protected void updateRoseClass(MetaElement metaElement, RoseNode roseNode) {
    MetaElement defaultMetaElement = null;
    if(metaElement instanceof MetaDatatype) {
      defaultMetaElement = new MetaDatatype();
      itemFactory.initialize(defaultMetaElement, MetaDatatype.class);
    } else {
      defaultMetaElement = new MetaClass();
      itemFactory.initialize(defaultMetaElement, MetaClass.class);
    }
    updateGeneralRoseElements(roseNode, metaElement, defaultMetaElement);
    
    // persistence tag
    TaggedValue tag = metaElement.getTaggedValue(UmlConstants.PERSISTENCE_TAG);
    if(tag != null && tag.getValue().equals("true")) {
      setOrAddAttribute(roseNode, RoseStrings.PERSISTENCE, RoseStrings.PERSISTENT);
    } else {
      RoseNode attribute = roseNode.findNodeWithKey(RoseStrings.PERSISTENCE);
      if(attribute != null) {
        roseNode.deleteNode(attribute);
      }
    }
  }
  
  protected RoseNode visitUseCase(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // find classifier in model
    MetaClassifier element = findClassifier(roseNode);
    if(element != null && !(element instanceof MetaUseCase)) return roseNode;
    // if found then synchronize package
    if(element != null) {
      updateRoseUseCase(element, roseNode);
      classTable.put(element, roseNode);
      putQuid(roseNode, element.getQualifiedName());
      updateRoseUseCaseInheritanceRelations((MetaUseCase)element, roseNode);
    }
    // if not in MetaModel -> delete it in Rose Model
    else {
      parent = roseNode.getParent();
      if(parent != null && parent instanceof RoseNode) {
        itemsToDelete.add(new ItemToDelete(roseNode, (RoseNode)parent));
      }
    }
    return roseNode;
  }
  
  protected void updateRoseUseCase(MetaElement metaElement, RoseNode roseNode) {
    MetaElement defaultMetaElement = new MetaUseCase();
    itemFactory.initialize(defaultMetaElement, MetaUseCase.class);
    updateGeneralRoseElements(roseNode, metaElement, defaultMetaElement);
  }
  
  protected void updateRoseInheritanceRelations(MetaClassifier clazz, RoseNode roseClass) {
    if(!(clazz instanceof MetaClass)) return;
    MetaClass c = (MetaClass)clazz;
    for(MetaClass ancestor : c.retrieveAncestors()) {
      RoseNode inheritanceRelation = findRoseInheritanceRelation(roseClass, ancestor.getQualifiedName());
      if(inheritanceRelation != null) {
        // update it, but currently there is nothing to do
      } else { // mark them for adding
        Object[] listEntry = { ancestor, roseClass };
        inheritanceTable.add(listEntry);
      }
    }
    removeRoseInheritanceRelations(roseClass, clazz);
    
    for(MetaClass ancestor : c.retrieveInterfaces()) {
      RoseNode realizeRelation = findRoseRealizeRelation(roseClass, ancestor.getQualifiedName());
      if(realizeRelation != null) {
        // update it, but currently there is nothing to do
      } else { // mark them for adding
        Object[] listEntry = { ancestor, roseClass };
        realizeTable.add(listEntry);
      }
    }
    removeRoseRealizeRelations(roseClass, clazz);
  }
  
  protected void updateRoseUseCaseInheritanceRelations(MetaUseCase useCase, RoseNode roseUseCase) {
    for(MetaUseCase ancestor : useCase.retrieveAncestors()) {
      RoseNode inheritanceRelation = findRoseInheritanceRelation(roseUseCase, ancestor.getQualifiedName());
      if(inheritanceRelation != null) {
        // update it, but currently there is nothing to do
      } else { // mark them for adding
        Object[] listEntry = { ancestor, roseUseCase };
        inheritanceTable.add(listEntry);
      }
    }
    removeRoseInheritanceRelations(roseUseCase, useCase);
  }
  
  protected RoseNode findRoseInheritanceRelation(RoseNode roseClass, String qualifiedNameSupplier) {
    RoseNode superClassesList = roseClass.findNodeWithKey(RoseStrings.SUPERCLASSES);
    if(superClassesList == null) return null;
    List superclasses = superClassesList.getNodes();
    RoseNode inheritanceRelation = null;
    for (int i = 0; i < superclasses.size(); i++) {
      inheritanceRelation = (RoseNode)superclasses.get(i);
      if(Util.getType(inheritanceRelation.getValue()).equals(RoseStrings.INHERITANCE_RELATIONSHIP)) {
        RoseNode supplier = inheritanceRelation.findNodeWithKey(RoseStrings.SUPPLIER);
        if(supplier == null) continue;
        String name = Util.getName(supplier.getValue()); 
        if(name != null) {
          if(convertQualifiedName(name).equals(qualifiedNameSupplier)) return inheritanceRelation;
        }
      }
    }
    return null;
  }

  protected void removeRoseInheritanceRelations(RoseNode roseClass, MetaClassifier metaClassifier) {
    if(!(metaClassifier instanceof MetaClass)) return;
    MetaClass metaClass = (MetaClass)metaClassifier;
    RoseNode superClassesList = roseClass.findNodeWithKey(RoseStrings.SUPERCLASSES);
    if(superClassesList == null) return;
    List superclasses = superClassesList.getNodes();
    RoseNode inheritanceRelation = null;
    for (int i = 0; i < superclasses.size(); i++) {
      inheritanceRelation = (RoseNode)superclasses.get(i);
      if(Util.getType(inheritanceRelation.getValue()).equals(RoseStrings.INHERITANCE_RELATIONSHIP)) {
        RoseNode supplier = inheritanceRelation.findNodeWithKey(RoseStrings.SUPPLIER);
        if(supplier == null) continue;
        String name = Util.getName(supplier.getValue()); 
        if(name != null) {
          name = convertQualifiedName(name);
          if(!metaClass.hasAncestor(name)) {
            itemsToDelete.add(new ItemToDelete(superClassesList, inheritanceRelation));
          }
        }
      }
    }
  }
  
  protected RoseNode findRoseRealizeRelation(RoseNode roseClass, String qualifiedNameSupplier) {
    RoseNode realizeList = roseClass.findNodeWithKey(RoseStrings.REALIZED_INTERFACES);
    if(realizeList == null) return null;
    List interfaces = realizeList.getNodes();
    RoseNode realizeRelation = null;
    for (int i = 0; i < interfaces.size(); i++) {
      realizeRelation = (RoseNode)interfaces.get(i);
      if(Util.getType(realizeRelation.getValue()).equals(RoseStrings.REALIZE_RELATIONSHIP)) {
        RoseNode supplier = realizeRelation.findNodeWithKey(RoseStrings.SUPPLIER);
        if(supplier == null) continue;
        String name = Util.getName(supplier.getValue()); 
        if(name != null) {
          if(convertQualifiedName(name).equals(qualifiedNameSupplier)) return realizeRelation;
        }
      }
    }
    return null;
  }

  protected void removeRoseRealizeRelations(RoseNode roseClass, MetaClassifier metaClassifier) {
    if(!(metaClassifier instanceof MetaClass)) return;
    MetaClass metaClass = (MetaClass)metaClassifier;
    RoseNode realizeList = roseClass.findNodeWithKey(RoseStrings.REALIZED_INTERFACES);
    if(realizeList == null) return;
    List interfaces = realizeList.getNodes();
    RoseNode realizeRelation = null;
    for (int i = 0; i < interfaces.size(); i++) {
      realizeRelation = (RoseNode)interfaces.get(i);
      if(Util.getType(realizeRelation.getValue()).equals(RoseStrings.REALIZE_RELATIONSHIP)) {
        RoseNode supplier = realizeRelation.findNodeWithKey(RoseStrings.SUPPLIER);
        if(supplier == null) continue;
        String name = Util.getName(supplier.getValue()); 
        if(name != null) {
          name = convertQualifiedName(name);
          if(!metaClass.hasInterface(name)) {
            itemsToDelete.add(new ItemToDelete(realizeList, realizeRelation));
          }
        }
      }
    }
  }
  
  protected RoseNode visitOperation(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // intentionally left blanc
    return null;
  }

  protected RoseNode visitParameter(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // intentionally left blanc
    return null;
  }

  protected RoseNode visitInheritanceRelationship(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // intentionally left blanc
    return null;
  }

  protected RoseNode visitClassattribute(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // find rose class having this attribute
    RoseNode roseClass = getClassOfAttribute(roseNode);
    // find corresponding MetaClass
    MetaClass metaClass = findClass(roseClass);
    MetaProperty metaProperty = null;
    if(metaClass != null) { // if null then class has been deleted and property has to be deleted too.
      for(MetaProperty property : metaClass.getProperties()) {
        if(property.getName().equals(Util.getName(roseNode.getValue()))) {
          metaProperty = property;
          break;
        }
      }
    }
    if(metaProperty != null) {
      updateRoseAttribute(metaProperty, roseNode);
      propertyTable.put(metaProperty, roseNode);
    } else {
      parent = roseNode.getParent();
      if(parent != null && parent instanceof RoseNode) {
        itemsToDelete.add(new ItemToDelete(roseNode, (RoseNode)parent));
      }
    }
    return roseNode;
  }
  
  protected void updateRoseAttribute(MetaProperty metaProperty, RoseNode roseNode) {
    MetaElement defaultMetaElement = new MetaProperty();
    itemFactory.initialize(defaultMetaElement, MetaProperty.class);
    updateGeneralRoseElements(roseNode, metaProperty, defaultMetaElement);
    // set type of attribute
    String quid = (String)quidTable.get(metaProperty.getTypeName());
    RoseNode type = roseNode.findNodeWithKey(RoseStrings.TYPE);
    if(type == null) {
      roseNode.addNode(new RoseNode(RoseStrings.TYPE, formatAttributeValue(unqualifyName(metaProperty.getTypeName())), RoseNode.STRING));
      if(quid != null) roseNode.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    } else {
      type.setValue(formatAttributeValue(unqualifyName(metaProperty.getTypeName())));
      if(quid != null) {
        RoseNode quidu = roseNode.findNodeWithKey(RoseStrings.QUIDU);
        if(quidu != null) roseNode.deleteNode(quidu);
      	roseNode.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
      } else {
      	updateQuiduLater(roseNode, metaProperty.getTypeName());
      }
    }
    setOrAddAttribute(roseNode, RoseStrings.INITV, metaProperty.getInitialValue());
  }

  protected RoseNode visitAssociation(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // find rose class having this attribute
    List roles = roseNode.findNodeWithKey(RoseStrings.ROLES).getNodes();
    RoseNode aggregateSide = null; RoseNode navigableSide = null; Boolean isOne1 = false; Boolean isOne2 = false;
    
    // if both multiplicities are "1", or if one is "1" and the other null then pmMDA treats this association like a property
    RoseNode supplier1 = ((RoseNode)roles.get(0)).findNodeWithKey(RoseStrings.SUPPLIER);
    RoseNode multiplicity1 = ((RoseNode)roles.get(0)).findNodeWithKey(RoseStrings.CLIENT_CARDINALITY);
    if(multiplicity1 != null) {
      isOne1 = multiplicity1.findNodeWithValue("\"1\"") != null; 
    }
    RoseNode supplier2 = ((RoseNode)roles.get(1)).findNodeWithKey(RoseStrings.SUPPLIER);
    RoseNode multiplicity2 = ((RoseNode)roles.get(1)).findNodeWithKey(RoseStrings.CLIENT_CARDINALITY);
    if(multiplicity2 != null) {
      isOne2 = multiplicity2.findNodeWithValue("\"1\"") != null; 
    }
    if((isOne1 && isOne2) || (isOne1 && multiplicity2 == null) || (isOne2 && multiplicity1 == null)) {
      return visitOneOneAssociation(roseNode, supplier1, supplier2, roles); 
    }

    MetaIndexedProperty metaProperty = null;
    MetaClassifier metaClass1 = findClassifier(Util.getName(supplier1.getValue()));
    MetaClassifier metaClass2 = findClassifier(Util.getName(supplier2.getValue()));
    // if one of the classifier is a use case then we do not process this relation (TODO: if one can manipulate usecases and relations within pmMDA then change here).
    if(metaClass1 instanceof MetaUseCase || metaClass2 instanceof MetaUseCase) {
    	return roseNode;
    }
    if(metaClass1 != null && metaClass2 != null) { // if one is null then a class has been deleted and the association is dangling -> delete it
      if(metaClass1 instanceof MetaClass) {
        for(MetaIndexedProperty property : ((MetaClass)metaClass1).getIndexedProperties()) {
          if(property.getName().equals(Util.getName(roseNode.getValue()))) {
            metaProperty = property;
            aggregateSide = (RoseNode)roles.get(0);
            navigableSide = (RoseNode)roles.get(1);
            break;
          }
        }
      }
      if(metaProperty == null) {
        if(metaClass2 instanceof MetaClass) {
          for(MetaIndexedProperty property : ((MetaClass)metaClass2).getIndexedProperties()) {
            if(property.getName().equals(Util.getName(roseNode.getValue()))) {
              metaProperty = property;
              aggregateSide = (RoseNode)roles.get(1);
              navigableSide = (RoseNode)roles.get(0);
              break;
            }
          }
        }
      }
    }
    if(metaProperty != null) {
      updateRoseAssociation(metaProperty, roseNode, aggregateSide, navigableSide);
      propertyTable.put(metaProperty, roseNode);
    } else {
      parent = roseNode.getParent();
      if(parent != null && parent instanceof RoseNode) {
        itemsToDelete.add(new ItemToDelete(roseNode, (RoseNode)parent));
      }
    }
    return roseNode;
  }
  
  protected void updateRoseAssociation(MetaIndexedProperty metaProperty, RoseNode association, RoseNode aggregateSide, RoseNode navigableSide) {
    MetaElement defaultMetaElement = new MetaIndexedProperty();
    itemFactory.initialize(defaultMetaElement, MetaIndexedProperty.class);
    updateGeneralRoseElements(association, metaProperty, defaultMetaElement);
    setOrAddAttribute(aggregateSide, RoseStrings.EXPORTCONTROL, toRoseVisibility(metaProperty.getVisibility()));

    // update containment type
    RoseNode containment = navigableSide.findNodeWithKey(RoseStrings.CONTAINMENT);
    RoseNode is_aggregate = aggregateSide.findNodeWithKey(RoseStrings.IS_AGGREGATE);
    if(!metaProperty.isAggregation() && !metaProperty.isComposition() && containment != null) {
      navigableSide.deleteNode(containment);
    }
    if(is_aggregate == null && (metaProperty.isAggregation() || metaProperty.isComposition())) {
      aggregateSide.addNode(new RoseNode(RoseStrings.IS_AGGREGATE, "TRUE", RoseNode.STRING));
    }
    if(metaProperty.isComposition()) {
      if( containment == null) {
        navigableSide.addNode(new RoseNode(RoseStrings.CONTAINMENT, formatAttributeValue(RoseStrings.BY_VALUE), RoseNode.STRING));
      } else {
        containment.setValue(formatAttributeValue(RoseStrings.BY_VALUE));
      }
    } else if(metaProperty.isAggregation()) {
      if( containment == null) {
        navigableSide.addNode(new RoseNode(RoseStrings.CONTAINMENT, formatAttributeValue("By Reference"), RoseNode.STRING));
      } else {
        containment.setValue(formatAttributeValue("By Reference"));
      }
    } else if (containment != null) {
      navigableSide.deleteNode(containment);
    }
    
    // set type of property
    String quid = (String)quidTable.get(metaProperty.getTypeName());
    RoseNode type = navigableSide.findNodeWithKey(RoseStrings.SUPPLIER);
    if(type == null) {
      navigableSide.addNode(new RoseNode(RoseStrings.SUPPLIER, formatAttributeValue(((MetaClass)metaProperty.getContext()).getQualifiedName()), RoseNode.STRING));
      if(quid != null) navigableSide.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    } else {
      if(quid != null) {
        RoseNode quidu = navigableSide.findNodeWithKey(RoseStrings.QUIDU);
        if(quidu != null) navigableSide.deleteNode(quidu);
      	navigableSide.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
      } else {
      	updateQuiduLater(navigableSide, metaProperty.getTypeName());
      }
    }
  }
  
  protected RoseNode visitOneOneAssociation(RoseNode roseNode, RoseNode supplier1, RoseNode supplier2, List roles) {
    RoseNode aggregateSide = null; RoseNode navigableSide = null;
    MetaProperty metaProperty = null;
    MetaClassifier metaClass1 = findClassifier(Util.getName(supplier1.getValue()));
    MetaClassifier metaClass2 = findClassifier(Util.getName(supplier2.getValue()));
    if(metaClass1 != null && metaClass2 != null) { // if one class is null then delete the association
      if(metaClass1 instanceof MetaClass) {
        for(MetaProperty property : ((MetaClass)metaClass1).getProperties()) {
          if(property.getName().equals(Util.getName(roseNode.getValue()))) {
            metaProperty = property;
            aggregateSide = (RoseNode)roles.get(0);
            navigableSide = (RoseNode)roles.get(1);
            break;
          }
        }
      }
      if(metaProperty == null) {
        if(metaClass2 instanceof MetaClass) {
          for(MetaProperty property : ((MetaClass)metaClass2).getProperties()) {
            if(property.getName().equals(Util.getName(roseNode.getValue()))) {
              metaProperty = property;
              aggregateSide = (RoseNode)roles.get(1);
              navigableSide = (RoseNode)roles.get(0);
              break;
            }
          }
        }
      }
    }
    if(metaProperty != null) {
      updateRoseOneOneAssociation(metaProperty, roseNode, aggregateSide, navigableSide);
      propertyTable.put(metaProperty, roseNode);
    } else {
      RoseNode parent = roseNode.getParent();
      if(parent != null && parent instanceof RoseNode) {
        itemsToDelete.add(new ItemToDelete(roseNode, (RoseNode)parent));
      }
    }
    return roseNode;
  }
  
  protected void updateRoseOneOneAssociation(MetaProperty metaProperty, RoseNode roseNode, RoseNode aggregateSide, RoseNode navigableSide) {
    MetaElement defaultMetaElement = new MetaProperty();
    itemFactory.initialize(defaultMetaElement, MetaProperty.class);
    updateGeneralRoseElements(roseNode, metaProperty, defaultMetaElement);
    setOrAddAttribute(aggregateSide, RoseStrings.EXPORTCONTROL, toRoseVisibility(metaProperty.getVisibility()));

    // set type of property
    String quid = (String)quidTable.get(metaProperty.getTypeName());
    RoseNode type = navigableSide.findNodeWithKey(RoseStrings.SUPPLIER);
    if(type == null) {
      navigableSide.addNode(new RoseNode(RoseStrings.SUPPLIER, formatAttributeValue(((MetaClass)metaProperty.getContext()).getQualifiedName()), RoseNode.STRING));
      if(quid != null) navigableSide.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    } else {
      if(quid != null) {
        RoseNode quidu = navigableSide.findNodeWithKey(RoseStrings.QUIDU);
        if(quidu != null) navigableSide.deleteNode(quidu);
      	navigableSide.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
      } else {
      	updateQuiduLater(navigableSide, metaProperty.getTypeName());
      }
    }
  }

  protected RoseNode visitRole(RoseNode roseNode, String roseNodeValue, String objectKey, String objectName, Object parent) {
    // intentionally left blanc
    return null;
  }
  
  public void processNewElements(RoseNode roseNode) {
    processNewPackages(roseNode);
    processNewClassesifiersAndProperties(roseNode);
    processNewInheritanceRelations(roseNode);
    processNewRealizeRelations(roseNode);
  }

  protected void processNewPackages(RoseNode roseNode) {
    for (MetaPackage item : model.getPackages()) {
      if(packageTable.containsKey(item)) continue;
      addPackage(roseNode, item);
    }
  }

  protected RoseNode addPackage(RoseNode roseModel, MetaPackage item) {
    assert(roseModel != null);
    RoseNode node = null;
    
    if(item == null) return addRootPackage(roseModel);
    RoseLocatorVisitor locator = new RoseLocatorVisitor(item.getName(), RoseStrings.CLASS_CATEGORY);
    RoseWalker roseWalker = new RoseWalker(roseModel);
    roseWalker.traverse(locator);
    if(locator.getResult() != null) return locator.getResult();
    node = locator.getBestMatch();
    if(node == null) node = addRootPackage(roseModel);
    
    String residuum = item.getName().substring(locator.getBestMatchingQualifiedName().length());
    if(residuum.startsWith(".")) residuum = residuum.substring(1);
    StringTokenizer s = new StringTokenizer(residuum, ".");
    while(s.hasMoreTokens()) {
      node = addRosePackage(node, s.nextToken());
      updateRosePackage(item, node);
    }
    return node;
  }

  protected RoseNode addRootPackage(RoseNode roseModel) {
    assert(roseModel != null);
    MetaPackage defaultItem = new MetaPackage();
    itemFactory.initialize(defaultItem, MetaPackage.class);
    RoseNode node = roseModel.findNodeWithKey(RoseStrings.ROOT_CATEGORY);
    if(node == null) {
      node = addRoseRootCategory(roseModel);
      updateRosePackage(defaultItem, node);
    }
    return node;
  }
  
  protected RoseNode addRootUseCasePackage(RoseNode roseModel) {
    assert(roseModel != null);
    MetaPackage defaultItem = new MetaPackage();
    itemFactory.initialize(defaultItem, MetaPackage.class);
    RoseNode node = roseModel.findNodeWithKey(RoseStrings.ROOT_USECASE_PACKAGE);
    if(node == null) {
      node = addRoseRootUseCaseCategory(roseModel);
      updateRosePackage(defaultItem, node);
    }
    return node;
  }
  
  protected void processNewClassesifiersAndProperties(RoseNode roseNode) {
    for(MetaDatatype type : model.getDatatypes()) {
      if(!classTable.containsKey(type)) {
        addDatatype(roseNode, type);
      }
    }
    for(MetaClass clazz : model.getClasses()) {
      if(!classTable.containsKey(clazz)) {
        addClass(roseNode, null, clazz);
      }
      processNewNestedClasses(roseNode, clazz);
    }
    for(MetaUseCase useCase : model.getUseCases()) {
    	if(!classTable.containsKey(useCase)) {
    		addUseCase(roseNode, null, useCase);
    	}
    }
    for(MetaPackage pack : model.getPackages()) {
      for(MetaClass clazz : pack.getClasses()) {
        if(!classTable.containsKey(clazz)) {
          addClass(roseNode, pack, clazz);
        }
        processNewNestedClasses(roseNode, clazz);
      }
      for(MetaUseCase useCase : pack.getUseCases()) {
      	if(!classTable.containsKey(useCase)) {
      		addUseCase(roseNode, pack, useCase);
      	}
      }
    }
    for (MetaClass clazz : model.getClasses()) {
      for(MetaProperty p : clazz.getProperties()) {
        if(propertyTable.containsKey(p)) continue;
        addProperty(roseNode, null, clazz, p);
      }
      for(MetaIndexedProperty p : clazz.getIndexedProperties()) {
        if(propertyTable.containsKey(p)) continue;
        addIndexedProperty(roseNode, null, clazz, p);
      }
      processElementsOfNewNestedClasses(roseNode, clazz);
    }
    for(MetaPackage pack : model.getPackages()) {
      for(MetaClass clazz : pack.getClasses()) {
        for(MetaProperty p : clazz.getProperties()) {
          if(propertyTable.containsKey(p)) continue;
          addProperty(roseNode, pack, clazz, p);
        }
        for(MetaIndexedProperty p : clazz.getIndexedProperties()) {
          if(propertyTable.containsKey(p)) continue;
          addIndexedProperty(roseNode, pack, clazz, p);
        }
        processElementsOfNewNestedClasses(roseNode, clazz);
      }
    }
  }
  
  protected void processNewNestedClasses(RoseNode roseNode, MetaClass clazz) {
    for(MetaClass nestedClass : clazz.getNestedClasses()) {
      if(!classTable.containsKey(nestedClass)) {
        addNestedClass(roseNode, clazz, nestedClass);
      }
      processNewNestedClasses(roseNode, nestedClass);
    }
  }
  
  protected void processElementsOfNewNestedClasses(RoseNode roseNode, MetaClass clazz) {
    for(MetaClass nestedClass : clazz.getNestedClasses()) {
      for(MetaProperty p : nestedClass.getProperties()) {
        if(propertyTable.containsKey(p)) continue;
        addProperty(roseNode, null, nestedClass, p);
      }
      for(MetaIndexedProperty p : nestedClass.getIndexedProperties()) {
        if(propertyTable.containsKey(p)) continue;
        MetaPackage metaPackage = clazz.retrievePackage();
        if(metaPackage instanceof MetaModel) metaPackage = null;
        addIndexedProperty(roseNode, metaPackage, clazz, p);
      }
      processElementsOfNewNestedClasses(roseNode, nestedClass);
    }
  }
  
  public RoseNode addDatatype(RoseNode roseModel, MetaDatatype item) {
    assert(roseModel != null && item != null);
    RoseNode node = null;

    node = addRootPackage(roseModel);
    node = addRoseClass(node, item.getName(), item.getQualifiedName());
    item.setStereotype("datatype");
    updateRoseClass(item, node);
    return node;
  }
  
  public RoseNode addClass(RoseNode roseModel, MetaPackage metaPackage, MetaClass item) {
    assert(roseModel != null && item != null);
    RoseNode node = null;

    if(metaPackage == null) {
      node = addRootPackage(roseModel);
    }
    else {
      RoseLocatorVisitor locator = new RoseLocatorVisitor(item.getQualifiedName(), RoseStrings.CLASS);
      RoseWalker roseWalker = new RoseWalker(roseModel);
      roseWalker.traverse(locator);
      node = locator.getResult();
      if(node != null) return node;
      node = locator.getBestMatch();
      if(node == null) node = addPackage(roseModel, metaPackage);
    }
    
    node = addRoseClass(node, item.getName(), item.getQualifiedName());
    updateRoseClass(item, node);
    updateRoseInheritanceRelations(item, node);
    return node;
  }
  
  public RoseNode addUseCase(RoseNode roseModel, MetaPackage metaPackage, MetaUseCase item) {
    assert(roseModel != null && item != null);
    RoseNode node = null;

    if(metaPackage == null) {
      node = addRootUseCasePackage(roseModel);
    }
    else {
      RoseLocatorVisitor locator = new RoseLocatorVisitor(item.getQualifiedName(), "UseCase");
      RoseWalker roseWalker = new RoseWalker(roseModel);
      roseWalker.traverse(locator);
      node = locator.getResult();
      if(node != null) return node;
      node = locator.getBestMatch();
      if(node == null) node = addPackage(roseModel, metaPackage);
    }
    
    node = addRoseUseCase(node, item.getName(), item.getQualifiedName());
    updateRoseUseCase(item, node);
    updateRoseUseCaseInheritanceRelations(item, node);
    return node;
  }
  
  public RoseNode addNestedClass(RoseNode roseModel, MetaClass metaClass, MetaClass item) {
    assert(roseModel != null && metaClass != null && item != null);
    RoseNode node = null;

    RoseLocatorVisitor locator = new RoseLocatorVisitor(item.getQualifiedName(), RoseStrings.CLASS);
    RoseWalker roseWalker = new RoseWalker(roseModel);
    roseWalker.traverse(locator);
    node = locator.getResult();
    if(node != null) return node;
    node = locator.getBestMatch();
    if(node == null) {
      if(metaClass.getContext() != null && metaClass.getContext() instanceof MetaClass) node = addNestedClass(roseModel, (MetaClass)metaClass.getContext(), metaClass);
      if(metaClass.getContext() != null && metaClass.getContext() instanceof MetaPackage) node = addClass(roseModel, (MetaPackage)metaClass.getContext(), metaClass);
      node = addClass(roseModel, null, metaClass);
    }
    
    node = addRoseNestedClass(node, item.getName(), item.getQualifiedName());
    updateRoseClass(item, node);
    updateRoseInheritanceRelations(item, node);
    return node;
  }
  
  public RoseNode addProperty(RoseNode roseModel, MetaElement metaElement, MetaClass metaClass, MetaProperty metaProperty) {
    assert(roseModel != null && metaClass != null && metaProperty != null);
    RoseNode node = null;

    String searchString = metaClass.getQualifiedName()+"."+metaProperty.getName();
    RoseLocatorVisitor locator = new RoseLocatorVisitor(searchString, RoseStrings.CLASSATTRIBUTE);
    RoseWalker roseWalker = new RoseWalker(roseModel);
    roseWalker.traverse(locator);
    node = locator.getResult();
    if(node != null) return node;
    node = locator.getBestMatch();
    if(node == null && (metaElement == null || metaElement instanceof MetaPackage)) node = addClass(roseModel, (MetaPackage)metaElement, metaClass);
    if(node == null && metaElement != null && metaElement instanceof MetaClass) node = addNestedClass(roseModel, (MetaClass)metaElement, metaClass);
    
    node = addRoseAttribute(node, metaProperty.getName());
    updateRoseAttribute(metaProperty, node);
    return node;
  }
  
  public RoseNode addIndexedProperty(RoseNode roseModel, MetaPackage metaPackage, MetaClass metaClass, MetaIndexedProperty metaProperty) {
    assert(roseModel != null && metaClass != null && metaProperty != null);
    RoseNode node = addPackage(roseModel, metaPackage);

    node = addRoseAssociation(node, metaProperty);
    return node;
  }
  
  public void deleteElements(RoseNode roseNode) {
    for(ItemToDelete i : itemsToDelete) {
      i.delete();
    }
    itemsToDelete.clear();
  }

  protected void processNewInheritanceRelations(RoseNode roseModel) {
    for(Object[] listEntry : inheritanceTable) {
      MetaClassifier superClassifier = (MetaClassifier)listEntry[0];
      RoseNode roseClass = (RoseNode)listEntry[1];
      RoseNode superClassList = roseClass.findNodeWithKey(RoseStrings.SUPERCLASSES);
      if(superClassList == null) {
        superClassList = new RoseNode(RoseStrings.SUPERCLASSES, RoseStrings.INHR_LIST, RoseNode.LIST);
        roseClass.addNode(superClassList);
      }
      RoseNode inheritanceRelation = new RoseNode("", RoseStrings.INHERITANCE_RELATIONSHIP, RoseNode.OBJECT);
      superClassList.addNode(inheritanceRelation);
      inheritanceRelation.addNode(new RoseNode(RoseStrings.QUID, formatAttributeValue(getNewQuid()), RoseNode.STRING));
      inheritanceRelation.addNode(new RoseNode(RoseStrings.SUPPLIER, formatAttributeValue(superClassifier.getQualifiedName()), RoseNode.STRING));

      String quid = (String)quidTable.get(superClassifier.getQualifiedName());
      if(quid != null) inheritanceRelation.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    }
  }
  
  protected void processNewRealizeRelations(RoseNode roseModel) {
    for(Object[] listEntry : realizeTable) {
      MetaClass metaInterface = (MetaClass)listEntry[0];
      RoseNode roseClass = (RoseNode)listEntry[1];
      RoseNode realizeList = roseClass.findNodeWithKey(RoseStrings.REALIZED_INTERFACES);
      if(realizeList == null) {
        realizeList = new RoseNode(RoseStrings.REALIZED_INTERFACES, RoseStrings.REALIZE_REL_LIST, RoseNode.LIST);
        roseClass.addNode(realizeList);
      }
      RoseNode realizeRelation = new RoseNode("", RoseStrings.REALIZE_RELATIONSHIP, RoseNode.OBJECT);
      realizeList.addNode(realizeRelation);
      realizeRelation.addNode(new RoseNode(RoseStrings.QUID, formatAttributeValue(getNewQuid()), RoseNode.STRING));
      realizeRelation.addNode(new RoseNode(RoseStrings.SUPPLIER, formatAttributeValue(metaInterface.getQualifiedName()), RoseNode.STRING));

      String quid = (String)quidTable.get(metaInterface.getQualifiedName());
      if(quid != null) realizeRelation.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    }
  }
  
  protected void updateGeneralRoseElements(RoseNode roseNode, MetaElement element, MetaElement defaultMetaElement) {
    // update documentation
    setOrAddAttribute(roseNode, RoseStrings.DOCUMENTATION, element.getDescription());
    // update stereotype
    setOrAddAttribute(roseNode, RoseStrings.STEREOTYPE, element.getStereotype());
    // update visibility
    if(!(Util.getType(roseNode.getValue()).equals(RoseStrings.ASSOCIATION) || Util.getType(roseNode.getValue()).equals("UseCase"))) {
      setOrAddAttribute(roseNode, RoseStrings.EXPORTCONTROL, toRoseVisibility(element.getVisibility()));
    }
    
    // update tagged values
    RoseNode attributes = roseNode.findNodeWithKey(RoseStrings.ATTRIBUTES);
    for (TaggedValue tag : element.getTags()) {
      String tagName = tag.getTag();
      String tagValue = tag.getValue();
      RoseNode tv = findTaggedValue(roseNode, RoseStrings.ATTRIBUTES, tagName);
      TaggedValue defaultTag = defaultMetaElement.getTaggedValue(tagName); 
      if(defaultTag == null) {
        itemsToDelete.add(new ItemToDelete(tv, attributes));
        continue;
      }
      if(tag.getTag().equals(UmlConstants.PERSISTENCE_TAG) && element instanceof MetaClass) { // Rose has a dedicated persistence information on class level
        continue;
      }
      String defaultValue = defaultTag.getValue();
      Boolean isDefault = false;
      if(tagValue == null && defaultValue == null) {
        isDefault = true;
      } else if(tagValue != null && defaultValue != null && tagValue.equals(defaultValue)) {
        isDefault = true;
      } else if(tagValue == null && defaultValue.equals("")) {
        isDefault = true;
      } else if(defaultValue == null && tagValue.equals("")) {
        isDefault = true;
      }
      if(tv != null) {
        RoseNode value = tv.findNodeWithKey(RoseStrings.VALUE);
        if(isDefault && attributes != null) {
          itemsToDelete.add(new ItemToDelete(tv, attributes));
        } else {
          if(value.getRoseNodeType() == RoseNode.VALUE) value = (RoseNode)value.getNodes().get(0);
          value.setValue(this.formatAttributeValue(tagValue));
        }
      } else if (!isDefault){
        addTaggedValue(roseNode, createTaggedValue(tag));
      }
    }
    
    // now remove unused tags
    if(attributes != null && attributes.getValue().equals(RoseStrings.ATTRIBUTE_SET)) {
      List tags = attributes.getNodes();
      for (int i = 0; i < tags.size(); i++)
      {
        RoseNode tv = (RoseNode)tags.get(i);
        RoseNode tool = tv.findNodeWithKey(RoseStrings.TOOL);
        RoseNode name = tv.findNodeWithKey(RoseStrings.NAME);
        if(Util.getName(tool.getValue()).equals(RosePersistenceHandler.TaggedValueToolIdentifier)) {
          TaggedValue tag = defaultMetaElement.getTaggedValue(Util.getName(name.getValue()));
          if(tag == null || (tag.getTag().equals(UmlConstants.PERSISTENCE_TAG) && defaultMetaElement.equals(RoseStrings.CLASS))) {
            itemsToDelete.add(new ItemToDelete(tv, attributes));
          }
        }
      }
    }
  }
  
  public RoseNode addRosePackage(RoseNode node, String packageName) {
    node = addUnitReferenceList(node);
    node = addRoseCategory(node, packageName);
    return node;
  }
  
  public RoseNode addUnitReferenceList(RoseNode roseNode) {
    if(roseNode == null || !(roseNode.getKey().equals("") || roseNode.getKey().equals(RoseStrings.ROOT_CATEGORY) || roseNode.getKey().equals(RoseStrings.ROOT_USECASE_PACKAGE)) || !Util.getType(roseNode.getValue()).equals(RoseStrings.CLASS_CATEGORY)) return null;
    RoseNode node = roseNode.findNodeWithValue(RoseStrings.UNIT_REFERENCE_LIST);
    if(node != null && node.getKey().equals(RoseStrings.LOGICAL_MODELS)) return node;
    node = new RoseNode(RoseStrings.LOGICAL_MODELS, RoseStrings.UNIT_REFERENCE_LIST, RoseNode.LIST);
    roseNode.addNode(node);
    return node;
  }
  
  public RoseNode addRoseCategory(RoseNode roseNode, String name) {
    if(roseNode == null || !roseNode.getKey().equals(RoseStrings.LOGICAL_MODELS) || !roseNode.getValue().equals(RoseStrings.UNIT_REFERENCE_LIST)) return null;
    RoseNode node = new RoseNode("", RoseStrings.CLASS_CATEGORY + " " + formatAttributeValue(name), RoseNode.OBJECT);
    roseNode.addNode(node);
    String quid = formatAttributeValue(getNewQuid());
    node.addNode(new RoseNode(RoseStrings.QUID, quid, RoseNode.STRING));
    quidTable.put(name, quid);
    return node;
  }
  
  public RoseNode addRoseRootCategory(RoseNode roseNode) {
    if(roseNode == null || !roseNode.getKey().equals("") || !Util.getType(roseNode.getValue()).equals(RoseStrings.DESIGN)) return null;
    RoseNode node = new RoseNode(RoseStrings.ROOT_CATEGORY, RoseStrings.CLASS_CATEGORY + " " + formatAttributeValue(RoseStrings.LOGICAL_VIEW), RoseNode.OBJECT);
    roseNode.addNode(node);
    String quid = formatAttributeValue(getNewQuid());
    node.addNode(new RoseNode(RoseStrings.QUID, quid, RoseNode.STRING));
    quidTable.put(RoseStrings.LOGICAL_VIEW, quid);
    return node;
  }
  
  public RoseNode addRoseRootUseCaseCategory(RoseNode roseNode) {
    if(roseNode == null || !roseNode.getKey().equals("") || !Util.getType(roseNode.getValue()).equals(RoseStrings.DESIGN)) return null;
    RoseNode node = new RoseNode(RoseStrings.ROOT_USECASE_PACKAGE, RoseStrings.CLASS_CATEGORY + " " + formatAttributeValue(RoseStrings.USE_CASE_VIEW), RoseNode.OBJECT);
    roseNode.addNode(node);
    String quid = formatAttributeValue(getNewQuid());
    node.addNode(new RoseNode(RoseStrings.QUID, quid, RoseNode.STRING));
    quidTable.put(RoseStrings.USE_CASE_VIEW, quid);
    return node;
  }
  
  public RoseNode addRoseClass(RoseNode node, String name, String qualifiedName) {
    node = addUnitReferenceList(node);
    RoseNode roseClass = new RoseNode("", RoseStrings.CLASS+" "+formatAttributeValue(name), RoseNode.OBJECT);
    node.addNode(roseClass);
    String quid = formatAttributeValue(getNewQuid());
    roseClass.addNode(new RoseNode(RoseStrings.QUID, quid, RoseNode.STRING));
    quidTable.put(qualifiedName, quid);
    return roseClass;
  }
  
  public RoseNode addRoseNestedClass(RoseNode node, String name, String qualifiedName) {
    RoseNode nestedList = node.findNodeWithKey(RoseStrings.NESTEDCLASSES);
    if(nestedList == null) {
      nestedList = new RoseNode(RoseStrings.NESTEDCLASSES, RoseStrings.NESTEDCLASSES, RoseNode.LIST);
      node.addNode(nestedList);
    }
    node = nestedList;
    RoseNode roseClass = new RoseNode("", RoseStrings.CLASS+" "+formatAttributeValue(name), RoseNode.OBJECT);
    node.addNode(roseClass);
    String quid = formatAttributeValue(getNewQuid());
    roseClass.addNode(new RoseNode(RoseStrings.QUID, quid, RoseNode.STRING));
    quidTable.put(qualifiedName, quid);
    return roseClass;
  }
  
  public RoseNode addRoseAttribute(RoseNode node, String name) {
    RoseNode attrList = node.findNodeWithKey(RoseStrings.CLASS_ATTRIBUTES);
    if(attrList == null || !attrList.getValue().equals(RoseStrings.CLASS_ATTR_LIST)) {
      attrList = new RoseNode(RoseStrings.CLASS_ATTRIBUTES, RoseStrings.CLASS_ATTR_LIST, RoseNode.LIST);
      node.addNode(attrList);
    }
    RoseNode roseAttribute = new RoseNode("", RoseStrings.CLASSATTRIBUTE+" "+formatAttributeValue(name), RoseNode.OBJECT);
    attrList.addNode(roseAttribute);
    roseAttribute.addNode(new RoseNode(RoseStrings.QUID, formatAttributeValue(getNewQuid()), RoseNode.STRING));
    return roseAttribute;
  }
  
  public RoseNode addRoseAssociation(RoseNode node, MetaIndexedProperty property) {
    node = addUnitReferenceList(node);

    RoseNode roseAssociation = new RoseNode("", RoseStrings.ASSOCIATION+" "+formatAttributeValue(property.getName()), RoseNode.OBJECT);
    node.addNode(roseAssociation);

    MetaElement defaultMetaElement = new MetaIndexedProperty();
    itemFactory.initialize(defaultMetaElement, MetaIndexedProperty.class);
    updateGeneralRoseElements(roseAssociation, property, defaultMetaElement);
    
    roseAssociation.addNode(new RoseNode(RoseStrings.QUID, formatAttributeValue(getNewQuid()), RoseNode.STRING));
    RoseNode roles = new RoseNode(RoseStrings.ROLES, RoseStrings.ROLE_LIST, RoseNode.LIST);
    roseAssociation.addNode(roles);
    RoseNode role1 = new RoseNode("", RoseStrings.ROLE+" "+formatAttributeValue("$UNNAMED$0"), RoseNode.OBJECT);
    RoseNode role2 = new RoseNode("", RoseStrings.ROLE+" "+formatAttributeValue("$UNNAMED$2"), RoseNode.OBJECT);
    
    role1.addNode(new RoseNode(RoseStrings.QUID, formatAttributeValue(getNewQuid()), RoseNode.STRING));
    role1.addNode(new RoseNode(RoseStrings.SUPPLIER, formatAttributeValue(((MetaClass)property.getContext()).getQualifiedName()), RoseNode.STRING));

    String quid = (String)quidTable.get(((MetaClass)property.getContext()).getQualifiedName());
    if(quid != null) role1.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    role1.addNode(new RoseNode(RoseStrings.EXPORTCONTROL, formatAttributeValue(toRoseVisibility(property.getVisibility())), RoseNode.STRING));
    roles.addNode(role1);
    
    role2.addNode(new RoseNode(RoseStrings.QUID, formatAttributeValue(getNewQuid()), RoseNode.STRING));
    role2.addNode(new RoseNode(RoseStrings.SUPPLIER, formatAttributeValue(property.getTypeName()), RoseNode.STRING));
    role2.addNode(new RoseNode(RoseStrings.CLIENT_CARDINALITY, "(value cardinality \""+
        Multiplicity.stringValue(property.getMultiplicity().getLowerBound())+".."+
        Multiplicity.stringValue(property.getMultiplicity().getUpperBound())+"\")", RoseNode.STRING));
    quid = (String)quidTable.get(property.getTypeName());
    if(quid != null) role2.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
    role2.addNode(new RoseNode(RoseStrings.IS_NAVIGABLE, "TRUE", RoseNode.STRING));
    roles.addNode(role2);
    
    if(property.isAggregation() || property.isComposition()) {
      if(property.isComposition()) {
        role2.addNode(new RoseNode(RoseStrings.CONTAINMENT, formatAttributeValue(RoseStrings.BY_VALUE), RoseNode.STRING));
      } else {
        role2.addNode(new RoseNode(RoseStrings.CONTAINMENT, formatAttributeValue("By Reference"), RoseNode.STRING));
      }
      role1.addNode(new RoseNode(RoseStrings.IS_AGGREGATE, "TRUE", RoseNode.STRING));
    }
    
    return roseAssociation;
  }
  
  public RoseNode addRoseUseCase(RoseNode node, String name, String qualifiedName) {
    node = addUnitReferenceList(node);
    RoseNode roseUseCase = new RoseNode("", "UseCase "+formatAttributeValue(name), RoseNode.OBJECT);
    node.addNode(roseUseCase);
    String quid = formatAttributeValue(getNewQuid());
    roseUseCase.addNode(new RoseNode(RoseStrings.QUID, quid, RoseNode.STRING));
    quidTable.put(qualifiedName, quid);
    return roseUseCase;
  }
  
  public RoseNode createTaggedValue(TaggedValue tag) {
    RoseNode newTv = new RoseNode("", RoseStrings.ATTRIBUTE, RoseNode.OBJECT);
    RoseNode tool = new RoseNode(RoseStrings.TOOL, formatAttributeValue(RosePersistenceHandler.TaggedValueToolIdentifier), RoseNode.STRING);
    RoseNode name = new RoseNode(RoseStrings.NAME, formatAttributeValue(tag.getTag()), RoseNode.STRING);
    RoseNode value = new RoseNode(RoseStrings.VALUE, RoseStrings.TEXT, RoseNode.VALUE);
    value.addNode(new RoseNode(RoseStrings.VALUE, formatAttributeValue(tag.getValue()), RoseNode.STRING));
    newTv.addNode(tool);
    newTv.addNode(name);
    newTv.addNode(value);
    return newTv;
  }

  /**
   * Creates a container where default tagged values are added to.
   * @param containerName of the container (e.g. RoseStrings.DEFAULT_CATEGORY).
   * @return the container for default tagged values.
   */
  public RoseNode createDefaultTaggedValueAttribute(String containerName) {
    RoseNode attr = new RoseNode("", RoseStrings.ATTRIBUTE, RoseNode.OBJECT);
    RoseNode tool = new RoseNode(RoseStrings.TOOL, formatAttributeValue(RosePersistenceHandler.TaggedValueToolIdentifier), RoseNode.STRING);
    RoseNode name = new RoseNode(RoseStrings.NAME, formatAttributeValue(containerName), RoseNode.STRING);
    RoseNode value = new RoseNode(RoseStrings.VALUE, RoseStrings.ATTRIBUTE_SET, RoseNode.LIST);
    attr.addNode(tool);
    attr.addNode(name);
    attr.addNode(value);
    return attr;
  }

  /**
   * Finds a tagged value within a given roseNode.
   * @param roseNode where an attribute with a tagged value list is contained.
   * @param roseNode key where the tagged values are contained (e.g. RoseStrings.VALUE or RoseStrings.ATTRIBUTES).
   * @param taggedValueName of the searched tagged value.
   * @return tagged value or null if not found.
   */
  public RoseNode findTaggedValue(RoseNode roseNode, String containerKey, String taggedValueName) {
    RoseNode attrSet = roseNode.findNodeWithKey(containerKey);
    if(attrSet != null && attrSet.getValue().equals(RoseStrings.ATTRIBUTE_SET)){
      List attributes = attrSet.getNodes();
      for (int i = 0; i < attributes.size(); i++)
      {
        RoseNode attr = (RoseNode)attributes.get(i);
        if(attr.getValue().equals(RoseStrings.ATTRIBUTE)) {
          RoseNode tool = attr.findNodeWithKey(RoseStrings.TOOL);
          RoseNode name = attr.findNodeWithKey(RoseStrings.NAME);
          if(tool != null
              && Util.getName(tool.getValue()).equals(RosePersistenceHandler.TaggedValueToolIdentifier)
              && name != null
              && Util.getName(name.getValue()).equals(taggedValueName)) {
            return attr;
          }
        }
      }
    }
    return null;
  }
  
  /**
   * Adds a tagged value to a rose node. If no tagged value is assigned to the rose node then the 
   * attribute set holding the tagged values will be created.
   * @param roseNode to add a tagged value to.
   * @param taggedValue to be added.
   */
  public void addTaggedValue(RoseNode roseNode, RoseNode taggedValue) {
    RoseNode attrSet = roseNode.findNodeWithKey(RoseStrings.ATTRIBUTES);
    if(attrSet == null || !attrSet.getValue().equals(RoseStrings.ATTRIBUTE_SET)){
      attrSet = new RoseNode(RoseStrings.ATTRIBUTES, RoseStrings.ATTRIBUTE_SET, RoseNode.LIST);
      roseNode.addNode(attrSet);
    }
    attrSet.addNode(taggedValue);
  }

  /**
   * Adds a standard attribute to a rose node. If the attribute already exists then only the value is assigned.
   * @param roseNode to add an attribute to.
   * @param key for the attribute to be added.
   * @param value for the attribute to be added.
   * @return the attribute created or set.
   */
  public RoseNode setOrAddAttribute(RoseNode roseNode, String key, String value){
    RoseNode attribute = null;
    if(value != null) {
      attribute = roseNode.findNodeWithKey(key);
      if(attribute != null) {
        attribute.setValue(formatAttributeValue(value));
      }
      else if(!value.equals("")) {
        attribute = new RoseNode(key, formatAttributeValue(value), RoseNode.STRING);
        roseNode.addNode(attribute);
      }
    }
    return attribute;
  }
  
  /**
   * Adds a standard attribute to a rose node. If the attribute already exists then only the value is assigned.
   * @param roseNode to add an attribute to.
   * @param key for the attribute to be added.
   * @param value for the attribute to be added.
   * @return the attribute created or set.
   */
  public RoseNode setOrAddAttribute(RoseNode roseNode, String key, Boolean value){
    RoseNode attribute = null;
    if(value != null) {
      attribute = roseNode.findNodeWithKey(key);
      if(attribute != null) {
        attribute.setValue(value.toString().toUpperCase());
      }
      else {
        attribute = new RoseNode(key, value.toString().toUpperCase(), RoseNode.STRING);
        roseNode.addNode(attribute);
      }
    }
    return attribute;
  }

  protected MetaClassifier findClassifier(RoseNode roseNode) {
    assert(roseNode != null);
    String qualifiedName = getQualifiedName(roseNode);
    return findClassifier(qualifiedName);
  }
  
  protected MetaClassifier findClassifier(String qualifiedName) {
    assert(qualifiedName != null);
    MetaClassifier temp = findDatatype(qualifiedName);
    if(temp == null) temp = findClass(qualifiedName);
    if(temp == null) temp = findUseCase(qualifiedName);
    return temp;
  }
  
  /**
   * Find a class within the MetaModel with the same qualified name as the class denoted with roseNode
   * @param roseNode Rose class to be searched in MetaModel
   * @return MetaClass if found, otherwise null.
   */
  protected MetaClass findClass(RoseNode roseNode) {
    assert(roseNode != null);
    String qualifiedName = getQualifiedName(roseNode);
    return findClass(qualifiedName);
  }
  
  /**
   * Find a class within the MetaModel with passed qualified name.
   * @param qualifiedName of the MetaClass to be looked up.
   * @return MetaClass if found, otherwise null.
   */
  protected MetaClass findClass(String qualifiedName) {
    assert(qualifiedName != null);
    qualifiedName = convertQualifiedName(qualifiedName);
    for(MetaClass clazz : model.getClasses()) {
      if(clazz.getName().equals(qualifiedName)) {
        return clazz;
      }
      MetaClass nClass = findNestedClass(qualifiedName, clazz);
      if(nClass != null) return nClass;
    }
    for(MetaPackage pack : model.getPackages()) {
      for(MetaClass clazz : pack.getClasses()) {
        if(clazz.getQualifiedName().equals(qualifiedName)) {
          return clazz;
        }
        MetaClass nClass = findNestedClass(qualifiedName, clazz);
        if(nClass != null) return nClass;
      }
    }
    return null;
  }
  
  protected MetaClass findNestedClass(String qualifiedName, MetaClass clazz) {
    assert(qualifiedName != null && clazz != null);
    qualifiedName = convertQualifiedName(qualifiedName);
    for(MetaClass nestedClass : clazz.getNestedClasses()) {
      if(nestedClass.getQualifiedName().equals(qualifiedName)) {
        return nestedClass;
      }
      MetaClass nClass = findNestedClass(qualifiedName, nestedClass);
      if(nClass != null) return nClass;
    }
    return null;
  }
  
  protected MetaDatatype findDatatype(RoseNode roseNode) {
    assert(roseNode != null);
    String qualifiedName = getQualifiedName(roseNode);
    return findDatatype(qualifiedName);
  }
  
  protected MetaDatatype findDatatype(String qualifiedName) {
    assert(qualifiedName != null);
    qualifiedName = convertQualifiedName(qualifiedName);
    for(MetaDatatype type : model.getDatatypes()) {
      if(type.getName().equals(qualifiedName)) {
        return type;
      }
    }
    return null;
  }
  
  /**
   * Find a use case within the MetaModel with passed qualified name.
   * @param qualifiedName of the MetaUseCase to be looked up.
   * @return MetaUseCase if found, otherwise null.
   */
  protected MetaUseCase findUseCase(String qualifiedName) {
    assert(qualifiedName != null);
    qualifiedName = convertQualifiedName(qualifiedName);
    for(MetaUseCase useCase : model.getUseCases()) {
      if(useCase.getName().equals(qualifiedName)) {
        return useCase;
      }
    }
    for(MetaPackage pack : model.getPackages()) {
      for(MetaUseCase useCase : pack.getUseCases()) {
        if(useCase.getQualifiedName().equals(qualifiedName)) {
          return useCase;
        }
      }
    }
    return null;
  }
  
  protected String convertQualifiedName(String qualifiedName) {
    assert(qualifiedName != null);
    // replace all :: by .
    int idx = qualifiedName.indexOf("::", 0);
    while(idx >= 0) {
      qualifiedName = qualifiedName.substring(0, idx) + "." + qualifiedName.substring(idx+2);
      idx = qualifiedName.indexOf("::", 0);
    }
    if(qualifiedName.startsWith(RoseStrings.LOGICAL_VIEW)) qualifiedName = qualifiedName.substring(RoseStrings.LOGICAL_VIEW.length()+1);
    if(qualifiedName.startsWith(RoseStrings.USE_CASE_VIEW)) qualifiedName = qualifiedName.substring(RoseStrings.USE_CASE_VIEW.length()+1);
    return qualifiedName;
  }
  
  public String unqualifyName(String name) {
    assert(name != null);
    int idx = name.lastIndexOf(".");
    if(idx >=0) return name.substring(idx+1);
    idx = name.lastIndexOf("::");
    if(idx >=0) return name.substring(idx+2);
    return name;
  }
  
  public String formatAttributeValue(String value) {
    if(value == null) return "\"\"";
    return "\""+value+"\"";
  }
  
  public void setValueOfTaggedValue(RoseNode v, String value) {
  	if(v.getNodes() != null && v.getNodes().size() > 0) {
    	v.setValue(RoseStrings.TEXT);
    	v = (RoseNode)v.getNodes().get(0);
    }
    v.setValue(formatAttributeValue(value));
  }
  
  /**
   * Returns the qualified name of the passed roseNode.
   * @param roseNode to determine the qualified name of.
   * @return the qualified name of the roseNode.
   */
  protected String getQualifiedName(RoseNode roseNode) {
    if(roseNode == null) return null;
    String temp = Util.getName(roseNode.getValue());
    while(roseNode.getParent() != null) {
      roseNode = roseNode.getParent();
      String currentValue = roseNode.getValue(); 
      if(Util.getType(currentValue).equals(RoseStrings.CLASS_CATEGORY)) {
        if(Util.getName(currentValue).equals(RoseStrings.LOGICAL_VIEW) || Util.getName(currentValue).equals(RoseStrings.USE_CASE_VIEW)) break;
        temp = Util.getName(currentValue) + "." + temp;
      } else if(Util.getType(currentValue).equals(RoseStrings.CLASS)) {
        temp = Util.getName(currentValue) + "." + temp;
      }
    }
    return temp;
  }

  /**
   * Returns the owning class of a class attribute
   * @param roseNode being the class attribute
   * @return roseNode beeing the owning class or null if not found.
   */
  protected RoseNode getClassOfAttribute(RoseNode roseNode) {
    if(!Util.getType(roseNode.getValue()).equals(RoseStrings.CLASSATTRIBUTE) && !Util.getType(roseNode.getValue()).equals(RoseStrings.ASSOCIATION)) return null;
    String temp = Util.getName(roseNode.getValue());
    while(roseNode.getParent() != null) {
      roseNode = roseNode.getParent();
      String currentValue = roseNode.getValue(); 
      if(Util.getType(currentValue).equals(RoseStrings.CLASS)) {
        return roseNode;
      }
    }
    return null;
  }
  
  protected String getNewQuid(){
    Double d = java.lang.Math.random();
    String r = String.format("%1.8f", d);
    r = r.substring(r.length()-8);
    java.util.Formatter f;
    String s = String.format("%06X%06X", java.util.Calendar.getInstance().getTimeInMillis(), new Long(r));
    if(s.length() > 12) return s.substring(s.length()-12);
    return s;
  }
  
  protected String toRoseVisibility(VisibilityCode visibility) {
    switch(visibility.getCode()) {
    case VisibilityCode.PUBLIC_ID: return "Public";
    case VisibilityCode.PROTECTED_ID: return "Protected";
    case VisibilityCode.PRIVATE_ID: return "Private";
    case VisibilityCode.PACKAGE_ID: return "Implementation";
    }
    return "implementation";
  }

  protected void putQuid(RoseNode roseNode, String qualifiedName) {
    RoseNode quid = roseNode.findNodeWithKey(RoseStrings.QUID);
    if(quid != null && quid.getValue() != null) {
      quidTable.put(qualifiedName, quid.getValue());
    }
  }

  protected void updateQuiduLater(RoseNode roseNode, String qualifiedTypeName) {
    Object[] listEntry = { qualifiedTypeName, roseNode };
  	updateQuiduLaterTable.add(listEntry);
  }

  /**
   * The RoseMdaVisitor visits the model sequentially. Thereof types of propertis might not yet been visited at the time the property is visited.
   * Properties can only be updated partially at that time. The remainder (link to the type - set the correct quidu) is done in here. Please
   * call this method after visiting of the rose model is complete.
   */
  public void deferredQuiduUpdate() {
    for(Object[] listEntry : updateQuiduLaterTable) {
    	String qualifiedTypeName = (String)listEntry[0];
    	RoseNode roseNode = (RoseNode)listEntry[1];
      String quid = (String)quidTable.get(qualifiedTypeName);
      if(quid != null) {
        RoseNode quidu = roseNode.findNodeWithKey(RoseStrings.QUIDU);
        if(quidu != null) roseNode.deleteNode(quidu);
      	roseNode.addNode(new RoseNode(RoseStrings.QUIDU, quid, RoseNode.STRING));
      }
    }
  }
  
  protected class ItemToDelete {
    public RoseNode parent;
    public RoseNode itemToDelete;
    
    public ItemToDelete(RoseNode itemToDelete, RoseNode parent) {
      this.parent = parent;
      this.itemToDelete = itemToDelete;
    }
    
    public void delete() {
      if(parent != null && itemToDelete != null) parent.deleteNode(itemToDelete);
    }
  }
  
}
