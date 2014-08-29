/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * @author siz
 * 
 */
public class DiskFile {

	private String name;
	private DiskPointer firstSector;
	private IFile source;
	private DiskFileType type;
	private boolean locked;
	private boolean closed;

	/**
	 * @param name
	 * @param track
	 * @param sector
	 * @param length
	 * @param contents
	 * @param type
	 * @param closed
	 * @param locked
	 */
	public DiskFile(String name, int track, int sector, IFile source, // InputStream
			// contents,
			DiskFileType type, boolean locked, boolean closed) {
		super();
		this.firstSector = new DiskPointer(track, sector);
		this.name = name;
		this.source = source;
		this.type = type;
		this.locked = locked;
		this.closed = closed;
	}

	/**
	 * Creates a disk file from a file resource
	 * 
	 * @param source
	 */
	public DiskFile(IFile source) {
		this(source.getName(), 0, 0, source, DiskFileType.PRG, false, true);
	}

	/**
	 * @return The file as disk sectors
	 * @throws IOException
	 * @throws CoreException
	 * 
	 */
	public List<DiskSector> toDiskSectors() throws IOException, CoreException {

		List<DiskSector> sectors = new ArrayList<DiskSector>();

		InputStream contents = this.source.getContents();

		while (contents.available() > DiskSector.SECTOR_SIZE) {
			byte data[] = new byte[DiskSector.SECTOR_SIZE];
			contents.read(data);
			sectors.add(new DiskSector(data));
		}

		if (contents.available() > 0) {
			byte data[] = new byte[contents.available()];
			contents.read(data);
			sectors.add(new DiskSector(data));
		}

		return sectors;
	}

	/**
	 * Returns number of sectors allocated by the file
	 * 
	 * @return Number of sectors
	 * @throws CoreException
	 * @throws IOException
	 */
	public int getNumberOfSectors() throws IOException, CoreException {

		int length = this.source.getContents().available();
		int sectors = length / DiskSector.SECTOR_SIZE;
		if (length % DiskSector.SECTOR_SIZE != 0) {
			sectors++;
		}

		return sectors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(this.firstSector.toString());
		result.append(" \""); //$NON-NLS-1$
		result.append(this.name);
		result.append("\" "); //$NON-NLS-1$
		result.append(this.locked ? "<" : " "); //$NON-NLS-1$ //$NON-NLS-2$
		result.append(this.type.toString());
		result.append(this.closed ? " " : "*"); //$NON-NLS-1$ //$NON-NLS-2$
		result.append("("); //$NON-NLS-1$
		if (this.source != null) {
			result.append(this.source.getName());
		}
		result.append(")"); //$NON-NLS-1$
		return result.toString();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the firstSector
	 */
	public DiskPointer getFirstSector() {
		return this.firstSector;
	}

	/**
	 * @param firstSector
	 *            the firstSector to set
	 */
	public void setFirstSector(DiskPointer firstSector) {
		this.firstSector = firstSector;
	}

	/**
	 * @return the type
	 */
	public DiskFileType getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DiskFileType type) {
		this.type = type;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return this.locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return this.closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * @return the source
	 */
	public IFile getSource() {
		return this.source;
	}
}
