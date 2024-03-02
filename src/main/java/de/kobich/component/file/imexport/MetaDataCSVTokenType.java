package de.kobich.component.file.imexport;

public enum MetaDataCSVTokenType {
	FILE_NAME("FileName", false),
	FILE("File", false),
	EXTENSION("Extension", true),
	RELATIVE_PATH("RelativePath", false),
	MEDIUM_NO("MediumNo", true),
	META_DATA("MetaData", true),
	ID("ID", false),
	DEPTH_FIRST("DepthFirst", false),
	WIDTH_FIRST("WidthFirst", false);
	
	private String name;
	
	private MetaDataCSVTokenType(String name, boolean isTag) {
		this.name = name;
	}
	
	public String tag() {
		return name;
	}
	
	public String attribute() {
		return name;
	}
}
