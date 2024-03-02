package de.kobich.component.file;

import java.io.File;

import de.kobich.commons.misc.validate.IValidatable;

/**
 * Adapter from file descriptors to validatable.
 * @author ckorn
 */
public class FileDescriptorValidatableAdapter implements IValidatable {
	private String text;
	private FileDescriptor fileDescriptor;
	
	/**
	 * Constructor
	 * @param fileDescriptor
	 */
	public FileDescriptorValidatableAdapter(String text, FileDescriptor fileDescriptor) {
		this.text = text;
		this.fileDescriptor = fileDescriptor;
	}

	@Override
	public String getId() {
		return fileDescriptor.getFile().getAbsolutePath();
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getCategory() {
		return new File(fileDescriptor.getRelativePath()).getParent();
	}

	/**
	 * @return the fileDescriptor
	 */
	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}
}
