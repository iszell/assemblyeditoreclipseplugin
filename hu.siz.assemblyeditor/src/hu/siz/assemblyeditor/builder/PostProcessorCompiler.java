/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;
import hu.siz.assemblyeditor.utils.AssemblyUtils;

/**
 * Create D64 image with MakeDisk
 * 
 * @author Siz
 * 
 */
public class PostProcessorCompiler extends AssemblyCompiler {

	private IPath oldPath;
	private IPath newPath;

	/**
	 * 
	 */
	public PostProcessorCompiler() {
		super();
	}

	@Override
	protected void createCompileCommand(IProgressMonitor monitor, String customCompiler, String customOptions) {
		String command = customCompiler != null ? customCompiler
				: this.store.getString(PreferenceConstants.P_POSTPROCESSORPATH);
		this.compileCommand.append(command);

		if (this.compileCommand.length() != 0) {
			oldPath = this.resource.getFullPath();
			newPath = oldPath.removeFileExtension().addFileExtension("PostProcessorTemp");
			String commandLine = customOptions != null ? customOptions
					: this.store.getString(PreferenceConstants.P_POSTPROCESSCMDLINE);
			commandLine = commandLine.replace("&i", newPath.lastSegment());
			commandLine = commandLine.replace("&o", oldPath.lastSegment());
			this.compileCommand.append(' ');
			this.compileCommand.append(commandLine);
		}
	}

	@Override
	protected void init(IProgressMonitor monitor) {
		try {
			this.resource.move(newPath, true, monitor);
		} catch (CoreException e) {
			AssemblyUtils.createLogEntry(e);
		}
	}

	@Override
	protected void cleanup(IProgressMonitor monitor) {
		try {
			this.resource.getParent().findMember(newPath.lastSegment()).delete(false, monitor);
		} catch (CoreException e) {
			AssemblyUtils.createLogEntry(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessErrorLine(java
	 * .lang.String)
	 */
	@Override
	protected void processErrorLine(String line) {
		// No errors written to stderr
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessInputLine(java
	 * .lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.ICompiler#getDerviceResourceNames(org.
	 * eclipse.core.resources.IResource)
	 */
	@Override
	protected Set<String> getDerivedResourceNames(IResource resource) {
		Set<String> names = new HashSet<String>();
		names.add(oldPath.lastSegment());
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#getDependencyName(java
	 * .lang.String)
	 */
	@Override
	protected String getDependencyName(String line) {
		return null;
	}
}