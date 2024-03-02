package de.kobich.component.file.io;

import java.io.File;

/**
 * Request to delete files.
 * @author ckorn
 */
public class RenameFileRequest {
	private File oldFile;
	private File newFile;

	/**
	 * Constructor
	 * @param oldFile
	 * @param newFile
	 */
	public RenameFileRequest(File oldFile, File newFile) {
		this.oldFile = oldFile;
		this.newFile = newFile;
	}

	/**
	 * @return the oldFile
	 */
	public File getOldFile() {
		return oldFile;
	}

	/**
	 * @return the newFile
	 */
	public File getNewFile() {
		return newFile;
	}

}
