package org.openhab.binding.fritzaha.internal.model;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author robert
 * 
 */
@XmlRootElement(name = "devicelist")
public class DevicelistModel {
	/* 
	 <devicelist version="1">
	 	<device identifier="08761 0121924" id="16" functionbitmask="896"
			fwversion="03.36" manufacturer="AVM" productname="FRITZ!DECT 200">
			<present>1</present>
			<name>FRITZ!DECT 200 #1</name>
			<switch>
				<state>0</state>
				<mode>manuell</mode>
				<lock>0</lock>
			</switch>
			<powermeter>
				<power>0</power>
				<energy>166</energy>
			</powermeter>
			<temperature>
				<celsius>255</celsius>
				<offset>0</offset>
			</temperature>
		</device>
	</devicelist>
	*/
	
	@XmlAttribute(name="version")
	private String apiVersion;
	
	@XmlElement(name = "device")
	private ArrayList<DeviceModel> devices;

	public ArrayList<DeviceModel> getDevicelist() {
		return devices;
	}

	public void setDevicelist(ArrayList<DeviceModel> devicelist) {
		this.devices = devicelist;
	}

	public String getXmlApiVersion() {
		return this.apiVersion;
	}
	
	public static void main(String[] argv) {
		// create JAXB context and instantiate marshaller
		try {

			TemperatureModel temp = new TemperatureModel();
			temp.setCelsius(new BigDecimal("25.5"));
			temp.setOffset(new BigDecimal("0"));

			PowerMeterModel meter = new PowerMeterModel();
			meter.setEnergy(new BigDecimal("7.01"));
			meter.setPower(new BigDecimal("0.67"));

			DeviceModel dev = new DeviceModel();
			dev.setPowermeter(meter);
			dev.setTemperature(temp);

			DevicelistModel model = new DevicelistModel();
			ArrayList<DeviceModel> devlist = new ArrayList<DeviceModel>();
			devlist.add(dev);
			model.setDevicelist(devlist);

			JAXBContext context = JAXBContext
					.newInstance(DevicelistModel.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Write to System.out
			m.marshal(dev, System.out);

			// Write to File
			// m.marshal(bookstore, new File(BOOKSTORE_XML));

			// get variables from our xml file, created before
			System.out.println();
			System.out.println("Output from our XML File: ");
			Unmarshaller um = context.createUnmarshaller();
			InputStream is = DevicelistModel.class
					.getResourceAsStream("sample.xml");

			model = (DevicelistModel) um.unmarshal(new InputStreamReader(is));
			ArrayList<DeviceModel> list = model.getDevicelist();
			System.out.println("API-Version: " + model.getXmlApiVersion());
			for (DeviceModel device : list) {
				System.out.println(device.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
