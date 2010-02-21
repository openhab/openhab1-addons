/* 
* openHAB, the open Home Automation Bus.
* Copyright 2010, openHAB.org
*
* See the contributors.txt file in the distribution for a
* full listing of individual contributors.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.openhab.core.internal;

import org.openhab.core.datatypes.DataType;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GenericItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class HeartbeatPublisher implements Runnable {

	private BundleContext context;

	public HeartbeatPublisher(BundleContext context) {
		this.context = context;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				break;
			}
			sendHeartbeat();
		}
	}

	private void sendHeartbeat() {
		ServiceReference serviceRef = context
				.getServiceReference("org.openhab.core.events.EventPublisher");
		if (serviceRef != null) {
			EventPublisher eventPublisher = (EventPublisher) context
					.getService(serviceRef);
			if (eventPublisher != null)
				eventPublisher.postCommand(new GenericItem() {

					@Override
					public void setState(DataType newState) {
						// TODO Auto-generated method stub

					}

					@Override
					public void initialize() {
						// TODO Auto-generated method stub

					}

					@Override
					public DataType getState() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public String getName() {
						return "TestItem";
					}

					@Override
					public void dispose() {
						// TODO Auto-generated method stub

					}
				}, new DataType() {
				});
		}
	}
}
