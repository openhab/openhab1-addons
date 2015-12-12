/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.engine.scriptmanager;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Watches the given directory for file updates. It notifies the <code>ScriptManager</code>, if a change is detected.
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 * 
 */
public class ScriptUpdateWatcher implements Runnable {
	static private final Logger logger = LoggerFactory.getLogger(ScriptUpdateWatcher.class);

	private ScriptManager scriptManager;
	private WatchService watcher;
	private File folder;

	private HashMap<File, Long> lastUpdate = new HashMap<File, Long>();

	public ScriptUpdateWatcher(ScriptManager scriptManager, File folder) {
		this.scriptManager = scriptManager;
		this.folder = folder;
	}

	@Override
	public void run() {
		try {
			watcher = FileSystems.getDefault().newWatchService();

			Path dir = Paths.get(folder.getAbsolutePath());
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			while (true) {
				WatchKey key;

				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					logger.info("ScriptUpdateWatcher interrupted");
					return;
				}

				long currentTime = System.currentTimeMillis();

				ArrayList<File> removedScripts = new ArrayList<File>();
				ArrayList<File> addedScripts = new ArrayList<File>();
				ArrayList<File> modifiedScripts = new ArrayList<File>();

				for (WatchEvent<?> event : key.pollEvents()) {

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					WatchEvent.Kind<Path> kind = ev.kind();

					Path fileName = ev.context();

					File f = new File(folder, fileName.toString());

					//skip files ending with ".script" (as these files are definitely no known scripting language)
					if(f.getName().endsWith(".script")) {
						continue;
					}
					
					Long lastTime = lastUpdate.get(f);

					if (lastTime == null || currentTime - lastTime > 5000) {
						logger.debug(kind.name() + ": " + fileName);
						lastUpdate.put(f, currentTime);
						if (kind == ENTRY_CREATE) {
							addedScripts.add(f);
						} else if (kind == ENTRY_DELETE) {
							removedScripts.add(f);
						} else if (kind == ENTRY_MODIFY) {
							modifiedScripts.add(f);
						}
					}
				}

				try {
					scriptManager.scriptsChanged(addedScripts, removedScripts, modifiedScripts);
				}
				catch (Exception ex) {
					logger.error("Error during script change processing", ex);
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}

			}

		} catch (IOException e1) {
			logger.error("WatchService could not be started", e1);
		}
	}

}
