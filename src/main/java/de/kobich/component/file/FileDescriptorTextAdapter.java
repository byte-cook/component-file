package de.kobich.component.file;

import de.kobich.commons.misc.extract.IText;

public class FileDescriptorTextAdapter implements IText {
	private FileDescriptor fileDescriptor;
	
	public FileDescriptorTextAdapter(FileDescriptor fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
	}

	@Override
	public String getText() {
		return fileDescriptor.getRelativePath();
	}

	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}
}
