/**
 * 
 */
package hu.siz.assemblyeditor.editor.diskimage;

import hu.siz.assemblyeditor.Messages;
import hu.siz.assemblyeditor.builder.diskimage.DiskImage;
import hu.siz.assemblyeditor.builder.diskimage.DiskImageModelHandler;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * @author siz
 * 
 */
public class DiskImageSourceEditorForm extends FormPage {

	private DiskImage model;
	private IFile modelFile;

	private Text diskHeader;

	public DiskImageSourceEditorForm(FormEditor editor)
			throws PartInitException {
		super(editor, "first", Messages.DiskImageSourceEditorForm_Label); //$NON-NLS-1$
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			this.modelFile = ((IFileEditorInput) input).getFile();
			this.model = DiskImageModelHandler.loadModel(this.modelFile);
		} else
			throw new PartInitException("Unsupported input type");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(Messages.DiskImageSourceEditorForm_Label);
		Composite body = form.getBody();
		GridLayout layout = new GridLayout(2, false);
		body.setLayout(layout);
		toolkit.createLabel(body, "Image header");
		this.diskHeader = toolkit.createText(body, this.model.getName());
		this.diskHeader.setTextLimit(16);

		Hyperlink link = toolkit.createHyperlink(body, "link", SWT.WRAP);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				MessageDialog.openInformation(getEditorSite().getShell(),
						"Próba", "Link activated!");
			}
		});
	}

	public void save() {
		fillModel();
		DiskImageModelHandler.saveModel(this.modelFile, this.model);
	}

	private void fillModel() {
		// TODO fillModel();
	}
}
