package hu.siz.assemblyeditor.editor.assembly;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author Siz
 * 
 */
public class AssemblySourceContentOutlinePage extends ContentOutlinePage {

	private IDocumentProvider documentProvider;
	private IEditorInput editorInput;
	private IEditorPart editorPart;

	public AssemblySourceContentOutlinePage(IDocumentProvider documentProvider,
			IEditorInput editorInput, IEditorPart editorPart) {
		super();
		this.documentProvider = documentProvider;
		this.editorInput = editorInput;
		this.editorPart = editorPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.contentoutline.ContentOutlinePage#
	 * addSelectionChangedListener
	 * (org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		super.addSelectionChangedListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#createControl(
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new AssemblyContentProvider(documentProvider));
		// viewer.setLabelProvider(new MyLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setInput(this.editorInput);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#getTreeStyle()
	 */
	@Override
	protected int getTreeStyle() {
		// TODO Auto-generated method stub
		return super.getTreeStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#fireSelectionChanged
	 * (org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	protected void fireSelectionChanged(ISelection selection) {
		// TODO Auto-generated method stub
		super.fireSelectionChanged(selection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.contentoutline.ContentOutlinePage#getControl()
	 */
	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return super.getControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		// TODO Auto-generated method stub
		return super.getSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#getTreeViewer()
	 */
	@Override
	protected TreeViewer getTreeViewer() {
		// TODO Auto-generated method stub
		return super.getTreeViewer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#init(org.eclipse
	 * .ui.part.IPageSite)
	 */
	@Override
	public void init(IPageSite pageSite) {
		// TODO Auto-generated method stub
		super.init(pageSite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.contentoutline.ContentOutlinePage#
	 * removeSelectionChangedListener
	 * (org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		super.removeSelectionChangedListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#selectionChanged
	 * (org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		super.selectionChanged(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.contentoutline.ContentOutlinePage#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		super.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.contentoutline.ContentOutlinePage#setSelection(org
	 * .eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
		super.setSelection(selection);
	}
}
