package ch.bbv.mda.persistence.mdr;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.jmi.reflect.RefPackage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.lib.jmi.xmi.XmiContext;

import ch.bbv.utilities.ResourceUtilities;

/**
 * This class supports the expansion of XML HREF references to other modules
 * within a model. The result of the resolver should be a valid URL. This is
 * necessary for Magic Draw as it doesn't have the entire model referenced but
 * just the archived model.
 * @author Matthias Bohlen
 * @author Chad Brandon
 */
public class MDRXmiReferenceResolverContext extends XmiContext {
  private String[] moduleSearchPaths;

  /**
   * log for all instances of the class.
   */
  private static Log log = LogFactory
      .getLog(MDRXmiReferenceResolverContext.class);

  private static final HashMap urlMap = new HashMap();

  /**
   * Constructs an instance of this class.
   * @param extents the extents.
   * @param config the xml input config.
   * @param moduleSearchPaths the paths to search for modules
   */
  public MDRXmiReferenceResolverContext(RefPackage[] extents,
      XMIInputConfig config, String[] moduleSearchPaths) {
    super(extents, config);
    this.moduleSearchPaths = moduleSearchPaths;
  }

  /**
   * @see org.netbeans.lib.jmi.xmi.XmiContext#toURL(java.lang.String)
   */
  public URL toURL(final String systemId) {
    if (log.isDebugEnabled()) {
      log.debug("attempting to resolve Xmi Href --> '" + systemId + "'");
    }

    final String suffix = this.getSuffix(systemId);

    // if the model URL has a suffix of '.zip' or '.jar', get
    // the suffix without it and store it in the urlMap
    String exts = "\\.jar|\\.zip";
    String suffixWithExt = suffix.replaceAll(exts, "");
    URL modelUrl = (URL) urlMap.get(suffixWithExt);

    // Several tries to construct a URL that really exists.
    if (modelUrl == null) {
      // If systemId is a valid URL, simply use it
      modelUrl = this.getValidURL(systemId);
      if (modelUrl == null) {
        // Try to find suffix in module list.
        final String modelUrlAsString = this.findModuleUrl(suffix);
        if (StringUtils.isNotBlank(modelUrlAsString)) {
          modelUrl = this.getValidURL(modelUrlAsString);
        }
        if (modelUrl == null) {
          // search the classpath
          modelUrl = this.findModelUrlOnClasspath(systemId);
        }
        if (modelUrl == null) {
          // Give up and let superclass deal with it.
          modelUrl = super.toURL(systemId);
        }
      }

      // if we've found the module model, log it
      // and place it in the map so we don't have to
      // find it if we need it again.
      if (modelUrl != null) {
        urlMap.put(suffixWithExt, modelUrl);
      }
    }
    if (modelUrl != null && !this.loggedReferencedModels.contains(modelUrl)) {
      log.info("referenced model --> '" + modelUrl + "'");
      this.loggedReferencedModels.add(modelUrl);
    }
    return modelUrl;
  }

  /**
   * Keeps track of the referenced models that have been logged.
   */
  private final Collection loggedReferencedModels = new ArrayList();

  /**
   * Finds a module in the module search path.
   * @param moduleName the name of the module without any path
   * @return the complete URL string of the module if found (null if not found)
   */
  private final String findModuleUrl(final String moduleName) {
    String moduleUrl = null;
    if (this.moduleSearchPaths != null) {
      if (log.isDebugEnabled()) {
        log.debug("findModuleURL: moduleSearchPath.length="
            + moduleSearchPaths.length);
      }
      for (int ctr = 0; ctr < moduleSearchPaths.length; ctr++) {
        final String moduleSearchPath = moduleSearchPaths[ctr];
        if (moduleSearchPath != null && moduleSearchPath.length() > 0) {
          if (moduleSearchPath.endsWith(moduleName)) {
            moduleUrl = moduleSearchPath;
          } else {
            final File candidate = new File(moduleSearchPath, moduleName);
            if (log.isDebugEnabled()) {
              log.debug("candidate '" + candidate.toString() + "' exists="
                  + candidate.exists());
            }
            if (candidate.exists()) {
              try {
                moduleUrl = candidate.toURL().toExternalForm();
              } catch (final MalformedURLException exception) {
                // ignore
              }
            }
          }
          if (moduleUrl != null && moduleName.endsWith(".zip")
              || moduleName.endsWith(".jar")) {
            // - typical case for MagicDraw
            moduleUrl = "jar:" + moduleUrl + "!/"
                + moduleName.substring(0, moduleName.length() - 4);
          }
          // - we've found a module
          if (moduleUrl != null && moduleUrl.length() > 0) {
            break;
          }
        }
      }
    }
    return moduleUrl;
  }

  /**
   * Gets the suffix of the <code>systemId</code>
   * @param systemId the system identifier.
   * @return the suffix as a String.
   */
  private final String getSuffix(String systemId) {
    int lastSlash = systemId.lastIndexOf("/");
    if (lastSlash > 0) {
      String suffix = systemId.substring(lastSlash + 1);
      return suffix;
    }
    return systemId;
  }

  /**
   * The suffixes to use when searching for referenced models on the classpath.
   */
  protected final static String[] CLASSPATH_MODEL_SUFFIXES = new String[] {
      "xml", "xmi" };

  /**
   * Searches for the model URL on the classpath.
   * @param systemId the system identifier.
   * @return the suffix as a String.
   */
  private final URL findModelUrlOnClasspath(final String systemId) {
    String modelName = StringUtils.substringAfterLast(systemId, "/");
    String dot = ".";

    // remove the first prefix because it may be an archive
    // (like magicdraw)
    modelName = StringUtils.substringBeforeLast(modelName, dot);

    URL modelUrl = null;
    if (StringUtils.isNotBlank(modelName)) {
      modelUrl = ResourceUtilities.getResource(modelName);
      if (modelUrl == null) {
        if (CLASSPATH_MODEL_SUFFIXES != null
            && CLASSPATH_MODEL_SUFFIXES.length > 0) {
          int suffixNum = CLASSPATH_MODEL_SUFFIXES.length;
          for (int ctr = 0; ctr < suffixNum; ctr++) {
            if (log.isDebugEnabled()) {
              log.debug("searching for model reference --> '" + modelUrl + "'");
            }
            String suffix = CLASSPATH_MODEL_SUFFIXES[ctr];
            modelUrl = ResourceUtilities.getResource(modelName + dot + suffix);
            if (modelUrl != null) {
              break;
            }
          }
        }
      }
    }
    return modelUrl;
  }

  /**
   * Returns a URL if the systemId is valid. Returns null otherwise. Catches
   * exceptions as necessary.
   * @param systemId the system id
   * @return the URL (if valid)
   */
  private final URL getValidURL(final String systemId) {
    InputStream stream = null;
    URL url = null;
    try {
      url = new URL(systemId);
      stream = url.openStream();
      stream.close();
    } catch (final Exception exception) {
      url = null;
    } finally {
      stream = null;
    }
    return url;
  }
}