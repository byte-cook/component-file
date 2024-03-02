package de.kobich.component.file.crc;

/**
 * The file CRC checksum service.
 * @author ckorn
 */
//@Service
public class FileCRCService {
//	private static final String FILE_COUNT = "FileCount";
//	private static final String MODE = "Mode";
//	private static final Logger logger = Logger.getLogger(FileCRCService.class);

	/**
	 * Checks files for crc
	 * @param request the request
	 * @return CRCResponse
	 * @throws FileException
	 */
//	public CRCResponse validateFiles(CheckFilesRequest request) throws FileException {
//		logger.debug("Check files");
//		boolean succeeded = true;
//		List<ProgressDataPayload> messages = new ArrayList<ProgressDataPayload>();
//		IServiceProgressMonitor monitor = request.getProgressMonitor(); 
//
//		ProgressDataPayload beginMessage = new ProgressDataPayload(MessageType.INFO, "Validate CRC files");
//		messages.add(beginMessage);
//		ProgressPayloadUtil.monitorBeginTask(beginMessage, monitor);
//
//		File crcFile = request.getCrcFile();
//		if (!crcFile.exists()) {
//			ProgressDataPayload fileNotFoundMessage = new ProgressDataPayload(MessageType.ERROR, "CRC file not found", crcFile.getAbsolutePath(), "<none>");
//			messages.add(fileNotFoundMessage);
//			ProgressPayloadUtil.monitorSubTask(fileNotFoundMessage, monitor);
//			succeeded = false;
//		}
//		else {
//			InputStream inputStream = null;
//			try {
//				inputStream = new BufferedInputStream(new FileInputStream(crcFile));
//				
//				// load CRC file
//				Properties crcContent = new Properties();
//				crcContent.load(inputStream);
//				ProgressDataPayload fileLoadedMessage = new ProgressDataPayload(MessageType.INFO, "CRC file loaded");
//				messages.add(fileLoadedMessage);
//				ProgressPayloadUtil.monitorSubTask(fileLoadedMessage, monitor);
//				
//				// sort files
//				List<FileDescriptor> fileDescriptors = request.getFileDescriptors();
//				Collections.sort(fileDescriptors, new DefaultFileDescriptorComparator());
//				
//				// check file count
//				String fileCountObj = crcContent.getProperty(FILE_COUNT);
//				crcContent.remove(FILE_COUNT);
//				if (fileCountObj == null) {
//					String msg = "File count not present in CRC file";
//					ProgressDataPayload errorMessage = new ProgressDataPayload(MessageType.ERROR, msg, FILE_COUNT, "<none>");
//					messages.add(errorMessage);
//					ProgressPayloadUtil.monitorSubTask(errorMessage, monitor);
//					succeeded = false;
//				}
//				else {
//					int expectedFileCount = Integer.parseInt(fileCountObj);
//					if (expectedFileCount != fileDescriptors.size()) {
//						String msg = "File count different";
//						ProgressDataPayload errorMessage = new ProgressDataPayload(MessageType.ERROR, msg, "" + expectedFileCount, "" + fileDescriptors.size());
//						messages.add(errorMessage);
//						ProgressPayloadUtil.monitorSubTask(errorMessage, monitor);
//						succeeded = false;
//					}
//					else {
//						ProgressDataPayload fileCountMessage = new ProgressDataPayload(MessageType.INFO, "File count: " + fileDescriptors.size());
//						messages.add(fileCountMessage);
//						ProgressPayloadUtil.monitorSubTask(fileCountMessage, monitor);
//					}
//				}
//				
//				// get crc mode
//				CRCMode mode = CRCMode.ADLER_32;
//				String modeObj = crcContent.getProperty(MODE);
//				crcContent.remove(MODE);
//				mode = CRCMode.getByName(modeObj);
//				if (mode == null) {
//					mode = CRCMode.ADLER_32;
//				}
//				ProgressDataPayload crcModeMessage = new ProgressDataPayload(MessageType.INFO, "Using mode: " + mode.name());
//				messages.add(crcModeMessage);
//				ProgressPayloadUtil.monitorSubTask(crcModeMessage, monitor);
//				
//				// check CRC values
//				int fileIndex = 0;
//				Adler32 crc = new Adler32(); 
//				for (FileDescriptor fileDescriptor : fileDescriptors) {
//					String key = getKey(fileDescriptor);
//					String expectedValueObj = crcContent.getProperty(key);
//					crcContent.remove(key);
//					if (expectedValueObj == null) {
//						String msg = "File not present in CRC file";
//						ProgressDataPayload errorMessage = new ProgressDataPayload(MessageType.ERROR, msg, "<none>", key);
//						messages.add(errorMessage);
//						ProgressPayloadUtil.monitorSubTask(errorMessage, monitor);
//						succeeded = false;
//					}
//					else {
//						File file = fileDescriptor.getFile();
//						
//						long expectedValue = Long.parseLong(expectedValueObj);
//						long value = 0;
//						if (CRCMode.ADLER_32.equals(mode)) {
//							InputStream is = new FileInputStream(file);
//							InputStream cis = new CheckedInputStream(is, crc);
//							while (cis.read() != -1) {
//							}
//							cis.close();
//							value = crc.getValue();
//							crc.reset();
//						}
//						else if (CRCMode.FILE_SIZE.equals(mode)) {
//							value = file.length();
//						}
//						
//						++ fileIndex;
//						if (expectedValue != value) {
//							String msg = "CRC values different for #" + fileIndex + ": " + file.getPath();
//							ProgressDataPayload errorMessage = new ProgressDataPayload(MessageType.ERROR, msg, "" + expectedValue, "" + value);
//							messages.add(errorMessage);
//							ProgressPayloadUtil.monitorSubTask(errorMessage, monitor);
//							succeeded = false;
//						}
//						else {
//							String msg = "CRC values equal for #" + fileIndex + ": " + file.getPath();
//							ProgressDataPayload errorMessage = new ProgressDataPayload(MessageType.INFO, msg, "" + expectedValue, "" + value);
//							messages.add(errorMessage);
//							ProgressPayloadUtil.monitorSubTask(errorMessage, monitor);
//						}
//					}
//				}
//				
//				// check for missing files
//				for (String path : crcContent.stringPropertyNames()) {
//					String msg = "File not found but defined in CRC file";
//					ProgressDataPayload errorMessage = new ProgressDataPayload(MessageType.ERROR, msg, path, "<none>");
//					messages.add(errorMessage);
//					ProgressPayloadUtil.monitorSubTask(errorMessage, monitor);
//					succeeded = false;
//				}
//				
//				ProgressDataPayload validationMessage = null;
//				if (succeeded) {
//					validationMessage = new ProgressDataPayload(MessageType.INFO, "Validation succeeded");
//				}
//				else {
//					validationMessage = new ProgressDataPayload(MessageType.ERROR, "Validation failed");
//				}
//				messages.add(validationMessage);
//				ProgressPayloadUtil.monitorEndTask(validationMessage, monitor);
//			} catch (IOException exc) {
//				throw new FileException(FileException.IO_ERROR);
//			} finally {
//				if (inputStream != null) {
//					try {
//						inputStream.close();
//					}
//					catch (IOException e) {
//						logger.warn("Stream cannot be closed");
//					}
//				}
//			}
//		}
//		return new CRCResponse(succeeded, messages);
//	}

	/**
	 * Creates the crc file 
	 * @param request the request
	 * @return CRCResponse
	 * @throws FileException
	 */
//	public CRCResponse createCRCFile(CreateCRCFileRequest request) throws FileException {
//		logger.debug("Create CRC file");
//
//		boolean succeeded = true;
//		List<ProgressDataPayload> messages = new ArrayList<ProgressDataPayload>();
//		IServiceProgressMonitor monitor = request.getProgressMonitor(); 
//		
//		ProgressDataPayload beginMessage = new ProgressDataPayload(MessageType.INFO, "Create CRC file");
//		messages.add(beginMessage);
//		ProgressPayloadUtil.monitorBeginTask(beginMessage, monitor);
//		
//		OutputStream outputStream = null;
//		try {
//			File crcFile = request.getCrcFile();
//			if (!crcFile.exists()) {
//				boolean status = crcFile.createNewFile();
//				if (!status) {
//					ProgressDataPayload fileNotCreatedMessage = new ProgressDataPayload(MessageType.ERROR, "CRC file cannot be created", crcFile.getAbsolutePath(), "<none>");
//					messages.add(fileNotCreatedMessage);
//					ProgressPayloadUtil.monitorSubTask(fileNotCreatedMessage, monitor);
//					succeeded = false;
//				}
//			}
//
//			outputStream = new BufferedOutputStream(new FileOutputStream(crcFile));
//			
//			// file count
//			Properties crcContent = new Properties();
//			List<FileDescriptor> fileDescriptors = request.getFileDescriptors();
//			Collections.sort(fileDescriptors, new DefaultFileDescriptorComparator());
//			crcContent.setProperty(FILE_COUNT, "" + fileDescriptors.size());
//			ProgressDataPayload fileCountMessage = new ProgressDataPayload(MessageType.INFO, "File count: " + fileDescriptors.size());
//			messages.add(fileCountMessage);
//			ProgressPayloadUtil.monitorSubTask(fileCountMessage, monitor);
//
//			// crc mode
//			CRCMode mode = request.getMode();
//			crcContent.setProperty(MODE, mode.name());
//			ProgressDataPayload crcModeMessage = new ProgressDataPayload(MessageType.INFO, "Mode: " + mode.name());
//			messages.add(crcModeMessage);
//			ProgressPayloadUtil.monitorSubTask(crcModeMessage, monitor);
//
//			int fileIndex = 0;
//			Adler32 crc = new Adler32(); 
//			for (FileDescriptor fileDescriptor : fileDescriptors) {
//				File file = fileDescriptor.getFile();
//				
//				String key = getKey(fileDescriptor);
//				String value = "";
//				
//				if (CRCMode.ADLER_32.equals(mode)) {
//					InputStream is = new FileInputStream(file);
//					InputStream cis = new CheckedInputStream(is, crc);
//					while (cis.read() != -1) {
//					}
//					cis.close();
//					value = "" + crc.getValue();
//					crc.reset();
//				}
//				else if (CRCMode.FILE_SIZE.equals(mode)) {
//					value = "" + file.length();
//				}
//				crcContent.setProperty(key, value);
//				
//				++ fileIndex;
//				ProgressDataPayload crcMessage = new ProgressDataPayload(MessageType.INFO, "CRC value for #" + fileIndex + ": " + file.getPath(), null, value);
//				messages.add(crcMessage);
//				ProgressPayloadUtil.monitorSubTask(crcMessage, monitor);
//			}
//			
//			crcContent.store(outputStream, "CRC file");
//			ProgressDataPayload fileSavedMessage = new ProgressDataPayload(MessageType.INFO, "CRC file saved successfully");
//			messages.add(fileSavedMessage);
//			ProgressPayloadUtil.monitorEndTask(fileSavedMessage, monitor);
//		} catch (IOException exc) {
//			throw new FileException(FileException.IO_ERROR);
//		} finally {
//			if (outputStream != null) {
//				try {
//					outputStream.close();
//				}
//				catch (IOException e) {
//					logger.warn("Stream cannot be closed");
//				}
//			}
//		}
//		return new CRCResponse(succeeded, messages);
//	}
	
	/**
	 * Returns the key for a file
	 * @param fileDescriptor the file
	 * @return the key
	 */
//	private String getKey(FileDescriptor fileDescriptor) {
////		String path = PathUtil.removeStartingSlash(fileDescriptor.getRelativePath());
////		path = PathUtil.pathWithoutFirstPathComponent(path);
//		return fileDescriptor.getRelativePath();
//	}
}
