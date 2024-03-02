package de.kobich.component.file.imexport.metadata;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.IMetaData;
import de.kobich.component.file.imexport.MetaDataExportingFormatType;


/**
 * Defines methods to export file's meta data.
 * @author ckorn
 */
public interface IMetaDataExporter {
	/**
	 * Returns an unique id
	 * @return an id
	 */
	public String getID();
	
	/**
	 * Indicates if this exporter the given meta data supports
	 * @param metaData
	 * @return boolean
	 */
	public boolean supportMetaData(IMetaData metaData);
	
	/**
	 * Indicates if this importer the given format supports
	 * @param metaData
	 * @return boolean
	 */
	public boolean supportFormat(MetaDataExportingFormatType format);
	
	/**
	 * Starts the export and returns the keys
	 */
	public List<String> beginExport(MetaDataExportingFormatType format) throws IOException;

	/**
	 * Starts the export
	 * @param fileDescriptor
	 */
	public void beginFile(FileDescriptor fileDescriptor);

	/**
	 * Exports meta data
	 * @param format the format
	 * @return key/value map
	 * @throws IOException
	 */
	public Map<String, String> exportData(MetaDataExportingFormatType format) throws IOException;

	/**
	 * Ends the export
	 * @param fileDescriptor
	 */
	public void endFile(FileDescriptor fileDescriptor);

	/**
	 * Ends the export
	 */
	public void endExport();
}
