package de.kobich.component.file.ant;

import java.io.File;
import java.io.OutputStream;
import java.util.Properties;


/**
 * Ant request.
 * @author ckorn
 */
public class FileAntInternallyRequest {
	private File buildFile;
	private String target;
	private Properties properties;
	private FileAntOutputLevel outputLevel;
	private OutputStream logOutputStream;
	private OutputStream errorOutputStream;
	
	/**
	 * Constructor
	 * @param buildFile the build file
	 * @param target the target
	 * @param properties the properties
	 * @param outputLevel
	 * @param logOutputStream
	 * @param errorOutputStream
	 */
	public FileAntInternallyRequest(File buildFile, String target, Properties properties, FileAntOutputLevel outputLevel, OutputStream logOutputStream, OutputStream errorOutputStream) {
		this.buildFile = buildFile;
		this.target = target;
		this.properties = properties;
		this.outputLevel = outputLevel;
		this.logOutputStream = logOutputStream;
		this.errorOutputStream = errorOutputStream;
	}

	/**
	 * @return the buildFile
	 */
	public File getBuildFile() {
		return buildFile;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @return the outputLevel
	 */
	public FileAntOutputLevel getOutputLevel() {
		return outputLevel;
	}

	/**
	 * @return the outputStream
	 */
	public OutputStream getLogOutputStream() {
		return logOutputStream;
	}

	/**
	 * @return the errorOutputStream
	 */
	public OutputStream getErrorOutputStream() {
		return errorOutputStream;
	}
}
