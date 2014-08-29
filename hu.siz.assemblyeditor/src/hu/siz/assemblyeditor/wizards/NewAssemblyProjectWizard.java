package hu.siz.assemblyeditor.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;

import hu.siz.assemblyeditor.Messages;
import hu.siz.assemblyeditor.builder.AssemblyBuilder;
import hu.siz.assemblyeditor.builder.AssemblyNature;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.*;

/**
 * This is a new project wizard. Its role is to create a new assembly project.
 */

public class NewAssemblyProjectWizard extends Wizard implements INewWizard {
	private NewAssemblyProjectWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewAssemblySourceWizard.
	 */
	public NewAssemblyProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		this.page = new NewAssemblyProjectWizardPage(this.selection);
		addPage(this.page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String projectName = this.page.getProjectName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(projectName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(),
					Messages.NewAssemblyProjectWizard_Error, realException
							.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	// TODO Add perspective switch after project creation and select newly created project
	private void doFinish(String projectName, IProgressMonitor monitor)
			throws CoreException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject(projectName);
		IProjectDescription projectDescription = workspace
				.newProjectDescription(projectName);

		String natures[] = { AssemblyNature.NATURE_ID };
		ICommand buildCommand = projectDescription.newCommand();
		buildCommand.setBuilderName(AssemblyBuilder.BUILDER_ID);
		ICommand buildSpec[] = { buildCommand };

		projectDescription.setBuildSpec(buildSpec);
		projectDescription.setNatureIds(natures);
		project.create(projectDescription, monitor);
		project.open(monitor);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}