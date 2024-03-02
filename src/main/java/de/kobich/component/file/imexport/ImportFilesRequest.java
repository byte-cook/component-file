package de.kobich.component.file.imexport;

import java.io.File;
import java.util.List;

import de.kobich.commons.monitor.progress.ProgressMonitorRequest;
import de.kobich.component.file.imexport.metadata.IMetaDataImporter;

/**
 * Request to import files.
 * @author ckorn
 */
public class ImportFilesRequest extends ProgressMonitorRequest {
	private File sourceFile;
	private List<IMetaDataImporter> metaDataImporters;

	/**
	 * Constructor
	 * @param sourceFile the source file
	 * @param fileDescriptors
	 * @param format the format
	 */
	public ImportFilesRequest(File sourceFile, List<IMetaDataImporter> metaDataImporters) {
		this.sourceFile = sourceFile;
		this.metaDataImporters = metaDataImporters;
	}

	/**
	 * @return the sourceFile
	 */
	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 * @return the metaDataImporters
	 */
	public List<IMetaDataImporter> getMetaDataImporters() {
		return metaDataImporters;
	}
}
