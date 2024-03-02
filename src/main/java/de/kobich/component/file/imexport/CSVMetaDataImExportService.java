package de.kobich.component.file.imexport;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.kobich.commons.monitor.progress.IServiceProgressMonitor;
import de.kobich.commons.monitor.progress.ProgressSupport;
import de.kobich.commons.utils.EscapeUtils;
import de.kobich.component.file.DefaultFileDescriptorComparator;
import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.FileException;
import de.kobich.component.file.imexport.metadata.IMetaDataExporter;

/**
 * Defines methods to export files.
 * @author ckorn
 */
@Service
public class CSVMetaDataImExportService {
	private static final Logger logger = Logger.getLogger(CSVMetaDataImExportService.class);
	private static final String CHARSET_NAME = "UTF-8";
	public static final String SEPARTATOR = ";";

	public void exportFiles(ExportFilesRequest request) throws FileException {
		List<FileDescriptor> fileDescriptors = new ArrayList<FileDescriptor>(request.getFileDescriptors());
		IServiceProgressMonitor monitor = request.getProgressMonitor();
		// monitor start
		ProgressSupport progressSupport = new ProgressSupport(monitor);
		progressSupport.monitorBeginTask("Importing files...", fileDescriptors.size());

		File targetFile = request.getTargetFile();
		BufferedWriter writer = null;
		try {
			IMetaDataExporter metaDataExporter = null;
			List<String> keys = null;
			for (IMetaDataExporter exporter : request.getMetaDataExporters()) {
				if (exporter.supportFormat(request.getFormat())) {
					metaDataExporter = exporter;
					break;
				}
			}
			
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
			writer = new BufferedWriter(new OutputStreamWriter(outputStream, CHARSET_NAME));
			
			String fileNameCol = EscapeUtils.escapeCSV(MetaDataCSVTokenType.FILE_NAME.tag());
			String fileCol = EscapeUtils.escapeCSV(MetaDataCSVTokenType.FILE.tag());
			String relativePathCol = EscapeUtils.escapeCSV(MetaDataCSVTokenType.RELATIVE_PATH.tag());
			String extensionCol = EscapeUtils.escapeCSV(MetaDataCSVTokenType.EXTENSION.tag());
			String depthFirstCol = EscapeUtils.escapeCSV(MetaDataCSVTokenType.DEPTH_FIRST.tag());
			String widthFirstCol = EscapeUtils.escapeCSV(MetaDataCSVTokenType.WIDTH_FIRST.tag());
			writer.write(fileNameCol + SEPARTATOR + fileCol + SEPARTATOR + relativePathCol + SEPARTATOR + extensionCol + SEPARTATOR + depthFirstCol + SEPARTATOR + widthFirstCol);
			if (metaDataExporter != null) {
				keys = metaDataExporter.beginExport(request.getFormat());
				for (String key : keys) {
					writer.write(SEPARTATOR + key);
				}
			}
			writer.newLine();
			logger.debug("File count: " + fileDescriptors.size());
			int fileCount = 0;
			Collections.sort(fileDescriptors, new DefaultFileDescriptorComparator());
			for (FileDescriptor fileDescriptor : fileDescriptors) {
				++ fileCount;
				logger.debug("File: " + fileDescriptor.getFileName());
				String fileName = EscapeUtils.escapeCSV(fileDescriptor.getFileName());
				writer.write(fileName); 
				writer.write(SEPARTATOR);
				String file = EscapeUtils.escapeCSV(fileDescriptor.getFile().getAbsolutePath());
				writer.write(file);
				writer.write(SEPARTATOR);
				String relativePath = EscapeUtils.escapeCSV(fileDescriptor.getRelativePath());
				writer.write(relativePath);
				writer.write(SEPARTATOR);
				String extension = EscapeUtils.escapeCSV(fileDescriptor.getExtension());
				writer.write(extension);
				writer.write(SEPARTATOR);
				if (fileDescriptor.hasMetaData() && metaDataExporter != null) {
					if (metaDataExporter.supportMetaData(fileDescriptor.getMetaData())) {
						metaDataExporter.beginFile(fileDescriptor);
						Map<String, String> map = metaDataExporter.exportData(request.getFormat());
						for (String key : keys) {
							String value = map.get(key);
							if (value == null) {
								value = "";
							}
							else {
								value = EscapeUtils.escapeCSV(value);
							}
							writer.write(SEPARTATOR); 
							writer.write(value);
						}
						metaDataExporter.endFile(fileDescriptor);
					}
				}
				writer.newLine();
				
				progressSupport.monitorSubTask("Exporting file #" + fileCount + ": " + fileDescriptor.getRelativePath(), 1);
			}
			if (metaDataExporter != null) {
				metaDataExporter.endExport();
			}

			writer.flush();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_EXPORT_ERROR, exc);
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				}
				catch (IOException exc) {
					logger.error(exc);
				}
			}
			// monitor end
			progressSupport.monitorEndTask("Exporting finished");
		}
	}

}
