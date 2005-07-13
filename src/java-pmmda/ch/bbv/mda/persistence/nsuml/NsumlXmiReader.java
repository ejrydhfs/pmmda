// Copyright (c) 1996-2005 The Regents of the University of California. All 
// Rights Reserved. Permission to use, copy, modify, and distribute this 
// software and its documentation without fee, and without a written 
// agreement is hereby granted, provided that the above copyright notice 
// and this paragraph appear in all copies. This software program and 
// documentation are copyrighted by The Regents of the University of 
// California. The software program and documentation are supplied "AS 
// IS", without any accompanying services from The Regents. The Regents 
// does not warrant that the operation of the program will be 
// uninterrupted or error-free. The end-user understands that the program 
// was developed for research purposes and is advised not to rely 
// exclusively on the program for any reason. IN NO EVENT SHALL THE 
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, 
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, 
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF 
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF 
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY 
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE 
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF 
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, 
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS. 

// Extended for XMIReader by Adrian Bachofen, bbv Software Services AG
// Extended by resolveEntity to solve path issues with referenced DTDs

package ch.bbv.mda.persistence.nsuml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException; 
import java.io.FileNotFoundException;
import javax.xml.parsers.ParserConfigurationException; 
import javax.xml.parsers.SAXParserFactory; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource; 
import org.xml.sax.SAXException; 
import ru.novosoft.uml.MFactory; 
import ru.novosoft.uml.model_management.MModel; 
import org.xml.sax.ext.DefaultHandler2;

public class NsumlXmiReader extends ru.novosoft.uml.xmi.XMIReader {

  /**
   * Logger of the class.
   */
  private static Log log = LogFactory.getLog(NsumlXmiReader.class);
  private boolean errors = false;
  private File source;
  private org.xml.sax.XMLReader theParser = null; 

  /** 
   * Constructor for XMIReader. 
   * @throws SAXException when there is a XML problem 
   * @throws ParserConfigurationException if a parser cannot 
   * be created which satisfies the requested configuration 
   */ 
   public NsumlXmiReader(File file) throws SAXException, ParserConfigurationException { 
    super(); 
    source = file;
    SAXParserFactory saxpf = SAXParserFactory.newInstance(); 
    saxpf.setValidating(false); 
    saxpf.setNamespaceAware(false); 

    setParser(saxpf.newSAXParser().getXMLReader()); 

    getParser().setFeature("http://xml.org/sax/features/namespaces", true);
    getParser().setErrorHandler(this); 
    getParser().setContentHandler(new DefaultHandler2()); 
    getParser().setEntityResolver(this); 
 
   } 
 
   /** 
    * Constructor for XMIReader. 
    * @param pFactory the factory 
    * @throws SAXException if there is a XML problem 
    * @throws ParserConfigurationException if a parser cannot 
    * be created which satisfies the requested configuration 
    */ 
   public NsumlXmiReader(MFactory pFactory, File file) 
       throws SAXException, ParserConfigurationException { 
       super(pFactory);
       source = file;
       SAXParserFactory saxpf = SAXParserFactory.newInstance(); 
       saxpf.setValidating(false); 
       saxpf.setNamespaceAware(false); 
 
       setParser(saxpf.newSAXParser().getXMLReader()); 
 
       getParser().setFeature("http://xml.org/sax/features/namespaces", true);
       getParser().setErrorHandler(this); 
       getParser().setContentHandler(new DefaultHandler2()); 
       getParser().setEntityResolver(this); 
 
   } 

   /** 
    * Parses an xmi inputsource. Sets errors to true if an exception is 
    * thrown. Could not change the API from the superclass. Therefore this 
    * strange construction. 
     * @see ru.novosoft.uml.xmi.XMIReader#parseStream(InputSource) 
     */ 
    protected void parseSourceStream(InputSource pIs) 
        throws SAXException, IOException { 
  
        cleanup(); 
  
        try { 
             getParser().parse(pIs); 
             performLinking(); 
        } catch (IOException e) { 
            log.error("NsumlXmiReader#parseSourceStream IOException while trying to read inputsource " 
                + pIs.getSystemId(), e); 
            throw e; 
        } catch (SAXException e) { 
            log.error("NsumlXmiReader#parseSourceStream Parsing error while trying to parse inputsource " 
                + pIs.getSystemId(), e); 
            throw e; 
        } catch (ClassCastException e) { 
            log.error("NsumlXmiReader#parseSourceStream Parsing error while trying to parse inputsource " 
                + pIs.getSystemId(), e); 
            throw new SAXException(e); 
         } 
  
     } 
  
    public InputSource resolveEntity (String publicId, String systemId)
    {
      String path = source.getParent();
      int idx = systemId.lastIndexOf("/");
      if(idx == -1){
        idx = systemId.lastIndexOf("\\");
      }
      path += File.separator+systemId.substring(idx+1);
      File file = new File(path);
      try{
        return new InputSource(new FileInputStream(file));
      }
      catch(FileNotFoundException e){
        // don't do anything
      }
      //System.out.println("NsumlXmiReader#resolveEntity: "+publicId+", "+systemId+", "+path);
      return null;
    }

/* Please do not delete this commented out code. Bob Tarling 3 Mar 2004. 
// This is useful for discovering load problems with corrupt XMI 
// 
// NSUML isn't particularly good at reporting detail of errors 
// and unfortunately is a bit too protective of it's data to just extend. 
// 
// To make use of this code - 
// a) Make the class public 
// b) Refactor to move this class to ru.novosoft.uml.xmi 
// c) Uncomment the code. 
// 
// By pretending to be part of the NSUML package this code can now get 
// access to the links attribute of the NSUML XMIReader and report on 
// (or ignore) any corrupt data. 
// 
// To ignore and not just report you also have to uncomment the 2 
// //i.remove(); lines 
// 
// 
*/ 
// protected void performLinking() { 
// try { 
// java.util.Iterator i = links.iterator(); 
// Object link = null; 
// while(i.hasNext()) { 
// link = i.next(); 
// Class c = link.getClass(); 
// java.lang.reflect.Field[] fields = c.getDeclaredFields(); 
// boolean methodType = fields[2].getBoolean(link); 
// Object sourceObject = fields[0].get(link); 
// if (sourceObject instanceof ru.novosoft.uml.MBase) { 
// if (methodType) { 
// String parameterXMIID = (String)fields[3].get(link); 
// String parameterXMIUUID = 
// (String)fields[4].get(link); 
// Object objectParameter = getObject(parameterXMIID, 
// parameterXMIUUID); 
// Object methodName = fields[1].get(link); 
// if (methodName.equals("type") 
// && sourceObject instanceof 
// ru.novosoft.uml.foundation.core.MAssociationEnd 
// && !(objectParameter instanceof 
// ru.novosoft.uml.foundation.core.MModelElement)) 
// { 
// System.out.println("Link data from XMI " 
// + sourceObject + " " + methodName + " " 
// + parameterXMIID + " " + parameterXMIUUID 
// + " " + objectParameter); 
// //i.remove(); 
// } 
// } else { 
// String parameterXMIID = (String)fields[3].get(link); 
// String parameterXMIUUID = (String)fields[4].get(link); 
// Object objectParameter = 
// getObject(parameterXMIID, parameterXMIUUID); 
// if (!(objectParameter instanceof 
// ru.novosoft.uml.foundation.core.MModelElement)) { 
// Object methodName = fields[1].get(link); 
// System.out.println("Invalid link data from XMI " 
// + sourceObject + " " + methodName + " " 
// + parameterXMIID + " " + parameterXMIUUID 
// + " " + objectParameter); 
// //i.remove(); 
// } 
// } 
// } 
// } 
// } catch (Exception e) { 
// System.out.println("Reflection failed"); 
// e.printStackTrace(); 
// } 
// super.performLinking(); 
// } 
  
    /** 
     * Parses a given inputsource to a model. Does not override the novosoft 
     * parse method since that does not have the right signature. 
     * @param pIs the input source for parsing 
     * @return MModel the UML model 
     * @throws SAXException if there is an XML problem 
     * @throws IOException if there is a file I/O problem 
     */ 
    public MModel parseToModel(InputSource pIs) 
        throws SAXException, IOException { 
         parseSourceStream(pIs); 
         MModel model = getParsedModel(); 
         if (errors) { 
            throw new SAXException("Errors parsing XMI"); 
        } 
         return model; 
    } 
  
    /** 
     * @param e true if there are errors 
     */ 
    public void setErrors(boolean e) { 
        errors = e; 
    } 
  
    /** 
     * @return true if there were errors 
     */ 
    public boolean getErrors() { 
        return errors; 
    } 
  
    /** 
     * @param parser the parser 
     */ 
    public void setParser(org.xml.sax.XMLReader parser) { 
         theParser = parser; 
     } 
  
    /** 
     * @return the parser 
     */ 
    public org.xml.sax.XMLReader getParser() { 
      return theParser; 
    } 
} 
