/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import hu.siz.assemblyeditor.preferences.PreferenceConstants;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;

/**
 * Compile using TAss
 * 
 * @author Siz
 * 
 */
public class TAssCompiler extends AssemblyCompiler {

	private static final String STRING_SEPARATOR = "\""; //$NON-NLS-1$
	private boolean hasListing = false;

	public TAssCompiler() {
		super();
	}

	@Override
	protected void createCompileCommand() {

		this.compileCommand.append(this.store
				.getString(PreferenceConstants.P_TASSPATH));

		if (this.compileCommand.length() != 0) {

			// append other options
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.store
					.getString(PreferenceConstants.P_TASSCMDLINE));

			// append architecture
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.store
					.getString(PreferenceConstants.P_TASSOPTARCHITECTURE));

			// append case sensitive compilation
			if (this.store
					.getBoolean(PreferenceConstants.P_TASSOPTCASESENSITIVE)) {
				this.compileCommand.append(" --case-sensitive"); //$NON-NLS-1$
			}

			// append long branch handling
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTLONGBRANCH)) {
				this.compileCommand.append(" --long-branch"); //$NON-NLS-1$
			}

			// append non linear output generation
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTNONLINEAR)) {
				this.compileCommand.append(" --nonlinear"); //$NON-NLS-1$
			}

			// append start address supression
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTNOSTART)) {
				this.compileCommand.append(" --nostart"); //$NON-NLS-1$
			}

			// append word start address creation
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTWORDSTART)) {
				this.compileCommand.append(" --wordstart"); //$NON-NLS-1$
			}

			// append destinations name
			this.compileCommand.append(" -o "); //$NON-NLS-1$
			this.compileCommand
					.append(this.resource.getLocation().removeFileExtension()
							.addFileExtension("prg").toOSString()); //$NON-NLS-1$

			// append listing file name
			if (this.store
					.getBoolean(PreferenceConstants.P_TASSOPTCREATELISTING)) {
				this.compileCommand.append(" -L "); //$NON-NLS-1$
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
	 * @see
	 * hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessErrorLine(java
	 * .lang.String)
	 */
	@Override
	protected void processErrorLine(String line) {
		Integer lineNumber = null;
		String message = null;
		int colonPosition;
		if (line.indexOf("[**Fatal**]") != -1 && line.indexOf('(') > -1) { //$NON-NLS-1$
			String tmpLine = line.substring(line.indexOf('(') + 1);
			colonPosition = tmpLine.indexOf(':');
			if (colonPosition != -1) {
				// Integer value between the first and second ':'
				lineNumber = Integer.valueOf(tmpLine.substring(
						colonPosition + 1, tmpLine.indexOf(')',
								colonPosition + 1)));
				message = tmpLine.substring(tmpLine.indexOf(']') + 2);
			}
		} else {
			colonPosition = line.indexOf(':');
			if (colonPosition != -1) {
				// Integer value between the first and second ':'
				lineNumber = Integer.valueOf(line.substring(colonPosition + 1,
						line.indexOf(':', colonPosition + 1)));
				message = line
						.substring(line.indexOf(':', colonPosition + 1) + 2);
			}
		}
		if (message == null) {
			message = line;
		}
		// String after the space following the second ':'

		if (message.startsWith("warning")) { //$NON-NLS-1$
			message = message.substring(9);
			this.handler.addWarning(this.resource, message, lineNumber, null,
					null);
		} else {
			this.handler.addError(this.resource, message, lineNumber, null,
					null);
		}
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
		// Ignore messages on stdin
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
			name = line.substring(line.indexOf(STRING_SEPARATOR) + 1, line
					.indexOf(STRING_SEPARATOR,
							line.indexOf(STRING_SEPARATOR) + 1));
		}

		return name;
	}
}