package de.kobich.component.file.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Request to delete files.
 * @author ckorn
 */
public class FilesRequest {
	private Collection<File> files;

	/**
	 * Constructor
	 * @param file
	 */
	public FilesRequest(File file) {
		this.files = new ArrayList<File>();
		this.files.add(file);
	}
	/**
	 * Constructor
	 * @param files
	 */
	public FilesRequest(Collection<File> files) {
		this.files = files;
	}

	/**
	 * @return the files
	 */
	public Collection<File> getFiles() {
		return files;
	}

}
