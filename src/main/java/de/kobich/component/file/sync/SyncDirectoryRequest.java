package de.kobich.component.file.sync;

import java.io.File;
import java.io.OutputStream;

import de.kobich.component.file.ant.FileAntOutputLevel;


/**
 * Synchronization request.
 * @author ckorn
 */
public class SyncDirectoryRequest extends AbstractSyncRequest {
	private File sourceDirectory;
	private File targetDirectory;
	private boolean overwriteNewerTargetFiles;
	
	public SyncDirectoryRequest(File sourceDirectory, File targetDirectory, boolean overwriteNewerTargetFiles,  
			FileAntOutputLevel outputLevel, OutputStream logOutputStream, OutputStream errorOutputStream) {
		super(outputLevel, logOutputStream, errorOutputStream);
		this.sourceDirectory = sourceDirectory;
		this.targetDirectory = targetDirectory;
		this.overwriteNewerTargetFiles = overwriteNewerTargetFiles;
	}

	/**
	 * @return the sourceDirectory
	 */
	public File getSourceDirectory() {
		return sourceDirectory;
	}

	/**
	 * @return the targetDirectory
	 */
	public File getTargetDirectory() {
		return targetDirectory;
	}

	/**
	 * @return the overwriteNewerTargetFiles
	 */
	public boolean isOverwriteNewerTargetFiles() {
		return overwriteNewerTargetFiles;
	}

}
