package hu.siz.assemblyeditor.wizards;

import org.eclipse.core.resources.*;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewAssemblyProjectWizardPage extends WizardPage {
	private Text projectName;
	private ISelection selection;

	/**
	 * Constructor for NewAssemblySourceWizardPage.
	 * 
	 * @param pageName
	 */
	public NewAssemblyProjectWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Assembly project");
		setDescription("This wizard creates a new assembly project.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite project = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		project.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label = new Label(project, SWT.NULL);
		label.setText("&Project name:");

		this.projectName = new Text(project, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		this.projectName.setLayoutData(gd);
		this.projectName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		initialize();
		dialogChanged();
		setControl(project);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (this.selection != null && this.selection.isEmpty() == false
				&& this.selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) this.selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IWorkspace) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				this.projectName.setText(container.getFullPath().toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(getProjectName());

		if (getProjectName().length() == 0) {
			updateStatus("Project name must be specified");
			return;
		} 
		
		if (container != null) {
			updateStatus("Project already exists");
			return;
		} 

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return this.projectName.getText();
	}
}