package hu.siz.assemblyeditor.preferences;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = AssemblyEditorPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(PreferenceConstants.P_TABSIZE, 10);
		store.setDefault(PreferenceConstants.P_COMPILER, PreferenceConstants.P_COMPILER_64TASS);
		store.setDefault(PreferenceConstants.P_EMULATOR, PreferenceConstants.P_EMULATOR_YAPE);
		store.setDefault(PreferenceConstants.P_EMULATORPATH, ""); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_MAKEDISKPATH, ""); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_TASSPATH, ""); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_TASSOPTNOSTART, false);
		store.setDefault(PreferenceConstants.P_TASSOPTLONGBRANCH, false);
		store.setDefault(PreferenceConstants.P_TASSOPTCASESENSITIVE, false);
		store.setDefault(PreferenceConstants.P_TASSOPTNONLINEAR, false);
		store.setDefault(PreferenceConstants.P_TASSOPTWORDSTART, false);
		store.setDefault(PreferenceConstants.P_TASSOPTARCHITECTURE, "--m65xx"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_TASSOPTCREATELISTING, true);
		store.setDefault(PreferenceConstants.P_TASSCMDLINE, "--ascii"); //$NON-NLS-1$
	}
}
