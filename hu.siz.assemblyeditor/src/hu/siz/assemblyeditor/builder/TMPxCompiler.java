/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Compile using TAss
 * 
 * @author Siz
 * 
 */
public class TMPxCompiler extends AssemblyCompiler {

	private static final String STRING_SEPARATOR = "\""; //$NON-NLS-1$
	private boolean hasListing = false;

	public TMPxCompiler() {
		super();
	}

	@Override
	protected void createCompileCommand(IProgressMonitor monitor) {

		this.compileCommand.append(this.store
				.getString(PreferenceConstants.P_TMPXPATH));

		if (this.compileCommand.length() != 0) {

			// append destinations name
			this.compileCommand.append(" -o "); //$NON-NLS-1$
			this.compileCommand
					.append(this.resource.getLocation().removeFileExtension()
							.addFileExtension("prg").toOSString()); //$NON-NLS-1$

			// append listing file name
			if (this.store
					.getBoolean(PreferenceConstants.P_TASSOPTCREATELISTING)) {
				this.compileCommand.append(" -l "); //$NON-NLS-1$
				this.compileCommand.append(this.resource.getLocation()
						.removeFileExtension()
						.addFileExtension("lst").toOSString()); //$NON-NLS-1$
				this.hasListing = true;
			}

			// append source name
			this.compileCommand.append(" -i "); //$NON-NLS-1$
			this.compileCommand.append(this.resource.getName().toString());
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
		System.err.println(line);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessInputLine(java
	 * .lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
		// Ignore messages on stdin
		System.out.println(line);
		int firstPare = line.indexOf('(');
		if (firstPare >= 0) {
			int nextPare = line.indexOf(')', firstPare);
			if (nextPare >= 0) {
				String lineNumberString = line.substring(firstPare + 1,
						nextPare);
				System.out.println(lineNumberString);
				try {
					int lineNumber = Integer.parseInt(lineNumberString);
					String message = line.substring(line.indexOf(':',
							nextPare + 4) + 2);
					handler.addError(resource, message, lineNumber, null, null);
				} catch (NumberFormatException nfe) {
					// Ignore
				}
			}
		}
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
		names.add(resource.getLocation().removeFileExtension()
				.addFileExtension("prg").toFile().getName()); //$NON-NLS-1$
		if (this.hasListing) {
			names.add(resource.getLocation().removeFileExtension()
					.addFileExtension("lst").toFile().getName()); //$NON-NLS-1$
		}

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

		if (line.contains(".include") || line.contains(".binary")) { //$NON-NLS-1$ //$NON-NLS-2$
			name = line.substring(
					line.indexOf(STRING_SEPARATOR) + 1,
					line.indexOf(STRING_SEPARATOR,
							line.indexOf(STRING_SEPARATOR) + 1));
		}

		return name;
	}
}