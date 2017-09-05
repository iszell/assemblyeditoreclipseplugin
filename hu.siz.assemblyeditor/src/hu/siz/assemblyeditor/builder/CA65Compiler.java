/**
 * Package:	hu.siz.assemblyeditor.builder
 * File:	As65Compiler.java
 * 
 * Created:	2010.05.07.
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author siz
 * 
 */
public class CA65Compiler extends AssemblyCompiler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#createCompileCommand()
	 */
	@Override
	protected void createCompileCommand(IProgressMonitor monitor, String customCompiler, String customOptions) {
		this.compileCommand.append(this.store
				.getString(PreferenceConstants.P_CA65PATH));
		this.compileCommand.append(this.store
				.getString(" --cpu 6502 "));
		this.compileCommand.append(this.resource.getName().toString());
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
		// TODO Auto-generated method stub
		return null;
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
		System.err.println(line);
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
		System.out.println(line);
	}

}
