package hu.siz.assemblyeditor.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {

	/**
	 * Editor setup
	 */
	public static final String P_TABSIZE = "TabSize"; //$NON-NLS-1$
	
	/**
	 * Compiler name
	 */
	public static final String P_COMPILER = "Compiler"; //$NON-NLS-1$

	public static final String P_COMPILER_64TASS = "64TAss"; //$NON-NLS-1$
	public static final String P_COMPILER_AS65 = "As65"; //$NON-NLS-1$
	public static final String P_COMPILER_CA65 = "CA65"; //$NON-NLS-1$
	public static final String P_COMPILER_DASM = "DAsm"; //$NON-NLS-1$
	public static final String P_COMPILER_SNAsm = "SNAsm"; //$NON-NLS-1$
	public static final String P_COMPILER_TMPX = "TMPx"; //$NON-NLS-1$
	public static final String P_COMPILER_XA = "XA"; //$NON-NLS-1$
	
	/**
	 * Emulator name
	 */
	public static final String P_EMULATOR = "Emulator"; //$NON-NLS-1$

	public static final String P_EMULATOR_MINUS4 = "Minus4"; //$NON-NLS-1$
	public static final String P_EMULATOR_PLUS4EMU = "Plus4Emu"; //$NON-NLS-1$
	public static final String P_EMULATOR_VICE = "ViCE"; //$NON-NLS-1$
	public static final String P_EMULATOR_YAPE = "YaPE"; //$NON-NLS-1$

	/**
	 * Emulator path
	 */
	public static final String P_EMULATORPATH = "Emulator"; //$NON-NLS-1$
	
	/**
	 * Path to makedisk
	 */
	public static final String P_MAKEDISKPATH = "MakeDiskPath"; //$NON-NLS-1$

	// Path to dasm.exe
	public static final String P_DASMPATH = "DAsmPath"; //$NON-NLS-1$

	// Path to 64tass.exe
	public static final String P_TASSPATH = "TAssPath"; //$NON-NLS-1$
	//// 64Tass preferences
	// --nostart
	public static final String P_TASSOPTNOSTART = "TassOptNoStart"; //$NON-NLS-1$
	// --long-branch
	public static final String P_TASSOPTLONGBRANCH = "TAssOptLongBranch"; //$NON-NLS-1$
	// --case-sensitive
	public static final String P_TASSOPTCASESENSITIVE = "TAssOptCaseSensitive"; //$NON-NLS-1$
	// --nonlinear
	public static final String P_TASSOPTNONLINEAR = "TAssOptNonLinear"; //$NON-NLS-1$
	// --wordstart
	public static final String P_TASSOPTWORDSTART = "TAssOptWordStart"; //$NON-NLS-1$
	// Architecture
	public static final String P_TASSOPTARCHITECTURE = "TAssOptArchitecture"; //$NON-NLS-1$
	// TAss compiler version
	public static final String P_TASSVERSION = "TAssOptTassVersion"; //$NON-NLS-1$
	public static final String P_TASSOPTCREATELISTING = "TAssOptCreateListing"; //$NON-NLS-1$
	public static final String P_TASSOPTCREATELABELS = "TAssOptCreateLabels"; //$NON-NLS-1$
	public static final String P_TASSCMDLINE = "TAssCmdLine"; //$NON-NLS-1$

	// Path to CA65.exe
	public static final String P_CA65PATH = "CA65Path"; //$NON-NLS-1$

	// Path to tmpx.exe
	public static final String P_TMPXPATH = "TMPxPath"; //$NON-NLS-1$

	// Path to xa.exe
	public static final String P_XAPATH = "XAPath"; //$NON-NLS-1$
	
	// Path to postprocessor command
	public static final String P_POSTPROCESSORPATH = "PostProcessorPath"; //$NON-NLS-1$
	public static final String P_POSTPROCESSCMDLINE = "PostProcessCmdLine"; //$NON-NLS-1$
}
