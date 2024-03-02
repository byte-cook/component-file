package de.kobich.component.file;

import java.io.File;
import java.util.Comparator;

/**
 * Compares FileDescriptors.
 */
public class DefaultFileDescriptorComparator implements Comparator<FileDescriptor> {
	/**
	 * Constructor
	 */
	public DefaultFileDescriptorComparator() {}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(FileDescriptor o1, FileDescriptor o2) {
		File f1 = o1.getFile();
		File f2 = o2.getFile();
		
		int res = 0;
		// parent file can be null for Windows-like paths
		if (f1.getParentFile() != null && f2.getParentFile() != null) {
			res = f1.getParentFile().compareTo(f2.getParentFile());
		}
		if (res == 0) {
			res = f1.compareTo(f2);
		}
        return res;
	}

}
