package de.kobich.component.file.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import de.kobich.commons.net.IProxyProvider;
import de.kobich.commons.net.Proxy;
import de.kobich.commons.runtime.executor.ExecuteRequest;
import de.kobich.commons.runtime.executor.Executor;
import de.kobich.commons.runtime.executor.command.CommandBuilder;
import de.kobich.commons.runtime.executor.command.CommandLineTool;
import de.kobich.commons.runtime.executor.command.CommandVariable;
import de.kobich.commons.utils.StreamUtils;
import de.kobich.component.file.FileException;
import de.kobich.component.file.FileTool;

/**
 * Wget download service.<p>
 * @author ckorn
 */
@Service
public class FileDownloadServiceByWget {

	/**
	 * Returns the tool to use or null, if no suitable tool can be found
	 * @param request
	 * @return
	 */
	public CommandLineTool mayConvert() {
		return FileTool.WGET;
	}

	/**
	 * Download files by definition file
	 * @param request
	 * @throws FileException
	 */
	public void downloadFilesByDefinitionFile(DownloadFilesByDefinitionFileRequest request) throws FileException {
		File downloadFile = request.getDefinitionFile();

		List<URL> urls = new ArrayList<URL>();

		BufferedReader is = null;
		try {
			is = new BufferedReader(new FileReader(downloadFile));

			String line;
			while ((line = is.readLine()) != null) {
				if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
					URL url = new URL(line);
					urls.add(url);
				}
			}

			DownloadFilesRequest downloadFilesRequest = new DownloadFilesRequest(urls, request.getTargetDirectory(), request
					.getLogOutputStream(), request.getErrorOutputStream());
			downloadFilesRequest.setProxyProvider(request.getProxyProvider());
			downloadFilesRequest.setLevel(request.getLevel());
			downloadFilesRequest.setRecursive(request.isRecursive());
			downloadFilesRequest.setCommandDefinitionStream(request.getCommandDefinitionStream());
			downloadFiles(downloadFilesRequest);
		}
		catch (IOException exc) {
			throw new FileException(FileException.DOWNLOAD_ERROR, exc);
		}
		finally {
			StreamUtils.forceClose(is);
		}
	}

	/**
	 * Download files
	 * @param request
	 * @throws FileException
	 */
	public void downloadFiles(DownloadFilesRequest request) throws FileException {
		IProxyProvider proxyProvider = null;
		InputStream definitionStream = null;
		CommandBuilder cb = null;
		try {
			List<URL> urls = request.getUrls();
			OutputStream logOutputStream = request.getLogOutputStream();
			OutputStream errorOutputStream = request.getErrorOutputStream();
			proxyProvider = request.getProxyProvider();
			proxyProvider.init();

			// read command definition
			definitionStream = request.getCommandDefinitionStream();
			if (definitionStream == null) {
				definitionStream = FileTool.WGET.getInternalDefinitionStream(FileDownloadServiceByWget.class);
				if (definitionStream == null) {
					throw new FileException(FileException.DOWNLOAD_ERROR);
				}
			}
			
			cb = new CommandBuilder(definitionStream);
			Executor executor = new Executor();
			for (URL url : urls) {
				List<CommandVariable> params = new ArrayList<CommandVariable>();
				List<CommandVariable> envs = new ArrayList<CommandVariable>();
				
				if (request.isRecursive()) {
					params.add(new CommandVariable("recursive"));
					params.add(new CommandVariable("recursive.level", "" + request.getLevel()));
				}
			
				// set proxy
				if (proxyProvider != null) {
					Proxy proxy = proxyProvider.getProxy(url);
					if (proxy != null) {
						if (proxy.getUsername() != null) {
							params.add(new CommandVariable("proxy.user", proxy.getUsername()));
						}
						if (proxy.getPassword() != null) {
							params.add(new CommandVariable("proxy.password", proxy.getPassword()));
						}
						String proxyURL = proxy.getServer() + ":" + proxy.getPort();
						envs.add(new CommandVariable("proxy.http", proxyURL));
					}
				}
				params.add(new CommandVariable("url", url.toString()));
				
				// create command
				cb.createCommand(params, envs);

				ExecuteRequest executeRequest = new ExecuteRequest(cb.getCommand(), logOutputStream, errorOutputStream);
				executeRequest.setEnv(cb.getEnvironment());
				executeRequest.setWorkingDirectory(request.getTargetDirectory());
				executeRequest.setRedirectErrorStream(true);
				executor.executeCommand(executeRequest);
			}
		}
		catch (IOException exc) {
			throw new FileException(FileException.COMMAND_IO_ERROR, exc, cb != null ? cb.getCommandDefinition().getCommand() : null);
		}
		catch (Exception exc) {
			throw new FileException(FileException.DOWNLOAD_ERROR, exc);
		}
		finally {
			proxyProvider.dispose();
			StreamUtils.forceClose(definitionStream);
			StreamUtils.forceClose(request.getLogOutputStream());
			StreamUtils.forceClose(request.getErrorOutputStream());
		}
	}
}
