/* copyright 2000-2004 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistency;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.xmi.IncompleteXMIException;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistency.nsuml.MdaTransformer;
import ch.bbv.mda.persistency.nsuml.NsUmlTransformer;

/**
 * The NSUML handler manipulates XMI 1.0 / UML 1.3 based models. These models
 * are created with UML modeling such as Poseidon, ArgoUML or with help of
 * converters Rational Rose.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class NsUmlPersistencyHandler implements PersistencyHandler {
  private static Log log = LogFactory.getLog(NsUmlPersistencyHandler.class);

  private NsUmlTransformer nsUmlTransformer;

  private MdaTransformer mdaTransformer;

  public NsUmlPersistencyHandler() {
    nsUmlTransformer = new NsUmlTransformer();
    mdaTransformer = new MdaTransformer();
  }

  public MetaModel readModel(File file) throws PersistencyException {
    MetaModel metaModel = new MetaModel();
    try {
      MModel mModel = nsUmlTransformer.loadArgoModel(file);
      nsUmlTransformer.synchronize(mModel, metaModel);
    } catch (IOException ioe) {
      log.error(ioe);
    } catch (ParserConfigurationException pce) {
      log.error(pce);
    } catch (SAXException saxe) {
      log.error(saxe);
    }
    return metaModel;
  }

  public void writeModel(MetaModel model, File file)
      throws PersistencyException {
    if (file.exists()) {
      updateModel(model, file);
    }
  }

  public void updateModel(MetaModel model, File file)
      throws PersistencyException {
    try {
      MModel mModel = nsUmlTransformer.loadArgoModel(file);
      mdaTransformer.synchronize(model, mModel);
      File temporaryFile = File.createTempFile("pmMDA", "xmi");
      temporaryFile.deleteOnExit();
      nsUmlTransformer.writeArgoModel(mModel, temporaryFile);
      InputStream input = new FileInputStream(temporaryFile);
      ZipFileUtilities utilities = new ZipFileUtilities();
      utilities.replaceEntry(file, getArgoModelFileName(file), input);
      temporaryFile.delete();
    } catch (IOException ioe) {
      log.error(ioe);
    } catch (ParserConfigurationException pce) {
      log.error(pce);
    } catch (SAXException saxe) {
      log.error(saxe);
    } catch (IncompleteXMIException ixmie) {
      log.error(ixmie);
    }
  }

  protected String getArgoModelFileName(File file)
      throws FileNotFoundException, IOException {
    ZipFile zipFile = new ZipFile(file);
    Enumeration entries = zipFile.entries();
    String modelFileName = null;
    while (entries.hasMoreElements()) {
      ZipEntry entry = (ZipEntry) entries.nextElement();
      modelFileName = entry.getName();
      if (modelFileName.endsWith(".xmi")) {
        break;
      }
      modelFileName = null;
    }
    zipFile.close();
    return modelFileName;
  }
}