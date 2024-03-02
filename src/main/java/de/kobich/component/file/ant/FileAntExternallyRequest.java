package de.kobich.component.file.ant;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Ant request.
 */
public class FileAntExternallyRequest {
	private final File buildFile;
	private final String target;
	private final Properties properties;
	private final FileAntOutputLevel outputLevel;
	private final OutputStream logOutputStream;
	private final OutputStream errorOutputStream;
	private InputStream commandDefinitionStream;
	
	/**
	 * Constructor
	 * @param buildFile the build file
	 * @param target the target
	 * @param properties the properties
	 * @param outputLevel
	 * @param logOutputStream
	 * @param errorOutputStream
	 */
	public FileAntExternallyRequest(File buildFile, String target, Properties properties, FileAntOutputLevel outputLevel, OutputStream logOutputStream, OutputStream errorOutputStream) {
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

	/**
	 * @return the commandDefinitionStream
	 */
	public InputStream getCommandDefinitionStream() {
		return commandDefinitionStream;
	}

	/**
	 * @param commandDefinitionStream the commandDefinitionStream to set
	 */
	public void setCommandDefinitionStream(InputStream commandDefinitionStream) {
		this.commandDefinitionStream = commandDefinitionStream;
	}
}
