/**
 * 
 */
package hu.siz.assemblyeditor.builder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;

import hu.siz.assemblyeditor.builder.AssemblyBuilder.AssemblyErrorHandler;
import hu.siz.assemblyeditor.builder.diskimage.DiskFile;
import hu.siz.assemblyeditor.builder.diskimage.DiskFullException;
import hu.siz.assemblyeditor.builder.diskimage.DiskImage;
import hu.siz.assemblyeditor.builder.diskimage.DiskImageModelHandler;

/**
 * @author siz
 * 
 */
public class DiskImageBuilder implements ICompiler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.ICompiler#compile(org.eclipse.core.
	 * resources .IResource,
	 * hu.siz.assemblyeditor.builder.AssemblyBuilder.AssemblyErrorHandler,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void compile(IResource resource, AssemblyErrorHandler handler, IProgressMonitor monitor,
			IPreferenceStore preferenceStore) {
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			DiskImage model = DiskImageModelHandler.loadModel(file);
			DiskImage image = new DiskImage(model);
			if (model.getFiles() != null) {
				for (DiskFile diskFile : model.getFiles()) {
					if (diskFile.getSource() != null && diskFile.getSource().exists()) {
						try {
							System.out.println("Adding file " + diskFile);
							image.add(diskFile.getName(), diskFile.getSource(), diskFile.getType(), diskFile.isLocked(),
									diskFile.isClosed());
						} catch (DiskFullException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			IFile imageFile = file.getWorkspace().getRoot().getFile(file.getFullPath().removeFileExtension()
					.addFileExtension(image.getImageDescriptor().getImageType().toString().toLowerCase()));
			try {
				imageFile.delete(true, monitor);
				imageFile.create(image.getImage(), true, monitor);
				imageFile.setDerived(true, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DiskFullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.siz.assemblyeditor.builder.ICompiler#getDependencies(org.eclipse.
	 * core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Set<String> getDependencies(IResource resource, IProgressMonitor monitor) {
		Set<String> dependencies = new HashSet<String>();

		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			DiskImage model = DiskImageModelHandler.loadModel(file);
			if (model.getFiles() != null) {
				for (DiskFile diskFile : model.getFiles()) {
					if (diskFile.getSource() != null && diskFile.getSource().exists()) {
						IPath dependencyPath = diskFile.getSource().getFullPath();
						String extension = dependencyPath.getFileExtension();
						if (extension != null) {
							if (extension.equals("prg")) { //$NON-NLS-1$
								dependencyPath = dependencyPath.removeFileExtension().addFileExtension("asm"); //$NON-NLS-1$
							}
						}
						dependencies.add(dependencyPath.toString());
					}
				}
			}
		}
		return dependencies;
	}
}
