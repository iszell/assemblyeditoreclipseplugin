/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;

/**
 * Create D64 image with MakeDisk
 * 
 * @author Siz
 * 
 */
public class MakeDiskCompiler extends AssemblyCompiler {

	private int lineNumber;

	/**
	 * 
	 */
	public MakeDiskCompiler() {
		super();
		this.lineNumber = 0;
	}

	@Override
	protected void createCompileCommand() {
		this.compileCommand.append(this.store
				.getString(PreferenceConstants.P_MAKEDISKPATH));

		if (this.compileCommand.length() != 0) {
			// append D64 name
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.resource.getLocation()
					.removeFileExtension().addFileExtension("d64").toString()); //$NON-NLS-1$

			// append source name
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.resource.getName().toString());

			// append disk name
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.resource.getLocation()
					.removeFileExtension().lastSegment().toUpperCase()); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessErrorLine(java
	 * .lang.String)
	 */
	@Override
	protected void processErrorLine(String line) {
		// No errors written to stderr
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessInputLine(java
	 * .lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
		this.lineNumber++;
		if (line.startsWith("Error ")) //$NON-NLS-1$
			this.handler.addError(this.resource, line, this.lineNumber, null,
					null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * hu.siz.assemblyeditor.builder.ICompiler#getDerviceResourceNames(org.
	 * eclipse.core.resources.IResource)
	 */
	@Override
	protected Set<String> getDerivedResourceNames(IResource resource) {
		Set<String> names = new HashSet<String>();
		names.add(resource.getLocation().removeFileExtension()
				.addFileExtension("d64").toFile().getName()); //$NON-NLS-1$

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
		String name = null;

		int splitpos = line.indexOf(" ");
		splitpos = splitpos >= 0 ? splitpos : line.indexOf("\t");
		if (splitpos >= 0) { //$NON-NLS-1$
			name = line.substring(0, splitpos); //$NON-NLS-1$
		}

		return name;
	}
}