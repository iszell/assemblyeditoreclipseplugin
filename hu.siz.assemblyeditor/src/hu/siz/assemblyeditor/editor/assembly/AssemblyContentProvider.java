/**
 * 
 */
package hu.siz.assemblyeditor.editor.assembly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.viewers.ITreePathContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * @author Siz
 * 
 */
public class AssemblyContentProvider implements ITreePathContentProvider {

	private IDocumentProvider documentProvider;
	private Map<IFileEditorInput, List<String>> labels = new HashMap<IFileEditorInput, List<String>>();

	public AssemblyContentProvider(IDocumentProvider documentProvider) {
		super();
		this.documentProvider = documentProvider;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		System.out.println("inputChanged: " + oldInput + "->" + newInput);
		if (newInput instanceof IFileEditorInput) {
			processEditorInput((IFileEditorInput) newInput);
		}
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreePathContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		String[] result;
		List<String> labelList = labels.get(inputElement);
		if (labelList == null) {
			result = new String[0];
		} else {
			result = labelList.toArray(new String[labelList.size()]);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreePathContentProvider#getChildren(org.eclipse.jface.viewers.TreePath)
	 */
	@Override
	public Object[] getChildren(TreePath parentPath) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreePathContentProvider#hasChildren(org.eclipse.jface.viewers.TreePath)
	 */
	@Override
	public boolean hasChildren(TreePath path) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreePathContentProvider#getParents(java.lang.Object)
	 */
	@Override
	public TreePath[] getParents(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param editorInput
	 */
	private void processEditorInput(IFileEditorInput editorInput) {
		IDocument document = documentProvider.getDocument(editorInput);
		String category = ((FastPartitioner) document.getDocumentPartitioner())
				.getManagingPositionCategories()[0];
		try {
			Position[] positions = document.getPositions(category);
			List<String> labels = new ArrayList<String>();
			for (Position p : positions) {
				if (((TypedPosition) p).getType().equals(
						AssemblyPartitionScanner.ASM_STRING)) {
					String label = document.get(p.offset, p.length);
					labels.add(label);
				}
			}
			this.labels.put(editorInput, labels);
		} catch (BadPositionCategoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
