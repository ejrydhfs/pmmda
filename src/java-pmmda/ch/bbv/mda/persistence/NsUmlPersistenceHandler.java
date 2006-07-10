/* 
 * copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$
 */
package ch.bbv.mda.persistence;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.model_management.MModel;
import ch.bbv.explorer.resources.Messages;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.nsuml.ArgoWriter;
import ch.bbv.mda.persistence.nsuml.Mda2NsUmlTransformer;
import ch.bbv.mda.persistence.nsuml.NsUml2MdaTransformer;
import ch.bbv.mda.resources.MessagesMda;

import com.jgoodies.validation.ValidationResult;

/**
 * The NSUML handler manipulates XMI 1.0 / UML 1.3 based models. These models
 * are created with UML modeling such as Poseidon, ArgoUML or with help of
 * converters Rational Rose.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class NsUmlPersistenceHandler implements PersistenceHandler {
  /**
   * Logger for all instances of the class.
   */
  private static Log log = LogFactory.getLog(NsUmlPersistenceHandler.class);

  /**
   * Transformer from NSUML to MDA.
   */
  private NsUml2MdaTransformer nsUmlTransformer;

  /**
   * Transformer from MDA to NSUML.
   */
  private Mda2NsUmlTransformer mdaTransformer;

  /**
   * Default constructor of the class.
   */
  public NsUmlPersistenceHandler() {
    nsUmlTransformer = new NsUml2MdaTransformer();
    mdaTransformer = new Mda2NsUmlTransformer();
  }

  public MetaModel readModel(File file, ValidationResult vResults) throws PersistenceException {
    MetaModel metaModel = new MetaModel();
    try {
      MModel mModel = nsUmlTransformer.loadArgoModel(file);
      if (mModel == null) {
        log.error("NsUmlPersistenceHandler#readModel: illegal file - " + file);
        JOptionPane.showMessageDialog(null, MessagesMda.msgInvalidFormat,
            Messages.titleWarning, JOptionPane.ERROR_MESSAGE);
        return null;
      }
      nsUmlTransformer.synchronize(mModel, metaModel);
    } catch (Exception e) {
      throw new PersistenceException(e);
    }
    return metaModel;
  }

  public void writeModel(MetaModel model, File file, ValidationResult vResults)
      throws PersistenceException {
    if (model.getFilename() != null
        && model.getFilename().equals(file.toString()) && file.exists()) {
      updateModel(model, file, vResults);
    } else if (file.getName().endsWith(".xmi")
        || file.getName().endsWith(".zargo")) {
      try {
        MModel mModel = MFactory.getDefaultFactory().createModel();
        mModel.setName(model.getName());
        mdaTransformer.synchronize(model, mModel);
        File temporaryFile = File.createTempFile("pmMDA", "xmi");
        nsUmlTransformer.writeArgoModel(mModel, temporaryFile);
        if (file.getName().endsWith(".xmi")) {
          temporaryFile.renameTo(file);
        } else {
          temporaryFile.deleteOnExit();
          ArgoWriter.writeEntry(file, temporaryFile);
          temporaryFile.delete();
        }
      } catch (Exception e) {
        throw new PersistenceException(e);
      }
    }
  }

  public void updateModel(MetaModel model, File file, ValidationResult vResults)
      throws PersistenceException {
    try {
      MModel mModel = nsUmlTransformer.loadArgoModel(file);
      mdaTransformer.synchronize(model, mModel);
      File temporaryFile = File.createTempFile("pmMDA", ".xmi");
      nsUmlTransformer.writeArgoModel(mModel, temporaryFile);
      if (file.getName().endsWith(".xmi")) {
        file.delete();
        temporaryFile.renameTo(file);
      } else {
        temporaryFile.deleteOnExit();
        InputStream input = new FileInputStream(temporaryFile);
        ZipFileUtilities.replaceEntry(file, getArgoModelFileName(file), input);
        temporaryFile.delete();
      }
    } catch (Exception e) {
      throw new PersistenceException(e);
    }
  }

  /**
   * Returns the ArgoUML file name of the file containing the XMI UML model. The
   * file is part of the set of files stored in a zip file and is recognized
   * through its extension.
   * @param file Zip file containing all artifacts of a ArgoUML project.
   * @return The name of the file containing the XMI UML model.
   * @throws FileNotFoundException if the requested file was not found.
   * @throws IOException if an IO error occured when reading the zip file.
   */
  private String getArgoModelFileName(File file) throws FileNotFoundException,
      IOException {
    ZipFile zipFile = new ZipFile(file);
    Enumeration< ? extends ZipEntry> entries = zipFile.entries();
    String modelFileName = null;
    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
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