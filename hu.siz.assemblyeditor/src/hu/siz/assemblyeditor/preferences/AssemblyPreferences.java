package hu.siz.assemblyeditor.preferences;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;
import hu.siz.assemblyeditor.Messages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Assembly editor preference page
 */
public class AssemblyPreferences extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public AssemblyPreferences() {
		super(GRID);
		setPreferenceStore(AssemblyEditorPlugin.getDefault()
				.getPreferenceStore());
		setDescription(Messages.AssemblyPreferences_PreferencesName);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new IntegerFieldEditor(PreferenceConstants.P_TABSIZE,
				Messages.AssemblyPreferences_TabSize, getFieldEditorParent(), 2));
		addField(new RadioGroupFieldEditor(PreferenceConstants.P_EMULATOR,
				Messages.AssemblyPreferences_EmulatorName, 1, new String[][] {
						{ Messages.AssemblyPreferences_Emulator_Minus4,
								PreferenceConstants.P_EMULATOR_MINUS4 },
						{ Messages.AssemblyPreferences_Emulator_Plus4Emu,
								PreferenceConstants.P_EMULATOR_PLUS4EMU },
						{ Messages.AssemblyPreferences_Emulator_ViCE,
								PreferenceConstants.P_EMULATOR_VICE },
						{ Messages.AssemblyPreferences_Emulator_YaPE,
								PreferenceConstants.P_EMULATOR_YAPE }, },
				getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_TASSPATH,
				Messages.TAssPreferences_Path, true, getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_CA65PATH,
				Messages.CA65Preferences_Path, true, getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_DASMPATH,
				Messages.DAsmPreferences_Path, true, getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_TMPXPATH,
				Messages.TMPxPreferences_Path, true, getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_XAPATH,
				Messages.XAPreferences_Path, true, getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_MAKEDISKPATH,
				Messages.AssemblyPreferences_MakeDiskPath, true,
				getFieldEditorParent()));
		addField(new FileFieldEditor(PreferenceConstants.P_EMULATORPATH,
				Messages.AssemblyPreferences_EmulatorPath, true,
				getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		// Nothing to do here
	}
}