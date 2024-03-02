package de.kobich.component.file.descriptor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.kobich.commons.misc.extract.StructureVariable;
import de.kobich.commons.misc.validate.rule.IValidationRule;
import de.kobich.commons.monitor.progress.ProgressMonitorRequest;
import de.kobich.component.file.FileDescriptor;

/**
 * Request to validate files.
 * @author ckorn
 */
public class ValidateFileDescriptorsRequest extends ProgressMonitorRequest {
	private Set<FileDescriptor> fileDescriptors;
	private String fileStructurePattern;
	private Map<StructureVariable, List<IValidationRule>> variable2RulesMap;
	
	/**
	 * Constructor
	 */
	public ValidateFileDescriptorsRequest(Set<FileDescriptor> fileDescriptors, String fileStructurePattern, Map<StructureVariable, List<IValidationRule>> variable2RulesMap) {
		this.fileDescriptors = fileDescriptors;
		this.fileStructurePattern = fileStructurePattern;
		this.variable2RulesMap = variable2RulesMap;
	}
	
	/**
	 * @return the fileDescriptors
	 */
	public Set<FileDescriptor> getFileDescriptors() {
		return fileDescriptors;
	}

	/**
	 * @return the fileStructurePattern
	 */
	public String getFileStructurePattern() {
		return fileStructurePattern;
	}

	/**
	 * @return the variable2RulesMap
	 */
	public Map<StructureVariable, List<IValidationRule>> getVariable2RulesMap() {
		return variable2RulesMap;
	}

}
