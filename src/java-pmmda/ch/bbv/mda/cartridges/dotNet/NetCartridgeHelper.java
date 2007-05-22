/* 
 * copyright 2005 http://www.bbv.ch
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 */

package ch.bbv.mda.cartridges.dotNet;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.mda.*;
import ch.bbv.mda.cartridges.BoConstants;
import ch.bbv.mda.generators.CartridgeImpl;
import ch.bbv.mda.operators.ModelOperators;
import ch.bbv.mda.pim.CodeType;
import ch.bbv.mda.pim.CodeValue;
import ch.bbv.utilities.TextUtilities;

/**
 * Helper class for .NET Cartridges and velocity templates.
 * 
 * @author Mikkala Pedersen
 */
public class NetCartridgeHelper {
  /**
   * Model which is currently processed.
   */
  private MetaModel model;
  /**
   * Cartridge currently processing the model.
   */
  private CartridgeImpl cartridge;
  
  private static final String NEW_LINE = "\r\n";  
  
  /**
   * List of all classes.
   */
  private List<MetaClass> classes;
  
  private static Log log = LogFactory.getLog(DogNetCartridge.class);
  
  public List<MetaClass> getAllImplementedDoClasses(MetaClass clazz) {
    List<MetaClass> classes = new ArrayList<MetaClass>();
    do {
      if (isClassDataObject(clazz)) {
        classes.add(clazz);
      }
      else {
        break;
      }
      List<MetaClass> ancestors = clazz.retrieveAncestors(); 
      if (ancestors.size() > 0) {
        clazz = ancestors.get(0);
      }
      else {
        break;
      }
    } while (true);
    return classes;
  }
  
  public String getControlClass(MetaProperty property) {
    MetaClassifier propertyType = model.findClassifier(property.getTypeName());
    if (propertyType != null && propertyType.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
      return "bbv.Common.Windows.Forms.ComboBox";
    }
    String dotNetType = this.convertJavaTypeToDotnet(property.retrieveTypeClassName());
    
    if (dotNetType.equals("byte") ||
        dotNetType.equals("short") ||
        dotNetType.equals("int") ||
        dotNetType.equals("long") || 
        dotNetType.equals("float") ||
        dotNetType.equals("double") ||
        dotNetType.equals("decimal")) {
      return "bbv.Common.Windows.Forms.NumericUpDown";
    }
    if (dotNetType.equals("bool")) {
      return "bbv.Common.Windows.Forms.CheckBox";
    }
    if (dotNetType.equals("DateTime")) {
      return "System.Windows.Forms.DateTimePicker";
    }
    if (propertyType instanceof MetaDatatype) {
      String typeName = cartridge.translateDatatype((MetaDatatype)propertyType, "UI", "DotNetWinForms", false);
      if (typeName != null && !typeName.equals("")) return typeName;
    }
    return "bbv.Common.Windows.Forms.TextBox";
  }
  
  public String getControlFieldName(MetaProperty property) {
    String controlClass = getControlClass(property);
    StringBuilder controlFieldName = new StringBuilder();
    if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      controlFieldName.append("comboBox");
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.NumericUpDown")) {
      controlFieldName.append("numeric");
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.TextBox")) {
      controlFieldName.append("text");
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.CheckBox")) {
      controlFieldName.append("checkBox");
    }
    else if (controlClass.equals("System.Windows.Forms.DateTimePicker")) {
      controlFieldName.append("dateTime");
    }
    else if (controlClass.equals("System.Windows.Forms.PictureBox")) {
    	controlFieldName.append("pictureBox");
    }
    else {
      controlFieldName.append("unknown");
    }
    controlFieldName.append(property.nameFirstUpper());
    return controlFieldName.toString();
  }
  
  public boolean hasLabel(MetaProperty property) {
    String controlClass = getControlClass(property);
    if (controlClass.equals("bbv.Common.Windows.Forms.CheckBox")) {
      return true;
    }
    return true;
  }
  
  public String getControlInitialization(MetaProperty property, String tab, int posY) throws Exception {
    String controlClass = getControlClass(property);
    String fieldName = getControlFieldName(property);
    StringBuilder initialization = new StringBuilder();
    
    initialization.append(tab).append("// ").append(NEW_LINE);
    initialization.append(tab).append("// ").append(fieldName).append(NEW_LINE);
    initialization.append(tab).append("// ").append(NEW_LINE);
    
    initialization.append(tab).append("this.").append(fieldName).append(".Dock = System.Windows.Forms.DockStyle.Top;").append(NEW_LINE);
    if (!property.canBeWritten()) {
      initialization.append(tab).append("this.").append(fieldName).append(".Enabled = false;").append(NEW_LINE);
    }
    initialization.append(tab).append("this.").append(fieldName).append(".Location = new System.Drawing.Point(0, ").append(posY).append(");").append(NEW_LINE);
    initialization.append(tab).append("this.").append(fieldName).append(".Name = \"").append(fieldName).append("\";").append(NEW_LINE);
    initialization.append(tab).append("this.").append(fieldName).append(".Size = new System.Drawing.Size(344, 20);").append(NEW_LINE);
    initialization.append(tab).append("this.").append(fieldName).append(".TabIndex = 0;").append(NEW_LINE);
    
    if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      initialization.append(tab).append("this.").append(fieldName).append(".AccessType = bbv.Common.Windows.Forms.AccessTypes.InOut;").append(NEW_LINE);
      // new ref code model
/*      CodeType referenceCode = model.getCodeTypeMgr().retrieve(model.findClass(property.getTypeName()));
      initialization.append(tab).append("this.").append(fieldName).append(".Items.AddRange(new object[] {");
      List<CodeValue> values = referenceCode.getValues();
      for (int i = 0; i < values.size(); i++) {
        initialization.append(NEW_LINE);
        initialization.append(tab).append("                               ").append(referenceCode.getQualifiedName()).append(".GetCode(").append(i).append(").ShortDescription");
        if (i < values.size() - 1) {
          initialization.append(",");
        }
      }
      initialization.append("});").append(NEW_LINE);*/      
      initialization.append(tab).append("this.").append(fieldName).append(".DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".SelectedIndexChanged += new System.EventHandler(this.").append(fieldName).append("_SelectedIndexChanged);").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.NumericUpDown")) {
      initialization.append(tab).append("this.").append(fieldName).append(".AccessType = bbv.Common.Windows.Forms.AccessTypes.InOut;").append(NEW_LINE);
      // TODO: DecimalPlaces, min, max, value, ValueChanged
      // TODO: float, double with decimal places
      // TODO: increment defined by attribute
      initialization.append(tab).append("this.").append(fieldName).append(".DecimalPlaces = 0;").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".Minimum = long.MinValue;").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".Maximum = long.MaxValue;").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".Value = 0;").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".ValueChanged += new System.EventHandler(this.").append(fieldName).append("_ValueChanged);").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.TextBox")) {
      initialization.append(tab).append("this.").append(fieldName).append(".AccessType = bbv.Common.Windows.Forms.AccessTypes.InOut;").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".Text = \"\";").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".Leave += new System.EventHandler(this.").append(fieldName).append("_Leave);").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.").append(fieldName).append("_KeyPress);").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".TextChanged += new System.EventHandler(this.").append(fieldName).append("_TextChanged);").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.CheckBox")) {
      initialization.append(tab).append("this.").append(fieldName).append(".AccessType = bbv.Common.Windows.Forms.AccessTypes.InOut;").append(NEW_LINE);
      //initialization.append(tab).append("this.").append(fieldName).append(".Text = \"").append(property.nameFirstUpper()).append("\";").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".Text = \"\";").append(NEW_LINE);
      initialization.append(tab).append("this.").append(fieldName).append(".CheckedChanged += new System.EventHandler(this.").append(fieldName).append("_CheckedChanged);").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.DateTimePicker")) {
      initialization.append(tab).append("this.").append(fieldName).append(".ValueChanged += new System.EventHandler(this.").append(fieldName).append("_ValueChanged);").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.PictureBox")) {
    	// TODO
    }
    else {
      throw new Exception("Unknown windows control: " + controlClass);
    }
    return initialization.toString();
  }
  
  public String getReadFromPdMethod(MetaProperty property, String tab, String doFieldName) throws Exception {
    String controlClass = getControlClass(property);
    String fieldName = getControlFieldName(property);
    String propertyNameUpper = property.nameFirstUpper();
    StringBuilder methodText = new StringBuilder();
    
    methodText.append(tab).append("private void Read").append(propertyNameUpper).append("FromPd()").append(NEW_LINE);
    methodText.append(tab).append("{").append(NEW_LINE);
    
    if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      MetaClass propertyType = model.findClass(property.getTypeName());
      CodeType referenceCode = model.getCodeTypeMgr().retrieve(propertyType);
      List<CodeValue> values = referenceCode.getValues();
      if (propertyType != null && propertyType.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
        String qualifiedPropertyName = doFieldName + "." + propertyNameUpper;
        methodText.append(tab).append("  int index = -1;").append(NEW_LINE);
        methodText.append(tab).append("  if (").append(qualifiedPropertyName).append(" != null) {").append(NEW_LINE);
        methodText.append(tab).append("    PmMda.Net.Dog.Reference.IReferenceCode[] ").append(TextUtilities.identifierPlural(property.getName())).append(" =").append(NEW_LINE);
        methodText.append(tab).append("      ").append(referenceCode.getQualifiedName()).append(".Codes;").append(NEW_LINE);
        methodText.append(tab).append("    for(int i=0;i<").append(TextUtilities.identifierPlural(property.getName())).append(".Length;i++) {").append(NEW_LINE);
        methodText.append(tab).append("      if(").append(qualifiedPropertyName).append(".Code == ").append(TextUtilities.identifierPlural(property.getName())).append("[i].Code) {").append(NEW_LINE);
        methodText.append(tab).append("        index = i;").append(NEW_LINE);
        methodText.append(tab).append("      }").append(NEW_LINE);
        methodText.append(tab).append("    }").append(NEW_LINE);
        methodText.append(tab).append("  }").append(NEW_LINE);
        methodText.append(tab).append("  if(").append(fieldName).append(".SelectedIndex != index) {").append(NEW_LINE);
        methodText.append(tab).append("    ").append(fieldName).append(".SelectedIndex = index;").append(NEW_LINE);
        methodText.append(tab).append("  }").append(NEW_LINE);
      }
      else {
        methodText.append(tab).append("  switch (").append(doFieldName).append(".").append(propertyNameUpper).append(")").append(NEW_LINE);
        methodText.append(tab).append("  {").append(NEW_LINE);
        for (int i = 0; i < values.size(); i++) {
          methodText.append(tab).append("    case ").append(values.get(i)).append(":").append(NEW_LINE);
          methodText.append(tab).append("      ").append(fieldName).append(".SelectedIndex = ").append(i).append(";").append(NEW_LINE);
          methodText.append(tab).append("      break;").append(NEW_LINE);
        }
        methodText.append(tab).append("    default:").append(NEW_LINE);
        methodText.append(tab).append("      ").append(fieldName).append(".SelectedIndex = -1;").append(NEW_LINE);
        methodText.append(tab).append("      break;").append(NEW_LINE);
        methodText.append(tab).append("  }").append(NEW_LINE);
      }
    }
    /*if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      MetaClass propertyType = model.findClass(property.getTypeName());
      CodeType referenceCode = model.getCodeTypeMgr().retrieve(propertyType);
      List<CodeValue> values = referenceCode.getValues();
      if (propertyType != null && propertyType.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
        String qualifiedPropertyName = doFieldName + "." + propertyNameUpper;
        methodText.append(tab).append("  int index = -1;").append(NEW_LINE);
        methodText.append(tab).append("  if (").append(qualifiedPropertyName).append(" != null)").append(NEW_LINE);
        methodText.append(tab).append("  {").append(NEW_LINE);
        for (int i = 0; i < values.size(); i++) {
          methodText.append(tab).append("  ");
          if (i != 0) {
            methodText.append("  else ");
          } else {
            methodText.append("  ");
          }
          
          String refElement = referenceCode.getQualifiedName() + "." + values.get(i).getName().toUpperCase();
          methodText.append("if (").append(qualifiedPropertyName).append(".Code ==").append(refElement).append(".Code)").append(NEW_LINE);
          methodText.append(tab).append("    {").append(NEW_LINE);
          methodText.append(tab).append("      index = ").append(i).append(";").append(NEW_LINE);
          
          methodText.append(tab).append("    }").append(NEW_LINE);
        }
        methodText.append(tab).append("  }").append(NEW_LINE);
        methodText.append(tab).append("  if(").append(fieldName).append(".SelectedIndex != index) {").append(NEW_LINE);
        methodText.append(tab).append("    ").append(fieldName).append(".SelectedIndex = index;").append(NEW_LINE);
        methodText.append(tab).append("  }").append(NEW_LINE);
      }
      else {
        methodText.append(tab).append("  switch (").append(doFieldName).append(".").append(propertyNameUpper).append(")").append(NEW_LINE);
        methodText.append(tab).append("  {").append(NEW_LINE);
        for (int i = 0; i < values.size(); i++) {
          methodText.append(tab).append("    case ").append(values.get(i)).append(":").append(NEW_LINE);
          methodText.append(tab).append("      ").append(fieldName).append(".SelectedIndex = ").append(i).append(";").append(NEW_LINE);
          methodText.append(tab).append("      break;").append(NEW_LINE);
        }
        methodText.append(tab).append("    default:").append(NEW_LINE);
        methodText.append(tab).append("      ").append(fieldName).append(".SelectedIndex = -1;").append(NEW_LINE);
        methodText.append(tab).append("      break;").append(NEW_LINE);
        methodText.append(tab).append("  }").append(NEW_LINE);
      }
    } */
    else if (controlClass.equals("bbv.Common.Windows.Forms.NumericUpDown")) {
      methodText.append(tab).append("  ").append(fieldName).append(".Value = ").append(doFieldName).append(".").append(propertyNameUpper).append(";").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.TextBox")) {
      methodText.append(tab).append("  if (((object) ").append(doFieldName).append(".").append(propertyNameUpper).append(") == null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    ").append(fieldName).append(".Text = string.Empty;").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("  else").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    ").append(fieldName).append(".Text = ").append(doFieldName).append(".").append(propertyNameUpper).append(".ToString()").append(";").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.CheckBox")) {
      methodText.append(tab).append("  ").append(fieldName).append(".Checked = ").append(doFieldName).append(".").append(propertyNameUpper).append(";").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.DateTimePicker")) {
      methodText.append(tab).append("  if(").append(doFieldName).append(".").append(propertyNameUpper).append(" == DateTime.MinValue)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    ").append(doFieldName).append(".").append(propertyNameUpper).append(" = DateTime.Now;").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("  ").append(fieldName).append(".Value = ").append(doFieldName).append(".").append(propertyNameUpper).append(";").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.PictureBox")) {
      methodText.append(tab).append("  if(").append(doFieldName).append(".").append(propertyNameUpper).append(" == null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    ").append(doFieldName).append(".").append(propertyNameUpper).append(" = new System.Drawing.Bitmap(1,1);").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("  ").append(fieldName).append(".Image = ").append(doFieldName).append(".").append(propertyNameUpper).append("; // TODO check").append(NEW_LINE);
    }
    else {
      throw new Exception("Unknown windows control: " + controlClass);
    }
    methodText.append(tab).append("}").append(NEW_LINE);
    return methodText.toString();
  }

  public String getUpdateMethod(MetaProperty property, String tab, String doFieldName) throws Exception {
    String controlClass = getControlClass(property);
    String fieldName = getControlFieldName(property);
    String propertyNameUpper = property.nameFirstUpper();
    StringBuilder methodText = new StringBuilder();
    
    methodText.append(tab).append("private void Update").append(propertyNameUpper).append("()").append(NEW_LINE);
    methodText.append(tab).append("{").append(NEW_LINE);
    methodText.append(tab).append("  if (m_propertyValueChanged)").append(NEW_LINE);
    methodText.append(tab).append("  {").append(NEW_LINE);
    methodText.append(tab).append("    try").append(NEW_LINE);
    methodText.append(tab).append("    {").append(NEW_LINE);

    if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      CodeType referenceCode = model.getCodeTypeMgr().retrieve(model.findClass(property.getTypeName()));
      methodText.append(tab).append("      ").append(referenceCode.getQualifiedName()).append(" ").append(property.getName()).append(" = ").append(referenceCode.getQualifiedName()).append(".Codes[").append(fieldName).append(".SelectedIndex] as ").append(referenceCode.getQualifiedName()).append(";").append(NEW_LINE);
      methodText.append(tab).append("      if(").append(property.getName()).append(" != null) {").append(NEW_LINE);
      methodText.append(tab).append("        ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(property.getName()).append(";").append(NEW_LINE);
      methodText.append(tab).append("      } else {").append(NEW_LINE);
      methodText.append(tab).append("        throw new Exception(\"Unknown reference code member.\");").append(NEW_LINE);
      methodText.append(tab).append("      }").append(NEW_LINE);
    }    
    /*if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      methodText.append(tab).append("      switch(").append(fieldName).append(".SelectedIndex)").append(NEW_LINE);
      methodText.append(tab).append("      {").append(NEW_LINE);
      CodeType referenceCode = model.getCodeTypeMgr().retrieve(model.findClass(property.getTypeName()));
      List<CodeValue> values = referenceCode.getValues();
      for (int i = 0; i < values.size(); i++) {
        methodText.append(tab).append("        case ").append(i).append(":").append(NEW_LINE);       
        methodText.append(tab).append("          ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(referenceCode.getQualifiedName()).append(".").append(values.get(i).getName().toUpperCase()).append(";").append(NEW_LINE);
        methodText.append(tab).append("          break;").append(NEW_LINE);
      }            
      methodText.append(tab).append("        default:").append(NEW_LINE);
      methodText.append(tab).append("          throw new Exception(\"Unknown reference code member.\");").append(NEW_LINE);
      methodText.append(tab).append("      }").append(NEW_LINE);
    }*/
    else if (controlClass.equals("bbv.Common.Windows.Forms.NumericUpDown")) {
      String propertyType = convertJavaTypeToDotnet(property.retrieveTypeClassName());
      methodText.append(tab).append("      ").append(doFieldName).append(".").append(propertyNameUpper).append(" = (").append(propertyType).append(") ").append(fieldName).append(".").append("Value").append(";").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.TextBox")) {
      String propertyType = convertJavaTypeToDotnet(property.retrieveTypeClassName());
      String propertyNS = null;
      MetaClass type = model.findClass(property.getTypeName());
      if (type != null) {
        propertyNS = type.retrievePackage().getAttribute(DogNetCartridge.DATA_OBJECTS_PACKAGE_ATTRIBUTE);
      }
      if (propertyNS != null && propertyNS.length() > 0) {
        propertyType = propertyNS + "." + propertyType;
      }
      if ("string".equals(propertyType))
      {
        methodText.append(tab).append("      ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(fieldName).append(".").append("Text").append(";").append(NEW_LINE);
      }
      else
      {
        methodText.append(tab).append("      ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(propertyType).append(".Parse(").append(fieldName).append(".").append("Text").append(")").append(";").append(NEW_LINE);
      }
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.CheckBox")) {
      methodText.append(tab).append("      ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(fieldName).append(".").append("Checked").append(";").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.DateTimePicker")) {
      methodText.append(tab).append("      ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(fieldName).append(".").append("Value").append(";").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.PictureBox")) {
      methodText.append(tab).append("      ").append(doFieldName).append(".").append(propertyNameUpper).append(" = ").append(fieldName).append(".").append("Image").append("; // TODO check").append(NEW_LINE);
    }
    else {
      throw new Exception("Unknown windows control: " + controlClass);
    }
    
    methodText.append(tab).append("    }").append(NEW_LINE);
    methodText.append(tab).append("    catch (Exception ex)").append(NEW_LINE);
    methodText.append(tab).append("    {").append(NEW_LINE);
    if (controlClass.equals("bbv.Common.Windows.Forms.TextBox")) {
      methodText.append(tab).append("      if (DialogResult.Cancel == MessageBox.Show(this, \"Could not set the property ").append(propertyNameUpper).append(". Error: \" + ex.Message, \"Setting property value failed\", MessageBoxButtons.OKCancel, MessageBoxIcon.Error, MessageBoxDefaultButton.Button1))").append(NEW_LINE);
      methodText.append(tab).append("      {").append(NEW_LINE);
      methodText.append(tab).append("        m_propertyValueChanged = false;").append(NEW_LINE);
      methodText.append(tab).append("        Read").append(propertyNameUpper).append("FromPd();").append(NEW_LINE);
      methodText.append(tab).append("      }").append(NEW_LINE);
      methodText.append(tab).append("      if (").append(fieldName).append(".Focus())").append(NEW_LINE);
      methodText.append(tab).append("      {").append(NEW_LINE);
      methodText.append(tab).append("        ").append(fieldName).append(".SelectionStart = 0;").append(NEW_LINE);
      methodText.append(tab).append("        ").append(fieldName).append(".SelectionLength = ").append(fieldName).append(".Text.Length;").append(NEW_LINE);
      methodText.append(tab).append("      }").append(NEW_LINE);
    }
    else {
      methodText.append(tab).append("      MessageBox.Show(this, \"Could not set the property ").append(propertyNameUpper).append(". Error: \" + ex.Message, \"Setting property value failed\", MessageBoxButtons.OK, MessageBoxIcon.Error);").append(NEW_LINE);
      methodText.append(tab).append("      m_propertyValueChanged = false;").append(NEW_LINE);
      methodText.append(tab).append("      Read").append(propertyNameUpper).append("FromPd();").append(NEW_LINE);
    }
    methodText.append(tab).append("    }").append(NEW_LINE);
    methodText.append(tab).append("  }").append(NEW_LINE);
    methodText.append(tab).append("  else").append(NEW_LINE);
    methodText.append(tab).append("  {").append(NEW_LINE);
    methodText.append(tab).append("    Read").append(propertyNameUpper).append("FromPd();").append(NEW_LINE);
    methodText.append(tab).append("  }").append(NEW_LINE);
    methodText.append(tab).append("}").append(NEW_LINE);
    return methodText.toString();
  }
  
  public String getChangeEventMethods(MetaProperty property, String tab, String doFieldName) throws Exception {
    String controlClass = getControlClass(property);
    String fieldName = getControlFieldName(property);
    String propertyNameUpper = property.nameFirstUpper();
    StringBuilder methodText = new StringBuilder();
  
    if (controlClass.equals("bbv.Common.Windows.Forms.ComboBox")) {
      methodText.append(tab).append("private void ").append(fieldName).append("_SelectedIndexChanged(object sender, System.EventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  if (").append(doFieldName).append(" != null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    m_propertyValueChanged = true;").append(NEW_LINE);
      methodText.append(tab).append("    Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.NumericUpDown")) {
      methodText.append(tab).append("private void ").append(fieldName).append("_ValueChanged(object sender, System.EventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  if (").append(doFieldName).append(" != null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    m_propertyValueChanged = true;").append(NEW_LINE);
      methodText.append(tab).append("    Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.TextBox")) {
      methodText.append(tab).append("private void ").append(fieldName).append("_TextChanged(object sender, System.EventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  if (").append(doFieldName).append(" != null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    m_propertyValueChanged = true;").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
      methodText.append(tab).append(NEW_LINE);
      methodText.append(tab).append("private void ").append(fieldName).append("_KeyPress(object sender, System.Windows.Forms.KeyPressEventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  if (").append(doFieldName).append(" != null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    if (e.KeyChar == (char) Keys.Enter)").append(NEW_LINE);
      methodText.append(tab).append("    {").append(NEW_LINE);
      methodText.append(tab).append("      Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("    }").append(NEW_LINE);
      methodText.append(tab).append("    if (e.KeyChar == (char) Keys.Escape)").append(NEW_LINE);
      methodText.append(tab).append("    {").append(NEW_LINE);
      methodText.append(tab).append("      m_propertyValueChanged = false;").append(NEW_LINE);
      methodText.append(tab).append("      Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("    }").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
      methodText.append(tab).append(NEW_LINE);      
      methodText.append(tab).append("private void ").append(fieldName).append("_Leave(object sender, System.EventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
    }
    else if (controlClass.equals("bbv.Common.Windows.Forms.CheckBox")) {
      methodText.append(tab).append("private void ").append(fieldName).append("_CheckedChanged(object sender, System.EventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  if (").append(doFieldName).append(" != null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    m_propertyValueChanged = true;").append(NEW_LINE);
      methodText.append(tab).append("    Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.DateTimePicker")) {
      methodText.append(tab).append("private void ").append(fieldName).append("_ValueChanged(object sender, System.EventArgs e)").append(NEW_LINE);
      methodText.append(tab).append("{").append(NEW_LINE);
      methodText.append(tab).append("  if (").append(doFieldName).append(" != null)").append(NEW_LINE);
      methodText.append(tab).append("  {").append(NEW_LINE);
      methodText.append(tab).append("    m_propertyValueChanged = true;").append(NEW_LINE);
      methodText.append(tab).append("    Update").append(propertyNameUpper).append("();").append(NEW_LINE);
      methodText.append(tab).append("  }").append(NEW_LINE);
      methodText.append(tab).append("}").append(NEW_LINE);
    }
    else if (controlClass.equals("System.Windows.Forms.PictureBox")) {
    	// TODO
    }
    else {
      throw new Exception("Unknown windows control: " + controlClass);
    }
    return methodText.toString();
  }
  
  public String nameFirstLower(String name) {
    return ModelOperators.identifierFirstLower(name);
  }
  
  public List<MetaProperty> getIndexedProperties(MetaClass clazz) {
    return getProperties(clazz, false, false, true);
  }
  
  public List<MetaProperty> getIndexedPropertiesForGUI(MetaClass clazz) {
    List<MetaProperty> properties = getProperties(clazz, false, false, true);
    for (int i = 0; i < properties.size(); i++) {
      MetaProperty property = properties.get(i);
      MetaClass propertyType = model.findClass(property.getTypeName());
      if(propertyType != null) {
        if (!propertyType.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
          properties.remove(i);
          i--;
        }
      } else {
        MetaDatatype dataType = model.findDatatype(property.getTypeName());
        if(dataType == null) {
          properties.remove(i);
          i--;
        }
      }
    }
    return properties;
  }
  
  public List<MetaProperty> getPrimitiveProperties(MetaClass clazz) {
    return getProperties(clazz, true, false, false);
  }
  
  public List<MetaProperty> getPrimitivePropertiesForGUI(MetaClass clazz) {
    List<MetaProperty> properties = getProperties(clazz, true, false, false);
    for (int i = 0; i < properties.size(); i++) {
      if (properties.get(i).getTypeName().endsWith("[]")) {
        properties.remove(i);
        i--;
      }
    }
    return properties;
  }
  
  public List<MetaProperty> getSingleProperties(MetaClass clazz) {
    return getProperties(clazz, false, true, false);
  }
  
  private List<MetaProperty> getProperties(MetaClass clazz, boolean primitive, boolean single, boolean indexed) {
    ArrayList<MetaProperty> properties = new ArrayList<MetaProperty>();
    for (MetaProperty property : clazz.getProperties()) {
      if (property.canBeRead() && !property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        MetaClass propertyType = model.findClass(property.getTypeName());
        boolean propertyTypeIsDataObject;
        if (propertyType == null ) {
          propertyTypeIsDataObject = false;
        }
        else {
          propertyTypeIsDataObject = propertyType.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE);
        }
        if (primitive && !propertyTypeIsDataObject) {
          properties.add(property);
        }
        if (single && propertyTypeIsDataObject) {
          properties.add(property);
        }
      }
    }
    if (indexed) {
      for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
        if (property.canBeRead() && !property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
          properties.add(property);
        }
      }
    }
    
    List<MetaClass> ancestors = clazz.retrieveAncestors(); 
    if (ancestors.size() > 0) {
      MetaClass baseClass = ancestors.get(0);
      properties.addAll(getProperties(baseClass, primitive, single, indexed));
    }
    
    return properties;
  }
  
  public <T> List<T> reverseList(List<T> list) {
    List<T> reverseList = new ArrayList<T>();
    for (int i = list.size() - 1; i >= 0; i--) {
      reverseList.add(list.get(i));
    }
    return reverseList;
  }
  
  public boolean containsDataObjects(MetaPackage pack) {
	    for (MetaClass clazz : pack.getClasses()) {
	      if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
	        return true;
	      }
	    }
	    return false;
	  }
	  
  public boolean containsReferenceCodes(MetaPackage pack) {
	    for (MetaClass clazz : pack.getClasses()) {
	      if (clazz.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
	        return true;
	      }
	    }
	    return false;
	  }
	  
  public String getPackageName(MetaPackage metaPackage) {
    String name = metaPackage.getName();
    int lastPointPos = name.lastIndexOf('.');
    if (lastPointPos == -1) {
      return name;
    }
    else {
      name = name.substring(lastPointPos + 1, name.length());
      return name;
    }
  }
  
  public String getTypeClassInstance(MetaClass clazz) {
    String qualifiedName = clazz.getQualifiedName();
    if (qualifiedName.equals("java.lang.String")) {
      return "MetaType.String";
    }
    if (qualifiedName.equals("java.lang.Boolean")) {
      return "MetaType.Boolean";
    }
    if (qualifiedName.equals("java.lang.Integer")) {
      return "MetaType.Integer";
    }
    if (qualifiedName.equals("java.lang.Long")) {
      return "MetaType.Long";
    }
    if (qualifiedName.equals("java.lang.Float")) {
      return "MetaType.Float";
    }
    if (qualifiedName.equals("java.lang.Double")) {
      return "MetaType.Double";
    }
    if (qualifiedName.equals("java.util.Date")) {
      return "MetaType.DateTime";
    }
    return "class_" + qualifiedName.replace('.', '_');
  }
  
  public boolean instanceOf(Object o, String className) {
    if (o == null) {
      return false;
    }
    return instanceOf(o.getClass(), className);
  }
  
  public boolean instanceOf(Class c, String className)
  {
       if (c.getName().equals(className)) {
          return true;
       }
       Class[] ifcs = c.getInterfaces();
       for (int i=0; i<ifcs.length; i++) {
           if (instanceOf(ifcs[i], className)) {
               return true;
           }
       }
       c = c.getSuperclass();
       if (c != null) {
         return instanceOf(c, className);
       }
       return false;
  }
  
  /**
   * Returns the qualified package name of the data type of the member.
   * @return The qualified package name where the data type of this member resides.
   */
  public String retrieveTypePackageName(MetaMember member) {
    String qualifiedTypeName = member.getTypeName();
    int lastPointPos = qualifiedTypeName.lastIndexOf('.');
    if (lastPointPos > -1) {
      return qualifiedTypeName.substring(0, lastPointPos);
    }
    else {
      return "";
    }
  }

  
  /**
   * Instances a new instance of the NetCartridgeHelper class.
   * @param cartridge currently processing the model
   * @param model The meta model currently processed by the cartridge.
   */
  public NetCartridgeHelper(CartridgeImpl cartridge, MetaModel model) {
    assert(model != null);
  	this.model = model;
  	this.cartridge = cartridge;
  	classes = new Vector<MetaClass>();
  	for (Iterator i = model.getPackages().iterator(); i.hasNext();) {
  	  MetaPackage metaPackage = (MetaPackage) i.next();
      if (!metaPackage.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
    	  for (MetaClass clazz : metaPackage.getClasses()) {
            if (!clazz.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
              classes.add(clazz);
            }
    	  }
      }
  	}
  }
  
  /**
   * Returns the description of an meta element separated in lines
   * with no prefix.
   * @param metaElement The meta element which description should be converted to lines.
   * @return The description of the meta element as list of lines.
   */
  public List<String> getXmlDocLines(MetaElement metaElement) {
    assert(metaElement != null);
    List<String> lines = new ArrayList<String>();
    String text = metaElement.getDescription();
    if ((text != null) && (text.length() > 0)) {
      StringBuilder currentLine = new StringBuilder();
      int start = 0;
      int end = start + MetaElement.DOC_LINE_LENGTH;
      while (end < (text.length() - 1)) {
        while ((end > start) && !Character.isWhitespace(text.charAt(end - 1))) {
          end--;
        }
        if (end > start) {
          currentLine.append(text.substring(start, end));
        } else {
          end = start + MetaElement.DOC_LINE_LENGTH;
          currentLine.append(text.substring(start, end));
          currentLine.append("-");
        }
        lines.add(currentLine.toString());
        currentLine.setLength(0);
        start = end;
        end = start + MetaElement.DOC_LINE_LENGTH;
      }
      if (start < (text.length() - 1)) {
        currentLine.append(text.substring(start));
        lines.add(currentLine.toString());
      }
    }
    return lines;
  }
  
  public boolean isClassDataObject(MetaClass clazz) {
    assert(clazz != null);
    if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
      return true;
    }
    return false;
  }
  
  /**
   * Returns whether the super (base) class of a meta class is a data object 
   * @param clazz The meta class.
   * @return True if the base class is a data object. Otherwiese false. If no base
   * class exists false is returned.
   */
  public boolean isBaseClassDataObject(MetaClass clazz) {
    assert(clazz != null);
    List ancestors = clazz.retrieveAncestors(); 
    if (ancestors.size() > 0) {
      MetaClass baseClass = (MetaClass) ancestors.get(0);
      if (baseClass.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Returns wheter a specified meta class has a base class which is a data object
   * and has a lightweight representation.<br/>
   * Note that the base class for the lightweight representation of the specified meta class
   * is sometimes not the same as for data objects. So if the base class don't have a
   * lightweight representation, the base class of the base class is tested. 
   * @param clazz The meta class.
   * @return True if the specified class has a base class which is a data object and has a lightweight
   * representation. Otherwise false.
   */
  public boolean isBaseClassDataObjectLw(MetaClass clazz) {
    assert(clazz != null);
    List ancestors = clazz.retrieveAncestors(); 
    if (ancestors.size() > 0) {
      MetaClass baseClass = (MetaClass) ancestors.get(0);
      if (!hasLightweight(baseClass)) {
        return isBaseClassDataObjectLw(baseClass);
      }
      if (baseClass.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Return the base class and interfaces as extend string for data objects.
   * @param clazz
   * @return The extend string.
   */
  public String getExtendString(MetaClass clazz) {
    return getExtendString(clazz, DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE);
  }

  /**
   * Returns the base class and interfaces as extend string for lightweight objects. 
   * @param clazz
   * @return
   */
  public String getExtendStringLW(MetaClass clazz) {
    return getExtendString(clazz, DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE);
  }
  
  /**
   * Returns all using statements.
   * @param doClass The class for which the using statements are generated.
   * @return The using statements without the starting "using ".
   */
  public List<String> getUsingStatements(MetaClass doClass) {
    return getUsingStatements(doClass, null, DogNetCartridge.DATA_OBJECTS_PACKAGE_ATTRIBUTE);
  }
  
  /**
   * Returns all using statements for lightweight objects.
   * @param lwClass The class for which the using statements are generated.
   * @return The using statements without the starting "using ".
   */
  public List<String> getUsingStatementsLW(MetaClass lwClass) {
    return getUsingStatements(lwClass, BoConstants.BO_LIGHTWEIGHT, DogNetCartridge.LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE);
  }
  
  /**
   * Convert a java type to a .NET Type.
   * @param javaType The type name in java
   * @return The CSharp type
   */
  public String convertJavaTypeToDotnet(String javaType) {
  	int idx = javaType.lastIndexOf(".");
  	String jType = javaType;
  	if(idx > -1 && idx < javaType.length()) {
  		jType = javaType.substring(idx+1);
  	}
  	String netType = jType;
    // primitive data types
    if ("boolean".equals(jType)) {
      netType = "bool";
    }
    else if ("java.lang.String".equals(jType)) {
      netType = "string";
    }
    else if ("String".equals(jType)) {
      netType = "string";
    }
    else if ("Date".equals(jType)) {
    	netType = "DateTime";
    }
    else if ("BigDecimal".equals(jType)) {
    	netType = "decimal";
    }
    else if ("BigInteger".equals(jType)) {
    	netType = "long";
    }
    else if ("Boolean".equals(jType)) {
    	netType = "bool";
    }
    else if ("Byte".equals(jType)) {
    	netType = "byte";
    }
    else if ("Char".equals(jType)) {
    	netType = "char";
    }
    else if ("Collection".equals(jType)) {
    	netType = "IList";
    }
    else if ("Container".equals(jType)) {
    	netType = "ContainerControl";
    }
    else if ("Double".equals(jType)) {
    	netType = "double";
    }
    else if ("Float".equals(jType)) {
    	netType = "float";
    }
    else if ("Integer".equals(jType)) {
    	netType = "int";
    }
    else if ("Long".equals(jType)) {
    	netType = "long";
    }
    else if ("List".equals(jType)) {
    	netType = "IList";
    }
    else if ("Object".equals(jType)) {
    	netType = "object";
    }
    else if ("Short".equals(jType)) {
    	netType = "short";
    }
    else if ("Time".equals(jType)) {
    	netType = "DateTime";
    }
    
    if(!netType.equals(jType)) return netType;
    return javaType;
  }
  
  /**
   * Gets the fully qulified name (Namespace.class, assembly)
   * of a java type
   * @param javaType The name of the type or class in java.
   * @return The fully qualified .NET type name
   */
  public String getFullyQualifiedDotnetType(String javaType, String netAssembly) {
    StringBuilder netType = new StringBuilder();
    netType.append(convertJavaTypeToDotnet(javaType));
    netType.append(", ");
    netType.append(netAssembly);
    return netType.toString();
  }
  
  /**
   * Returns all classes of the model.
   * @return Classes of the currently processed model.
   */
  public List<MetaClass> getClasses() {
    return classes;
  }
  
  /**
   * Returns all classes in the current meta model which have a lightweight representation
   * @return A list of all data object classes.
   */
  public List<MetaClass> getDataObjects() {
    List<MetaClass> dataObjects = new ArrayList<MetaClass>();
    for (MetaClass clazz : classes) {
      if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        dataObjects.add(clazz);
      }
    }
    return dataObjects;
  }
  
  /**
   * Returns all classes in the current meta model which have a lighteight representation.
   * @return A list of all lightweight objects.
   */
  public List<MetaClass> getLightWeightObjects() {
    List<MetaClass> lwObjects = new ArrayList<MetaClass>();
    for (MetaClass clazz : classes) {
      if (clazz.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        if (hasLightweight(clazz)) {
          lwObjects.add(clazz);
        }
      }
    }
    return lwObjects;
  }
  
  /**
   * Gets all data object classes which don't derive from an other data object.
   * To find the classes the current model is scaned.
   * @return Returns all data object which don't derive from another data object.
   */
  public List<MetaClass> getBaseDataObjects() {
    List<MetaClass> baseClasses = new Vector<MetaClass>();
    for (MetaClass currentClass : classes) {
      if (currentClass.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        boolean isBaseClass = true;
        for (MetaClass ancestor : currentClass.retrieveAncestors()) {
          if (ancestor.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
            isBaseClass = false;
            break;
          }
        }
        if (isBaseClass) {
          baseClasses.add(currentClass);
        }
      }
    }
    return baseClasses;
  }
  
  /**
   * Returns only the persistant data objects types which derive from the clazz.
   * @param clazz
   * @return Returns all data object classes which derive from the specified class.
   */
  public List<MetaClass> getDerivedClasses(MetaClass clazz) {
    List<MetaClass> derivedClasses = new Vector<MetaClass>();
    for (MetaClass currentClass : classes) {
      if (!currentClass.equals(clazz)) {
        if (currentClass.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
          for (MetaClass ancestor : currentClass.retrieveAncestors()) {
            if (ancestor.equals(clazz)) {
              derivedClasses.add(currentClass);
              break;
            }
          }
        }
      }
    }
    return derivedClasses;
  }
  
  /**
   * Gets all reference code. To find the classes the current model is scaned.
   * @return Returns all reference codes.
   */
  public List<MetaClass> getReferenceCodes() {
    List<MetaClass> refCodes = new Vector<MetaClass>();
    for (MetaClass currentClass : classes) {
      if (currentClass.hasStereotype(BoConstants.REFERENCE_CODE_STEREOTYPE)) {
        refCodes.add(currentClass);
      }
    }
    return refCodes;
  }
  
  /**
   * Returns all properties which are not ignored or doesn't point to an ignored class. 
   * @param clazz The meta class for which the properties are returned.
   * @return A list of all properties
   */
  public List<MetaProperty> getAllProperties(MetaClass clazz) {
    List<MetaProperty> properties = new ArrayList<MetaProperty>();
    for (MetaProperty property : clazz.getProperties()) {
      if (!property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        properties.add(property);      
      }
    }
    for (MetaClass superClass : clazz.retrieveAncestors()) {
      if (!superClass.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        properties.addAll(getAllProperties(superClass));
      }
    }
    return properties;
  }
  
  /**
   * Returns all indexed properties which are not ignored or doesn't point to an ignored class. 
   * @param clazz The meta class for which the indexed properties are returned.
   * @return A list of all properties
   */
  public List<MetaIndexedProperty> getAllIndexedProperties(MetaClass clazz) {
    List<MetaIndexedProperty> properties = new ArrayList<MetaIndexedProperty>();
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      if (!property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        properties.add(property);      
      }
    }
    for (MetaClass superClass : clazz.retrieveAncestors()) {
      if (!superClass.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        properties.addAll(getAllIndexedProperties(superClass));
      }
    }
    return properties;
  }

  /**
   * Returns a string representing the passed date in a short and us form. Suitable for mysql and mmssql
   * @param date to be converted
   * @return String representation of date
   */
  public String formatDate(Date date) {
  	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  	return format.format(date);
  }
  
  /**
   * Search and return the base class for a lightweight object.
   * @param clazz
   * @return
   */
  private MetaClass findBaseClassForLw(MetaClass clazz) {
    List ancestors = clazz.retrieveAncestors();
    if (ancestors.size() > 0) {
      MetaClass baseClass = (MetaClass) ancestors.get(0);
      if (hasLightweight(baseClass)) {
        return baseClass;
      }
      return findBaseClassForLw(baseClass);
    }
    return null;
  }
  
  /**
   * Gets the extend string for a specified meta class.
   * @param clazz The meta class to get the extend string for.
   * @param attribute The class name attribute. Must be eighter
   * DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE or
   * DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE.
   * @return The extend string which can be used in C# source files
   */
  private String getExtendString(MetaClass clazz, String attribute) {
    assert(clazz != null);
    assert(attribute.equalsIgnoreCase(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE) ||
           attribute.equalsIgnoreCase(DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE));
    StringBuilder extend = new StringBuilder("");
    MetaClass baseClass = null;
    if (attribute.equalsIgnoreCase(DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE)) {
      baseClass = findBaseClassForLw(clazz);
    }
    else {
      List ancestors = clazz.retrieveAncestors();
      if (ancestors.size() > 0) {
        baseClass = (MetaClass) ancestors.get(0);
      }
    }
    if (baseClass != null && !baseClass.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
      extend.append(" : ");
      if (baseClass.hasStereotype(BoConstants.DATA_OBJECT_STEREOTYPE)) {
        extend.append(baseClass.getAttribute(attribute));
      }
      else {
        extend.append(baseClass.getName());
      }
    }
    for (MetaClass baseInterface : clazz.retrieveInterfaces()) {
      if (!baseInterface.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        if (extend.length() == 0) {
          extend.append(" : ");          
        }
        else {
          extend.append(", ");
        }
        extend.append(baseInterface.getName());
      }
    }
    if (extend.length() == 0) {
      extend.append(" : ");          
    }
    else {
      extend.append(", ");
    }
    if (attribute.equalsIgnoreCase(DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE)) {
      extend.append("IDataObject, ISerializable");
    }
    if (attribute.equalsIgnoreCase(DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE)) {
      extend.append("ILightweightObject");
    }
  
    return extend.toString();
  }
  
  /**
   * Returns whether a meta class has its lightweight representation 
   * @param clazz The meta class to check for a lightweight representation.
   * @return True if the meta class has a lightweight representation.
   */
  private boolean hasLightweight(MetaClass clazz) {
    assert (clazz != null);
    for (MetaProperty property : clazz.getProperties()) {
      if (!property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG) && property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
        return true;
      }
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      if(!property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG) && property.booleanValueOfTag(BoConstants.BO_LIGHTWEIGHT)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Returns the using statement for a meta class.
   * @param clazz The meta class for which the the using statements are returned.
   * @param propertyKey A tagged value which must be true (i.e. DogNetCartridge.LIGHTWEIGHT_TAG
   * for lightweight objects) or null if no tagged value is used.
   * @param attribute The attribute which defines wheter the using statements are used for the data object or
   * the lightweight object representations. Must be eighter
   * DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE or
   * DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE.
   * @return A list of all using statements.
   */
  private List<String> getUsingStatements(MetaClass clazz, String propertyKey, String attribute) {
    //TODO: add only namespaces different to the current class.
    assert(attribute.equalsIgnoreCase(DogNetCartridge.DATA_OBJECTS_PACKAGE_ATTRIBUTE) ||
           attribute.equalsIgnoreCase(DogNetCartridge.LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE));
    List<String> usingStatements = new Vector<String>();
    
    for (MetaClass item : clazz.retrieveAncestors()) {
      if (!item.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        addUsingStatements(usingStatements, item, attribute);
      }
    }
    for (MetaClass item : clazz.retrieveInterfaces()) {
      if (!item.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG)) {
        addUsingStatements(usingStatements, item, attribute);
      }
    }
    for (MetaProperty property : clazz.getProperties()) {
      if (!property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG) &&
          (propertyKey == null || property.booleanValueOfAttribute(propertyKey))) {
        MetaClass type = clazz.getModel().findClass(property.getTypeName());
        if (type != null) {
          addUsingStatements(usingStatements, type, attribute);
        } else {
          addUsingStatements(usingStatements, retrieveTypePackageName(property));
        }
      }
    }
    for (MetaIndexedProperty property : clazz.getIndexedProperties()) {
      if (!property.booleanValueOfTag(DogNetCartridge.IGNORE_ELEMENT_TAG) &&
          (propertyKey == null || property.booleanValueOfAttribute(propertyKey))) {
        MetaClass type = clazz.getModel().findClass(property.getTypeName());
        if (type != null) {
          addUsingStatements(usingStatements, type, attribute);
        } else {
          addUsingStatements(usingStatements, retrieveTypePackageName(property));
        }
      }
    }
    return usingStatements;
  }
  
  /**
   * Adds a using statement to the statement list.
   * @param statements The list of all statements
   * @param statement The new statement to add
   */
  private void addUsingStatements(List<String> statements, String statement) {
    if (statement == null || statement.equals("")) {
      return;
    }
    if (statement.startsWith("java.")) {
      log.warn("Java import statement " + statement + " can not be converted to .NET!");
      return;
    }
    if (!statements.contains(statement)) {
      statements.add(statement);
    }
  }
  
  /**
   * Adds a using statement for a specified class to a statements list.
   * @param statements The list of existing statements
   * @param usedClass The class for which a using statement should be added.
   * @param attribute The attribute used to get the namespace (package) of the used class.
   * Must be eighter DogNetCartridge.DATA_OBJECT_CLASS_NAME_ATTRIBUTE or
   * DogNetCartridge.LIGHTWEIGHT_OBJECT_CLASS_NAME_ATTRIBUTE.
   */
  private void addUsingStatements(List<String> statements, MetaClass usedClass, String attribute) {
    assert(attribute.equalsIgnoreCase(DogNetCartridge.DATA_OBJECTS_PACKAGE_ATTRIBUTE) ||
        attribute.equalsIgnoreCase(DogNetCartridge.LIGHTWEIGHT_OBJECTS_PACKAGE_ATTRIBUTE));
    String usingStatement = usedClass.getAttribute(attribute);
    if (usingStatement == null) {
      addUsingStatements(statements, usedClass.retrievePackage().getQualifiedName());
    } else {
      addUsingStatements(statements, usingStatement);
    }  
  }
}
