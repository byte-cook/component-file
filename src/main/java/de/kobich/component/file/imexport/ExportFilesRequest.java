package de.kobich.component.file.imexport;

import java.io.File;
import java.util.List;
import java.util.Set;

import de.kobich.commons.monitor.progress.ProgressMonitorRequest;
import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.imexport.metadata.IMetaDataExporter;

/**
 * Request to export files.
 * @author ckorn
 */
public class ExportFilesRequest extends ProgressMonitorRequest {
	private File targetFile;
	private Set<FileDescriptor> fileDescriptors;
	private MetaDataExportingFormatType format;
	private List<IMetaDataExporter> metaDataExporters;

	/**
	 * Constructor
	 * @param targetFile the target file
	 * @param fileDescriptors
	 * @param format the format
	 */
	public ExportFilesRequest(File targetFile, Set<FileDescriptor> fileDescriptors, MetaDataExportingFormatType format, List<IMetaDataExporter> metaDataExporters) {
		this.targetFile = targetFile;
		this.fileDescriptors = fileDescriptors;
		this.format = format;
		this.metaDataExporters = metaDataExporters;
	}

	/**
	 * @return the fileDescriptors
	 */
	public Set<FileDescriptor> getFileDescriptors() {
		return fileDescriptors;
	}

	/**
	 * @return the targetFile
	 */
	public File getTargetFile() {
		return targetFile;
	}

	/**
	 * @return the format
	 */
	public MetaDataExportingFormatType getFormat() {
		return format;
	}

	/**
	 * @return the metaDataExporter
	 */
	public List<IMetaDataExporter> getMetaDataExporters() {
		return metaDataExporters;
	}
}
