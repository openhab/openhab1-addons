package android.net.wifi;

//import com.connectsdk.core.Util;
import org.openhab.binding.connectsdk.internal.ConnectSDKBinding;

public class WifiInfo
// implements android.os.Parcelable
{

	private final ConnectSDKBinding binding;
	WifiInfo(ConnectSDKBinding binding) { 
		this.binding = binding;
	}

	public java.lang.String getSSID() {
		throw new RuntimeException("Stub!");
	}

	public java.lang.String getBSSID() {
		throw new RuntimeException("Stub!");
	}

	public int getRssi() {
		throw new RuntimeException("Stub!");
	}

	public int getLinkSpeed() {
		throw new RuntimeException("Stub!");
	}

	public java.lang.String getMacAddress() {
		throw new RuntimeException("Stub!");
	}

	public int getNetworkId() {
		throw new RuntimeException("Stub!");
	}

	// public android.net.wifi.SupplicantState getSupplicantState() { throw new RuntimeException("Stub!"); }
	public int getIpAddress() {
		byte[] b = this.binding.getLocalIPAddress().getAddress();
		// inverse operation to Util.convertIpAddress(ip);
		return (b[3] & 0x000000ff) << 24 | (b[2] & 0x000000ff) << 16 | (b[1] & 0x000000ff) << 8 | b[0]
					& 0x000000ff;
	}

	

	public boolean getHiddenSSID() {
		throw new RuntimeException("Stub!");
	}

	// public static android.net.NetworkInfo.DetailedState getDetailedStateOf(android.net.wifi.SupplicantState
	// suppState) { throw new RuntimeException("Stub!"); }
	public java.lang.String toString() {
		throw new RuntimeException("Stub!");
	}

	public int describeContents() {
		throw new RuntimeException("Stub!");
	}

	// public void writeToParcel(android.os.Parcel dest, int flags) { throw new RuntimeException("Stub!"); }
	public static final java.lang.String LINK_SPEED_UNITS = "Mbps";
}
