package ch.bbv.mda.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dog.ui.ReferenceCodeComboBox;
import ch.bbv.explorer.*;
import ch.bbv.mda.*;
import ch.bbv.mda.uml.MetaUseCase;
import ch.bbv.mda.validation.IdentifierValidator;
import ch.persi.xml.XmlNode;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;

public class MetaViewView extends NodePropertiesView implements
		ExplorerNodeOwner, ViewConstants {

	/**
	 * serialUID, change this value on each check-in/changeing this class
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
	 * Pattern for validating identifiers
	 */
	static Pattern validName;

	private JTextField nameText;

	private JTextArea descriptionText;

	private JScrollPane descriptionPane;

	private ReferenceCodeComboBox visibility;

	private JScrollPane westScrollPane;

	private JPanel eastPanel;

	private JPanel centerPanel;

	private JPanel westTree;

	private JScrollPane eastViewTree;

	private GridBagConstraints eastTreeConstraints;

	private JButton buttonToRight;

	private JButton buttonToLeft;

	private ExplorerTree viewTree;

	private MetaView originalMetaView;

	private ExplorerTree attributeTree;

	/**
	 * default constructor
	 */
	public MetaViewView() {
		initialize();
	}

	/**
	 * the UI Sector
	 */

	private void initWestPanel() {
		westScrollPane = createAttributeTree();
	}

	private JTree getAttributeTree() {
		return attributeTree.getTree();
	}

	private ExplorerTree getViewTree() {
		return viewTree;
	}

	private JTree createViewTree() {
		viewTree = new ExplorerTree();
		return viewTree.getTree();
	}

	private JScrollPane createAttributeTree() {
		attributeTree = new ExplorerTree();
		JScrollPane panel = new JScrollPane(attributeTree.getTree());
		panel.setPreferredSize(new Dimension(400, 400));
		return panel;
	}

	private void initEastPanel() {
		eastPanel = new JPanel(new BorderLayout());
		eastPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		// view tree
		eastViewTree = new JScrollPane(createViewTree());
		eastTreeConstraints = new GridBagConstraints();
		eastTreeConstraints.gridx = 0;
		eastTreeConstraints.gridy = 0;
		eastTreeConstraints.gridwidth = GridBagConstraints.HORIZONTAL;
		eastTreeConstraints.fill = GridBagConstraints.BOTH;
		eastTreeConstraints.weighty = 1.0;
		eastTreeConstraints.weightx = 1.0;

		// add tree
		getEastPanel().add(eastViewTree, BorderLayout.CENTER);

		// add properties form
		getEastPanel().add(createPropertiesForm(), BorderLayout.SOUTH);
	}

	private JPanel createPropertiesForm() {
		initalizeFields();
		// FormLayout layout = new FormLayout(
		// "right:pref, 3dlu, pref, 4dlu, right:pref, 3dlu, pref");
		FormLayout layout = new FormLayout(
				"center:pref, 4dlu, left:pref, 3dlu", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.appendSeparator("General");
		builder.append("&Name", nameText);
		builder.append("&Description", descriptionPane, 2);
		builder.appendSeparator("Modifiers");
		builder.append("&Modifier", visibility);

		return builder.getPanel();
	}

	private void initCenterPanel() {
		FormLayout layout = new FormLayout("center:pref, 10dlu", "pref, 100dlu");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.nextRow(2);
		buttonToRight = getButtonToRight();
		buttonToLeft = getButtonToLeft();
		builder.append(buttonToRight);
		builder.append(buttonToLeft);
		centerPanel = builder.getPanel();
	}

	/**
	 * this map contains the attribute to package mapping. the key is the
	 * attribte name the value is the package name
	 */
	private Map<String, String> fields;

	private JButton getButtonToRight() {
		JButton b = new JButton(">>");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean mustRemove = true;
				boolean isPackage = false;
				ExplorerNode node = (ExplorerNode) getAttributeTree().getLastSelectedPathComponent();
				if (node.getDataObject().getClass().equals(MetaModel.class)){
					JOptionPane.showMessageDialog(null,"adding the full model to the view is not allowed", "error", 
							JOptionPane.ERROR_MESSAGE);
					return; //
				} else if (node.getDataObject().getClass().equals(MetaPackage.class)){
					JOptionPane.showMessageDialog(null,"adding a full package to the view is not allowed", "error", 
							JOptionPane.ERROR_MESSAGE);
					return; //add complett packages is also not allowed
				}
				
				
				if (fields == null) {
					fields = new HashMap<String, String>();
				}

				if (node == null) {
					// no node is selected
					return;
				}
				ExplorerNode originalNode = node.clone();
				ExplorerNode parent = (ExplorerNode) node.getParent();
				ExplorerNode root = (ExplorerNode) node.getRoot();
				ExplorerNode packageNode = getPackage(node, node.getRoot());
				fields.put(node.getName(), packageNode.getName());

				if (!parent.equals(root)) {
					isPackage = true;
					// search full path
					if (parent.getChildCount() > 1) {
						ExplorerNode cloneOfParent = parent.clone();
						ExplorerNode[] nodesToRemove = new ExplorerNode[cloneOfParent
								.getChildCount()];
						int x = 0;
						for (Iterator<ExplorerNode> i = cloneOfParent
								.getChildren().iterator(); i.hasNext(); x++) {
							ExplorerNode item = i.next();
							if (!item.getName().equals(node.getName())) {
								nodesToRemove[x] = item;
							}
						}
						for (int i = 0; i < nodesToRemove.length; i++) {
							if (nodesToRemove[i] != null)
								nodesToRemove[i].removeFromParent();
						}
						node = cloneOfParent;
					} else {
						node = parent;
						originalNode = parent;
					}
				}

				DefaultTreeModel attributeModel = (DefaultTreeModel) getAttributeTree()
						.getModel();
				if (mustRemove) {
					attributeModel.removeNodeFromParent(originalNode);
				}
				ExplorerNode rootNodeView = (ExplorerNode) getViewTree()
						.getTree().getModel().getRoot();
				DefaultTreeModel model = (DefaultTreeModel) getViewTree()
						.getTree().getModel();
				if (isPackage) {
					ExplorerNode viewTreeRoot = (ExplorerNode) model.getRoot();
					Enumeration enu = viewTreeRoot.asTreeNode().children();
					boolean found = false;
					while (enu != null && enu.hasMoreElements()) {
						ExplorerNode current = (ExplorerNode) enu.nextElement();
						if (current.equals(parent)) {
							for (Iterator<ExplorerNode> nodes = node
									.getChildren().iterator(); nodes.hasNext();) {
								model.insertNodeInto(nodes.next(), current,
										current.getChildCount());
							}
							found = true;
							break;
						}
					}
					if (!found) {
						model.insertNodeInto(node, rootNodeView, rootNodeView
								.getChildCount());
					}
				} else {
					model.insertNodeInto(node, rootNodeView, rootNodeView
							.getChildCount());
				}
				getViewTree().getTree().expandRow(0);
			}
		});
		return b;
	}

	private JButton getButtonToLeft() {
		JButton b = new JButton("<<");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean mustRemove = true;
				boolean isPackage = false;
				ExplorerNode node = (ExplorerNode) getViewTree().getTree()
						.getLastSelectedPathComponent();
				if (node == null) {
					// no node selected
					return;
				}
				ExplorerNode originalNode = node.clone();
				ExplorerNode parent = (ExplorerNode) node.getParent();
				ExplorerNode root = (ExplorerNode) node.getRoot();
				fields.remove(node.getName());

				if (!parent.equals(root)) {
					isPackage = true;
					// search full path
					if (parent.getChildCount() > 1) {
						ExplorerNode cloneOfParent = parent.clone();
						ExplorerNode[] nodesToRemove = new ExplorerNode[cloneOfParent
								.getChildCount()];
						int x = 0;
						for (Iterator<ExplorerNode> i = cloneOfParent
								.getChildren().iterator(); i.hasNext(); x++) {
							ExplorerNode item = i.next();
							if (!item.getName().equals(node.getName())) {
								nodesToRemove[x] = item;
							}
						}
						for (int i = 0; i < nodesToRemove.length; i++) {
							if (nodesToRemove[i] != null)
								nodesToRemove[i].removeFromParent();
						}
						node = cloneOfParent;
					} else {
						node = parent;
						originalNode = parent;
					}
				}

				DefaultTreeModel viewModel = (DefaultTreeModel) getViewTree()
						.getTree().getModel();
				if (mustRemove) {
					viewModel.removeNodeFromParent(originalNode);
				}

				ExplorerNode rootNodeAttribute = (ExplorerNode) getAttributeTree()
						.getModel().getRoot();
				DefaultTreeModel model = (DefaultTreeModel) getAttributeTree()

				.getModel();
				if (isPackage) {
					ExplorerNode attributeTreeRoot = (ExplorerNode) model
							.getRoot();
					Enumeration enu = attributeTreeRoot.asTreeNode().children();
					boolean found = false;
					while (enu != null && enu.hasMoreElements()) {
						ExplorerNode current = (ExplorerNode) enu.nextElement();
						if (current.equals(parent)) {
							for (Iterator<ExplorerNode> nodes = node
									.getChildren().iterator(); nodes.hasNext();) {
								model.insertNodeInto(nodes.next(), current,
										current.getChildCount());
							}
							found = true;
							break;
						}
					}
					if (!found) {
						model.insertNodeInto(node, rootNodeAttribute,
								rootNodeAttribute.getChildCount());
					}
				} else {
					model.insertNodeInto(node, rootNodeAttribute,
							rootNodeAttribute.getChildCount());
				}

				getAttributeTree().expandRow(0);
			}
		});
		return b;
	}

	private void initalizeFields() {
		nameText = new JTextField(TEXT_FIELD_SIZE);
		// nameText.addFocusListener(new VerificationActions());
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
		this.setPreferredSize(new Dimension(600, 600));
		GridBagLayout gb = new GridBagLayout();
		this.setLayout(gb);
		initCenterPanel();
		initWestPanel();
		initEastPanel();

		// build tree for the west part
		westTree = new JPanel();
		westTree.setLayout(new BorderLayout());
		westTree.add(getWestPanel());
		Insets myInsets = new Insets(2, 5, 2, 5);
		GridBagConstraints cc = new GridBagConstraints();
		cc.gridx = 0;
		cc.gridy = 0;
		cc.gridwidth = 1;
		cc.gridheight = GridBagConstraints.REMAINDER; 
		cc.weightx = 1.0;
		cc.weighty = 1.0;
		cc.anchor = GridBagConstraints.LINE_END;
		cc.fill = GridBagConstraints.BOTH;
		cc.insets = myInsets;
		cc.ipadx = 0;
		cc.ipady = 0;
		add(westTree, cc);

		// add middel buttons
		cc.gridx = 1;
		cc.gridy = 0;
		cc.insets = new Insets(2, 41, 2, 5);
		add(getCenterPanel(), cc);
		
		cc.insets = myInsets;
		// add tree on the east
		cc.gridx = 2;
		cc.gridy = 0;
		add(getEastPanel(), cc);

		validName = Pattern.compile("^[0-9\\.]" // no number and no dot at the
				// beginning of the identifier
				+ "|[^\\w&&[^\\.]]" // no special characters other than dot
				+ "|\\.[0-9]"); // no number after a dot
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
			String nameError = this.validateViewName(nameText.getText());
			if ( nameError != null) {
				result.addError(nameError);
				JOptionPane.showMessageDialog(null,nameError, "validation error", JOptionPane.ERROR_MESSAGE);
				super.validateView(result);
				return;//do not also check the other condition if a error found
			}
			if (this.fields == null || fields.isEmpty()) {
				result.addError("Invalid content for this view");
				String error = "Invalid content for this view. Please select one or more fields";
				JOptionPane.showMessageDialog(null, error, "validation error",JOptionPane.ERROR_MESSAGE);
			}
		}
		super.validateView(result);
	}
	
	private ExplorerNode buildViewTreeRoot(){
		ExplorerNode root = getModel().getRoot().clonebyCopy(null);
		MetaModel model = (MetaModel)root.getDataObject();
		TreeConstructor constructor = this.new ViewTreeConstructor(this.getNodeTypeFactory(), this);
		model.accept(constructor);
		return constructor.getRoot();
	}
	
	/**
	 * Overriden methods from the framework
	 */
	@Override
	public void populateView() {
		validationCounter = 0;// @see validateView
		// TODO this is a hack mb, hack is required while in swing add is not
		// add and replace
		westTree.remove(getWestPanel());
		getEastPanel().remove(eastViewTree);
		ExplorerNode root = buildViewTreeRoot();

		attributeTree.setRoot(root, this);
		westScrollPane = new JScrollPane(attributeTree.getTree());
		westTree.add(getWestPanel());
		UiNodeType nodeType = getNodeTypeFactory().getNodeTypeForBo(MetaView.class);
		ExplorerNode node = null;
		if (isExistingView() || isViewEvent()) {
			// edit existing view
			MetaView metaView = (MetaView) this.getDataObject();
			originalMetaView = metaView.clone();
			loadFieldValues(metaView);
			String viewName = this.getNode().getName();
			assert viewName != null;
			node = new ExplorerNode(nodeType, metaView, metaView);
			node.setNodeOwner(root.getNodeOwner());
			List<ExplorerNode> x = getNodesForView(this.getNode().getParent().toString()
					+ "." + viewName, root);
			for (ExplorerNode childNode : x) {
				node.addChild(childNode);
			}
		} else {
			// create new view
			node = new ExplorerNode(nodeType, new MetaView(), new MetaView());
			nameText.setText(null);
			descriptionText.setText(null);
		}

		viewTree = new ExplorerTree();
		viewTree.setRoot(node, this);
		JTree tree = viewTree.getTree();
		eastViewTree = new JScrollPane(tree);
		getEastPanel().add(eastViewTree, BorderLayout.CENTER);

		if (isViewEvent()) {
			for (int i = 0; i < tree.getRowCount(); i++) {
				tree.expandRow(i);
			}
		}

		if (nodes != null && !nodes.isEmpty()) {
			nodes.clear();
			// nodes = null;
		}

	}

	/**
	 * Overrides setViewEditable from the framework
	 */
	@Override
	public void setViewEditable(boolean editable) {
		if (!editable) {
			this.buttonToLeft.setEnabled(false);
			this.buttonToRight.setEnabled(false);
			this.nameText.setEditable(false);
			this.descriptionText.setEditable(false);
			this.descriptionText.setEnabled(false);
			this.descriptionPane.setEnabled(false);
			this.visibility.setEditable(false);
			this.visibility.setEnabled(false);
		}
	}

	/**
	 * overrides cancelChanges() from the framework
	 */
	@Override
	public void cancelChanges() {
		// TODO Auto-generated method stub
	}

	/**
	 * overrides commitChanges from the framework
	 */
	@Override
	public void commitChanges() {
		MetaView model = (MetaView) getDataObject();
		String viewName = nameText.getText();
		model.setName(viewName.trim());
		model.setVisibility((VisibilityCode) visibility.getSelectedCode());
		model.setDescription(descriptionText.getText());
		addFieldsToMetaView(model, null);

		MetaModel rootModel = (MetaModel) getModel().getRoot().getDataObject();
		if (isExistingView()) {
			if (model.equals(getOriginalMetaView())) {
				return; // no changes
			}

			// handle case: chance name or visibility
			if (!getOriginalMetaView().getName().equals(model.getName())
					|| getOriginalMetaView().getVisibility().getCode() != model.getVisibility().getCode()) 
			{
				updateTaggedValues(getOriginalMetaView(), model.getName(),
						model.getVisibility().getCode(), rootModel);
			}

			// handle case: remove fields from view
			if (getOriginalMetaView().hasMoreFields(model.getFields())
					|| getOriginalMetaView().hasOtherFields(model.getFields())) 
			{
				removeView(getOriginalMetaView(), model, rootModel);
			}
		}

		// tagsView.commitChanges();
		for (Iterator<MetaView.FieldDefinition> i = model.getFields().iterator(); i.hasNext();) {
			MetaView.FieldDefinition cur = i.next();
			MetaElement el = rootModel.findElement(cur.getFullyQualifiedName());
			XmlNode xmlDef = null;
			boolean update = false;
			if (el.getTaggedValue(VIEW_TAG) != null) {
				boolean alreadyExists = false;
				xmlDef = new XmlNode();
				xmlDef.parse(el.getTaggedValue(VIEW_TAG).getValue());
				List<XmlNode> nodes = xmlDef.getAllAsList(VIEW_NODE);
				for (int x = 0, l = nodes.size(); x < l; x++) {
					XmlNode view = nodes.get(x);
					if (view.getAttribute(NAME).equals(
							this.node.getParent().toString() + "."
									+ model.getName())) {
						// view is exists
						alreadyExists = true;
						break;
					}
				}
				if (alreadyExists) {
					continue;
				}
				update = true;
			} else {
				xmlDef = new XmlNode(ROOT_NODE);
			}
			XmlNode viewNode = xmlDef.append(VIEW_NODE);
			viewNode.setAttribute(NAME, this.node.getParent().toString() + "." + model.getName());
			viewNode.setAttribute(DESCRIPTION, model.getDescription());
			viewNode.setAttribute(VISIBILITY, Integer.toString(visibility.getSelectedCode().getCode()));
			if (update) {
				el.getTaggedValue(VIEW_TAG).setValue(xmlDef.asXML());
			} else {
				el.addTaggedValue(new TaggedValue(VIEW_TAG, xmlDef.asXML()));
			}
		}
		model.clearFields();
		getModel().nodeChanged();
		if (fields != null) {
			fields.clear();
		}
		this.originalMetaView = null;
	}

	/**
	 * END overriden method from the framework
	 */

	/**
	 * HELPER methods section
	 */
	private MetaView getOriginalMetaView() {
		return this.originalMetaView;
	}

	/**
	 * returns true if the display event == view
	 */
	private boolean isViewEvent() {
		return this.isEditable() == false && this.isNewObject() == false;
	}

	/**
	 * returns true if the view not new and editable
	 */
	private boolean isExistingView() {
		return (!this.isNewObject()) && this.isEditable();
	}

	/**
	 * returns all nodes for the given view, founded in the given tree model.
	 * The method travers the complett tree and search for view definitions with
	 * the name given by argument view
	 */
	private List<ExplorerNode> nodes;

	private List<ExplorerNode> getNodesForView(String view,
			ExplorerNode attributeTree) {
		if (nodes == null) {
			nodes = new ArrayList<ExplorerNode>();
		}
		for (ExplorerNode child : attributeTree.getChildren()) {
			if (child.getDataObject().getClass().equals(MetaProperty.class)
					|| child.getDataObject().getClass().equals(MetaIndexedProperty.class)) {
				MetaProperty property = (MetaProperty) child.getDataObject();
				if (property.getTaggedValue(VIEW_TAG) != null) {
					XmlNode def = new XmlNode();
					def.parse(property.getTaggedValue(VIEW_TAG).getValue());
					for (Iterator<XmlNode> views = def.getAll(VIEW_NODE); views.hasNext();) {
						XmlNode viewNode = views.next();
						if (viewNode.getAttribute(NAME).equals(view)) {
							ExplorerNode realclassNode = (ExplorerNode) child.getParent();
							ExplorerNode classNode = realclassNode.clone();
							List<Integer> indexes = new ArrayList<Integer>();
							for (int i = 0; i < classNode.getChildCount(); i++) {
								ExplorerNode c = (ExplorerNode) classNode
										.getChildAt(i);
								if (!c.getName().equals(child.getName())) {
									indexes.add(new Integer(i));
								}
							}

							// the follwoing code is requried while
							// classNode.remove(index) changes the index length
							// and then a ArrayIndexOutOfBoundsException is
							// thrown
							boolean first = true;
							for (int i = 0, l = indexes.size(); i < l; i++) {
								if (first) {
									classNode.remove(indexes.get(i).intValue());
									first = false;
								} else {
									int index = indexes.get(i).intValue();
									classNode.remove(index - i);
								}
							}
							addToOriginalMetaView(classNode);

							// in the case from one class are two or more
							// attributes are used in the view. its required the
							// class
							// only one time
							ExplorerNode n = retrievNode(classNode.getName(),
									nodes);
							if (n != null) {
								nodes.remove(n);
								nodes.add(mergeNodes(n, classNode));
							} else {
								nodes.add(classNode);
							}
							putToFields(child.getName(), classNode.getParent().toString());
						}
					}
				}
			}
			getNodesForView(view, child);
		}
		return nodes;
	}

	private ExplorerNode retrievNode(String name, List<ExplorerNode> lst) {
		for (ExplorerNode node : lst) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * if a class has two or more attributes in the view, its required display
	 * the class only one time with two attributes instead of two times the same
	 * class with one attribute. This method merge two class nodes. All
	 * attributes from the source nodes will be added to the target node.
	 */
	private ExplorerNode mergeNodes(ExplorerNode target, ExplorerNode source) {
		for (ExplorerNode c : source.getChildren()) {
			target.addChild(c);
		}
		return target;
	}

	/**
	 * build the OriginalMetaView for supporting the commitChanges method
	 */
	private void addToOriginalMetaView(ExplorerNode classNode) {
		for (ExplorerNode field : classNode.getChildren()) {
			MetaView.FieldDefinition def = new MetaView.FieldDefinition(field.getName(), 
					classNode.getParent().toString(), "." + getClassName(field), (MetaProperty)field.getDataObject());
			getOriginalMetaView().appendField(def);
		}

	}

	/**
	 * if we load a existing view, build up the fields map, these map is
	 * required for the commitChanges and there helper methods.
	 */
	private void putToFields(String name, String packageName) {
		if (this.fields == null) {
			this.fields = new HashMap<String, String>();
		}
		this.fields.put(name, packageName);
	}

	/**
	 * returns the package for the given class or attribute node
	 */
	private ExplorerNode getPackage(TreeNode current, TreeNode root) {
		if (current.getParent().equals(root)) {
			return (ExplorerNode) current;
		} else {
			return getPackage(current.getParent(), root);
		}
	}

	/**
	 * if an existing view is loaded, load the name, description and the
	 * visibility
	 */
	private void loadFieldValues(MetaView metaView) {
		nameText.setText(metaView.getName());
		descriptionText.setText(metaView.getDescription());
		visibility.setSelectedCode(metaView.getVisibility());
	}

	/**
	 * if in edit mode fields remove from the view, this method removes the
	 * reference in the tagged value.
	 */
	private void removeView(MetaView originalView, MetaView newView,
			MetaModel rootModel) {
		List<MetaView.FieldDefinition> defs = originalView.getDelta(newView
				.getFields());
		for (MetaView.FieldDefinition def : defs) {
			MetaElement el = rootModel.findElement(def.getFullyQualifiedName());
			if (el.getTaggedValue(VIEW_TAG) != null) {
				XmlNode xmlDef = new XmlNode();
				xmlDef.parse(el.getTaggedValue(VIEW_TAG).getValue());
				List<XmlNode> nodes = xmlDef.getAllAsList(VIEW_NODE);
				boolean hasRemoved = false;
				for (int x = 0, l = nodes.size(); x < l; x++) {
					XmlNode view = nodes.get(x);
					if (view.getAttribute(NAME).equals(def.getPackage() + "." + newView.getName())) {
						view.remove();
						hasRemoved = true;
					}
				}
				// if the field are used only in one view, the tagged value is
				// not longer required
				if (nodes.size() == 1 && hasRemoved) {
					el.removeTaggedValue(VIEW_TAG);
				}
			}
		}
	}

	/**
	 * in the case of name or visibility code is changed, this method update all
	 * existing tagged values for the renamed view
	 */
	private void updateTaggedValues(MetaView originalView, String newName,
			int newCode, MetaModel rootModel) {
		List<MetaView.FieldDefinition> defs = originalView.getFields();
		for (MetaView.FieldDefinition def : defs) {
			MetaElement el = rootModel.findElement(def.getFullyQualifiedName());
			if (el.getTaggedValue(VIEW_TAG) != null) {
				XmlNode xmlDef = new XmlNode();
				xmlDef.parse(el.getTaggedValue(VIEW_TAG).getValue());
				List<XmlNode> nodes = xmlDef.getAllAsList(VIEW_NODE);
				for (int x = 0, l = nodes.size(); x < l; x++) {
					XmlNode view = nodes.get(x);
					if (view.getAttribute(NAME).equals(def.getPackage() + "." + originalView.getName())) {
						// view.remove();
						view.removeAttribute(NAME);
						view.setAttribute(NAME, def.getPackage() + "." + newName);
						view.removeAttribute(VISIBILITY);
						view.setAttribute(VISIBILITY, Integer.toString(newCode));
						el.getTaggedValue(VIEW_TAG).setValue(xmlDef.asXML());
					}
				}
			}
		}
	}

	/**
	 * adding the selectet fields to the view.
	 */
	private String packageName;

	private void addFieldsToMetaView(MetaView model, ExplorerNode root) {
		if (root == null) {
			root = (ExplorerNode) getViewTree().getTreeModel().getRoot();
		}

		Enumeration enu = root.children();
		while (enu.hasMoreElements()) {
			ExplorerNode current = (ExplorerNode) enu.nextElement();
			if (current.getDataObject().getClass().equals(MetaPackage.class)) {
				packageName = current.toString();
			}
			if (current.getDataObject().getClass().equals(MetaProperty.class)
					|| current.getDataObject().getClass().equals(MetaIndexedProperty.class)) {
				String clazz = "." + current.getParent().toString();
				MetaProperty metaE = (MetaProperty)current.getDataObject();
				MetaView.FieldDefinition def = new MetaView.FieldDefinition(current.getName(), packageName, clazz, metaE);
				model.appendField(def);
			}
			if (current.getChildCount() > 0) {
				List<ExplorerNode> childs = current.getChildren();
				for (Iterator<ExplorerNode> iter = childs.iterator(); iter.hasNext();) {
					ExplorerNode el = iter.next();
					if (el.getDataObject().getClass().equals(MetaProperty.class)
							|| el.getDataObject().getClass().equals(MetaIndexedProperty.class)) {
						String clazz = "." + getClassName(current);
						packageName = fields.get(el.getName()).toString();
						MetaProperty metaE = (MetaProperty)el.getDataObject();
						MetaView.FieldDefinition def = new MetaView.FieldDefinition(el.getName(), packageName, clazz, metaE);
						model.appendField(def);
					}
					addFieldsToMetaView(model, el);
				}
			}
		}
	}

	/**
	 * returns the class name for a current attribute node
	 */
	private String getClassName(TreeNode current) {
		ExplorerNode n = (ExplorerNode) current;
		if (n.getDataObject().getClass().equals(MetaClass.class)) {
			return current.toString();
		} else {
			return getClassName(n.getParent());
		}
	}

	private JScrollPane getWestPanel() {
		return westScrollPane;
	}

	private JPanel getEastPanel() {
		return eastPanel;
	}

	private JPanel getCenterPanel() {
		return centerPanel;
	}
	
	/**
	 * Validation is only required on commitChanges event, not if the view populated.
	 * this is a work around, better is implements more validation events like:
	 * displayValidation
	 * commitValidation
	 * ont the framework
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

		//don't display datatypes, uses cases, views, sterotypes and taggedvalues in the View-UI
		@Override public void visit(MetaDatatype datatype) {}
		@Override public void visit(MetaUseCase metaUseCase) {}
		@Override public void visit(MetaView metaView) {}
		@Override public void visit(Stereotype stereotype) {}
		@Override public void visit(TaggedValue taggedValue) {}
		
	}
	
	

	// ExploerNode interface, do not comment this methods, javadoc taken the
	// comments from the interface
	public DataObjectMgr getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public UiNodeTypeFactory getNodeTypeFactory() {
		return getModel().getNodeOwner().getNodeTypeFactory();
	}

	public JTree getTree() {
		return getModel().getNodeOwner().getTree();
	}

	public void nodeChanged(ExplorerNode node) {
		// TODO Auto-generated method stub

	}

	public void nodeInserted(ExplorerNode node, int index) {
		// TODO Auto-generated method stub

	}

	public void nodeRemoved(ExplorerNode parent, ExplorerNode node, int index) {
		// TODO Auto-generated method stub

	}

}
