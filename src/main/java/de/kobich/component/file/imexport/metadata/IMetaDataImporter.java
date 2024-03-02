package de.kobich.component.file.imexport.metadata;

import java.io.IOException;
import java.util.Map;

import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.IMetaData;
import de.kobich.component.file.imexport.MetaDataImportingFormatType;


/**
 * Defines methods to import file's meta data.
 * @author ckorn
 */
public interface IMetaDataImporter {
	/**
	 * Returns an unique id
	 * @return an id
	 */
	public String getID();
	
	/**
	 * Indicates if this importer the given format supports
	 * @param metaData
	 * @return boolean
	 */
	public boolean supportFormat(MetaDataImportingFormatType format);
	
	/**
	 * Starts the import
	 */
	public void beginImport(MetaDataImportingFormatType format);
	
	/**
	 * Starts the import
	 * @param fileDescriptor
	 */
	public void beginFile(FileDescriptor fileDescriptor);

	/**
	 * Exports meta data
	 * @param writer writer
	 * @param metaData meta data
	 * @param format the format
	 * @throws IOException
	 */
	public void importData(MetaDataImportingFormatType format, Map<String, String> map) throws IOException;
	
	/**
	 * Ends the import and returns the meta data object
	 * @return the meta data
	 */
	public IMetaData endFile(FileDescriptor fileDescriptor);
	
	/**
	 * Ends the import and returns the meta data object
	 * @return the meta data
	 */
	public void endImport();
}
