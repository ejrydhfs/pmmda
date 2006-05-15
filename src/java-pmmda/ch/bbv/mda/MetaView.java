package ch.bbv.mda;

import java.util.ArrayList;
import java.util.List;

public class MetaView extends MetaElement {
	
	 
	  private String description;
	  private List<FieldDefinition> fields;
	  
	  public MetaView(){
		  setName("View");
		  fields = new ArrayList<FieldDefinition>();
	  }
	  
	  public void setDescription(String s){
		  description = s;
	  }
	  
	  public void appendField(FieldDefinition def){
		  fields.add(def);
	  }
	  
	  public static class FieldDefinition {
		  
		  private StringBuilder builder;
		  private String attribute;
		  private String pack;
		  
		  public FieldDefinition(String attribute, String pack){
			  this.attribute = attribute;
			  this.pack = pack;
			  builder = new StringBuilder(100);
			  builder.append(pack).append(".").append(attribute);
		  }
		  
		  public String getAttribute(){
			  return this.attribute;
		  }
		  
		  public String getPackage(){
			  return this.pack;
		  }
		  
		  public String toString(){
			  return getFullQualifiedName();
		  }
		  
		  public String getFullQualifiedName(){
			  return builder.toString();
		  }
	  }
	  
	  public List<FieldDefinition> getFields(){
		  return fields;
	  }

	  /**
	   * Implements the visitor pattern.
	   * @param visitor visitor to process.
	   */
	  public void accept(Visitor visitor) {
	    visitor.visit(this);
	  }


}
