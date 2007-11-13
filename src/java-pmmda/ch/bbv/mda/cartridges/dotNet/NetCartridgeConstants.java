package ch.bbv.mda.cartridges.dotNet;

public interface NetCartridgeConstants {

	public static String USING_BLOCK = "usings";
	public static String USING_BLOCK_BEGIN = "/* %% USER DEFINED USING STATEMENTS START %% */";
	public static String USING_BLOCK_END = "/* %% USER DEFINED USING STATEMENTS END   %% */";

	public static String DECLARATIONS_BLOCK = "declarations";
	public static String DECLARATIONS_BLOCK_BEGIN = "/* %% USER DEFINED DECLARATION START %% */";
	public static String DECLARATIONS_BLOCK_END = "/* %% USER DEFINED DECLARATION END %% */";

	public static String INITIALIZATIONS_BLOCK = "initializations";
	public static String INITIALIZATIONS_BLOCK_BEGIN = "/* %% USER DEFINED INITIALIZATION START %% */";
	public static String INITIALIZATIONS_BLOCK_END = "/* %% USER DEFINED INITIALIZATION END   %% */";

	public static String SERIAL_INITIALIZATIONS_BLOCK = "serial_initializations";
	public static String SERIAL_INITIALIZATIONS_BLOCK_BEGIN = "/* %% USER DEFINED SERIALIZATION INITIALIZATION START %% */";
	public static String SERIAL_INITIALIZATIONS_BLOCK_END = "/* %% USER DEFINED SERIALIZATION INITIALIZATION END   %% */";

	public static String CONSTRUCTORS_BLOCK = "constructors";
	public static String CONSTRUCTORS_BLOCK_BEGIN = "/* %% USER DEFINED CONSTRUCTORS START %% */";
	public static String CONSTRUCTORS_BLOCK_END = "/* %% USER DEFINED CONSTRUCTORS END %% */";

	public static String DEFINITIONS_BLOCK = "definitions";
	public static String DEFINITIONS_BLOCK_BEGIN = "/* %% USER DEFINED CODE START %% */";
	public static String DEFINITIONS_BLOCK_END = "/* %% USER DEFINED CODE END %% */";
	
	public static String RESSOURCE_FOLDER = "file.resource.loader.path";
	


	/**
	 * DataObjects
	 */

	public static final String DOG_NET_DO_FACTORY_VM = "dog-net-do-factory";
	public static final String DOG_NET_CODES_VM = "dog-net-codes";
	public static final String DOG_NET_LW_VISITOR_CLASS_VM = "dog-net-lw-visitor-class";
	public static final String DOG_NET_LW_VISITOR_INTERFACE_VM = "dog-net-lw-visitor-interface";
	public static final String DOG_NET_LW_VM = "dog-net-lw-class";
	public static final String DOG_NET_DO_VM = "dog-net-do-class";
	public static final String INTERFACE_PACKAGE_POSTFIX_KEY = "net.dog.interface-package-postfix";
	public static final String DATA_OBJECTS_PACKAGE_POSTFIX_KEY = "net.dog.data-objects-package-postfix";
	public static final String LIGHTWEIGHT_OBJECTS_PACKAGE_POSTFIX_KEY = "net.dog.lightweight-objects-package-postfix";
	public static final String INTERFACE_CLASS_PREFIX_KEY = "net.dog.interface-class-prefix";
	public static final String DATA_OBJECTS_CLASS_PREFIX_KEY = "net.dog.data-objects-class-prefix";
	public static final String LIGHTWEIGHT_OBJECTS_CLASS_PREFIX_KEY = "net.dog.lightweight-objects-class-prefix";
	public static final String INTERFACE_CLASS_POSTFIX_KEY = "net.dog.interface-class-postfix";
	public static final String DATA_OBJECTS_CLASS_POSTFIX_KEY = "net.dog.data-objects-class-postfix";
	public static final String LIGHTWEIGHT_OBJECTS_CLASS_POSTFIX_KEY = "net.dog.lightweight-objects-class-postfix";
	public static final String ROOT_FOLDER_KEY = "dog.net.cartridge.root-folder";
	public static final String META_DATA_ROOT_FOLDER_KEY = "net.metadata.root-folder";

	/**
	 * Views
	 */
	public static final String VIEW_ROOT_FOLDER_KEY = ROOT_FOLDER_KEY;

	/**
	 * Hibernate
	 */

	public static final String TABLE_PREFIX_KEY = "net.nhibernate.table-prefix";
	public static final String TABLE_POSTFIX_KEY = "net.nhibernate.table-postfix";
	public static final String COLUMN_PREFIX_KEY = "net.nhibernate.column-prefix";
	public static final String COLUMN_POSTFIX_KEY = "net.nhibernat.column-postfix";
	public static final String SOURCE_ROOT_FOLDER_KEY = "net.nhibernate.root-folder";
	public static final String DOTNET_IGNORE = ".net-ignore";

	/**
	 * WPF
	 */

	public static final String WPF_ROOT_FOLDER_KEY = "net.wpf.root-folder";
	public static final String WPF_GENERATE = "wpf-generate";


}
