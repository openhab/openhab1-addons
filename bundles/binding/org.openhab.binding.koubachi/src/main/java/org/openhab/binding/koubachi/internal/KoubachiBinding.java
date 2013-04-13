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
package org.openhab.binding.koubachi.internal;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.openhab.binding.koubachi.KoubachiBindingProvider;
import org.openhab.binding.koubachi.internal.api.Device;
import org.openhab.binding.koubachi.internal.api.KoubachiResource;
import org.openhab.binding.koubachi.internal.api.KoubachiResourceType;
import org.openhab.binding.koubachi.internal.api.Plant;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Active Binding which queries the Koubachi server frequently.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public class KoubachiBinding extends AbstractActiveBinding<KoubachiBindingProvider> {

	private static final Logger logger =  LoggerFactory.getLogger(KoubachiBinding.class);
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Koubachi Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return KoubachiConnector.getRefreshInterval();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected boolean isProperlyConfigured() {
		return KoubachiConnector.isProperlyConfigured();
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		List<Device> devices = KoubachiConnector.getDevices();
		List<Plant> plants = KoubachiConnector.getPlants();
		
		for (KoubachiBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				KoubachiResourceType resourceType = provider.getResourceType(itemName);
				String resourceId = provider.getResourceId(itemName);
				String propertyName = provider.getPropertyName(itemName);
				
				KoubachiResource resource = null;
				if (KoubachiResourceType.DEVICE.equals(resourceType)) {
					resource = findResource(resourceId, devices);
				} else {
					resource = findResource(resourceId, plants);
				}
				
				if (resource == null) {
					logger.debug("Cannot find Koubachi resource with id '{}'", resourceId);
					continue;
				}
				
				try {
					Object propertyValue = PropertyUtils.getProperty(resource, propertyName);
					State state = createState(propertyValue.getClass(), propertyValue);
					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					}
				} catch (Exception e) {
					logger.warn("Reading value '{}' from Resource '{}' throws went wrong", propertyName, resource);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <R extends KoubachiResource> R findResource(String id, List<R> resources) {
		for (KoubachiResource resource : resources) {
			if (resource.getId().equals(id)) {
				return (R) resource;
			}
		}
		return null;
	}
	
	/**
	 * Creates an openHAB {@link State} in accordance to the given {@code dataType}. Currently
	 * {@link Date} and {@link BigDecimal} are handled explicitly. All other {@code dataTypes}
	 * are mapped to {@link StringType}.
	 * 
	 * @param dataType
	 * @param propertyValue
	 * 
	 * @return the new {@link State} in accordance to {@code dataType}. Will never be {@code null}.
	 */
	private State createState(Class<?> dataType, Object propertyValue) {
		if (Date.class.isAssignableFrom(dataType)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) propertyValue);
			return new DateTimeType(calendar);
		} else if (BigDecimal.class.isAssignableFrom(dataType)) {
			return new DecimalType((BigDecimal) propertyValue);
		} else {
			return new StringType(propertyValue.toString());
		}
	}
	
	
}
