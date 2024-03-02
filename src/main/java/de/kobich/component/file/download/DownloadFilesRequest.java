package de.kobich.component.file.download;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;


/**
 * Download files request.
 * @author ckorn
 */
public class DownloadFilesRequest extends AbstractDownloadFilesRequest {
	private final List<URL> urls;

	/**
	 * @param executive
	 * @param urls
	 */
	public DownloadFilesRequest(List<URL> urls, File targetDirectory, OutputStream logOutputStream, OutputStream errorOutputStream) {
		super(targetDirectory, logOutputStream, errorOutputStream);
		this.urls = urls;
	}
	
	/**
	 * @return the urls
	 */
	public List<URL> getUrls() {
		return urls;
	}
}
