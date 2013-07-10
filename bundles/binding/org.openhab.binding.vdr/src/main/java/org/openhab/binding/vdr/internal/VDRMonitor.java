/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.vdr.internal;

import org.openhab.binding.vdr.VDRBindingProvider;
import org.openhab.binding.vdr.VDRCommandType;
import org.openhab.binding.vdr.internal.VDRBinding.VDRConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The VDRMonitor connects periodically to a VDR on the svdr port and updates
 * several state informations
 * 
 * The followining checks are implemented: - recording status
 * 
 * @author Wolfgang Willinghoefer
 * @since 0.9.0
 */
public class VDRMonitor extends AbstractActiveService {

	static final Logger logger = LoggerFactory.getLogger(VDRMonitor.class);

	/* the VDRBinding Service */
	private static VDRBinding vdrBinding;

	/**
	 * the refresh interval which is used to poll the several VDR variables
	 * (e.g. recording state; defaults to 60000ms)
	 */
	private long refreshInterval = 120000;
	
	private EventPublisher eventPublisher = null;
	
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}
	
	public void setVDRBinding(VDRBinding vdrBinding) {
		VDRMonitor.vdrBinding = vdrBinding;
		setProperlyConfiguredAndStart();
	}

	public void unsetVDRBinding(VDRBinding vdrBinding) {
		VDRMonitor.vdrBinding = null;
	}
	

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "VDR Monitor Service";
	}
	
	
	@Override
	protected void execute() {
		logger.debug("VDRMonitor execute called");
		if (vdrBinding != null) {
			for (VDRConfig vdrConfig : vdrBinding.vdrConfigCache.values()) {
				Boolean recording = checkRecording(vdrConfig);
				if (recording != null) {
					if (eventPublisher != null) {
						VDRBindingProvider bindingProvider = vdrBinding
								.findFirstMatchingBindingProviderByVDRId(vdrConfig.vdrId);
						if (bindingProvider != null) {
							String itemName = bindingProvider
									.getBindingItemName(vdrConfig.vdrId,
											VDRCommandType.RECORDING);
							State newState = recording.booleanValue() ? OnOffType.ON
									: OnOffType.OFF;
							eventPublisher.postUpdate(itemName, newState);

						}
					}
				}
			}
		} else {
			logger.info("VDRBinding not available");
		}
	}

	private Boolean checkRecording(VDRConfig vdrConfig) {
		return vdrConfig.getVDRConnection().isRecording();
	}

}
