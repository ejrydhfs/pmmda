/* copyright 2000-2006 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.mda.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.uml.UmlPackage;

import ch.bbv.mda.MetaModel;
import ch.bbv.mda.persistence.mdr.Mda2MdrTransformer;
import ch.bbv.mda.persistence.mdr.Mdr2MdaTransformer;
import ch.bbv.mda.persistence.mdr.MdrLoader;
import ch.bbv.mda.persistence.mdr.MdrUmlModel;
import ch.bbv.mda.persistence.nsuml.ArgoWriter;

import com.jgoodies.validation.ValidationResult;

public class MdrPersistenceHandler implements PersistenceHandler {
    /**
     * Logger for all instances of the class.
     */
    private static Log log = LogFactory.getLog(MdrPersistenceHandler.class);

    /**
     * Transformer from MDR to MDA.
     */
    private Mdr2MdaTransformer mdrTransformer;

    /**
     * Transformer from MDA to MDR.
     */
    private Mda2MdrTransformer mdaTransformer;

    /**
     * Default constructor of the class.
     */
    public MdrPersistenceHandler() {
        mdrTransformer = new Mdr2MdaTransformer();
        mdaTransformer = new Mda2MdrTransformer();
    }

    public MetaModel readModel(File file, ValidationResult vResults) throws PersistenceException {
        MetaModel metaModel = new MetaModel();
        try {
            UmlPackage uml = mdrTransformer.loadArgoModel(file);
            MdrUmlModel mdrModel = new MdrUmlModel(uml);
            mdrTransformer.synchronize(mdrModel, metaModel);

        } catch (NullPointerException n) {
            n.printStackTrace();
        } catch (FileNotFoundException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return metaModel;
    }

    public void writeModel(MetaModel model, File file, ValidationResult vResults)
            throws PersistenceException {

            // file doesn't exists
            if (file.getName().endsWith(".xmi") || file.getName().endsWith(".zargo")) {
                try {
                    // MdrLoader mdrLoader.
                    MdrLoader mdrLoader = new MdrLoader();
                    UmlPackage umlPackage = null;
                    try {
                        umlPackage = mdrLoader.init();
                    } catch (PersistenceException e) {
                        e.printStackTrace();
                    }

                    MdrUmlModel mdrModel = new MdrUmlModel(umlPackage);
                    mdrModel.setName(model.getName());

                    mdaTransformer.synchronize(model, mdrModel, mdrLoader);

                    File temporaryFile = File.createTempFile("pmMDA", "xmi");

                    mdrLoader.write(temporaryFile);

                    if (file.getName().endsWith(".xmi")) {
                        if (file.exists()) {
                            file.delete();
                        }
                        temporaryFile.renameTo(file);
                    } else {
                        temporaryFile.deleteOnExit();
                        System.out.println(file.getAbsolutePath()+file.getName());
                        if(file.exists()){  
                            ZipFileUtilities.replaceEntry(file, getArgoModelFileName(file), new FileInputStream(temporaryFile));
                        }else{
                            ArgoWriter.writeEntry(file, temporaryFile);
                        }
                        temporaryFile.delete();
                    }

                } catch (Exception e) {
                    throw new PersistenceException(e);
                }
            }
        }
    

    public void updateModel(MetaModel model, File file, ValidationResult vResults) throws PersistenceException {
        writeModel(model, file, vResults);   
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
