package de.kobich.component.file.io;

import java.io.File;


/**
 * Request to move files.
 * @author ckorn
 */
public class CopyFileRequest {
	private final File sourceFile;
	private final File targetFile;
	private final FileCreationType type;

	/**
	 * Constructor
	 * @param sourceFile
	 * @param targetFile
	 */
	public CopyFileRequest(File sourceFile, File targetFile, FileCreationType type) {
		this.sourceFile = sourceFile;
		this.targetFile = targetFile;
		this.type = type;
	}

	/**
	 * @return the sourceFile
	 */
	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 * @return the targetFile
	 */
	public File getTargetFile() {
		return targetFile;
	}

	/**
	 * @return the type
	 */
	public FileCreationType getType() {
		return type;
	}

}
