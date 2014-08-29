/**
 * Package:	hu.siz.assemblyeditor.builder.diskimage
 * Project:	hu.siz.assemblyeditor
 * File:	DiskImageModelHandler.java
 * 
 * Created:	2010.10.21.
 */
package hu.siz.assemblyeditor.builder.diskimage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author siz
 * 
 */
public class DiskImageModelHandler {

	private static final String ATTRNAME_CLOSED = "closed"; //$NON-NLS-1$
	private static final String ATTRNAME_ID = "id"; //$NON-NLS-1$
	private static final String ATTRNAME_INTERLEAVE = "interleave"; //$NON-NLS-1$
	private static final String ATTRNAME_LOCKED = "locked"; //$NON-NLS-1$
	private static final String ATTRNAME_NAME = "name"; //$NON-NLS-1$
	private static final String ATTRNAME_SOURCE = "source"; //$NON-NLS-1$
	private static final String ATTRNAME_TYPE = "type"; //$NON-NLS-1$
	private static final String NODENAME_FILE = "file"; //$NON-NLS-1$

	public static DiskImage loadModel(IFile modelFile) {

		DiskImage diskImage = null;

		try {
			DiskImageDescriptor imageDescriptor = new DiskImageDescriptor();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document xmlDocument = builder.parse(modelFile.getRawLocation()
					.toFile());
			Element root = xmlDocument.getDocumentElement();

			String name = root.getAttribute(ATTRNAME_NAME);
			String id = root.getAttribute(ATTRNAME_ID);
			String type = root.getAttribute(ATTRNAME_TYPE);
			Integer interleave = null;
			String interleaveString = root.getAttribute(ATTRNAME_INTERLEAVE);

			if (type != null) {
				try {
					imageDescriptor = new DiskImageDescriptor(DiskImageType
							.valueOf(type.toUpperCase()));
				} catch (IllegalArgumentException iae) {
					// Ignore
				}
			}
			if (interleaveString != null) {
				try {
					interleave = Integer.valueOf(interleaveString);
				} catch (NumberFormatException nfe) {
					// Ignore error
				}
			}

			diskImage = new DiskImage(name, id, imageDescriptor, interleave);
			List<DiskFile> fileList = getFiles(modelFile, root
					.getElementsByTagName(NODENAME_FILE));
			if (fileList != null) {
				diskImage.setFiles(fileList);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return diskImage;
	}

	private static List<DiskFile> getFiles(IFile modelFile, NodeList files) {
		List<DiskFile> fileList = new ArrayList<DiskFile>();
		for (int i = 0; i < files.getLength(); i++) {
			Element fileElement = (Element) files.item(i);
			String name = fileElement.getAttribute(ATTRNAME_NAME);
			String source = fileElement.getAttribute(ATTRNAME_SOURCE);
			String closed = fileElement.getAttribute(ATTRNAME_CLOSED);
			String locked = fileElement.getAttribute(ATTRNAME_LOCKED);
			String type = fileElement.getAttribute(ATTRNAME_TYPE);

			IFile file = null;
			try {
				file = modelFile.getProject().getFile(source);
			} catch (IllegalArgumentException iae) {
				// Ignore
			}
			boolean closedFlag = true;
			boolean lockedFlag = false;
			if (closed != null && !"".equals(closed)) { //$NON-NLS-1$
				closedFlag = Boolean.valueOf(closed);
			}
			if (locked != null && !"".equals(locked)) { //$NON-NLS-1$
				lockedFlag = Boolean.valueOf(locked);
			}
			DiskFileType fileType = DiskFileType.PRG;
			if (type != null) {
				try {
					fileType = DiskFileType.valueOf(type.toUpperCase());
				} catch (IllegalArgumentException iae) {
					// Ignore
				}
			}
			if ((name == null || "".equals(name)) && file != null) { //$NON-NLS-1$
				name = file.getFullPath().removeFileExtension().lastSegment();
			}
			DiskFile diskFile = new DiskFile(name, -1, -1, file, fileType,
					lockedFlag, closedFlag);
			fileList.add(diskFile);
		}
		return fileList;
	}

	public static void saveModel(IFile modelFile, DiskImage diskImage) {

	}
}
