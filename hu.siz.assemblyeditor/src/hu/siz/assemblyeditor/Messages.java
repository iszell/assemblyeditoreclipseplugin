package hu.siz.assemblyeditor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "hu.siz.assemblyeditor.messages"; //$NON-NLS-1$
	public static String AssemblyBuilder_AddingDependencies;
	public static String AssemblyBuilder_Compiling;
	public static String AssemblyCompiler_CreateCompileCommandFailed;
	public static String AssemblyCompilerPreferences_Compiler_64TAss;
	public static String AssemblyCompilerPreferences_Compiler_AS65;
	public static String AssemblyCompilerPreferences_Compiler_CA65;
	public static String AssemblyCompilerPreferences_Compiler_DAsm;
	public static String AssemblyCompilerPreferences_Compiler_SNAsm;
	public static String AssemblyCompilerPreferences_Compiler_TMPx;
	public static String AssemblyCompilerPreferences_Compiler_XA;
	public static String AssemblyCompilerPreferences_CompilerName;
	public static String AssemblyCompilerPreferences_PreferencesName;
	public static String AssemblyCompilerPreferences_TabNameCompiler;
	public static String AssemblyCompilerPreferences_TabName64TAss;
	public static String AssemblyPreferences_Emulator_Minus4;
	public static String AssemblyPreferences_Emulator_Plus4Emu;
	public static String AssemblyPreferences_Emulator_ViCE;
	public static String AssemblyPreferences_Emulator_YaPE;
	public static String AssemblyPreferences_EmulatorName;
	public static String AssemblyPreferences_EmulatorPath;
	public static String AssemblyPreferences_MakeDiskPath;
	public static String AssemblyPreferences_PreferencesName;
	public static String AssemblyPreferences_TabSize;
	public static String AssembyView_Name;
	public static String CA65Preferences_Path;
	public static String DAsmPreferences_Path;
	public static String DiskImageDescriptor_UnsupportedImageType;
	public static String DiskImageSourceEditorForm_Label;
	public static String FieldEditorOverlayPage_ConfigureWorkspaceSettings;
	public static String FieldEditorOverlayPage_UseCustomizedSettings;
	public static String FieldEditorOverlayPage_UseWorkspaceSettings;
	public static String NewAssemblyProjectWizard_Error;
	public static String PropertyStore_CannotWriteResourceProperty;
	public static String TAssPreferences_2byteStartAddress;
	public static String TAssPreferences_6502;
	public static String TAssPreferences_65816;
	public static String TAssPreferences_65DTV02;
	public static String TAssPreferences_65xx;
	public static String TAssPreferences_65xx_undocumented;
	public static String TAssPreferences_CaseSensitive;
	public static String TAssPreferences_CreateListing;
	public static String TAssPreferences_cpu64;
	public static String TAssPreferences_Description;
	public static String TAssPreferences_NonLinear;
	public static String TAssPreferences_OtherOptions;
	public static String TAssPreferences_Path;
	public static String TAssPreferences_ReplaceLongBranch;
	public static String TAssPreferences_SuppressStartAddress;
	public static String TAssPreferences_TargetArchitecture;
	public static String TAssPreferences_TAssVersion;
	public static String TAssPreferences_TAssVersion_new;
	public static String TAssPreferences_TAssVersion_old;
	public static String TMPxPreferences_Path;
	public static String XAPreferences_Path;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
		// Nothing to do.
	}
}
