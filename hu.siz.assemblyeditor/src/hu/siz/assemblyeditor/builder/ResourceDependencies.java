/**
 * 
 */
package hu.siz.assemblyeditor.builder;

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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;
import hu.siz.assemblyeditor.utils.AssemblyUtils;

/**
 * @author siz
 * 
 */
public class ResourceDependencies {

	private static final String STATEFILE_NAME = "ResourceDependency.dat"; //$NON-NLS-1$
	private static final Map<String, Set<String>> resourceDependencies = new HashMap<>();
	private Lock lock = new ReentrantLock();
	private static ResourceDependencies instance;

	private ResourceDependencies() {
		instance = this;
		restoreState();
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
	public Set<String> get(String resourcePath) {
		lock.lock();
		try {
			final Set<String> result = resourceDependencies.get(resourcePath);
			return result;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Convenience method for {@link #get(String)}
	 * 
	 * @param resource
	 *            the resource
	 */
	public Set<String> get(IResource resource) {
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
	public void add(String resourcePath, String dependentResourcePath) {
		// AssemblyUtils.createLogEntry(IStatus.OK,
		// "ResourceDependencies.add(" + resourcePath + ", " + dependentResourcePath +
		// ")");
		lock.lock();
		try {
			Set<String> dependencies = resourceDependencies.get(resourcePath);
			if (dependencies == null) {
				dependencies = new HashSet<>();
				resourceDependencies.put(resourcePath, dependencies);
			}
			dependencies.add(dependentResourcePath);
			saveState();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Clean dependency database
	 */
	public synchronized void clean() {
		lock.lock();
		try {
			resourceDependencies.clear();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Clean project data from dependency database
	 */
	public synchronized void clean(IProject project) {
		// AssemblyUtils.createLogEntry(IStatus.OK, "ResourceDependencies.clean()");
		lock.lock();
		try {
			String projectPath = project.getFullPath().toString() + IPath.SEPARATOR;
			Map<String, Set<String>> oldDependencies = new HashMap<String, Set<String>>();
			oldDependencies.putAll(resourceDependencies);
			clean();

			for (Entry<String, Set<String>> entry : oldDependencies.entrySet()) {
				if (!entry.getKey().startsWith(projectPath)) {
					resourceDependencies.put(entry.getKey(), entry.getValue());
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Save dependency database
	 */
	public void saveState() {
		// AssemblyUtils.createLogEntry(IStatus.OK, "ResourceDependencies.saveState()");
		lock.lock();
		try {
			IPath stateLocation = AssemblyEditorPlugin.getDefault().getStateLocation();
			OutputStream file = new FileOutputStream(stateLocation.append(STATEFILE_NAME).toOSString());
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(resourceDependencies);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Restore dependency database
	 */
	@SuppressWarnings("unchecked")
	private void restoreState() {
		// AssemblyUtils.createLogEntry(IStatus.OK,
		// "ResourceDependencies.restoreState()");
		lock.lock();
		try {
			clean();
			IPath stateLocation = AssemblyEditorPlugin.getDefault().getStateLocation();
			InputStream file = new FileInputStream(stateLocation.append(STATEFILE_NAME).toOSString());
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			resourceDependencies.putAll((Map<String, Set<String>>) input.readObject());
			input.close();
		} catch (Exception e) {
			AssemblyUtils.createLogEntry(e);
			clean();
		} finally {
			lock.unlock();
		}
	}
}
