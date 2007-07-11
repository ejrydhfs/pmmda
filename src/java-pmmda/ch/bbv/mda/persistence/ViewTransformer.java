package ch.bbv.mda.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.MetaView;
import ch.bbv.mda.TaggedValue;
import ch.bbv.mda.VisibilityCode;
import ch.bbv.mda.ui.ViewConstants;
import ch.persi.xml.XmlNode;

public class ViewTransformer extends ExternalTransformer implements
    ViewConstants {

  private static final String DOT = ".";

  class ViewElement {
    public MetaProperty metaProperty;
    public TaggedValue taggedValue;
    public MetaPackage pack;
  }

  private MetaModel metaModel;
  private List<ViewElement> collectedViews;
  private List<ViewElement> collectedProperties;

  public ViewTransformer(){
    collectedViews = new ArrayList<ViewElement>();
    collectedProperties = new ArrayList<ViewElement>();
  }
  
  
  /**
   * Add a View Element 
   * @param property
   * @param metaPackage
   * @param taggedValue
   */
  public void addCollectedView(MetaProperty property, MetaPackage metaPackage, TaggedValue taggedValue) {
    ViewElement element = new ViewElement();
    element.metaProperty = property;
    element.pack = metaPackage;
    element.taggedValue = taggedValue;
    collectedViews.add(element);
  }  
  
  /**
   * Add a property View Element
   * @param property
   * @param metaPackage
   * @param taggedValue
   */
  public void addCollectedProperty(MetaProperty property, MetaPackage metaPackage, TaggedValue taggedValue){
    ViewElement element = new ViewElement();
    element.metaProperty = property;
    element.pack = metaPackage;
    element.taggedValue = taggedValue;
    collectedProperties.add(element);
  }

  public List<ViewElement> getCollectedViews() {
    return collectedViews;
  }

  public List<ViewElement> getCollectedProperties() {
    return collectedProperties;
  }

  public void setMetaModel(MetaModel metaModel) {
    this.metaModel = metaModel;
  }

  public MetaModel getMetaModel() {
    return this.metaModel;
  }

  /**
   * Returns the unqualified name. the substring after the last dot.
   * 
   * @param qualifiedName
   *          the qualified name
   * @return the unqualified name
   */
  public String getUnqualifiedName(String qualifiedName) {
    return qualifiedName.substring(qualifiedName.lastIndexOf(DOT) + 1);
  }

  /**
   * Returns the name space of the qualified name. this is the part before the
   * last dot.
   * 
   * @param qualifiedName
   * @return the name space
   */
  public String getNamespace(String qualifiedName) {
    int idx = qualifiedName.lastIndexOf(DOT);
    return idx > 0?qualifiedName.substring(0, idx):"";
  }

  /**
   * Information about views are collected during processing of the model. The
   * information collected is within (collectedViews) This method processes the
   * collected information about views and stores that information in MetaViews.
   * 
   * @param metaModel
   *          to assign the MetaViews to.
   */
  public void createViewFromXML(String xmlString) {
    if(xmlString == null || xmlString.trim().length() == 0){
      return;
    }
    XmlNode xml = new XmlNode();
    xml.parse(xmlString);
    for (Iterator<XmlNode> iter = xml.getAll(VIEW_NODE); iter.hasNext();) {
      XmlNode view = iter.next();
      String name = view.getAttribute(NAME);
      MetaView metaView = metaModel.findView(name);
      if (metaView == null) {
        metaView = new MetaView();
        metaView.setName(getUnqualifiedName(name));
        obtainPackage(metaModel, name).addView(metaView);
      }
      metaView.setDescription(view.getAttribute(DESCRIPTION));
      metaView.setVisibility(VisibilityCode.getCode(Integer.parseInt(view
          .getAttribute(VISIBILITY))));
    }

  }

  MetaPackage obtainPackage(MetaModel metaModel, String qualifiedViewName) {

    MetaPackage metaPackage = null;
    if (getNamespace(qualifiedViewName).length() > 0) {
      metaPackage = metaModel.findPackage(getNamespace(qualifiedViewName));
    }

    if (metaPackage == null) {
      metaPackage = metaModel;
    }

    return metaPackage;
  }

  protected void extractViews(MetaModel metaModel) {
    // first create all views
    if (collectedViews != null) {
      for (ViewElement v : collectedViews) {
        createViewFromXML(v.taggedValue.getValue());
      }
    }

    // second build view hierarchy
    if (collectedViews != null) {
      for (ViewElement v : collectedViews) {
        XmlNode xml = new XmlNode();
        xml.parse(v.taggedValue.getValue());

        for (Iterator<XmlNode> iter = xml.getAll(VIEW_NODE); iter.hasNext();) {
          XmlNode view = iter.next();
          String name = view.getAttribute(NAME);
          MetaView metaView = metaModel.findView(name);

          for (Iterator<XmlNode> refIter = view.getAll(REFERENCE); refIter
              .hasNext();) {
            XmlNode reference = refIter.next();
            metaView.addView(metaModel.findView(reference.getValue()));
          }
        }
      }
    }

    // third assign properties to the views
    if (collectedProperties != null) {
      for (ViewElement v : collectedProperties) {
        XmlNode xml = new XmlNode();
        xml.parse(v.taggedValue.getValue());

        for (Iterator<XmlNode> iter = xml.getAll(VIEW_NODE); iter.hasNext();) {
          XmlNode view = iter.next();
          String name = view.getAttribute(NAME);
          MetaView metaView = metaModel.findView(name);
          // backward compatibility
          if (metaView == null) {
            metaView = new MetaView();
            metaView.setName(name.substring(name.lastIndexOf(DOT) + 1));
            obtainPackage(metaModel, name).addView(metaView);
            String value = view.getAttribute(DESCRIPTION);
            if (value != null) {
              metaView.setDescription(value);
            }
            value = view.getAttribute(VISIBILITY);
            if (value != null) {
              metaView.setVisibility(VisibilityCode.getCode(Integer
                  .parseInt(value)));
            }
          }
          metaView.appendField(v.metaProperty);
        }
      }
    }
  }

}
