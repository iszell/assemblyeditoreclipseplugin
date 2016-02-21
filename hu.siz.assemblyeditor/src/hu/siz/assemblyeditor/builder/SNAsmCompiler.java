/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author siz
 *
 */
public class SNAsmCompiler extends AssemblyCompiler {

	/* (non-Javadoc)
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#createCompileCommand()
	 */
	@Override
	protected void createCompileCommand(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#getDependencyName(java.lang.String)
	 */
	@Override
	protected String getDependencyName(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#getDerivedResourceNames(org.eclipse.core.resources.IResource)
	 */
	@Override
	protected Set<String> getDerivedResourceNames(IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#processErrorLine(java.lang.String)
	 */
	@Override
	protected void processErrorLine(String line) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.siz.assemblyeditor.builder.AssemblyCompiler#processInputLine(java.lang.String)
	 */
	@Override
	protected void processInputLine(String line) {
		// TODO Auto-generated method stub

	}

}
