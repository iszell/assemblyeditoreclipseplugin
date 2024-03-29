/*******************************************************************************
 * Copyright (c) 2003 Berthold Daum.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Berthold Daum
 *******************************************************************************/
package hu.siz.assemblyeditor.properties;

import hu.siz.assemblyeditor.Messages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;

/**
 * @author Berthold Daum
 */
public abstract class FieldEditorOverlayPage extends FieldEditorPreferencePage
		implements IWorkbenchPropertyPage {

	// Stores all created field editors
	private List<FieldEditor> editors = new ArrayList<FieldEditor>();

	// Stores owning element of properties
	private IAdaptable element;

	// Additional buttons for property pages
	private Button useWorkspaceSettingsButton;
	private Button useProjectSettingsButton;
	private Button configureButton;

	// Overlay preference store for property pages
	private IPreferenceStore overlayStore;

	// The image descriptor of this pages title image
	private ImageDescriptor image;

	// Cache for page id
	private String pageId;

	/**
	 * Constructor
	 * 
	 * @param style
	 *            - layout style
	 */
	public FieldEditorOverlayPage(int style) {
		super(style);
	}

	/**
	 * Constructor
	 * 
	 * @param title
	 *            - title string
	 * @param style
	 *            - layout style
	 */
	public FieldEditorOverlayPage(String title, int style) {
		super(title, style);
	}

	/**
	 * Constructor
	 * 
	 * @param title
	 *            - title string
	 * @param image
	 *            - title image
	 * @param style
	 *            - layout style
	 */
	public FieldEditorOverlayPage(String title, ImageDescriptor image, int style) {
		super(title, image, style);
		this.image = image;
	}

	/**
	 * Returns the id of the current preference page as defined in plugin.xml
	 * Subclasses must implement.
	 * 
	 * @return - the qualifier
	 */
	protected abstract String getPageId();

	/**
	 * Receives the object that owns the properties shown in this property page.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public void setElement(IAdaptable element) {
		this.element = element;
	}

	/**
	 * Delivers the object that owns the properties shown in this property page.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
	 */
	@Override
	public IAdaptable getElement() {
		return this.element;
	}

	/**
	 * Returns true if this instance represents a property page
	 * 
	 * @return - true for property pages, false for preference pages
	 */
	public boolean isPropertyPage() {
		return getElement() != null;
	}

	/**
	 * We override the addField method. This allows us to store each field
	 * editor added by subclasses in a list for later processing.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#addField(org.eclipse.jface.preference.FieldEditor)
	 */
	@Override
	protected void addField(FieldEditor editor) {
		this.editors.add(editor);
		super.addField(editor);
	}

	/**
	 * We override the createControl method. In case of property pages we create
	 * a new PropertyStore as local preference store. After all control have
	 * been create, we enable/disable these controls.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createControl()
	 */
	@Override
	public void createControl(Composite parent) {
		// Special treatment for property pages
		if (isPropertyPage()) {
			// Cache the page id
			this.pageId = getPageId();
			// Create an overlay preference store and fill it with properties
			this.overlayStore = new PropertyStore((IResource) getElement(),
					super.getPreferenceStore(), this.pageId);
			// Set overlay store as current preference store
		}
		super.createControl(parent);
		// Update state of all subclass controls
		if (isPropertyPage())
			updateFieldEditors();
	}

	/**
	 * We override the createContents method. In case of property pages we
	 * insert two radio buttons at the top of the page.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		if (isPropertyPage())
			createSelectionGroup(parent);
		return super.createContents(parent);
	}

	/**
	 * Creates and initializes a selection group with two choice buttons and one
	 * push button.
	 * 
	 * @param parent
	 *            - the parent composite
	 */
	private void createSelectionGroup(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Composite radioGroup = new Composite(comp, SWT.NONE);
		radioGroup.setLayout(new GridLayout());
		radioGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.useWorkspaceSettingsButton = createRadioButton(radioGroup,
				Messages.FieldEditorOverlayPage_UseWorkspaceSettings);
		this.useProjectSettingsButton = createRadioButton(radioGroup,
				Messages.FieldEditorOverlayPage_UseCustomizedSettings);
		this.configureButton = new Button(comp, SWT.PUSH);
		this.configureButton
				.setText(Messages.FieldEditorOverlayPage_ConfigureWorkspaceSettings);
		this.configureButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configureWorkspaceSettings();
			}
		});
		// Set workspace/project radio buttons
		if (this.overlayStore != null
				&& this.overlayStore instanceof PropertyStore
				&& ((PropertyStore) this.overlayStore).isUseCustomSettings()) {
			this.useProjectSettingsButton.setSelection(true);
			this.configureButton.setEnabled(false);
		} else {
			this.useWorkspaceSettingsButton.setSelection(true);
		}
	}

	/**
	 * Convenience method creating a radio button
	 * 
	 * @param parent
	 *            - the parent composite
	 * @param label
	 *            - the button label
	 * @return - the new button
	 */
	private Button createRadioButton(Composite parent, String label) {
		final Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FieldEditorOverlayPage.this.configureButton
						.setEnabled(button == FieldEditorOverlayPage.this.useWorkspaceSettingsButton);
				updateFieldEditors();
			}
		});
		return button;
	}

	/**
	 * Returns in case of property pages the overlay store, in case of
	 * preference pages the standard preference store
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
	 */
	@Override
	public IPreferenceStore getPreferenceStore() {
		if (isPropertyPage())
			return this.overlayStore;
		return super.getPreferenceStore();
	}

	/*
	 * Enables or disables the field editors and buttons of this page
	 */
	private void updateFieldEditors() {
		// We iterate through all field editors
		boolean enabled = this.useProjectSettingsButton.getSelection();
		updateFieldEditors(enabled);
	}

	/**
	 * Enables or disables the field editors and buttons of this page Subclasses
	 * may override.
	 * 
	 * @param enabled
	 *            - true if enabled
	 */
	protected void updateFieldEditors(boolean enabled) {
		Composite parent = getFieldEditorParent();
		for (FieldEditor editor : this.editors) {
			editor.setEnabled(enabled, parent);
		}
	}

	/**
	 * We override the performOk method. In case of property pages we copy the
	 * values in the overlay store into the property values of the selected
	 * project. We also save the state of the radio buttons.
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if (isPropertyPage() && this.overlayStore instanceof PropertyStore) {
			((PropertyStore) this.overlayStore)
					.setUseCustomSettings(this.useProjectSettingsButton
							.getSelection());
		}
		return super.performOk();
	}

	/**
	 * We override the performDefaults method. In case of property pages we
	 * switch back to the workspace settings and disable the field editors.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		if (isPropertyPage()) {
			this.useWorkspaceSettingsButton.setSelection(true);
			this.useProjectSettingsButton.setSelection(false);
			this.configureButton.setEnabled(true);
			updateFieldEditors();
		}
		super.performDefaults();
	}

	/**
	 * Creates a new preferences page and opens it
	 * 
	 * @see com.bdaum.SpellChecker.preferences.SpellCheckerPreferencePage#configureWorkspaceSettings()
	 */
	protected void configureWorkspaceSettings() {
		try {
			// create a new instance of the current class
			IPreferencePage page = this.getClass().newInstance();
			page.setTitle(getTitle());
			page.setImageDescriptor(this.image);
			// and show it
			showPreferencePage(this.pageId, page);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show a single preference pages
	 * 
	 * @param id
	 *            - the preference page identification
	 * @param page
	 *            - the preference page
	 */
	protected void showPreferencePage(String id, IPreferencePage page) {
		final IPreferenceNode targetNode = new PreferenceNode(id, page);
		PreferenceManager manager = new PreferenceManager();
		manager.addToRoot(targetNode);
		final PreferenceDialog dialog = new PreferenceDialog(getControl()
				.getShell(), manager);
		BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
			@Override
			public void run() {
				dialog.create();
				dialog.setMessage(targetNode.getLabelText());
				dialog.open();
			}
		});
	}

}
