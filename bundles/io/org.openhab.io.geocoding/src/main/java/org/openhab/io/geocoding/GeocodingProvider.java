package org.openhab.io.geocoding;

import org.openhab.library.location.types.LocationType;


public interface GeocodingProvider {
	public LocationType getLocation(String street, String houseNr, String postalCode, String city, String country) throws GeocodingException;
	public LocationType getLocation(String location) throws GeocodingException;
	public double distance (LocationType place1, LocationType place2);
}
