/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.Messages;
import hu.siz.assemblyeditor.builder.AssemblyBuilder.AssemblyErrorHandler;
import hu.siz.assemblyeditor.utils.AssemblyUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Abstract assembly compiler
 * 
 * @author Siz
 * 
 */
public abstract class AssemblyCompiler implements ICompiler {

	protected IPreferenceStore store;
	protected StringBuilder compileCommand;
	protected IResource resource;
	protected AssemblyErrorHandler handler;
	protected Process process;

	private final static boolean MODE_STDOUT = false;
	private final static boolean MODE_STDERR = true;

	class ConsoleOutputProcessor implements Runnable {

		private boolean mode;

		public ConsoleOutputProcessor(boolean mode) {
			super();
			this.mode = mode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			String currentLine = null;
			BufferedReader in = null;

			if (this.mode == MODE_STDOUT) {
				in = new BufferedReader(new InputStreamReader(
						AssemblyCompiler.this.process.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						AssemblyCompiler.this.process.getErrorStream()));
			}

			try {
				while ((currentLine = in.readLine()) != null) {
					if (this.mode == MODE_STDOUT) {
						processInputLine(currentLine);
					} else {
						processErrorLine(currentLine);
					}
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the compile command string in <code>String compileCommand</code>
	 */
	protected abstract void createCompileCommand();

	/**
	 * Process one line of InputStream
	 */
	protected abstract void processInputLine(String line);

	/**
	 * Process one line of ErrorStream
	 */
	protected abstract void processErrorLine(String line);

	/**
	 * Get derived resource names
	 */
	protected abstract Set<String> getDerivedResourceNames(IResource resource);

	/**
	 * Get derived resource names
	 */
	protected abstract String getDependencyName(String line);

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.ICompiler#getDependencies(org.eclipse.
	 * core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Set<String> getDependencies(IResource resource,
			IProgressMonitor monitor) {
		Set<String> dependencies = new HashSet<String>();

		IFile file = resource.getWorkspace().getRoot()
				.getFile(resource.getFullPath());

		String currentLine = null;
		BufferedReader in = null;

		try {
			in = new BufferedReader(new InputStreamReader(file.getContents()));
			while ((currentLine = in.readLine()) != null) {
				String name = getDependencyName(currentLine);
				if (name != null) {
					if (File.separatorChar == '\\') {
						name = name.replace('/', File.separatorChar);
					}
					IPath dependencyPath = resource.getParent().getFullPath()
							.append(name);
					String extension = dependencyPath.getFileExtension();
					if (extension != null) {
						if (extension.equals("prg")) { //$NON-NLS-1$
							dependencyPath = dependencyPath
									.removeFileExtension().addFileExtension(
											"asm"); //$NON-NLS-1$
						}
						if (extension.equals("d64")) { //$NON-NLS-1$
							dependencyPath = dependencyPath
									.removeFileExtension().addFileExtension(
											"mdc"); //$NON-NLS-1$
						}
					}
					dependencies.add(dependencyPath.toString());
				}
			}
		} catch (Exception e) {
			AssemblyUtils.createLogEntry(e);
		}

		// Set<String> newDependencies = new HashSet<String>();
		// for (String dependency : dependencies) {
		// IResource depResource = resource.getWorkspace().getRoot()
		// .findMember(dependency);
		// if (depResource != null) {
		// newDependencies.addAll(getDependencies(depResource, monitor));
		// } else {
		// System.err.println("Dependency " + dependency
		// + " cannot be resolved");
		// }
		// if (monitor.isCanceled()) {
		// break;
		// }
		// }
		// dependencies.addAll(newDependencies);

		return dependencies;
	}

	/**
	 * Add compilation results to the project
	 */
	protected void addDerivedResource(IResource resource, String name,
			IProgressMonitor monitor) {
		IFile newFile = resource.getWorkspace().getRoot()
				.getFile(resource.getParent().getFullPath().append(name));
		try {
			newFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
			newFile.setDerived(true, monitor);
		} catch (CoreException e) {
			AssemblyUtils.createLogEntry(e);
			try {
				newFile.delete(true, monitor);
			} catch (CoreException e1) {
				AssemblyUtils.createLogEntry(e1);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.ICompiler#compile(org.eclipse.core.resources
	 * .IResource)
	 */
	@Override
	public void compile(IResource resource, AssemblyErrorHandler handler,
			IProgressMonitor monitor, IPreferenceStore preferenceStore) {
		this.resource = resource;
		this.handler = handler;
		this.store = preferenceStore;

		this.compileCommand = new StringBuilder();

		createCompileCommand();

		if (this.compileCommand.length() != 0) {
			try {
				this.process = Runtime.getRuntime().exec(
						this.compileCommand.toString(), null,
						resource.getParent().getLocation().toFile());

				new Thread(new ConsoleOutputProcessor(MODE_STDOUT)).start();
				new Thread(new ConsoleOutputProcessor(MODE_STDERR)).start();

				this.process.waitFor();

				for (String name : getDerivedResourceNames(resource)) {
					addDerivedResource(resource, name, monitor);
				}
			} catch (Exception e) {
				handler.addError(resource, e.getLocalizedMessage(), null, null,
						null);
			}
		} else {
			handler.addError(resource,
					Messages.AssemblyCompiler_CreateCompileCommandFailed, null,
					null, null);
		}
	}
}