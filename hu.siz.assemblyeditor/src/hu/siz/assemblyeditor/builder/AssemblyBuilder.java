package hu.siz.assemblyeditor.builder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;
import hu.siz.assemblyeditor.Messages;
import hu.siz.assemblyeditor.preferences.PreferenceConstants;
import hu.siz.assemblyeditor.properties.PropertyStore;
import hu.siz.assemblyeditor.utils.AssemblyUtils;

public class AssemblyBuilder extends IncrementalProjectBuilder {

	private static final String EXT_ASM = "asm"; //$NON-NLS-1$
	private static final String EXT_DIS = "dis"; //$NON-NLS-1$
	private static final String EXT_INC = "inc"; //$NON-NLS-1$
	private static final String EXT_MDC = "mdc"; //$NON-NLS-1$

	private static final String CUSTOMPREFIX = ";#$"; //$NON-NLS-1$
	private static final String PREFIX_COMPILER = CUSTOMPREFIX + "compiler="; //$NON-NLS-1$
	private static final String PREFIX_COMPILEROPTIONS = CUSTOMPREFIX + "compilerOptions="; //$NON-NLS-1$
	private static final String PREFIX_POSTPROCESSOR = CUSTOMPREFIX + "postProcessor="; //$NON-NLS-1$
	private static final String PREFIX_POSTPROCESSOROPTIONS = CUSTOMPREFIX + "postProcessorOptions="; //$NON-NLS-1$

	class AssemblyDeltaVisitor implements IResourceDeltaVisitor {

		private IProgressMonitor monitor;

		/**
		 * Delta visitor for incremental project builder
		 * 
		 * @param monitor
		 */
		public AssemblyDeltaVisitor(IProgressMonitor monitor) {
			super();
			this.monitor = monitor;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse
		 * .core.resources.IResourceDelta)
		 */
		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				compileResource(resource, this.monitor);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				compileResource(resource, this.monitor);
				break;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	class AssemblyResourceVisitor implements IResourceVisitor {
		private IProgressMonitor monitor;
		public static final boolean MODE_CLEAN = false;
		public static final boolean MODE_BUILD = true;
		private boolean mode;

		/**
		 * Resource visitor for project full build
		 * 
		 * @param IProgressMonitor
		 *            monitor - progress monitor for process
		 * @param boolean
		 *            mode - MODE_CLEAN - project is cleaned; MODE_BUILD - project is
		 *            built
		 */
		public AssemblyResourceVisitor(IProgressMonitor monitor, boolean mode) {
			super();
			this.mode = mode;
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResource resource) {
			if (this.mode == MODE_CLEAN) {
				cleanResource(resource, this.monitor);
			} else {
				compileResource(resource, this.monitor);
			}
			// return true to continue visiting children.
			return true;
		}
	}

	/*
	 * Internal error handler class: adds resource markers
	 */
	class AssemblyErrorHandler {
		public void addError(IResource resource, String message, Integer lineNumber, Integer startCol, Integer endCol) {
			addMarker(resource, message, lineNumber, IMarker.SEVERITY_ERROR, startCol, endCol);
		}

		public void addWarning(IResource resource, String message, Integer lineNumber, Integer startCol,
				Integer endCol) {
			addMarker(resource, message, lineNumber, IMarker.SEVERITY_WARNING, startCol, endCol);
		}

		public void addInfo(IResource resource, String message, Integer lineNumber, Integer startCol, Integer endCol) {
			addMarker(resource, message, lineNumber, IMarker.SEVERITY_INFO, startCol, endCol);
		}

		private void addMarker(IResource resource, String message, Integer lineNumber, int severity, Integer startCol,
				Integer endCol) {
			try {
				IMarker marker = resource.createMarker(MARKER_TYPE);
				marker.setAttribute(IMarker.MESSAGE, message);
				marker.setAttribute(IMarker.SEVERITY, severity);
				int actualLineNumber = 1;
				if (lineNumber != null) {
					actualLineNumber = lineNumber;
				}
				marker.setAttribute(IMarker.LINE_NUMBER, actualLineNumber);
				if (startCol != null) {
					// marker.setAttribute(IMarker.CHAR_START, startCol);
				}
				if (endCol != null) {
					// marker.setAttribute(IMarker.CHAR_END, endCol);
				}
			} catch (CoreException e) {
				AssemblyUtils.createLogEntry(e);
			}
		}
	}

	public static final String BUILDER_ID = "hu.siz.assemblyeditor.assemblyBuilder"; //$NON-NLS-1$
	public static final String MARKER_TYPE = "hu.siz.assemblyeditor.AssemblyMarker"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD || kind == CLEAN_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				build(FULL_BUILD, args, monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		ResourceDependencies.getInstance().clean(getProject());
		getProject().accept(new AssemblyResourceVisitor(monitor, AssemblyResourceVisitor.MODE_CLEAN));
	}

	/*
	 * Compiles one resource
	 */
	private void compileResource(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile) {

			monitor.subTask(Messages.AssemblyBuilder_Compiling + resource.getName());

			ICompiler compiler = null;
			deleteMarkers((IFile) resource);

			IPreferenceStore store = new PropertyStore(resource, AssemblyEditorPlugin.getDefault().getPreferenceStore(),
					AssemblyEditorPlugin.PLUGIN_ID);

			String ext = resource.getFileExtension();
			String[] customSettings = getCustomCompileOptions(resource);
			if (EXT_ASM.equalsIgnoreCase(ext) || EXT_INC.equalsIgnoreCase(ext)) {
				String compilername = customSettings[0] != null ? customSettings[0]
						: store.getString(PreferenceConstants.P_COMPILER);

				if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_64TASS)) {
					compiler = new TAssCompiler();
				} else if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_AS65)) {
					compiler = new As65Compiler();
				} else if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_CA65)) {
					compiler = new CA65Compiler();
				} else if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_DASM)) {
					compiler = new DAsmCompiler();
				} else if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_SNAsm)) {
					compiler = new SNAsmCompiler();
				} else if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_TMPX)) {
					compiler = new TMPxCompiler();
				} else if (compilername.equalsIgnoreCase(PreferenceConstants.P_COMPILER_XA)) {
					compiler = new XACompiler();
				}
			} else if (EXT_MDC.equalsIgnoreCase(ext)) {
				compiler = new MakeDiskCompiler();
			} else if (EXT_DIS.equalsIgnoreCase(ext)) {
				compiler = new DiskImageBuilder();
			}

			if (compiler != null) {
				if (!EXT_INC.equalsIgnoreCase(ext)) {
					compiler.compile(resource, new AssemblyErrorHandler(), monitor, store, null, customSettings[1]);
					String postProcessor = customSettings[2] != null ? customSettings[2]
							: store.getString(PreferenceConstants.P_POSTPROCESSORPATH);
					if (postProcessor != null && postProcessor.length() != 0) {
						IResource prg = resource.getParent().findMember(
								resource.getFullPath().removeFileExtension().addFileExtension("prg").lastSegment());
						if (prg != null) {
							new PostProcessorCompiler().compile(prg, new AssemblyErrorHandler(), monitor, store,
									customSettings[2], customSettings[3]);
						}
					}
				}
				addDependencies(resource, compiler, monitor);
			}

			/*
			 * Compile dependent resources
			 */
			Set<String> dependencies = ResourceDependencies.getInstance().get(resource);
			if (dependencies != null) {
				for (String dependent : dependencies) {
					IResource res = resource.getWorkspace().getRoot().findMember(dependent);
					if (res != null) {
						// System.out.println("Compiling dependent resource "
						// + res.getName());
						this.compileResource(res, monitor);
					}
					if (monitor.isCanceled()) {
						break;
					}
				}
			}
		}
	}

	private String[] getCustomCompileOptions(IResource resource) {
		String[] result = new String[4];
		try {
			IFile file = resource.getWorkspace().getRoot().getFile(resource.getFullPath());

			String currentLine = null;
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(file.getContents()));
			while ((currentLine = in.readLine()) != null) {
				if (currentLine.startsWith(PREFIX_COMPILER)) {
					result[0] = currentLine.substring(PREFIX_COMPILER.length());
				} else if (currentLine.startsWith(PREFIX_COMPILEROPTIONS)) {
					result[1] = currentLine.substring(PREFIX_COMPILEROPTIONS.length());
				} else if (currentLine.startsWith(PREFIX_POSTPROCESSOR)) {
					result[2] = currentLine.substring(PREFIX_POSTPROCESSOR.length());
				} else if (currentLine.startsWith(PREFIX_POSTPROCESSOROPTIONS)) {
					result[3] = currentLine.substring(PREFIX_POSTPROCESSOROPTIONS.length());
				}
			}
		} catch (Exception e) {
			AssemblyUtils.createLogEntry(e);
		}
		return result;
	}

	private void cleanResource(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile && resource.isDerived()) {
			try {
				resource.delete(true, monitor);
			} catch (CoreException e) {
				AssemblyUtils.createLogEntry(e);
			}
		}
	}

	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
			AssemblyUtils.createLogEntry(ce);
		}
	}

	/*
	 * Adds dependency links to all resources which the current is depends on
	 */
	private void addDependencies(IResource resource, ICompiler compiler, IProgressMonitor monitor) {
		monitor.subTask(Messages.AssemblyBuilder_AddingDependencies + resource.getName());

		String resourcePath = resource.getFullPath().toString();
		Set<String> dependencies = compiler.getDependencies(resource, monitor);
		if (dependencies != null) {
			for (String dep : dependencies) {
				if (monitor.isCanceled()) {
					break;
				}
				IResource depRes = resource.getWorkspace().getRoot().findMember(dep);
				String dependency;
				if (depRes != null) {
					dependency = depRes.getFullPath().toString();
				} else {
					// System.out.println("Can't find dependency " + dep);
					dependency = dep;
				}
				// System.out.println("Adding dependency " + resourcePath
				// + " for " + dependency);
				ResourceDependencies.getInstance().add(dependency, resourcePath);
			}
		}
	}

	private void fullBuild(final IProgressMonitor monitor) throws CoreException {
		getProject().accept(new AssemblyResourceVisitor(monitor, AssemblyResourceVisitor.MODE_BUILD));
	}

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new AssemblyDeltaVisitor(monitor));
	}
}
