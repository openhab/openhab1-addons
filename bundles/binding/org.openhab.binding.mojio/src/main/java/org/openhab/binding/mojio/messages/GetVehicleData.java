package org.openhab.binding.mojio.messages;

import static org.openhab.io.net.http.HttpUtil.executeUrl;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.mojio.internal.MojioException;
import org.openhab.binding.mojio.messages.GetMojioData;
import org.openhab.binding.mojio.messages.MojioStatusResponse;
import org.openhab.binding.mojio.messages.MojioType;

public class GetVehicleData extends AbstractFunction {
	private static final String RESOURCE_URL = API_BASE_URL + "v1/Vehicles";

  public static final boolean ASC = false;
  public static final boolean DESC = true;

  private int limit = 10;
  private int offset = 0;
  private String sortBy = "Name";
  private boolean order = ASC;
  private String criteria = "";

  public GetVehicleData(String tag) {
    super(HTTP_GET, tag);
  }

  public GetVehicleData(String tag, String id) {
    super(HTTP_GET, tag);
    this.criteria = "Id=" + id;
  }

	public VehicleStatusResponse execute() {
		final String url = buildQueryString();
		String json = null;

		try {
			json = executeQuery(url);

			final VehicleStatusResponse response = JSON.readValue(json, VehicleStatusResponse.class);

			return response;
		} catch (final Exception e) {
			throw newException("Could not get authorization.", e, url, json);
		}
	}

	private String buildQueryString() {
		final StringBuilder urlBuilder = new StringBuilder(RESOURCE_URL);

		try {
			urlBuilder.append("?limit=");
			urlBuilder.append(this.limit);
      urlBuilder.append("&offset=");
			urlBuilder.append(offset);
			urlBuilder.append("&sortBy=");
			urlBuilder.append(sortBy);
			urlBuilder.append("&desc=");
			urlBuilder.append(order);
			urlBuilder.append("&criteria=");
			urlBuilder.append(criteria);
			return URIUtil.encodeQuery(urlBuilder.toString());
		} catch (final Exception e) {
			throw new MojioException(e);
		}
	}

  protected static VehicleType extractVehicle(VehicleStatusResponse vehicleStatus, int number) {
    if(vehicleStatus.totalRows <= number) return null;

    VehicleType[] vehicles = vehicleStatus.vehicles.toArray(new VehicleType[vehicleStatus.totalRows]);

    return vehicles[number];
  }

  public static VehicleType findById(String token, String id) {
    final GetVehicleData vehicleData = new GetVehicleData(token, id);
    final VehicleStatusResponse vehicleStatus = vehicleData.execute();
    return extractVehicle(vehicleStatus, 0);
  }

  public static VehicleType findByMojioIMEI(String token, String IMEI) {
    final GetMojioData mojioData = new GetMojioData(token, IMEI);
    final MojioStatusResponse mojio = mojioData.execute();

    if(mojio.totalRows == 0) {
      return null;
    }

    MojioType[] mojios = mojio.mojios.toArray(new MojioType[mojio.totalRows]);
    return findById(token, mojios[0].vehicleId);
  }
}
