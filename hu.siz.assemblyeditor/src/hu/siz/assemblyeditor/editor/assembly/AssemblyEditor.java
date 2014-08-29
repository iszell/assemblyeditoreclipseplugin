package hu.siz.assemblyeditor.editor.assembly;

import hu.siz.assemblyeditor.editor.AssemblyColorManager;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * Assembly source editor
 * 
 * @author Siz
 * 
 */
public class AssemblyEditor extends TextEditor {

	private AssemblyColorManager colorManager;

	/**
	 * 
	 */
	public AssemblyEditor() {
		super();

		this.colorManager = new AssemblyColorManager();
		setSourceViewerConfiguration(new AssemblySourceViewerConfiguration(
				this.colorManager));
		setDocumentProvider(new AssemblyDocumentProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			return new AssemblySourceContentOutlinePage(getDocumentProvider(),
					getEditorInput(), this);
		}
		return super.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#dispose()
	 */
	@Override
	public void dispose() {
		this.colorManager.dispose();
		super.dispose();
	}
}
