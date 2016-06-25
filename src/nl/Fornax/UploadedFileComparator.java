package nl.Fornax;

import java.util.Comparator;
import nl.Fornax.obj.UploadedFile;

/**
 * @author Fornax
 */
public class UploadedFileComparator implements Comparator<UploadedFile>{
	//Tine little useless comparator class
	@Override
	public int compare(UploadedFile o1, UploadedFile o2) {
		return ((Long) o1.getUploadTime()).compareTo((Long) o2.getUploadTime());
	}
}
