package hu.siz.assemblyeditor.preferences;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;
import hu.siz.assemblyeditor.Messages;
import hu.siz.assemblyeditor.properties.FieldEditorOverlayPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Assembly editor preference page
 */
public class AssemblyCompilerPreferences extends FieldEditorOverlayPage
		implements IWorkbenchPreferencePage {

	private Map<Composite, List<FieldEditor>> editors = new HashMap<Composite, List<FieldEditor>>();

	public AssemblyCompilerPreferences() {
		super(GRID);
		setPreferenceStore(AssemblyEditorPlugin.getDefault()
				.getPreferenceStore());
		setDescription(Messages.AssemblyCompilerPreferences_PreferencesName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
	 * ()
	 */
	@Override
	public void createFieldEditors() {
		TabFolder tabFolder = new TabFolder(getFieldEditorParent(), SWT.NONE);
		TabItem compilerTab = new TabItem(tabFolder, SWT.NONE);
		compilerTab
				.setText(Messages.AssemblyCompilerPreferences_TabNameCompiler);
		Composite compilerComposite = new Composite(tabFolder, SWT.NONE);
		compilerTab.setControl(compilerComposite);

		addCompilerFields(compilerComposite);

		TabItem tassTab = new TabItem(tabFolder, SWT.NONE);
		tassTab.setText(Messages.AssemblyCompilerPreferences_TabName64TAss);
		Composite tassComposite = new Composite(tabFolder, SWT.NONE);
		tassTab.setControl(tassComposite);

		addTAssFields(tassComposite);

	}

	/**
	 * Add compiler tab field editors to the parent
	 * 
	 * @param parent
	 */
	private void addCompilerFields(Composite parent) {
		addField(
				new RadioGroupFieldEditor(
						PreferenceConstants.P_COMPILER,
						Messages.AssemblyCompilerPreferences_CompilerName,
						1,
						new String[][] {
								{
										Messages.AssemblyCompilerPreferences_Compiler_64TAss,
										PreferenceConstants.P_COMPILER_64TASS },
								{
										Messages.AssemblyCompilerPreferences_Compiler_AS65,
										PreferenceConstants.P_COMPILER_AS65 },
								{
										Messages.AssemblyCompilerPreferences_Compiler_CA65,
										PreferenceConstants.P_COMPILER_CA65 },
								{
										Messages.AssemblyCompilerPreferences_Compiler_DAsm,
										PreferenceConstants.P_COMPILER_DASM },
								{
										Messages.AssemblyCompilerPreferences_Compiler_SNAsm,
										PreferenceConstants.P_COMPILER_SNAsm },
								{
										Messages.AssemblyCompilerPreferences_Compiler_TMPx,
										PreferenceConstants.P_COMPILER_TMPX },
								{
										Messages.AssemblyCompilerPreferences_Compiler_XA,
										PreferenceConstants.P_COMPILER_XA }, },
						parent), parent);
	}

	/**
	 * Add TAss preference field editors to the parent
	 * 
	 * @param parent
	 */
	private void addTAssFields(Composite parent) {
		addField(new RadioGroupFieldEditor(PreferenceConstants.P_TASSVERSION,
				Messages.TAssPreferences_TAssVersion, 1, new String[][] {
						{ Messages.TAssPreferences_TAssVersion_old, "old" }, //$NON-NLS-1$
						{ Messages.TAssPreferences_TAssVersion_new, "new" } //$NON-NLS-1$
				}, parent), parent);
		addField(
				new RadioGroupFieldEditor(
						PreferenceConstants.P_TASSOPTARCHITECTURE,
						Messages.TAssPreferences_TargetArchitecture, 1,
						new String[][] {
								{ Messages.TAssPreferences_6502, "--m65c02" }, //$NON-NLS-1$
								{ Messages.TAssPreferences_65xx_undocumented,
										"--m6502" }, //$NON-NLS-1$
								{ Messages.TAssPreferences_65xx, "--m65xx" }, //$NON-NLS-1$
								{ Messages.TAssPreferences_65DTV02,
										"--m65dtv02" }, //$NON-NLS-1$
								{ Messages.TAssPreferences_65816, "--m65816" }, //$NON-NLS-1$
								{ Messages.TAssPreferences_cpu64, "--mcpu64" } //$NON-NLS-1$
						}, parent), parent);
		addField(new BooleanFieldEditor(PreferenceConstants.P_TASSOPTNOSTART,
				Messages.TAssPreferences_SuppressStartAddress, parent), parent);
		addField(new BooleanFieldEditor(
				PreferenceConstants.P_TASSOPTLONGBRANCH,
				Messages.TAssPreferences_ReplaceLongBranch, parent), parent);
		addField(new BooleanFieldEditor(
				PreferenceConstants.P_TASSOPTCASESENSITIVE,
				Messages.TAssPreferences_CaseSensitive, parent), parent);
		addField(new BooleanFieldEditor(PreferenceConstants.P_TASSOPTNONLINEAR,
				Messages.TAssPreferences_NonLinear, parent), parent);
		addField(new BooleanFieldEditor(PreferenceConstants.P_TASSOPTWORDSTART,
				Messages.TAssPreferences_2byteStartAddress, parent), parent);
		addField(new BooleanFieldEditor(
				PreferenceConstants.P_TASSOPTCREATELISTING,
				Messages.TAssPreferences_CreateListing, parent), parent);
		addField(new StringFieldEditor(PreferenceConstants.P_TASSCMDLINE,
				Messages.TAssPreferences_OtherOptions, parent), parent);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.properties.FieldEditorOverlayPage#getPageId()
	 */
	@Override
	protected String getPageId() {
		AssemblyEditorPlugin.getDefault();
		return AssemblyEditorPlugin.PLUGIN_ID;
	}

	/**
	 * Add a field editor to the parent
	 * 
	 * @param editor
	 * @param parent
	 */
	private void addField(FieldEditor editor, Composite parent) {
		addField(editor);
		List<FieldEditor> editorList = this.editors.get(parent);
		if (editorList == null) {
			editorList = new ArrayList<FieldEditor>();
			this.editors.put(parent, editorList);
		}
		editorList.add(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.properties.FieldEditorOverlayPage#updateFieldEditors
	 * (boolean)
	 */
	@Override
	protected void updateFieldEditors(boolean enabled) {
		for (Entry<Composite, List<FieldEditor>> entry : this.editors
				.entrySet()) {
			for (FieldEditor editor : entry.getValue()) {
				editor.setEnabled(enabled, entry.getKey());
			}
		}
	}
}