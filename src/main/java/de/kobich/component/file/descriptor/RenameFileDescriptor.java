package de.kobich.component.file.descriptor;

import java.io.File;
import java.util.Comparator;

import org.springframework.lang.Nullable;

import de.kobich.component.file.DefaultFileDescriptorComparator;
import de.kobich.component.file.FileDescriptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Contains the new name of a file descriptor.
 * @author ckorn
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RenameFileDescriptor implements IFileDescriptorRenameable {
	private static final Comparator<FileDescriptor> COMPARATOR = new DefaultFileDescriptorComparator(); 
	@EqualsAndHashCode.Include
	@Getter
	private final FileDescriptor fileDescriptor;
	private IRenameAttributeProvider attributeProvider;
	private String name;
	
	/**
	 * Constructor
	 * @param fileDescriptor
	 */
	public RenameFileDescriptor(FileDescriptor fileDescriptor, @Nullable IRenameAttributeProvider attributeProvider) {
		this.fileDescriptor = fileDescriptor;
		this.attributeProvider = attributeProvider;
		this.name = fileDescriptor.getFileName();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getOriginalName() {
		return fileDescriptor.getFileName();
	}

	@Override
	public String getCategory() {
		return new File(fileDescriptor.getRelativePath()).getParent();
	}

	@Override
	public String getAttribute(String attribute) {
		if (attributeProvider != null) {
			return attributeProvider.getAttribute(this.fileDescriptor, attribute);
		}
		return null;
	}
	
	public void reload() {
		if (attributeProvider != null) {
			this.attributeProvider.reload();
		}
	}

	@Override
	public int compareTo(IFileDescriptorRenameable arg0) {
		return COMPARATOR.compare(this.getFileDescriptor(), arg0.getFileDescriptor());
	}
}
