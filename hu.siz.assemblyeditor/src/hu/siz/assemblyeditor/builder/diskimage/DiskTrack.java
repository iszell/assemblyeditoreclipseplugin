/**
 * 
 */
package hu.siz.assemblyeditor.builder.diskimage;

import java.util.ArrayList;
import java.util.List;

/**
 * Disk track
 * 
 * @author siz
 */
/**
 * @author Siz
 * 
 */
public class DiskTrack {

	private List<DiskSector> sectors;

	/**
	 * 
	 * @param numberOfSectors
	 */
	public DiskTrack(int numberOfSectors) {
		super();
		this.sectors = new ArrayList<DiskSector>();
		for (int s = 0; s < numberOfSectors; s++) {
			this.sectors.add(null);
		}
	}

	/**
	 * Read one sector
	 * 
	 * @param index
	 *            - index of sector
	 */
	public DiskSector getSector(int index) {
		if (index >= this.sectors.size()) {
			return null;
		}
		return this.sectors.get(index);
	}

	/**
	 * Write one sector
	 * 
	 * @param index
	 *            - index of sector
	 * @param sector
	 *            - data
	 */
	public void setSector(int index, DiskSector sector) {
		this.sectors.set(index, sector);
	}

	/**
	 * 
	 * @return The ArrayList of the tracks' sectors
	 */
	public List<DiskSector> getSectors() {
		return this.sectors;
	}

	/**
	 * Sets all sectors of the track
	 * 
	 * @param sectors
	 */
	public void setSectors(List<DiskSector> sectors) {
		this.sectors = sectors;
	}

	/**
	 * Check if this track is already full
	 * 
	 * @return true if there are no free sectors
	 */
	public boolean isFull(boolean allowReserved) {
		for (DiskSector sector : this.sectors) {
			if (sector == null || (allowReserved && sector == DiskImage.RESERVED_SECTOR)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get number of sectors on this track
	 * 
	 * @return the number of sectors
	 */
	public int getNumberOfSectors() {
		return this.sectors.size();
	}

	@Override
	public String toString() {
		return "DiskTrack [sectors=" + sectors + "]";
	}
}
