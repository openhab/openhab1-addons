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

import java.util.Collection;

import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.service.ActiveServiceStatusListener;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.dropbox.DropboxBindingProvider;
import org.openhab.io.dropbox.DropboxSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>Binding to allow user interaction with the {@link DropboxSynchronizer}. Since
 * we do not really connect hardware to openHAB this Binding is to perceive as
 * a virtual binding.</p>
 * 
 * <p>The Binding can handle Switch- and StringTypes to control the behaviour
 * of the {@link DropboxSynchronizer}. On the other hand it posts the {@link State}
 * updates of the {@link DropboxSynchronizer} the openHAB bus. Hence the connected
 * items should be configured with <code>autoupdate="false"</code> to reflect
 * the "real" state of the {@link DropboxSynchronizer}</p>
 * 
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class DropboxBinding extends AbstractEventSubscriberBinding<DropboxBindingProvider> implements ActiveServiceStatusListener {

	private static final Logger logger = 
		LoggerFactory.getLogger(DropboxBinding.class);
	
	private DropboxSynchronizer synchronizer;

	private EventPublisher eventPublisher = null;
	
	
	public DropboxBinding() {
	}
	
		
	public void activate() {
	}
	
	public void deactivate() {
	}
	
	public void addSynchronizer(DropboxSynchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.synchronizer.addStatusListener(this);
		
		// post the current status of the synchronizer to 
		// keep the added listener in sync ...
		postStatus(this.synchronizer.isRunning() ? OnOffType.ON : OnOffType.OFF);
	}
	
	public void removeSynchronizer(DropboxSynchronizer synchronizer) {
		this.synchronizer.removeStatusListener(this);
		this.synchronizer = null;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
	
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (this.synchronizer != null) {
			if (command instanceof OnOffType) {
				if (OnOffType.ON.equals(command)) {
					synchronizer.activate();
				} else {
					synchronizer.deactivate();
				}
			} else if (command instanceof StringType) {
				try {
					DropboxSyncMode syncMode = 
						DropboxSyncMode.valueOf(command.toString());
					synchronizer.changeSyncMode(syncMode);
				} catch (IllegalArgumentException iae) {
					logger.debug("Unknown SyncMode '{}'. Valid SyncModes are 'DROPBOX_TO_LOCAL', 'LOCAL_TO_DROPBOX' and 'BIDIRECTIONAL'.", command.toString());
				}
			} else {
				logger.debug("Unknown command type '{}'. Dropbox binding can only handle commands of type 'OnOffType' and 'StringType'.", command.getClass().getSimpleName());
			}
		}
	}

	
	@Override
	public void started() {
		postStatus(OnOffType.ON);
	}

	@Override
	public void shutdownCompleted() {
		postStatus(OnOffType.OFF);
	}
	
	private void postStatus(State state) {
		for (DropboxBindingProvider provider : providers) {
			Collection<String> itemNames = provider.getItemNamesOf(SwitchItem.class);
			for (String itemName : itemNames) {
				eventPublisher.postUpdate(itemName, state);
			}
		}
	}	

	
}
