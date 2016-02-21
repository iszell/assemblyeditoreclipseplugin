/**
 * Package:	hu.siz.assemblyeditor.builder
 * Project:	{project_name}
 * File:	{XACompiler.java
 * 
 * Created:	2010.05.07.
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author siz
 * 
 */
public class XACompiler extends AssemblyCompiler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#createCompileCommand()
	 */
	@Override
	protected void createCompileCommand(IProgressMonitor monitor) {
		this.compileCommand.append(this.store
				.getString(PreferenceConstants.P_XAPATH));

		this.compileCommand.append(' ');
		this.compileCommand.append(this.resource.getName().toString());

		this.compileCommand.append(" -o"); //$NON-NLS-1$
		this.compileCommand.append(this.resource.getLocation()
				.removeFileExtension().addFileExtension("prg").toOSString()); //$NON-NLS-1$
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#getDerivedResourceNames
	 * (org.eclipse.core.resources.IResource)
	 */
	@Override
	protected Set<String> getDerivedResourceNames(IResource resource) {
		Set<String> names = new HashSet<String>();
		names.add(resource.getLocation().removeFileExtension()
				.addFileExtension("prg").toFile().getName()); //$NON-NLS-1$

		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#processErrorLine(java.
	 * lang.String)
	 */
	@Override
	protected void processErrorLine(String line) {
		int colonPos = line.indexOf(':');
		if (colonPos != -1) {
			String newLine = line.substring(colonPos + 1);
			if (newLine.startsWith("line ")) {
				int endPos = newLine.indexOf(':');
				int lineNumber = Integer.valueOf(newLine.substring(5, endPos));
				this.handler.addError(this.resource,
						newLine.substring(endPos + 2), lineNumber, null, null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#processInputLine(java.
	 * lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
	}

}
