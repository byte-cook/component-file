package de.kobich.component.file.sync;

import java.io.File;
import java.io.OutputStream;

import de.kobich.component.file.ant.FileAntOutputLevel;


/**
 * Synchronization request.
 * @author ckorn
 */
public class SyncByDefinitionFileRequest extends AbstractSyncRequest {
	private File syncDefinitionFile;
	
	public SyncByDefinitionFileRequest(File syncDefinitionFile,  
			FileAntOutputLevel outputLevel, OutputStream logOutputStream, OutputStream errorOutputStream) {
		super(outputLevel, logOutputStream, errorOutputStream);
		this.syncDefinitionFile = syncDefinitionFile;
	}

	/**
	 * @return the taskPropertyFile
	 */
	public File getSyncDefinitionFile() {
		return syncDefinitionFile;
	}
}
