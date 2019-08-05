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
public class TAssCompiler extends AssemblyCompiler {

	private static final String STRING_SEPARATOR = "\""; //$NON-NLS-1$
	private boolean hasListing = false;
	private boolean hasLabels = false;

	public TAssCompiler() {
		super();
	}

	@Override
	protected void createCompileCommand(IProgressMonitor monitor, String customCompiler, String customOptions) {

		this.compileCommand.append(this.store.getString(PreferenceConstants.P_TASSPATH));

		if (this.compileCommand.length() != 0) {

			if (customOptions != null) {
				this.compileCommand.append(' ');
				this.compileCommand.append(customOptions);
			}

			// append other options
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.store.getString(PreferenceConstants.P_TASSCMDLINE));

			// append architecture
			this.compileCommand.append(" "); //$NON-NLS-1$
			this.compileCommand.append(this.store.getString(PreferenceConstants.P_TASSOPTARCHITECTURE));

			// append case sensitive compilation
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTCASESENSITIVE)) {
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
			this.compileCommand.append('"');
			this.compileCommand
					.append(this.resource.getLocation().removeFileExtension().addFileExtension("prg").toOSString()); //$NON-NLS-1$
			this.compileCommand.append('"');

			// append listing file name
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTCREATELISTING)) {
				this.compileCommand.append(" -L "); //$NON-NLS-1$
				this.compileCommand.append('"');
				this.compileCommand
						.append(this.resource.getLocation().removeFileExtension().addFileExtension("lst").toOSString()); //$NON-NLS-1$
				this.compileCommand.append('"');
				this.hasListing = true;
			}

			// append label file name
			if (this.store.getBoolean(PreferenceConstants.P_TASSOPTCREATELABELS)) {
				this.compileCommand.append(" -l "); //$NON-NLS-1$
				this.compileCommand.append('"');
				this.compileCommand
						.append(this.resource.getLocation().removeFileExtension().addFileExtension("lbl").toOSString()); //$NON-NLS-1$
				this.compileCommand.append('"');
				this.hasLabels = true;
			}

			// append source name
			this.compileCommand.append(" -i "); //$NON-NLS-1$
			this.compileCommand.append('"');
			this.compileCommand.append(this.resource.getName().toString());
			this.compileCommand.append('"');
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
		final String[] parts = line.split(":");
		final int i_filename;
		final int i_lineNumber;
		final Integer i_column;
		final Integer i_serverity;
		final int i_message;

		if ("new".equals(this.store.getString(PreferenceConstants.P_TASSVERSION))) {
			/*
			 * Ignore lines with less than 5 parts (1: filename, 2: line number,
			 * 3: column, 4: severity, 5+: message)
			 */
			i_filename = 0;
			i_lineNumber = 1;
			i_column = 2;
			i_serverity = 3;
			i_message = 4;
		} else {
			i_filename = 0;
			i_lineNumber = 1;
			i_column = null;
			i_serverity = null;
			i_message = 2;
		}

		if (parts.length > i_message) {
			final String filename = parts[i_filename].trim();
			final Integer lineNumber = Integer.parseInt(parts[i_lineNumber]);
			final Integer column = i_column == null ? null : Integer.parseInt(parts[i_column]);
			final String severity = i_serverity == null ? "error" : parts[i_serverity].trim();
			final StringBuilder sb = new StringBuilder();
			for (int i = i_message; i < parts.length; i++) {
				if (sb.length() != 0) {
					sb.append(":");
				}
				sb.append(parts[i]);
			}
			final String message = sb.toString().trim();
			final IResource res = this.resource.getProject()
					.findMember(this.resource.getProjectRelativePath().removeLastSegments(1).append(filename));
			if (res != null) {
				if ("warning".equals(severity)) {
					this.handler.addWarning(res, message, lineNumber, column, null);
				} else if ("error".equals(severity) || "fatal error".equals(severity)) {
					this.handler.addError(res, message, lineNumber, column, null);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#ProcessInputLine(java
	 * .lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
		System.out.println(line);
		// Ignore messages on stdin
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
		names.add(resource.getLocation().removeFileExtension().addFileExtension("prg").toFile().getName()); //$NON-NLS-1$
		if (this.hasListing) {
			names.add(resource.getLocation().removeFileExtension().addFileExtension("lst").toFile().getName()); //$NON-NLS-1$
		}
		if (this.hasLabels) {
			names.add(resource.getLocation().removeFileExtension().addFileExtension("lbl").toFile().getName()); //$NON-NLS-1$
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
			name = line.substring(line.indexOf(STRING_SEPARATOR) + 1,
					line.indexOf(STRING_SEPARATOR, line.indexOf(STRING_SEPARATOR) + 1));
		}

		return name;
	}
}