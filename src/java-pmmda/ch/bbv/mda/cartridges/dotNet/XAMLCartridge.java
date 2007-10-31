package ch.bbv.mda.cartridges.dotNet;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import ch.bbv.application.VersionId;
import ch.bbv.mda.MetaClass;
import ch.bbv.mda.MetaModel;
import ch.bbv.mda.MetaPackage;
import ch.bbv.mda.generators.CartridgeImplNet;

import com.jgoodies.validation.ValidationResult;

public class XAMLCartridge extends CartridgeImplNet {

  private static final String ABBRV = "DotNetXAML";
  private static final String CARTRIDGE_NAME = ".Net XAML";
  private static String TEMPLATE_DEFINITION = "view-definition";

  private static String TEMPLATE_PRESENTER = "xaml-presenter";

  private MetaModel model;

  private File rootDirectory;

  private String viewPackageName;

  private Template presenterTemplate;

  public XAMLCartridge() {
    super(CARTRIDGE_NAME, ABBRV, new VersionId(0, 3, 0));
  }

  public XAMLCartridge(String name, String abbreviation, VersionId version) {
    super(name, abbreviation, version);
  }

  @Override
  public void initialize(Properties properties) {
    super.initialize(properties);

    // FIXME instantiate other templates
    presenterTemplate = retrieveTemplate(TEMPLATE_PRESENTER, null);

  }

  @Override
  public void preProcessModel(MetaModel model, ValidationResult messages) {
    super.preProcessModel(model, messages);
    this.model = model;
  }

  @Override
  public void preProcessClass(MetaClass clazz, ValidationResult messages) {
    super.preProcessClass(clazz, messages);
  }

  @Override
  public void preProcessPackage(MetaPackage metaPackage,
      ValidationResult messages) {
    super.preProcessPackage(metaPackage, messages);
  }

  @Override
  public void processModel(MetaModel model, ValidationResult messages) {

    rootDirectory = new File(getContext().getProperty(
        NetCartridgeConstants.VIEW_ROOT_FOLDER_KEY));
    if (!rootDirectory.exists()) {
      rootDirectory.mkdirs();
    }

    super.processModel(model, messages);
  }

  @Override
  public void processPackage(MetaPackage metaPackage, ValidationResult messages) {
    super.processPackage(metaPackage, messages);

    for (MetaClass clazz : metaPackage.getClasses()) {
      processClass(clazz, messages);
    }
  }

  @Override
  public void processClass(MetaClass clazz, ValidationResult messages) {
    super.processClass(clazz, messages);
    try {
      // FIXME: add other templates to process
      processTemplate(rootDirectory, viewPackageName,
          getPresenterFileName(clazz), presenterTemplate,
          createPresenterContext(clazz), messages);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private String getPresenterFileName(MetaClass clazz) {
    return clazz.getName() + "Presenter.cs";
  }

  private VelocityContext createPresenterContext(MetaClass clazz) {
    VelocityContext context = new VelocityContext();
    context.put("class", clazz);
    return context;
  }
}
