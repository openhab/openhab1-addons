/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.io.dropbox.internal;

import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.io.dropbox.DropboxBindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class DropboxBinding extends AbstractEventSubscriberBinding<DropboxBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(DropboxBinding.class);
	
	private DropboxSynchronizerImpl synchronizer;

	
	public DropboxBinding() {
		System.err.println("doit");
	}
	
		
	public void activate() {
		System.err.println("activate");
	}
	
	public void deactivate() {
		System.err.println("deactivate");
	}
	
	public void addSynchronizer(DropboxSynchronizerImpl synchronizer) {
		this.synchronizer = synchronizer;
	}
	
	public void removeSynchronizer(DropboxSynchronizerImpl synchronizer) {
		this.synchronizer = null;
	}
	

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (command instanceof OnOffType) {
			if (this.synchronizer != null) {
				if (OnOffType.ON.equals(command)) {
					synchronizer.activate();
				} else {
					synchronizer.deactivate();
				}
			}
		} else if (command instanceof StringType) {
			try {
				DropboxSyncMode syncMode = DropboxSyncMode.valueOf(command.toString());
				synchronizer.changeSyncMode(syncMode);
			} catch (IllegalArgumentException iae) {
				logger.debug("Unknown SyncMode '{}'. Valid SyncModes are 'DROPBOX_TO_LOCAL', 'LOCAL_TO_DROPBOX' and 'BIDIRECTIONAL'.", command.toString());
			}
		} else {
			logger.debug("Unknown command type '{}'. Dropbox binding can only handle commands of type 'OnOffType' and 'StringType'.", command.getClass().getSimpleName());
		}
	}
	
}
