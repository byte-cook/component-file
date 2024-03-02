package de.kobich.component.file.imexport;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.kobich.commons.monitor.progress.IServiceProgressMonitor;
import de.kobich.commons.monitor.progress.ProgressData;
import de.kobich.commons.monitor.progress.ProgressSupport;
import de.kobich.commons.utils.StreamUtils;
import de.kobich.component.file.DefaultFileDescriptorComparator;
import de.kobich.component.file.FileDescriptor;
import de.kobich.component.file.FileException;
import de.kobich.component.file.IMetaData;
import de.kobich.component.file.imexport.metadata.IMetaDataExporter;
import de.kobich.component.file.imexport.metadata.IMetaDataImporter;

/**
 * Defines methods to export files.
 * @author ckorn
 */
@Service
public class XMLMetaDataImExportService {
	private static final Logger logger = Logger.getLogger(XMLMetaDataImExportService.class);
	private static final String CHARSET_NAME = "UTF-8";

	public void exportFiles(ExportFilesRequest request) throws FileException {
		logger.debug("Exporting files");
		List<FileDescriptor> fileDescriptors = new ArrayList<FileDescriptor>(request.getFileDescriptors());
		IServiceProgressMonitor monitor = request.getProgressMonitor();
		// monitor start
		ProgressSupport progressSupport = new ProgressSupport(monitor);
		progressSupport.monitorBeginTask("Importing files...", fileDescriptors.size());

		File targetFile = request.getTargetFile();
		OutputStream os = null;
		try {
			IMetaDataExporter metaDataExporter = null;
			for (IMetaDataExporter exporter : request.getMetaDataExporters()) {
				if (exporter.supportFormat(request.getFormat())) {
					metaDataExporter = exporter;
					break;
				}
			}
			
			os = new FileOutputStream(targetFile);
			OutputStream outputStream = new BufferedOutputStream(os);
			
			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, CHARSET_NAME);
//			writer = new IndentingXMLStreamWriter(writer);
			writer.writeStartDocument(CHARSET_NAME, "1.0");
			
			writer.writeStartElement(MetaDataXMLTokenType.FILE_DESCRIPTORS.tag());
			writer.writeAttribute(MetaDataXMLTokenType.COUNT.attribute(), fileDescriptors.size() + "");

			if (metaDataExporter != null) {
				metaDataExporter.beginExport(request.getFormat());
			}
			int fileCount = 0;
			Collections.sort(fileDescriptors, new DefaultFileDescriptorComparator());
			for (FileDescriptor fileDescriptor : fileDescriptors) {
				++fileCount;
				String file = fileDescriptor.getFile().getAbsolutePath();
				String relativePath = fileDescriptor.getRelativePath();
				
				writer.writeStartElement(MetaDataXMLTokenType.FILE_DESCRIPTOR.tag());
				writer.writeAttribute(MetaDataXMLTokenType.FILE.attribute(), file);
				writer.writeAttribute(MetaDataXMLTokenType.RELATIVE_PATH.attribute(), relativePath);

				if (fileDescriptor.hasMetaData() && metaDataExporter != null) {
					if (metaDataExporter.supportMetaData(fileDescriptor.getMetaData())) {
						String metaDataID = metaDataExporter.getID();
						
						writer.writeStartElement(MetaDataXMLTokenType.META_DATA.tag());
						writer.writeAttribute(MetaDataXMLTokenType.ID.attribute(), metaDataID);
						metaDataExporter.beginFile(fileDescriptor);
						Map<String, String> map = metaDataExporter.exportData(request.getFormat());
						for (String mapKey : map.keySet()) {
							String mapValue = map.get(mapKey);
							
							writer.writeStartElement(MetaDataXMLTokenType.ATTRIBUTE.tag());
							writer.writeAttribute(MetaDataXMLTokenType.KEY.attribute(), mapKey);
							writer.writeAttribute(MetaDataXMLTokenType.VALUE.attribute(), mapValue);
							writer.writeEndElement();
						}
						metaDataExporter.endFile(fileDescriptor);
						writer.writeEndElement();
					}
				}
				writer.writeEndElement();

				// monitor sub task
				progressSupport.monitorSubTask("Exporting file #" + fileCount + ": " + fileDescriptor.getRelativePath(), 1);
			}
			if (metaDataExporter != null) {
				metaDataExporter.endExport();
			}
			writer.writeEndElement();
			
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		}
		catch (IOException exc) {
			throw new FileException(FileException.FILE_EXPORT_ERROR, exc);
		}
		catch (XMLStreamException exc) {
			throw new FileException(FileException.FILE_EXPORT_ERROR, exc);
		}
		finally {
			StreamUtils.forceClose(os);
			// monitor end
			progressSupport.monitorEndTask("Export finished");
		}
	}

	public Set<FileDescriptor> importFiles(ImportFilesRequest request) throws FileException {
		logger.debug("Importing files");
		IServiceProgressMonitor monitor = request.getProgressMonitor();
		ProgressSupport progressSupport = new ProgressSupport(monitor);

		File sourceFile = request.getSourceFile();
		InputStream is = null;
		XMLEventReader reader = null;
		try {
			final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			is = new FileInputStream(sourceFile);
			reader = inputFactory.createXMLEventReader(is);
			Set<FileDescriptor> fileDescriptors = null;
			FileDescriptor fileDescriptor = null;
			IMetaDataImporter metaDataImporter = null;
			Map<String, String> metaDataMap = null;
			while (reader.hasNext()) {
				final XMLEvent evt = reader.nextEvent();
				switch (evt.getEventType()) {
					case XMLStreamConstants.START_ELEMENT:
						final StartElement startElt = evt.asStartElement();
						final String startEltName = startElt.getName().getLocalPart();
						if (startEltName.equals(MetaDataXMLTokenType.FILE_DESCRIPTORS.tag())) {
							fileDescriptors = new HashSet<FileDescriptor>();
							int count = ProgressData.INDETERMINATE_MODE;
							Attribute countAttribute = startElt.getAttributeByName(new QName(MetaDataXMLTokenType.COUNT.attribute()));
							if (countAttribute != null) {
								String countString = countAttribute.getValue();
								if (StringUtils.isNumeric(countString)) {
									count = Integer.parseInt(countString);
								}
							}
							// monitor start
							progressSupport.monitorBeginTask("Importing files...", count);
						}
						else if (startEltName.equals(MetaDataXMLTokenType.FILE_DESCRIPTOR.tag())) {
							String file = null;
							Attribute fileAttribute = startElt.getAttributeByName(new QName(MetaDataXMLTokenType.FILE.attribute()));
							if (fileAttribute != null) {
								file = fileAttribute.getValue();
							}
							String relativePath = null;
							Attribute relPathAttribute = startElt.getAttributeByName(new QName(MetaDataXMLTokenType.RELATIVE_PATH.attribute()));
							if (relPathAttribute != null) {
								relativePath = relPathAttribute.getValue();
							}
							fileDescriptor = new FileDescriptor(new File(file), relativePath);
						}
						else if (startEltName.equals(MetaDataXMLTokenType.META_DATA.tag())) {
							Attribute idAttribute = startElt.getAttributeByName(new QName(MetaDataXMLTokenType.ID.attribute()));
							if (idAttribute != null) {
								String id = idAttribute.getValue();
								metaDataMap = new HashMap<String, String>();
								for (IMetaDataImporter importer : request.getMetaDataImporters()) {
									if (importer.getID().equals(id) && importer.supportFormat(MetaDataImportingFormatType.XML)) {
										metaDataImporter = importer;
										metaDataImporter.beginImport(MetaDataImportingFormatType.XML);
										metaDataImporter.beginFile(fileDescriptor);
										break;
									}
								}
							}
						}
						else if (startEltName.equals(MetaDataXMLTokenType.ATTRIBUTE.tag())) {
							Attribute keyAttribute = startElt.getAttributeByName(new QName(MetaDataXMLTokenType.KEY.attribute()));
							Attribute valueAttribute = startElt.getAttributeByName(new QName(MetaDataXMLTokenType.VALUE.attribute()));
							metaDataMap.put(keyAttribute.getValue(), valueAttribute.getValue());
						}
						break;
					case XMLStreamConstants.CHARACTERS:
						break;
					case XMLStreamConstants.END_ELEMENT:
						final EndElement endElt = evt.asEndElement();
						final String endEltName = endElt.getName().getLocalPart();
						if (endEltName.equals(MetaDataXMLTokenType.FILE_DESCRIPTOR.tag())) {
							fileDescriptors.add(fileDescriptor);
							// monitor sub task
							progressSupport.monitorSubTask("Importing file #" + fileDescriptors.size() + ": " + fileDescriptor.getRelativePath(), 1);
						}
						else if (endEltName.equals(MetaDataXMLTokenType.META_DATA.tag())) {
							if (metaDataImporter != null) {
								metaDataImporter.importData(MetaDataImportingFormatType.XML, metaDataMap);
								IMetaData metaData = metaDataImporter.endFile(fileDescriptor);
								fileDescriptor.setMetaData(metaData);
								metaDataImporter.endImport();
								metaDataImporter = null;
							}
						}
						break;
					case XMLStreamConstants.END_DOCUMENT:
						break;
				}
			}
			reader.close();
			return fileDescriptors;
		}
		catch (IOException ex) {
			throw new FileException(FileException.XML_ERROR, ex);
		}
		catch (XMLStreamException ex) {
			throw new FileException(FileException.XML_ERROR, ex);
		}
		finally {
			StreamUtils.forceClose(is);
			// monitor end
			progressSupport.monitorEndTask("Import finished");
		}
	}
}
