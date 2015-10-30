package hu.siz.assemblyeditor.editor.assembly;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

import hu.siz.assemblyeditor.editor.AssemblyColorManager;

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
		setDocumentProvider(new TextFileDocumentProvider());
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
