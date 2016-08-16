/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeLibraryLoader {

	private static final Set<String> loadedLibraries = new TreeSet<String>();
	private static final Logger logger = LoggerFactory.getLogger(NativeLibraryLoader.class);

	private static boolean initialized;

	// private constructor
	private NativeLibraryLoader() {
		// forbid object construction
	}

	public static synchronized void load(String fileName) {
		// check for debug property; if found enable all logging levels
		if (!initialized) {
			initialized = true;
		}

		// first, make sure that this library has not already been previously loaded
		if (loadedLibraries.contains(fileName)) {
			logger.info("Library [" + fileName + "] has already been loaded; no need to load again.");
			return;
		}

		loadedLibraries.add(fileName);

    // path = /lib/{platform}/{filename}
    String platform = "linux_arm";
		String path = "/lib/" + platform + "/" + fileName;
		logger.debug("Attempting to load [" + fileName + "] using path: [" + path + "]");
		try {
			loadLibraryFromClasspath(path);
			logger.debug("Library [" + fileName + "] loaded successfully using embedded resource file: [" + path + "]");
		} catch (Exception | UnsatisfiedLinkError e) {
			logger.error("Unable to load [" + fileName + "] using path: [" + path + "]", e);
		}
	}

	/**
	 * Loads library from classpath
	 *
	 * The file from classpath is copied into system temporary directory and then loaded. The temporary file is deleted after exiting. Method uses String as filename because the pathname is
	 * "abstract", not system-dependent.
	 *
	 * @param path
	 *            The file path in classpath as an absolute path, e.g. /package/File.ext (could be inside jar)
	 * @throws IOException
	 *             If temporary file creation or read/write operation fails
	 * @throws IllegalArgumentException
	 *             If source file (param path) does not exist
	 * @throws IllegalArgumentException
	 *             If the path is not absolute or if the filename is shorter than three characters (restriction of {@see File#createTempFile(java.lang.String, java.lang.String)}).
	 */
	public static void loadLibraryFromClasspath(String path) throws IOException {
		Path inputPath = Paths.get(path);

		if (!inputPath.isAbsolute()) {
			throw new IllegalArgumentException("The path has to be absolute, but found: " + inputPath);
		}

		String fileNameFull = inputPath.getFileName().toString();
		int dotIndex = fileNameFull.indexOf('.');
		if (dotIndex < 0 || dotIndex >= fileNameFull.length() - 1) {
			throw new IllegalArgumentException("The path has to end with a file name and extension, but found: " + fileNameFull);
		}

		String fileName = fileNameFull.substring(0, dotIndex);
		String extension = fileNameFull.substring(dotIndex);

		Path target = Files.createTempFile(fileName, extension);
		File targetFile = target.toFile();
		targetFile.deleteOnExit();

		try (InputStream source = NativeLibraryLoader.class.getResourceAsStream(inputPath.toString())) {
			if (source == null) {
				throw new FileNotFoundException("File " + inputPath + " was not found in classpath.");
			}
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
		// Finally, load the library
		System.load(target.toAbsolutePath().toString());
	}
}
