package org.openhab.io.geocoding.internal;

import java.io.IOException;
import java.util.Dictionary;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.geocoding.GeocodingException;
import org.openhab.io.geocoding.GeocodingProvider;
import org.openhab.library.location.types.LocationType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

/**
 * 
 * @author Robert Delbrück
 * @since 1.6.1
 *
 */
public class GeocodingProviderImpl extends AbstractActiveService implements ManagedService, GeocodingProvider {
	private static final Logger LOG = LoggerFactory.getLogger(GeocodingProviderImpl.class);
	

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		
	}
	

	@Override
	protected void execute() {
	}

	@Override
	protected long getRefreshInterval() {
		return 0;
	}

	@Override
	protected String getName() {
		return "Geocoding Provider";
	}


	@Override
	public LocationType getLocation(String street, String houseNr,
			String postalCode, String city, String country) throws GeocodingException {
		
		StringBuilder sb = new StringBuilder();
		if (street != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(street);
		}
		
		if (houseNr != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(houseNr);
		}
		
		if (postalCode != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(postalCode);
		}
		
		if (city != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(city);
		}
		
		if (country != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(country);
		}
		
		return this.getLocation(sb.toString());
	}


	@Override
	public double distance(LocationType place1, LocationType place2) {
        float lat1 = ((DecimalType) place1.getLatitude()).floatValue();
        float lng1 = ((DecimalType) place1.getLongitude()).floatValue();
        float lat2 = ((DecimalType) place2.getLatitude()).floatValue();
        float lng2 = ((DecimalType) place2.getLongitude()).floatValue();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                        * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double earthRadiusKm = 6369;
        double distKm = earthRadiusKm * c;

        // return the distance in meters
        return distKm * 1000;
	}


	@Override
	public LocationType getLocation(String location) throws GeocodingException {
		final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("de").getGeocoderRequest();
        GeocodeResponse geocoderResponse;
		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
		} catch (IOException e) {
			throw new GeocodingException("error while geocoding", e);
		}
        for (GeocoderResult geocoderResult : geocoderResponse.getResults()) {
            GeocoderGeometry geometry = geocoderResult.getGeometry();
            LatLng latLng = geometry.getLocation();
            return new LocationType(latLng.getLat(), latLng.getLng());
        }
        
        return null;
	}

}
