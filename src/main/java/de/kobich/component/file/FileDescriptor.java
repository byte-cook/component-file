package de.kobich.component.file;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import de.kobich.commons.utils.RelativePathUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a file.
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileDescriptor implements Serializable {
	private static final long serialVersionUID = -9168544779428407872L;
	public static final String METADATA_PROP = "metaData";
	public static final String CONTENT_PROP = "content";
	private final PropertyChangeSupport support;
	@Getter
	@EqualsAndHashCode.Include
	private final File file;
	@Getter
	private final String fileName;
	@Getter
	private final String relativePath;
	@Getter
	private final String extension;
	private IMetaData metaData;
	
	/**
	 * Constructor
	 * @param file the actual file
	 * @param sourceDirectory used to determine the relative path
	 */
	public FileDescriptor(File file, File sourceDirectory) {
		this.file = file;
		this.fileName = FilenameUtils.getName(file.getAbsolutePath());
		String relPath = file.getAbsolutePath().replace(sourceDirectory.getAbsolutePath(), "");
		relPath = RelativePathUtils.convertBackslashToSlash(relPath);
		relPath = RelativePathUtils.ensureStartingSlash(relPath);
		this.relativePath = relPath;
		this.extension = FilenameUtils.getExtension(file.getName());
		this.support = new PropertyChangeSupport(this);
	}
	
	/**
	 * Constructor
	 * @param file
	 * @param relativePath
	 * @param fileDescriptor the one that should be replaced
	 */
	public FileDescriptor(File file, File sourceDirectory, FileDescriptor fileDescriptor) {
		this(file, sourceDirectory);
		this.setMetaData(fileDescriptor.getMetaData());
	}
	
	/**
	 * Constructor
	 * @param file the actual file
	 * @param relativePath the path relative to a source folder
	 */
	public FileDescriptor(File file, String relativePath) {
		this.file = file;
		this.fileName = FilenameUtils.getName(file.getAbsolutePath());
		this.relativePath = relativePath;
		this.extension = FilenameUtils.getExtension(file.getName());
		this.support = new PropertyChangeSupport(this);
	}
	
	/**
	 * Returns the import directory.
	 * Import directory + relative path = absolute path
	 * @return
	 */
	public File getImportDirectory() {
		String filePath = getFile().getAbsolutePath();
		// adjust slashes for relative path
		filePath = RelativePathUtils.convertBackslashToSlash(filePath);
		String importPath = filePath.replace(relativePath, "");
		return new File(importPath);
	}

	/**
	 * Indicates if meta data are set
	 * @return boolean
	 */
	public boolean hasMetaData() {
		return metaData != null;
	}
	
	/**
	 * Indicates if meta data are set and from given class
	 * @return boolean
	 */
	public boolean hasMetaData(Class<? extends IMetaData> metaDataClass) {
		if (getMetaData() != null) {
			if (getMetaData().getClass().isAssignableFrom(metaDataClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the metaData
	 */
	public IMetaData getMetaData() {
		return metaData;
	}
	
	/**
	 * @param metaDataClass
	 * @return
	 */
	public <T extends IMetaData> T getMetaData(Class<T> metaDataClass) {
		if (hasMetaData(metaDataClass)) {
			return metaDataClass.cast(getMetaData());
		}
		return null;
	}
	
	public <T extends IMetaData> Optional<T> getMetaDataOptional(Class<T> metaDataClass) {
		return Optional.ofNullable(this.getMetaData(metaDataClass));
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(IMetaData metaData) {
		if (this.metaData == null || !this.metaData.equals(metaData)) {
			support.firePropertyChange(METADATA_PROP, this.metaData, metaData);
			this.metaData = metaData;
		}
	}
	
	/**
	 * Adds a listener for this class only, {@code IMetaData} and {@code IContent} manage their own {@code PropertyChangeSupport} (requires additional registration) 
	 * @param listener PropertyChangeListener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	/**
	 * @param listener PropertyChangeListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	@Override
	public String toString() {
		return getRelativePath();
	}
}
