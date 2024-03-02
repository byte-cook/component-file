package de.kobich.component.file.descriptor;

import java.io.File;

import de.kobich.commons.misc.rename.IRenameable;
import de.kobich.component.file.FileDescriptor;

/**
 * Contains the new name of a file descriptor.
 * @author ckorn
 */
public interface IFileDescriptorRenameable extends IRenameable, Comparable<IFileDescriptorRenameable> {
	
	FileDescriptor getFileDescriptor();

	@Override
	default String getOriginalName() {
		return getFileDescriptor().getFileName();
	}

	@Override
	default String getCategory() {
		return new File(getFileDescriptor().getRelativePath()).getParent();
	}

}
