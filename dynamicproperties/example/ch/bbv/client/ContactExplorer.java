/* 
 * copyright 2000-2006 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */
package ch.bbv.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.bbv.Contact;
import ch.bbv.RootClass;
import ch.bbv.VisitorFunctor;
import ch.bbv.application.Application;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.DataObjectId;
import ch.bbv.dog.DataObjectMgr;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.Property;
import ch.bbv.dynamicproperties.core.PropertyType;
import ch.bbv.dynamicproperties.objecteditor.DynamicPropertyView;
import ch.bbv.dynamicproperties.pmmda.DynamicPropertyFunctor;
import ch.bbv.explorer.Explorer;
import ch.bbv.explorer.ExplorerDocument;
import ch.bbv.explorer.ExplorerDocumentImpl;
import ch.bbv.explorer.ExplorerMenuBar;
import ch.bbv.explorer.ExplorerNode;
import ch.bbv.explorer.UiNodeTypeFactory;
import ch.bbv.lw.LwTransformer;

/**
 * The user explorer provides an explorer user interface to manipulate the users
 * roles for Internet providers. The defined information is used to configure
 * the rights of a Tomcat server. The user explorer uses the explorer framework
 * and the artifacts generated with the pmMDA tool to implement a complete user
 * interface to visualize and manipulate user roles with only one hand written
 * class.
 * 
 * @author Marcel Baumann
 * @version $Revision$
 */
public class ContactExplorer extends Explorer {
	private static final String DATASOURCE = "TEST";

	/**
	 * Logger instance of the class.
	 */
	private static Log log = LogFactory.getLog(ContactExplorer.class);

	/*
	 * User interface node type factory of the explorer instances.
	 */
	private static UiNodeTypeFactory factory;

	/**
	 * Lightweight object handler of the explorer instances.
	 */
	private static DataObjectMgr manager;

	/**
	 * The document displayed in the explorer.
	 */
	private ExplorerDocument document;

	/**
	 * Creates a new MDA explorer.
	 * 
	 * @param title
	 *            title of the explorer frame.
	 * @return the newly created user explorer.
	 */
	public static ContactExplorer create(String title) {
		Class[] types = { String.class };
		Object[] arguments = { title };
		ContactExplorer explorer = (ContactExplorer) Explorer.createAndShow(ContactExplorer.class, types, arguments);
		return explorer;
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param title
	 *            title of the explorer frame.
	 */
	public ContactExplorer(String title) {
		super(factory, manager);
		this.setTitle(title);
		document = new ExplorerDocumentImpl();
		if (log.isInfoEnabled()) {
			log.info("Start initializing user explorer");
		}
	}

	/**
	 * Registers all the node types used the explorer.
	 */
	public static void registerNodeTypes() {
		manager = Application.getApplication().getDataObjectMgr();
		factory = new UiNodeTypeFactory();
		TypeFactory typeFactory = new TypeFactory(factory, manager);
		typeFactory.registerAllTypes();
	}

	/**
	 * Returns the document of the explorer.
	 * 
	 * @return the document of the explorer.
	 */
	public ExplorerDocument getDocument() {
		return document;
	}

	/**
	 * Initializes the explorer. The initial tree of lightweight objects is
	 * created.
	 */
	protected void initialize() {
		ExplorerMenuBar menuBar = new ExplorerMenuBar(this);
		setMenuBar(menuBar);

		DataObjectHandler handler = manager.getDataObjectHandler();

		RootClass rootClass = null;
		for (DataObject dataObject : handler.retrieve(DATASOURCE, RootClass.class, "")) {
			rootClass = (RootClass) dataObject;
			break;
		}

		if (rootClass == null) {
			rootClass = new RootClass();
			rootClass.setId(DataObjectId.ILLEGAL_ID);
		}

		if (rootClass.getPropertyInstanceManager() == null) {
			
			
			PropertyInstanceManager manager = PropertyInstanceManager.instance();
			for(PropertyType type:manager.getSystemPropertyTypes()){
				manager.addType(type);
			}
			
			
			//Property birthdate = new Property("Birthday", PropertyInstanceManager.getType(Date.class));
			Property firstname = new Property("Firstname", PropertyInstanceManager.getType(String.class));

//			manager.addProperty(Contact.class, birthdate);
			manager.addProperty(Contact.class, firstname);

	/*		PropertyType type = new ListType("intList", PropertyInstanceManager.getType(Integer.class), ListViewer.class,
					TableEditor.class);
			manager.addType(type);
			Property intList = new Property("Lieblingszahlen", type);
			manager.addProperty(Contact.class, intList);
/*
			/*ComplexType complexType = new ComplexType("TPrivateContact", ComplexViewer.class, ComplexEditor.class);
			complexType.addProperty(birthdate);
			complexType.addProperty(firstname);
			manager.addType(complexType);
			
			Property privateContact = new Property("PrivateContact", complexType);
			manager.addProperty(Contact.class, privateContact);
*/
			/*PropertyType complexListType = new ListType("complexList", complexType, ListViewer.class, TableEditor.class);
			manager.addType(complexListType);
			Property complexList = new Property("MeineComplexeListe", complexListType);
			manager.addProperty(Contact.class, complexList);*/

			rootClass.setPropertyInstanceManager(PropertyInstanceManager.instance());
			handler.store(DATASOURCE, RootClass.class, rootClass);
		} else {
			PropertyInstanceManager.setSingletonInstance(rootClass.getPropertyInstanceManager());
		}

		new DynamicPropertyView();

		rootClass.accept(new VisitorFunctor(new DynamicPropertyFunctor()));
		PropertyInstanceManager.instance().setModified(true);

		handler.store(DATASOURCE, RootClass.class, rootClass);

		ExplorerNode root = createTree(LwTransformer.transform(rootClass), rootClass);
		root.setDataObject(rootClass);
		setRoot(root);
	}
}
