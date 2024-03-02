package de.kobich.component.file.download;

import java.io.File;
import java.io.OutputStream;


/**
 * Download files by definition file request.
 * @author ckorn
 */
public class DownloadFilesByDefinitionFileRequest extends AbstractDownloadFilesRequest {
	private File definitionFile;

	/**
	 * @param definitionFile
	 */
	public DownloadFilesByDefinitionFileRequest(File definitionFile, File targetDirectory, OutputStream logOutputStream, OutputStream errorOutputStream) {
		super(targetDirectory, logOutputStream, errorOutputStream);
		this.definitionFile = definitionFile;
	}
	
	/**
	 * @return the definitionFile
	 */
	public File getDefinitionFile() {
		return definitionFile;
	}
}
