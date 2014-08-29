/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 * @author siz
 * 
 */
public class ResourceDependencies {

	private static final String STATEFILE_NAME = "ResourceDependency.dat"; //$NON-NLS-1$
	private static Map<String, Set<String>> resourceDependencies;
	private static ResourceDependencies instance;

	public ResourceDependencies() {
		instance = this;
		if (resourceDependencies == null) {
			restoreState();
		}
	}

	/**
	 * @return the current instance of the <code>ResourceDependencies</code>
	 */
	public static synchronized ResourceDependencies getInstance() {
		if (instance == null) {
			instance = new ResourceDependencies();
		}
		return instance;
	}

	/**
	 * Returns all resource names which depends on this one
	 * 
	 * @param resourcePath
	 *            the full resource path
	 */
	public synchronized Set<String> get(String resourcePath) {
		return resourceDependencies.get(resourcePath);
	}

	/**
	 * Convenience method for {@link #get(String)}
	 * 
	 * @param resource
	 *            the resource
	 */
	public synchronized Set<String> get(IResource resource) {
		return get(resource.getFullPath().toString());
	}

	/**
	 * Add dependency
	 * 
	 * @param resourcePath
	 *            The full resource path
	 * @param dependentResourcePath
	 *            The full dependent resource path
	 */
	public synchronized void add(String resourcePath,
			String dependentResourcePath) {
		Set<String> dependencies;
		if (resourceDependencies.containsKey(resourcePath)) {
			dependencies = resourceDependencies.get(resourcePath);
			if (!dependencies.contains(dependentResourcePath)) {
				dependencies.add(dependentResourcePath);
			}
		} else {
			dependencies = new HashSet<String>();
			dependencies.add(dependentResourcePath);
		}
		resourceDependencies.put(resourcePath, dependencies);
		saveState();
	}

	/**
	 * Clean dependency database
	 */
	public synchronized void clean() {
		resourceDependencies.clear();
	}

	/**
	 * Clean project data from dependency database
	 */
	public synchronized void clean(IProject project) {
		String projectPath = project.getFullPath().toString() + IPath.SEPARATOR;
		Map<String, Set<String>> newDependencies = new HashMap<String, Set<String>>();

		for (Entry<String, Set<String>> entry : resourceDependencies.entrySet()) {
			if (!entry.getKey().startsWith(projectPath)) {
				newDependencies.put(entry.getKey(), entry.getValue());
			}
		}
		resourceDependencies = newDependencies;
	}

	/**
	 * Save dependency database
	 */
	public synchronized void saveState() {
		IPath stateLocation = AssemblyEditorPlugin.getDefault()
				.getStateLocation();
		try {
			OutputStream file = new FileOutputStream(stateLocation.append(
					STATEFILE_NAME).toOSString());
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(resourceDependencies);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Restore dependency database
	 */
	@SuppressWarnings("unchecked")
	private static synchronized void restoreState() {
		IPath stateLocation = AssemblyEditorPlugin.getDefault()
				.getStateLocation();
		try {
			InputStream file = new FileInputStream(stateLocation.append(
					STATEFILE_NAME).toOSString());
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			resourceDependencies = (Map<String, Set<String>>) input
					.readObject();
			input.close();
		} catch (Exception e) {
			resourceDependencies = new HashMap<String, Set<String>>();
		}
	}
}
