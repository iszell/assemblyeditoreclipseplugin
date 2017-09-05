/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.builder.AssemblyBuilder.AssemblyErrorHandler;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Interface to implement a resource compiler
 * 
 * @author Siz
 */
public interface ICompiler {
	/**
	 * Compile one resource
	 */
	public void compile(IResource resource, AssemblyErrorHandler handler, IProgressMonitor monitor,
			IPreferenceStore preferenceStore, String customCompiler, String customOptions);

	/**
	 * Get dependency resource names
	 */
	public Set<String> getDependencies(IResource resource, IProgressMonitor monitor);
}
