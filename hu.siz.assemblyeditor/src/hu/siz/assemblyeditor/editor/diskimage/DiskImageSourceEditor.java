package hu.siz.assemblyeditor.editor.diskimage;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @author siz
 * 
 */
public class DiskImageSourceEditor extends FormEditor {
	
	DiskImageSourceEditorForm form;

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (input instanceof IFileEditorInput) {
			super.init(site, input);
		} else {
			throw new PartInitException("Unsupported input type");
		}

	}

	@Override
	protected void addPages() {
		try {
			this.form = new DiskImageSourceEditorForm(this);
			addPage(this.form);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		this.form.save();
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
}
