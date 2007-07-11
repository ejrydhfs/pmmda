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
import ch.bbv.mda.MetaProperty;
import ch.bbv.mda.MetaView;
import ch.bbv.mda.generators.CartridgeImplNet;

import com.jgoodies.validation.ValidationResult;

public class ViewCartridge extends CartridgeImplNet {

  private static String TEMPLATE_DEFINITION = "view-definition";
  private static String TEMPLATE_FACADE = "view-facade";

  private MetaModel model;

  private File rootDirectory;

  private String viewPackageName;

  private Template viewDefinitionTemplate;
  private Template viewFacadeTemplate;

  public ViewCartridge() {
    super(".Net View", "DotNetView", new VersionId(0, 3, 0));
  }

  public ViewCartridge(String name, String abbreviation, VersionId version) {
    super(name, abbreviation, version);
  }

  @Override
  public void initialize(Properties properties) {
    super.initialize(properties);
    viewDefinitionTemplate = retrieveTemplate(TEMPLATE_DEFINITION, null);
    viewFacadeTemplate = retrieveTemplate(TEMPLATE_FACADE, null);
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

    for (MetaView view : model.getViews()) {
      processView(view, messages);
    }
  }

  @Override
  public void processPackage(MetaPackage metaPackage, ValidationResult messages) {
    super.processPackage(metaPackage, messages);

    for (MetaView view : metaPackage.getViews()) {
      processView(view, messages);
    }
  }

  public void processView(MetaView view, ValidationResult messages) {
    try {
      processTemplate(rootDirectory, viewPackageName, getFacadeFileName(view),
          viewFacadeTemplate, createFacadeContext(view), messages);
      processTemplate(rootDirectory, viewPackageName,
          getDefinitionFileName(view), viewDefinitionTemplate,
          createDefinitionContext(view), messages);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getFacadeFileName(MetaView view) {
    return view.getName() + "Facade.cs";
  }

  private String getDefinitionFileName(MetaView view) {
    return view.getName() + "Definition.cs";
  }

  private VelocityContext createFacadeContext(MetaView view) {
    VelocityContext context = new VelocityContext();

    context.put("view", view);
    return context;
  }

  private VelocityContext createDefinitionContext(MetaView view) {
    VelocityContext context = new VelocityContext();
    context.put("view", view);
    return context;
  }
}
