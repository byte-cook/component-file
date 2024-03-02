package de.kobich.component.file.imexport;

import java.io.File;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.FileException;


/**
 * Service to import/export files.
 * @author ckorn
 */
@Service
public class MetaDataImExportService {
	@Autowired
	private XMLMetaDataImExportService xmlService;
	@Autowired
	private CSVMetaDataImExportService csvService;
	
	/**
	 * Exports files
	 * @param request the request
	 * @throws FileException
	 */
	public void exportFiles(ExportFilesRequest request) throws FileException {
		MetaDataExportingFormatType type = request.getFormat();
		
		switch (type) {
			case XML:
				xmlService.exportFiles(request);
				break;
			case CSV:
				csvService.exportFiles(request);
				break;
		}
	}

	/**
	 * Import file
	 * @param request the request
	 * @throws FileException
	 */
	public Set<FileDescriptor> importFiles(ImportFilesRequest request) throws FileException {
		File sourceFile = request.getSourceFile();
		String format = FilenameUtils.getExtension(sourceFile.getName());
		MetaDataImportingFormatType type = MetaDataImportingFormatType.getByName(format);
		if (type == null) {
			throw new FileException(FileException.ILLEGAL_FORMAT, format);
		}
		
		switch (type) {
			case XML:
				return xmlService.importFiles(request);
		}
		
		return null;
	}
}
