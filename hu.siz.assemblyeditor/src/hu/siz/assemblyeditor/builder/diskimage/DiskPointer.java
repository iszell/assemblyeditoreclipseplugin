/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

/**
 * @author siz
 * 
 */
public class DiskPointer {

	private int track;
	private int sector;

	/**
	 * Pointer to a disk sector using track and sector indexing
	 * 
	 * @param track
	 * @param sector
	 */
	public DiskPointer(int track, int sector) {
		super();
		this.track = track;
		this.sector = sector;
	}

	/**
	 * Empty diskpointer. (Track=0, sector=0)
	 * 
	 * @see #DiskPointer(int, int)
	 */
	public DiskPointer() {
		this(0, 0);
	}

	/**
	 * @return the track
	 */
	public int getTrack() {
		return this.track;
	}

	/**
	 * @param track
	 *            the track to set
	 */
	public void setTrack(int track) {
		this.track = track;
	}

	/**
	 * @return the sector
	 */
	public int getSector() {
		return this.sector;
	}

	/**
	 * @param sector
	 *            the sector to set
	 */
	public void setSector(int sector) {
		this.sector = sector;
	}

	public byte[] toByteArray() {
		byte[] r = new byte[2];

		r[0] = (byte) this.track;
		r[1] = (byte) this.sector;

		return r;
	}

	@Override
	public String toString() {
		return "(" + this.track + "," + this.sector + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
