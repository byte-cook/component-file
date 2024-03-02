package de.kobich.component.file.crc;

public enum CRCMode {
	ADLER_32, FILE_SIZE;
	
	public static CRCMode getByName(String name) {
		for (CRCMode mode : CRCMode.values()) {
			if (mode.name().equals(name)) {
				return mode;
			}
		}
		return null;
	}
}
