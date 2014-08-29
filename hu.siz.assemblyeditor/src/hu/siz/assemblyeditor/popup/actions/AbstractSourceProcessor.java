package hu.siz.assemblyeditor.popup.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class AbstractSourceProcessor implements IObjectActionDelegate {

	private final static String ENCODING = "ISO-8859-1"; //$NON-NLS-1$
	private ISelection selection;

	// String msgText = "";

	/**
	 * Source conversion framework
	 */
	public AbstractSourceProcessor() {
		super();
	}

	/**
	 * AbstractSourceProcessor one character
	 * <p>
	 * <p>
	 * This method should be implemented by subclasses
	 */
	abstract void processChar(StringBuilder s, int b);

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// Nothing to do
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
	public void run(IAction action) {

		// FIXME Check for unsaved sources before processing
		if (!this.selection.isEmpty()
				&& this.selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) this.selection)
					.getFirstElement();

			if (element instanceof IFile) {
				try {
					IFile file = (IFile) element;
					InputStream is = file.getContents();
					int b;
					StringBuilder source = new StringBuilder();

					while ((b = is.read()) > -1) {
						processChar(source, b);
					}
					
					is.close();

					InputStream os = new ByteArrayInputStream(source.toString()
							.getBytes(ENCODING));
					
					NullProgressMonitor monitor = new NullProgressMonitor();
					file.setContents(os, false, true, monitor);
					
					os.close();

					file.refreshLocal(IResource.DEPTH_ZERO, monitor);

				} catch (Exception e) {
					Shell shell = new Shell();
					MessageDialog.openInformation(shell,
							"Something went wrong!", e.toString());
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
}
