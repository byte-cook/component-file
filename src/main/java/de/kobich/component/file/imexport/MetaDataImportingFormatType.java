package de.kobich.component.file.imexport;

public enum MetaDataImportingFormatType {
	XML;
	
	/**
	 * Returns by name
	 * @return
	 */
	public static MetaDataImportingFormatType getByName(String name) {
		for (MetaDataImportingFormatType type : MetaDataImportingFormatType.values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
