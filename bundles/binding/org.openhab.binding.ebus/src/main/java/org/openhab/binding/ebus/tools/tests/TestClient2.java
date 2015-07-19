package org.openhab.binding.ebus.tools.tests;

import org.openhab.binding.ebus.internal.utils.EBusUtils;

public class TestClient2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 30 08 50 22 03 CC 0D 00 BB 00 02 0F 01 C3 00 AA
		byte[] byteArray = EBusUtils.toByteArray("FF 30 50 23 09 F8 B5 27 F6 FF 5D 01 00 00");
		byte crc = 0;

		byteArray = EBusUtils.toByteArray("B5 27 F6 FF 5D 01 00 00");	crc = (byte) 0xF8;
		byteArray = EBusUtils.toByteArray("B5 27 05 00 5D 01 00 00");	crc = (byte) 0xA4;
		byteArray = EBusUtils.toByteArray("B5 27 1E 00 5D 01 00 00");	crc = (byte) 0xE0;
		byteArray = EBusUtils.toByteArray("B5 27 1E 00 5D 01 00 00");	crc = (byte) 0xE0;
		byteArray = EBusUtils.toByteArray("B5 27 FB FF 5D 01 00 00");	crc = (byte) 0x3C;
		byteArray = EBusUtils.toByteArray("B5 27 14 00 5D 01 00 00");	crc = (byte) 0x30;

		//FF	30	50 23	09	F8 B5 27 F6 FF 5D 01 00 00	45


		byte b = 0;
		int c = 0x5C;

		//for (int c = 0; c < 255; c++) {

		for (int i = 0; i < byteArray.length; i++) {
			b = EBusUtils.crc8(byteArray[i], b, (byte)c);
		}
		System.out.println("TestClient2.main() -> " + EBusUtils.toHexDumpString(b));
		if(b == crc) {
			System.err.println("................" + c);
		}

		b = 0;
		//}




	}

}
