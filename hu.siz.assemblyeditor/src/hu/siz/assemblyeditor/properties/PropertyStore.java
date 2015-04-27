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
import hu.siz.assemblyeditor.utils.AssemblyUtils;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * @author Berthold Daum
 * 
 */
public class PropertyStore extends PreferenceStore {

	/***
	 * Name of resource property for the selection of workbench or project
	 * settings
	 ***/
	public static final String USECUSTOMSETTINGS = "useProjectSettings"; //$NON-NLS-1$

	private IResource resource;
	private IPreferenceStore workbenchStore;
	private String pageId;
	private boolean inserting = false;
	private boolean useCustomSettings;

	public PropertyStore(IResource resource, IPreferenceStore workbenchStore,
			String pageId) {
		this.resource = resource;
		this.workbenchStore = workbenchStore;
		this.pageId = pageId;
		try {
			this.useCustomSettings = TRUE.equals(resource
					.getPersistentProperty(new QualifiedName(this.pageId,
							USECUSTOMSETTINGS)));
		} catch (CoreException e) {
			this.useCustomSettings = false;
			AssemblyUtils.createLogEntry(e);
		}
	}

	/**
	 * @return the useCustomSettings
	 */
	public boolean isUseCustomSettings() {
		return this.useCustomSettings;
	}

	/**
	 * @param useCustomSettings
	 *            the useCustomSettings to set
	 */
	public void setUseCustomSettings(boolean useCustomSettings) {
		this.useCustomSettings = useCustomSettings;
	}

	/*** Write modified values back to properties ***/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPersistentPreferenceStore#save()
	 */
	@Override
	public void save() throws IOException {
		writeProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferenceStore#save(java.io.OutputStream,
	 * java.lang.String)
	 */
	@Override
	public void save(OutputStream out, String header) throws IOException {
		writeProperties();
	}

	/**
	 * Writes modified preferences into resource properties.
	 */
	private void writeProperties() throws IOException {
		setProperty(USECUSTOMSETTINGS, String.valueOf(this.useCustomSettings));
		for (String name : super.preferenceNames()) {
			setProperty(name, getString(name));
		}
	}

	/**
	 * Convenience method to set a property
	 * 
	 * @param name
	 *            - the preference name
	 * @param value
	 *            - the property value or null to delete the property
	 * @throws IOException
	 */
	private void setProperty(String name, String value) throws IOException {
		String propertyValue = null;
		if (isUseCustomSettings()) {
			propertyValue = value;
		}
		try {
			this.resource.setPersistentProperty(new QualifiedName(this.pageId,
					name), propertyValue);
		} catch (CoreException e) {
			throw new IOException(
					Messages.PropertyStore_CannotWriteResourceProperty + name,
					e);
		}
	}

	/*** Get default values (Delegate to workbench store) ***/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.
	 * lang.String)
	 */
	@Override
	public boolean getDefaultBoolean(String name) {
		return this.workbenchStore.getDefaultBoolean(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang
	 * .String)
	 */
	@Override
	public double getDefaultDouble(String name) {
		return this.workbenchStore.getDefaultDouble(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang
	 * .String)
	 */
	@Override
	public float getDefaultFloat(String name) {
		return this.workbenchStore.getDefaultFloat(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang
	 * .String)
	 */
	@Override
	public int getDefaultInt(String name) {
		return this.workbenchStore.getDefaultInt(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang
	 * .String)
	 */
	@Override
	public long getDefaultLong(String name) {
		return this.workbenchStore.getDefaultLong(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang
	 * .String)
	 */
	@Override
	public String getDefaultString(String name) {
		return this.workbenchStore.getDefaultString(name);
	}

	/*** Get property values ***/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String
	 * )
	 */
	@Override
	public boolean getBoolean(String name) {
		insertValue(name);
		return super.getBoolean(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String name) {
		insertValue(name);
		return super.getDouble(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String name) {
		insertValue(name);
		return super.getFloat(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String name) {
		insertValue(name);
		return super.getInt(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String name) {
		insertValue(name);
		return super.getLong(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		insertValue(name);
		return super.getString(name);
	}

	/**
	 * @param name
	 */
	private synchronized void insertValue(String name) {
		if (this.inserting)
			return;
		if (super.contains(name))
			return;
		this.inserting = true;
		String prop = null;
		try {
			prop = getProperty(name);
		} catch (CoreException e) {
			AssemblyUtils.createLogEntry(e);
		}
		if (prop == null)
			prop = this.workbenchStore.getString(name);
		if (prop != null)
			setValue(name, prop);
		this.inserting = false;
	}

	/**
	 * Convenience method to fetch a property
	 * 
	 * @param name
	 *            - the preference name
	 * @return - the property value
	 * @throws CoreException
	 */
	private String getProperty(String name) throws CoreException {
		return this.resource.getPersistentProperty(new QualifiedName(
				this.pageId, name));
	}

	/*** Misc ***/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String name) {
		return this.workbenchStore.contains(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.
	 * String)
	 */
	@Override
	public void setToDefault(String name) {
		setValue(name, getDefaultString(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
	 */
	@Override
	public boolean isDefault(String name) {
		String defaultValue = getDefaultString(name);
		if (defaultValue == null)
			return false;
		return defaultValue.equals(getString(name));
	}

}
