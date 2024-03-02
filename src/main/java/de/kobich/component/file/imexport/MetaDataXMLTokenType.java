package de.kobich.component.file.imexport;

public enum MetaDataXMLTokenType {
	FILE_DESCRIPTORS("file-descriptors", true),
	COUNT("count", true),
	FILE_DESCRIPTOR("file-descriptor", true),
	FILE("file", false),
	RELATIVE_PATH("relative-path", false),
	META_DATA("meta-data", true),
	ID("id", false),
	ATTRIBUTE("attribute", true),
	KEY("key", false),
	VALUE("value", false);
	
	private String name;
	
	private MetaDataXMLTokenType(String name, boolean isTag) {
		this.name = name;
	}
	
	public String startTag() {
		return "<" + name + ">";
	}
	
	public String tag() {
		return name;
	}
	
	public String attribute() {
		return name;
	}

	public String endTag() {
		return "</" + name + ">";
	}
}
