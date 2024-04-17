package de.kobich.component.file.descriptor;

import de.kobich.component.file.FileDescriptor;

public interface IRenameAttributeProvider {

	String getAttribute(FileDescriptor fileDescriptor, String attribute);

	void reload();

}