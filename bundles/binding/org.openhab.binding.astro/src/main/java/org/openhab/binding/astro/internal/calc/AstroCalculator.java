/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.calc;

import java.util.Calendar;
import java.util.Date;


/**
 * Calculates the SunPosition (azimuth, elevation) and DayInfo (sunrise, noon, sunset).
 * The calculation for the sunset is simplified, it MAY differ up to two minutes.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 * @see Based on calculation procedure by NOAA (http://www.srrb.noaa.gov/highlights/sunrise/sunrise.html)
 */
public class AstroCalculator {
  private Calendar calendar;
  private double latitude;
  private double longitude;

  /**
   * Calculates the sun position (azimuth and elevation) with the specified date at the specified coordinates.
   * 
   * @param calendar
   *          date and time to calculate
   * @param latitude
   *          the latitude
   * @param longitude
   *          the longitude
   * @return SunPosition (azimuth and elevation)
   */
  public SunPosition getSunPosition(Calendar calendar, double latitude, double longitude) {
    setParameters(calendar, latitude, longitude);
    return getSunPosition();
  }

  /**
   * Calculates sunrise, noon and sunset with the specified date at the specified coordinates.
   * 
   * @param calendar
   *          date and time to calculate
   * @param latitude
   *          the latitude
   * @param longitude
   *          the longitude
   * @return DayInfo (sunrise, noon, sunset)
   */
  public DayInfo getDayInfo(Calendar calendar, double latitude, double longitude) {
    setParameters(calendar, latitude, longitude);
    return getDayInfo();
  }

  private void setParameters(Calendar calendar, double latitude, double longitude) {
    this.calendar = calendar;
    this.latitude = latitude;
    this.longitude = longitude * -1;
  }

  private int getTimeZone() {
    return calendar.getTimeZone().getDSTSavings() / 3600000;
  }

  private boolean isDaylightSavingTime() {
    return calendar.getTimeZone().inDaylightTime(new Date(calendar.getTimeInMillis()));
  }

  /**
   * Converts time from midnight to calendar object.
   */
  private Calendar toCalendar(double timeFromMidnight) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
    cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
    cal.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, (int) timeFromMidnight * 60000);
    return cal;
  }

  /**
   * Calculates azimuth and elevation of the sun.
   */
  private SunPosition getSunPosition() {
    // change time zone to positive hours in western hemisphere
    int zone = getTimeZone() * -1;
    int hh = calendar.get(Calendar.HOUR_OF_DAY) - (isDaylightSavingTime() ? 1 : 0);

    // timenow is GMT time for calculation in hours since 0Z
    long timenow = hh + calendar.get(Calendar.MINUTE) / 60 / 3600 + zone;

    double jd = calcJulianDate();

    double t = calcTimeJulianCent(jd + timenow / 24);
    double solarDec = calcSunDeclination(t);
    double eqtime = calcEquationOfTime(t);

    double solarTimeFix = eqtime - 4 * (longitude) + 60 * zone;
    double trueSolarTime = hh * 60 + calendar.get(Calendar.MINUTE) + solarTimeFix;

    while (trueSolarTime > 1440) {
      trueSolarTime = trueSolarTime - 1440;
    }

    double hourangle = trueSolarTime / 4 - 180;
    if (hourangle < -180) {
      hourangle = hourangle + 360;
    }

    double csz = Math.sin(degToRad(latitude)) * Math.sin(degToRad(solarDec)) + Math.cos(degToRad(latitude)) * Math.cos(degToRad(solarDec)) * Math.cos(degToRad(hourangle));

    if (csz > 1) {
      csz = 1;
    }
    else if (csz < -1) {
      csz = -1;
    }

    double zenith = radToDeg(Math.acos(csz));

    double azDenom = (Math.cos(degToRad(latitude)) * Math.sin(degToRad(zenith)));
    double azimuth = 0;

    if (Math.abs(azDenom) > 0.001) {
      double azRad = ((Math.sin(degToRad(latitude)) * Math.cos(degToRad(zenith))) - Math.sin(degToRad(solarDec))) / azDenom;
      if (Math.abs(azRad) > 1) {
        azRad = azRad < 0 ? -1 : 1;
      }
      azimuth = 180 - radToDeg(Math.acos(azRad));
      if (hourangle > 0) {
        azimuth = -azimuth;
      }
    }
    else {
      if (latitude > 0) {
        azimuth = 180;
      }
    }
    if (azimuth < 0) {
      azimuth = azimuth + 360;
    }

    return new SunPosition(azimuth, 90 - zenith);
  }

  /**
   * Calculates sunrise, solarnoon and sunset.
   */
  private DayInfo getDayInfo() {
    double jd = calcJulianDate();

    double t = calcTimeJulianCent(jd);
    // *** First pass to approximate sunrise
    double eqtime = calcEquationOfTime(t);
    double solarDec = calcSunDeclination(t);
    double hourangle = calcHourAngleSunset(latitude, solarDec);
    double delta = longitude - radToDeg(hourangle);
    double timeDiff = 4 * delta;
    double solSetUTC = 720 + timeDiff - eqtime;

    // *** Second pass includes fractional jday in gamma calc
    double newt = calcTimeJulianCent(calcJDFromJulianCent(t) + solSetUTC / 1440);
    eqtime = calcEquationOfTime(newt);
    solarDec = calcSunDeclination(newt);
    hourangle = calcHourAngleSunset(latitude, solarDec);
    delta = longitude - radToDeg(hourangle);
    timeDiff = 4 * delta;
    solSetUTC = 720 + timeDiff - eqtime;

    Calendar sunset = toCalendar(adjustTimezoneAndDaylightSaving(solSetUTC));

    double solNoonUTC = 720 + (longitude * 4) - eqtime;
    Calendar noon = toCalendar(adjustTimezoneAndDaylightSaving(solNoonUTC));

    double solRiseUTC = solNoonUTC - (solSetUTC - solNoonUTC);
    Calendar sunrise = toCalendar(adjustTimezoneAndDaylightSaving(solRiseUTC));
    return new DayInfo(sunrise, noon, sunset);
  }

  private double adjustTimezoneAndDaylightSaving(double utc) {
    return utc + (60 * getTimeZone()) + (isDaylightSavingTime() ? 60 : 0);
  }

  /**
   * Julian day from calendar day.
   */
  private double calcJulianDate() {
    int month = calendar.get(Calendar.MONTH) + 1;
    int year = calendar.get(Calendar.YEAR);
    if (month <= 2) {
      year = year - 1;
      month = month + 12;
    }

    double a = Math.floor(year / 100);
    double b = 2 - a + Math.floor(a / 4);

    return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + calendar.get(Calendar.DAY_OF_MONTH) + b - 1524.5;
  }

  /**
   * Convert Julian Day to centuries since J2000.
   */

  private double calcTimeJulianCent(double jd) {
    return (jd - 2451545) / 36525;
  }

  /**
   * Calculate the difference between true solar time and mean solar time.
   */
  private double calcEquationOfTime(double t) {
    double epsilon = calcObliquityCorrection(t);
    double l0 = calcGeomMeanLongSun(t);
    double e = calcEccentricityEarthOrbit(t);
    double m = calcGeomMeanAnomalySun(t);

    double y = Math.tan(degToRad(epsilon) / 2);
    y = y * y;

    double sin2l0 = Math.sin(2 * degToRad(l0));
    double sinm = Math.sin(degToRad(m));
    double cos2l0 = Math.cos(2 * degToRad(l0));
    double sin4l0 = Math.sin(4 * degToRad(l0));
    double sin2m = Math.sin(2 * degToRad(m));

    double etime = y * sin2l0 - 2 * e * sinm + 4 * e * y * sinm * cos2l0 - 0.5 * y * y * sin4l0 - 1.25 * e * e * sin2m;

    return radToDeg(etime) * 4;
  }

  /**
   * Calculate the corrected obliquity of the ecliptic.
   */
  private double calcObliquityCorrection(double t) {
    double e0 = calcMeanObliquityOfEcliptic(t);
    double omega = 125.04 - 1934.136 * t;
    return e0 + 0.00256 * Math.cos(degToRad(omega));
  }

  /**
   * Calculate the mean obliquity of the ecliptic.
   */
  private double calcMeanObliquityOfEcliptic(double t) {
    double seconds = 21.448 - t * (46.815 + t * (0.00059 - t * (0.001813)));
    return 23 + (26 + (seconds / 60)) / 60;
  }

  /**
   * Convert degree angle to radians.
   */
  private double degToRad(double angleDeg) {
    return (Math.PI * angleDeg / 180);
  }

  /**
   * Convert radian angle to degrees.
   */
  private double radToDeg(double angleRad) {
    return (180 * angleRad / Math.PI);
  }

  /**
   * Calculate the declination of the sun.
   */
  private double calcSunDeclination(double t) {
    double e = calcObliquityCorrection(t);
    double lambda = calcSunApparentLong(t);
    double sint = Math.sin(degToRad(e)) * Math.sin(degToRad(lambda));
    return radToDeg(Math.asin(sint));
  }

  /**
   * Calculate the apparent longitude of the sun.
   */
  private double calcSunApparentLong(double t) {
    double o = calcSunTrueLong(t);
    double omega = 125.04 - 1934.136 * t;
    return o - 0.00569 - 0.00478 * Math.sin(degToRad(omega));
  }

  /**
   * Calculate the true longitude of the sun.
   */
  private double calcSunTrueLong(double t) {
    return calcGeomMeanLongSun(t) + calcSunEqOfCenter(t);
  }

  /**
   * Calculate the Geometric Mean Longitude of the sun.
   */
  private double calcGeomMeanLongSun(double t) {
    double l0 = 280.46646 + t * (36000.76983 + 0.0003032 * t);
    do {
      if (l0 > 360) {
        l0 = l0 - 360;
      }
      else if (l0 < 0) {
        l0 = l0 + 360;
      }
    } while (l0 > 360 || l0 < 0);

    return l0;
  }

  /**
   * Calculate the Geometric Mean Anomaly of the sun.
   */
  private double calcGeomMeanAnomalySun(double t) {
    return 357.52911 + t * (35999.05029 - 0.0001537 * t);
  }

  /**
   * Calculate the equation of center for the sun.
   */
  private double calcSunEqOfCenter(double t) {
    double m = calcGeomMeanAnomalySun(t);

    double mrad = degToRad(m);
    double sinm = Math.sin(mrad);
    double sin2m = Math.sin(mrad + mrad);
    double sin3m = Math.sin(mrad + mrad + mrad);

    return sinm * (1.914602 - t * (0.004817 + 1.4e-05 * t)) + sin2m * (0.019993 - 0.000101 * t) + sin3m * 0.000289;
  }

  /**
   * Calculate the hour angle of the sun at sunset for the latitude.
   */
  private double calcHourAngleSunset(double lat, double solarDec) {
    double latRad = degToRad(lat);
    double sdRad = degToRad(solarDec);
    return (Math.acos(Math.cos(degToRad(90.833)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad))) * -1;
  }

  /**
   * Convert centuries since J2000 to julian day.
   */
  private double calcJDFromJulianCent(double t) {
    return t * 36525 + 2451545;
  }

  /**
   * Calculate the eccentricity of earth's orbit.
   */
  private double calcEccentricityEarthOrbit(double t) {
    return 0.016708634 - t * (4.2037e-05 + 1.267e-07 * t);
  }

}
