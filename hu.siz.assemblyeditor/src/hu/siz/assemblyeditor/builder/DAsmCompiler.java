/**
 * Package:	hu.siz.assemblyeditor.builder
 * Project:	hu.siz.assemblyeditor
 * File:	DAsmCompiler.java
 * 
 * Created:	2010.05.14.
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;
import hu.siz.assemblyeditor.utils.AssemblyUtils;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;

/**
 * @author siz
 * 
 */
public class DAsmCompiler extends AssemblyCompiler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#createCompileCommand()
	 */
	@Override
	protected void createCompileCommand() {
		this.compileCommand.append(this.store
				.getString(PreferenceConstants.P_DASMPATH));

		this.compileCommand.append(' ');
		this.compileCommand.append(this.resource.getName().toString());

		this.compileCommand.append(" -v3"); //$NON-NLS-1$

		this.compileCommand.append(" -o"); //$NON-NLS-1$
		this.compileCommand.append(this.resource.getLocation()
				.removeFileExtension().addFileExtension("prg").toOSString()); //$NON-NLS-1$

		//TODO: parse listing for errors (no stdout when listing is used)
//		this.compileCommand.append(" -l"); //$NON-NLS-1$
//		this.compileCommand.append(this.resource.getLocation()
//				.removeFileExtension().addFileExtension("lst").toOSString()); //$NON-NLS-1$
//
//		this.compileCommand.append(" -s"); //$NON-NLS-1$
//		this.compileCommand.append(this.resource.getLocation()
//				.removeFileExtension().addFileExtension("sym").toOSString()); //$NON-NLS-1$
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
		names.add(resource.getLocation().removeFileExtension()
				.addFileExtension("lst").toFile().getName()); //$NON-NLS-1$
		names.add(resource.getLocation().removeFileExtension()
				.addFileExtension("sym").toFile().getName()); //$NON-NLS-1$

		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#processErrorLine(java
	 * .lang.String)
	 */
	@Override
	protected void processErrorLine(String line) {
		// DAsm does not write stderr
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#processInputLine(java
	 * .lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
		int colonPos = line.indexOf(':');
		if (colonPos != -1) {
			int strPos = line.indexOf('(');
			int endPos = line.indexOf(')', strPos + 1);
			if (strPos != -1 && endPos != -1) {
				try {
					int lineNumber = Integer.valueOf(line.substring(strPos + 1,
							endPos));
					this.handler.addError(this.resource, line, lineNumber,
							null, null);
				} catch (NumberFormatException nfe) {
					AssemblyUtils.createLogEntry(nfe);
				}
			}
		}
	}

}
