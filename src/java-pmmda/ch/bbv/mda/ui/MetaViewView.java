package ch.bbv.mda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.*;
import ch.bbv.explorer.views.NodeTableView;
import ch.bbv.mda.*;
import ch.bbv.mda.uml.MetaUseCase;
import ch.bbv.mda.validation.IdentifierValidator;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class MetaViewView extends NodePropertiesView implements
		ExplorerNodeOwner, ViewConstants {

	/**
	 * serialUID, change this value on each check-in/changing this class
	 */
	private static final long serialVersionUID = 7526472295622776147L;

	/**
	 * The size of a text field in the view.
	 */
	private final static int TEXT_FIELD_SIZE = 10;

	/**
	 * The size of a text area in the view.
	 */
	private final static int TEXT_AREA_SIZE = 20;
	
	/**
	 * Distance of display elements to the border or between each other.
	 */
	private final static int BORDER_MARGIN = 7;

	/**
	 * UI related members
	 */
	private JPanel westTreePanel;
	
	private JPanel viewTreePanel;
	
	private JPanel rootObjectListPanel;

	private JTextField nameText;

	private JTextArea descriptionText;

	private JScrollPane descriptionPane;

	private ReferenceCodeComboBox visibility;

  private JButton buttonSelectedPropertiesToRight;

  private JButton buttonSelectedPropertiesToLeft;

  private JButton buttonRootObjectToRight;

  private JButton buttonRootObjectToLeft;

	private ExplorerTree viewTree;

	private ExplorerTree modelTree;

	/**
	 * View originally available. This is the view before modification by this NodePropertiesView
	 */
	private MetaView originalMetaView;

	/**
	 * ExplorerNodeOwner related fields
	 */
	
	private UiNodeTypeFactory factory;
	
	/**
	 * default constructor
	 */
	public MetaViewView() {
		initialize();
	}

	/*
	 * the UI Sector
	 */

	private JPanel createWestPanel() {
	  westTreePanel = new JPanel();
    westTreePanel.setLayout(new BorderLayout());
		westTreePanel.add(createModelTree());
		return westTreePanel;
	}

	protected JTree getModelTree() {
		return modelTree.getTree();
	}

	private JScrollPane createModelTree() {
		modelTree = new ExplorerTree();
		JScrollPane pane = new JScrollPane(modelTree.getTree());
		pane.setPreferredSize(new Dimension(400, 400));
		return pane;
	}

	protected ExplorerTree getViewTree() {
		return viewTree;
	}

	private JTree createViewTree() {
		viewTree = new ExplorerTree();
		return viewTree.getTree();
	}
	
	private NodeTableView getRootObjectList() {
	  return (NodeTableView)rootObjectListPanel.getComponent(0);
	}

  private JPanel createEastPanel() {
    FormLayout layout = new FormLayout("fill:min(200dlu;pref):grow", "fill:default:grow, "+BORDER_MARGIN+"dlu, 100dlu:grow, min");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.append(createSelectedPropertiesPanel());
    builder.nextRow();
    builder.append(createRootObjectListPanel());
    builder.append(createViewPropertiesForm());
    return builder.getPanel();
  }

	private JPanel createViewPropertiesForm() {
		initalizeViewProperties();
    FormLayout layout = new FormLayout(
        BORDER_MARGIN+"dlu, right:max(40dlu;pref), "+BORDER_MARGIN+"dlu, left:pref:grow, "+BORDER_MARGIN+"dlu, "+BORDER_MARGIN+"dlu", "p, p, p, p, p, "+BORDER_MARGIN+"dlu");
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.addSeparator("General", cc.xyw(2, 1, 4));
    builder.addLabel("&Name", cc.xy(2, 2));
    builder.add(nameText, cc.xyw(4, 2, 2));
    builder.addLabel("&Description", cc.xy(2, 3));
    builder.add(descriptionPane, cc.xyw(4, 3, 2));
    builder.addSeparator("Modifiers", cc.xyw(2, 4, 4));
    builder.addLabel("&Modifier", cc.xy(2, 5));
    builder.add(visibility, cc.xy(4, 5));

    return builder.getPanel();
	}
	
  private JPanel createSelectedPropertiesPanel() {
    FormLayout layout = new FormLayout(2*BORDER_MARGIN+40+"dlu:none, fill:default:grow", "fill:default:grow");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);

    buttonSelectedPropertiesToRight = getButtonSelectedPropertiesToRight();
    buttonSelectedPropertiesToLeft = getButtonSelectedPropertiesToLeft();
    CellConstraints cc = new CellConstraints();
    cc.xy(1, 1, "c, c");
    builder.add(createButtonPanel(buttonSelectedPropertiesToRight, buttonSelectedPropertiesToLeft), cc);
    builder.setColumn(2);
    viewTreePanel = new JPanel();
    viewTreePanel.setLayout(new BorderLayout());
    viewTreePanel.add(new JScrollPane(createViewTree()));
    builder.append(viewTreePanel);
    return builder.getPanel();
  }

  private JPanel createButtonPanel(JButton buttonToRight, JButton buttonToLeft) {
    String marginBetweenButtons = BORDER_MARGIN+"px";
    String buttonWidth = "center:"+(int)(buttonToRight.getPreferredSize().getWidth()+2*BORDER_MARGIN)+"dlu";
    FormLayout buttonLayout = new FormLayout(buttonWidth, "pref, "+marginBetweenButtons+", pref");
    DefaultFormBuilder buttonBuilder = new DefaultFormBuilder(buttonLayout);
    buttonBuilder.append(buttonToRight);
    buttonBuilder.setRow(3);
    buttonBuilder.append(buttonToLeft);
    return buttonBuilder.getPanel();
  }
  
	private JButton getButtonSelectedPropertiesToRight() {
		JButton b = new JButton(">>");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// First obtain and check node
				ExplorerNode node = (ExplorerNode) getModelTree().getLastSelectedPathComponent();
				if (node == null) {	// no node is selected
					return;
				}
				
				nodeToTree(getModelTree(), getViewTree().getTree(), node);
			}
		});
		return b;
	}

	private JButton getButtonSelectedPropertiesToLeft() {
		JButton b = new JButton("<<");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// First obtain and check node
				ExplorerNode node = (ExplorerNode) getViewTree().getTree().getLastSelectedPathComponent();
				if (node == null) {	// no node is selected
					return;
				}
				
				nodeToTree(getViewTree().getTree(), getModelTree(), node);
			}
		});
		return b;
	}

  private JPanel createRootObjectListPanel() {
    FormLayout layout = new FormLayout(2*BORDER_MARGIN+40+"dlu:none, fill:default:grow", "fill:default:grow");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);

    buttonRootObjectToRight = getButtonRootObjectToRight();
    buttonRootObjectToLeft = getButtonRootObjectToLeft();
    CellConstraints cc = new CellConstraints();
    cc.xy(1, 1, "c, c");
    builder.add(createButtonPanel(buttonRootObjectToRight, buttonRootObjectToLeft), cc);
    builder.setColumn(2);
    rootObjectListPanel = new JPanel();
    rootObjectListPanel.setLayout(new BorderLayout());
    builder.append(rootObjectListPanel);
    return builder.getPanel();
  }
  
  private JButton getButtonRootObjectToRight() {
    JButton b = new JButton(">>");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        // First obtain and check node
        ExplorerNode node = (ExplorerNode) getModelTree().getLastSelectedPathComponent();
        if (node == null) { // no node is selected
          return;
        }
        
        // add to root object list
        getRootObjectList().setModel(node);
      }
    });
    return b;
  }

  private JButton getButtonRootObjectToLeft() {
    JButton b = new JButton("<<");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        
        // TODO remove from root object list
      }
    });
    return b;
  }

	/**
	 * Moves a node (MetaModel, MetaPackage, MetaClass, MetaProperty, etc.) to the right and assigns it to the view
	 * @param fromTree tree to remove node from
	 * @param toTree to assign node to.
	 * @param node to be assigned to the view.
	 */
	protected void nodeToTree(JTree fromTree, JTree toTree, ExplorerNode node) {
		if(node.getDataObject() instanceof MetaProperty || node.getDataObject() instanceof MetaView) {
			propertyToTree(fromTree, toTree, node);
		} else if (node.getChildCount() == 0){
			removeNodeFromTree((DefaultTreeModel) fromTree.getModel(), node);
		} else {
			while(node.getChildCount() > 0) {
				ExplorerNode child = (ExplorerNode)node.getChildAt(0);
				nodeToTree(fromTree, toTree, child);
			}
		}
	}
	
	/**
	 * Moves a property or a MetaView to the passed tree. The passed node must be a property or a view
	 * @param fromTree tree to remove node from
	 * @param toTree to assign node to.
	 * @param node property to be assigned to the view
	 */
	private void propertyToTree(JTree fromTree, JTree toTree, ExplorerNode node) {
    assert node.getDataObject() instanceof MetaProperty || node.getDataObject() instanceof MetaView;
		
		// add selected property to viewTree
		DefaultTreeModel model = (DefaultTreeModel) toTree.getModel();
		ExplorerNode clonedNode = node.clonebyCopy(this);
		addNodeToTree(model, clonedNode);

		// remove node from modelTree
		removeNodeFromTree((DefaultTreeModel) fromTree.getModel(), node);

		// expand viewTree
    toTree.expandRow(0);
	}
	
	/**
	 * Finds the corresponding node in the model. First we obtain the entire path to the passed node and then we search for this path within the model.
	 * Nodes within the model and within the path to node are equal if name match.
	 * @param model to search for the node with same path
	 * @param node to be searched within model
	 * @return null if none found, otherwise Explorernode with same path as passed node.
	 */
	private ExplorerNode findNodeInTree(DefaultTreeModel model, ExplorerNode node) {
		ExplorerNode currentTreeNode = (ExplorerNode) model.getRoot();
		ExplorerNode[] path = node.getPath();
		for(int i=1; i<path.length; i++) {
			for(ExplorerNode n : currentTreeNode.getChildren()) {
				if(n.getName().equals(path[i].getName())) {
					currentTreeNode = n;
					break;
				}
			}
			if(!currentTreeNode.getName().equals(path[i].getName())) {
				currentTreeNode = null;
				break;
			}
		}
		return currentTreeNode;
	}

	/**
	 * Passed explorer node will be removed from tree. If parent of node has no furhter children, then parent will be removed as well and so on.
	 * @param model DefaultTreeModel to remove node from
	 * @param node ExplorerNode to be removed from model
	 */
	private void removeNodeFromTree(DefaultTreeModel model, ExplorerNode node) {
	  if(model == null || node == null) return;
		TreeNode parent = node.getParent();
		model.removeNodeFromParent(node);
		while(parent != null && parent.getChildCount() == 0 && parent.getParent() != null) {
			if(!(parent instanceof ExplorerNode)) break;
			node = (ExplorerNode)parent;
			parent = parent.getParent();
			model.removeNodeFromParent(node);
		}
	}
	
	/**
	 * Adds a node to the tree. It starts at the beginning of a nodes path and checks whether a corresponding node is already 
	 * available. If not it adds one, if yes it just proceeds. Thereof only nodes within the path that are not yet part of the tree will be added.
	 * @param model to add the node and its entire path to.
	 * @param node to be added to the TreeModel.
	 */
	private void addNodeToTree(DefaultTreeModel model, ExplorerNode node) {
		ExplorerNode[] path = node.getPath();
		assert path.length > 1;
		ExplorerNode parent = (ExplorerNode) model.getRoot();
		for (int i = 1; i < path.length; i++) {
			ExplorerNode existingNode = findNodeInTree(model, path[i]);
			if (existingNode != null) {
				parent = existingNode;
			} else {
				ExplorerNode newChild = path[i].clone();
				newChild.removeAllChildren();
				parent.addChild(newChild);
				parent = newChild;
			}
		}
	}

	/**
	 * Finds the ExplorerNode within passed tree having the same qualified name as the passed element.
	 * @param tree to search for the corresponding ExplorerNode.
	 * @param element defining qualified name for the ExplorerNode to be searched.
	 * @return null if not found, ExplorerElement if found.
	 */
	private ExplorerNode findElementInTree(JTree tree, MetaElement element) {
		String qualifiedName = element.getQualifiedName();
		ExplorerNode node = (ExplorerNode)tree.getModel().getRoot();
		ExplorerNode packageNode = null;
  	for(ExplorerNode e : node.getChildren()) {
  		if(qualifiedName.startsWith(e.getName()) && (packageNode == null || e.getName().length() > packageNode.getName().length())) {
  			packageNode = e;
  		}
  	}
  	if(packageNode == null) return null;
  	if(packageNode.getName().length() == qualifiedName.length()) return packageNode;
  	qualifiedName = qualifiedName.substring(packageNode.getName().length()+1);
  	node = packageNode;
		while(node != null && !qualifiedName.equals("")) {
			int idx = qualifiedName.indexOf(".");
			String name;
			if(idx >= 0) {
				name = qualifiedName.substring(0, idx);
			} else {
				name = qualifiedName;
			}
			node = node.findChild(name);
			if(qualifiedName.length() > name.length()) {
				qualifiedName = qualifiedName.substring(name.length()+1);
			} else {
				qualifiedName = "";
			}
		}
		return node;
	}

	/**
	 * Returns all leaves of the passed ExplorerNode. Only leaves being MetaProperties or MetaViews are passed back.
	 * @param root ExplorerNode to obtain leaves from. If it is a leaf itself then it is returned.
	 * @param checkRoot if set to true then the root is added if it is a leave otherwise the leave is not considered
	 * @param leaveType only leaves of this type are added. Set it to object if you want to obtain all leaves
	 * @return List containing all leaves being an instance of MetaProperty.
	 */
	private List<ExplorerNode> getLeaves(ExplorerNode root, boolean checkRoot, Class leaveType) {
		List<ExplorerNode> leaves = new ArrayList<ExplorerNode>();
		if(checkRoot && root.isLeaf() && root.getDataObject().getClass().equals(leaveType)) {
			leaves.add(root);
		}
		Enumeration enu = root.children();
		while (enu.hasMoreElements()) {
			ExplorerNode current = (ExplorerNode) enu.nextElement();
			if(current.isLeaf() && current.getDataObject().getClass().equals(leaveType)) {
				leaves.add(current);
			} else if (!current.isLeaf()){
				leaves.addAll(getLeaves(current, checkRoot, leaveType));
			}
		}
		return leaves;
	}
	
	private void initalizeViewProperties() {
		nameText = new JTextField(TEXT_FIELD_SIZE);
		descriptionText = new JTextArea(3, TEXT_AREA_SIZE);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		descriptionPane = new JScrollPane(descriptionText,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		visibility = new ReferenceCodeComboBox(VisibilityCode.class,
				VisibilityCode.getCodes());
	}

	private void initialize() {
		this.setPreferredSize(new Dimension(800, 600));
		
    FormLayout layout = new FormLayout("pref:grow, pref:grow", "fill:default:grow");
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);

    builder.add(createWestPanel(), cc.xy(1, 1));
    builder.add(createEastPanel(), cc.xy(2, 1));
    add(builder.getPanel());
	}

	/**
	 * END UI Sector
	 */
	private int validationCounter = 0;

	private String validateViewName(String viewName) {
		if (ValidationUtils.isBlank(viewName)) {
			return "Invalid name for this view. Please enter a non null" + "\n" + "alpha numeric string starting with a capital letter";
		} else if (!IdentifierValidator.checkJavaIdentifier(viewName)) {
			return "name: " + viewName + " is not a valid Java identifier";
		} else if (!Character.isUpperCase(viewName.charAt(0))) {
			return "name does not start with upper case";
		} else {
			return null;
		}
	}

	@Override
	public void validateView(ValidationResult result) {
		if (isValidationRequired()) {
			String nameError = validateViewName(nameText.getText());
			if ( nameError != null) {
				result.addError(nameError);
				JOptionPane.showMessageDialog(null,nameError, "validation error", JOptionPane.ERROR_MESSAGE);
			} else if (getLeaves(getViewTree().getRoot(), false, MetaProperty.class).size() <= 0 && getLeaves(getViewTree().getRoot(), false, MetaView.class).size() <= 0 ) {
				result.addError("Invalid content for this view");
				String error = "Invalid content for this view. Please add at least one field.";
				JOptionPane.showMessageDialog(null, error, "validation error",JOptionPane.ERROR_MESSAGE);
			}
		}
		super.validateView(result);
	}
	
	private ExplorerNode buildViewTreeRoot(){
    // duplicate the model, set a new owner and factory, filter out data types, stereotypes, etc.
    MetaModel model = (MetaModel)getModel().getRoot().getDataObject();
		TreeConstructor constructor = this.new ViewTreeConstructor(factory, this);
		model.accept(constructor);
		return constructor.getRoot();
	}
	
	@Override
	public void populateView() {
		validationCounter = 0;// @see validateView

		if(factory == null) {
      factory = new UiNodeTypeFactory();
      MetaViewTypeFactory typeFactory = new MetaViewTypeFactory(factory, getModel().getRoot().getNodeOwner().getManager());
      typeFactory.registerAllTypes();
		}

    westTreePanel.removeAll();
    modelTree = new ExplorerTree();
    ExplorerNode root = buildViewTreeRoot();
    modelTree.setRoot(root, root.getNodeOwner());
 		westTreePanel.add(new JScrollPane(modelTree.getTree()));
		
		UiNodeType nodeType = getNodeTypeFactory().getNodeTypeForBo(MetaView.class);
		MetaView metaView = (MetaView)getDataObject();
		originalMetaView = metaView.clone();
		ExplorerNode viewNode = new ExplorerNode(nodeType, metaView, metaView);
		viewNode.setNodeOwner(this);
		
		// create view Tree
		createViewTree();
		viewTree.setRoot(viewNode, viewNode.getNodeOwner());
		removeNodeFromTree(modelTree.getTreeModel(), findElementInTree(modelTree.getTree(), metaView));
		for(MetaProperty field : metaView.getFields()) {
			ExplorerNode eNode = findElementInTree(modelTree.getTree(), field);
			if(eNode != null) {
				nodeToTree(modelTree.getTree(), viewTree.getTree(), eNode);
			}
		}
		for(MetaView view : metaView.getViews()) {
			ExplorerNode eNode = findElementInTree(modelTree.getTree(), view);
			if(eNode != null) {
				nodeToTree(modelTree.getTree(), viewTree.getTree(), eNode);
			}
		}
    loadFieldValues(metaView);

		JTree tree = viewTree.getTree();
		viewTreePanel.removeAll();
		viewTreePanel.add(new JScrollPane(tree));
    // now expand the entire view
    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }

    rootObjectListPanel.removeAll();
    List<UiField> params = new ArrayList<UiField>();
    params.add(new UiField("name", String.class, false));
    params.add(new UiField("description", String.class, false));
    NodeTableView rootObjectList = new NodeTableView(params);
    //rootObjectList.setModel(null); // TODO add root objects here (obtain them from metaView)
    rootObjectListPanel.add(rootObjectList);
    
	}

	/**
	 * Overrides setViewEditable from the framework
	 */
	@Override
	public void setViewEditable(boolean editable) {
		this.buttonSelectedPropertiesToLeft.setEnabled(editable);
		this.buttonSelectedPropertiesToRight.setEnabled(editable);
		this.nameText.setEditable(editable);
		this.descriptionText.setEditable(editable);
		this.descriptionText.setEnabled(editable);
		this.descriptionPane.setEnabled(editable);
		this.visibility.setEditable(editable);
		this.visibility.setEnabled(editable);
    this.buttonRootObjectToLeft.setEnabled(editable);
    this.buttonRootObjectToRight.setEnabled(editable);
	}

	/**
	 * overrides cancelChanges() from the framework
	 */
	@Override
	public void cancelChanges() {
		// TODO Auto-generated method stub
	}

	/**
	 * Removes the properties no longer contained in this view.
	 * The difference is computed by comparing the originalView (a clone made when we populated this view) and the passed newView.
	 * @param newView to check for removed properties 
	 */
	private void removeDeletedPropertiesFromView(MetaView newView) {
		// determine the difference between original MetaView and passed MetaView.
		List<MetaProperty> propertiesToBeRemoved = new ArrayList<MetaProperty>();
		for(MetaProperty originalField : getOriginalMetaView().getFields()) {
			MetaProperty field = newView.findField(originalField);
			if(field == null) {
				propertiesToBeRemoved.add(originalField);
			}
		}
		//now remove our view content from properties within list (toBeRemoved)
		newView.removeFromProperties(propertiesToBeRemoved, getQualifiedName(newView), (MetaModel)getModel().getRoot().getDataObject());
	}

	/**
	 * The original view is removed entirely from the model
	 * @param newView current MetaView.
	 */
	private void removeViewWithFormerName(MetaView newView) {
		getOriginalMetaView().removeFromPackage(getQualifiedName(getOriginalMetaView()), (MetaModel)getModel().getRoot().getDataObject(), getContext(getOriginalMetaView()));
		getOriginalMetaView().removeFromProperties(getOriginalMetaView().getFields(), getQualifiedName(getOriginalMetaView()), (MetaModel)getModel().getRoot().getDataObject());
	}

	/**
	 * Adds newly assigned properties and views. TaggedValues of those properties newly assigned to this view are added.
	 * Properties having been already part of the view before editing, are updated.
	 * @param newView view having additional or changed properties assigned (compared to getOriginalMetaView())
	 */
	private void addNewOrChangedPropertiesAndViewsToView(MetaView newView) {
		newView.addToPackage(getQualifiedName(newView), (MetaModel)getModel().getRoot().getDataObject(), getContext(newView));
		newView.addToProperties(newView.getFields(), getQualifiedName(newView), (MetaModel)getModel().getRoot().getDataObject());
	}

	/**
	 * overrides commitChanges from the framework
	 */
	@Override
	public void commitChanges() {
		MetaView newView = (MetaView) getDataObject();
		String viewName = nameText.getText();
		newView.setName(viewName.trim());
		newView.setVisibility((VisibilityCode) visibility.getSelectedCode());
		newView.setDescription(descriptionText.getText());
		setFieldsToMetaView(newView, getViewTree().getRoot());

		// In case of this view has been renamed, we have to remove the "old" view before adding the "new" one.
		// Because the new view may have less properties than the existing one - the easiest way is to remove the old view and add the new one from scratch (rather than building the difference)
		removeViewWithFormerName(newView);
		// add tagged values - values being part of this view before modification will be updated as well.
		addNewOrChangedPropertiesAndViewsToView(newView);
		
		getModel().nodeChanged();
		this.originalMetaView = null;
	}

	/*
	 * END overriden method from the framework
	 */

	/*
	 * HELPER methods section
	 */
	
	/**
	 * Helper member to obtain qualified name of a new view. This helper is necessary because in case of a newly created view the context is not yet set during commit.
	 * We thereof obtain the parent ExplorerNode of this MetaView and construct the qualified name with help of that.
	 * @param view MetaView to obtain the qualified name from.
	 * @return qualified name. If for some reason there is no parent ExplorerNode or if it is of wrong type, then the name of the view is returned. 
	 */
	private String getQualifiedName(MetaView view) {
		if(view.getContext() == null && getModel().getParent() != null) {
			ExplorerNode parent = ((ExplorerNode)getModel().getParent());
			if(parent.getDataObject() instanceof MetaPackage && !(parent.getDataObject() instanceof MetaModel)) {
				return ((MetaPackage)parent.getDataObject()).getQualifiedName()+"."+view.getName();
			}
		}
		return view.getQualifiedName();
	}
	
	/**
	 * Helper member to obtain the enclosing MetaPackage of a view. This helper is necessary because in case of a newly created view the context is not yet set during commit.
	 * We thereof obtain the parent ExplorerNode of this MetaView and take the MetaPackage from there. If a context is set on the view then this one is returned.
	 * @param view MetaView to obtain the context from.
	 * @return MetaPackage the view belongs to is returned. If none available then null is returned. 
	 */
	private MetaPackage getContext(MetaView view) {
		if(view.getContext() == null && getModel().getParent() != null) {
			ExplorerNode parent = ((ExplorerNode)getModel().getParent());
			if(parent.getDataObject() instanceof MetaPackage) {
				return ((MetaPackage)parent.getDataObject());
			}
		}
		return (MetaPackage)view.getContext();
	}
	
	private MetaView getOriginalMetaView() {
		return this.originalMetaView;
	}

	/**
	 * if an existing view is loaded, load the name, description and the
	 * visibility
	 * @param metaView to load MetaViewView ui elements from
	 */
	private void loadFieldValues(MetaView metaView) {
	  if(metaView == null) return;
		nameText.setText(metaView.getName());
		descriptionText.setText(metaView.getDescription());
		visibility.setSelectedCode(metaView.getVisibility());
	}

  /**
   * Adds all properties and MetaViews within the tree of the root ExplorerNode to the passed metaView. Previously stored fields are cleared.
   * @param metaView to add properties. Properties are added as MetaField.FieldDefinition
   * @param root identifies the ExplorerNode tree, we want to add MetaProperties from.
   */
	private void setFieldsToMetaView(MetaView metaView, ExplorerNode root) {
		List<ExplorerNode> properties = getLeaves(root, false, MetaProperty.class);
		metaView.getFields().clear();
		for(ExplorerNode node : properties) {
			MetaProperty field = (MetaProperty)node.getDataObject();
			metaView.appendField(field);
		}

		properties = getLeaves(root, false, MetaView.class);
		metaView.removeAllViews();
		for(ExplorerNode node : properties) {
			metaView.addView((MetaView)node.getDataObject());
		}
	}
	
	/**
	 * Validation is only required on commitChanges event, not if the view populated.
	 * this is a work around, better is implements more validation events like:
	 * displayValidation
	 * commitValidation
	 * ont the framework
	 * @return true if not during populateView process.
	 */
	private boolean isValidationRequired(){
		return ++validationCounter > 1;
	}

	/**
	 * END Helper methods section
	 */
	
	private class ViewTreeConstructor extends TreeConstructor {
		public ViewTreeConstructor(UiNodeTypeFactory factory, ExplorerNodeOwner explorerNodeOwner) {
			super(factory, explorerNodeOwner);
		}

		//don't display datatypes, uses cases, sterotypes and taggedvalues in the View-UI
		@Override public void visit(MetaDatatype datatype) {}
		@Override public void visit(MetaUseCase metaUseCase) {}
		@Override public void visit(Stereotype stereotype) {}
		@Override public void visit(TaggedValue taggedValue) {}
		
	}

	@Override
	public DataObjectMgr getManager() {
		return getModel().getNodeOwner().getManager();
	}

  @Override
	public UiNodeTypeFactory getNodeTypeFactory() {
		return factory;
	}

  @Override
	public JTree getTree() {
		return getModel().getNodeOwner().getTree();
	}

  @Override
	public void nodeChanged(ExplorerNode node) {
		// TODO Auto-generated method stub

	}

  @Override
	public void nodeInserted(ExplorerNode node, int index) {
    int[] childIndices = new int[1];
    childIndices[0] = index;
    DataObject rootObject = node.getRoot().getDataObject();
    if(rootObject == null || rootObject instanceof MetaView) {
      if(viewTree != null && viewTree.getTree() != null) {
        viewTree.getTreeModel().nodesWereInserted(node, childIndices);
      }
    }
    if(rootObject == null || rootObject instanceof MetaModel) {
      if(modelTree != null && modelTree.getTreeModel() != null) {
        modelTree.getTreeModel().nodesWereInserted(node, childIndices);
      }
    }
	}

  @Override
	public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
		// TODO Auto-generated method stub
	}

}
