package hu.siz.assemblyeditor.editor.assembly;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

import hu.siz.assemblyeditor.AssemblyEditorPlugin;
import hu.siz.assemblyeditor.editor.AssemblyColorManager;
import hu.siz.assemblyeditor.editor.IColorConstants;
import hu.siz.assemblyeditor.preferences.PreferenceConstants;

public class AssemblySourceViewerConfiguration extends
		TextSourceViewerConfiguration {
	private DoubleClickStrategy doubleClickStrategy;
	private AssemblyScanner scanner;
	private AssemblyColorManager colorManager;

	public AssemblySourceViewerConfiguration(AssemblyColorManager colorManager) {
		super();
		this.colorManager = colorManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#
	 * getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		List<String> contentTypes = new ArrayList<String>();
		contentTypes.add(IDocument.DEFAULT_CONTENT_TYPE);
		return contentTypes.toArray(new String[contentTypes.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#
	 * getDoubleClickStrategy(org.eclipse.jface.text.source.ISourceViewer,
	 * java.lang.String)
	 */
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (this.doubleClickStrategy == null)
			this.doubleClickStrategy = new DoubleClickStrategy();
		return this.doubleClickStrategy;
	}

	/**
	 * Returns the source scanner
	 * 
	 * @return
	 */
	private AssemblyScanner getSourceScanner() {
		if (this.scanner == null) {
			this.scanner = new TassScanner(this.colorManager);
			this.scanner.setDefaultReturnToken(new Token(new TextAttribute(
					this.colorManager.getColor(IColorConstants.DEFAULT))));
		}
		return this.scanner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.text.source.SourceViewerConfiguration#getTabWidth(org
	 * .eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public int getTabWidth(ISourceViewer sourceViewer) {
		IPreferenceStore store = AssemblyEditorPlugin.getDefault()
				.getPreferenceStore();

		return store.getInt(PreferenceConstants.P_TABSIZE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#
	 * getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer defaultRepairer = new DefaultDamagerRepairer(
				getSourceScanner());
		reconciler.setDamager(defaultRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(defaultRepairer, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}
}