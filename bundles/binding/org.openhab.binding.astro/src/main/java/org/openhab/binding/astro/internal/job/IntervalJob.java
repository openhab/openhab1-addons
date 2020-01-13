/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.astro.internal.job;

import java.util.Calendar;

import org.openhab.binding.astro.internal.calc.MoonCalc;
import org.openhab.binding.astro.internal.calc.SunCalc;
import org.openhab.binding.astro.internal.model.Moon;
import org.openhab.binding.astro.internal.model.PlanetName;
import org.openhab.binding.astro.internal.model.Sun;
import org.quartz.JobDataMap;

/**
 * Calculates and publishes the current sun azimuth and elevation and moon
 * illumination end distance.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class IntervalJob extends AbstractBaseJob {

    @Override
    protected void executeJob(JobDataMap jobDataMap) {
        Calendar now = Calendar.getInstance();

        // sun
        SunCalc sunCalc = new SunCalc();
        Sun sun = (Sun) context.getPlanet(PlanetName.SUN);
        sunCalc.setSunPosition(now, context.getConfig().getLatitude(), context.getConfig().getLongitude(), sun);
        planetPublisher.publish(PlanetName.SUN);

        // moon
        MoonCalc moonCalc = new MoonCalc();
        Moon moon = (Moon) context.getPlanet(PlanetName.MOON);
        moonCalc.setMoonPosition(now, context.getConfig().getLatitude(), context.getConfig().getLongitude(), moon);
        planetPublisher.publish(PlanetName.MOON);
    }
}
