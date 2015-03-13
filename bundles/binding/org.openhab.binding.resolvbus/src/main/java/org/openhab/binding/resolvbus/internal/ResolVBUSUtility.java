package org.openhab.binding.resolvbus.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;
import org.openhab.binding.resolvbus.model.ResolVBUSPacket;

public class ResolVBUSUtility {
	// Constants
	public static final int VBUS_PROTOCOL_V1 = 0x10;
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/**
	 * Returns the checksum for the specified bytes.
	 * 
	 * @param buffer
	 *            the buffer where the bytes are in.
	 * @param offset
	 *            the offset from where to start calculating the checksum.
	 * @param length
	 *            the length of bytes to calculate the checksum for.
	 * @return the checksum for the specified bytes.
	 */
	public static char calcChecksum(char[] buffer, int offset, int length) {
		char crc = 0x7F;

		for (int i = 0; i < length; i++) {
			crc = (char) ((crc - buffer[offset + i]) & 0x7F);
		}

		return crc;
	}
	
	public static byte calcChecksum(byte[] buffer, int offset, int length) {
		byte crc = 0x7F;

		for (int i = 0; i < length; i++) {
			crc = (byte) ((crc - buffer[offset + i]) & 0x7F);
		}

		return crc;
	}

	/**
	 * This method injects the outsourced MSB into the origin bytes
	 * 
	 * @param buffer
	 *            the buffer where the bytes are in.
	 * @param offset
	 *            the offset from where to start injecting the septett.
	 * @param length
	 *            the length of bytes to inject the septett to.
	 */
	public static void VBus_InjectSeptett(char[] buffer, int offset, int length) {
		char septett;
		int i;
		septett = buffer[offset + length];
		for (i = 0; i < length; i++) {
			if ((septett & (1 << i)) == (1 << i)) {
				buffer[offset + i] = (char) (buffer[offset + i] | 0x80);
			}
		}
	}
	
	public static void VBus_InjectSeptett(byte[] buffer, int offset, int length) {
		byte septett;
		int i;
		septett = buffer[offset + length];
		for (i = 0; i < length; i++) {
			if ((septett & (1 << i)) == (1 << i)) {
				buffer[offset + i] = (byte) (buffer[offset + i] | 0x80);
			}
		}
	}

	/**
	 * This methods parses the VBusPacket and stores the
	 * 
	 * @param vbPacket
	 * @param xmlPacket
	 */
//	public static List<Integer> parsePacket(ResolVBUSInputStream vInputStream, ResolVBUSPacket vPacket) {
//		List<Integer> valueList = new ArrayList<Integer>();
//
//		for (Field field : xmlPacket.getField()) {
//			int fieldValue = 0;
//			
//			// Calculate number of bytes from bitsize
//			int byteLength = (field.getBitSize().intValue() + 7) / 8;
//			
//			int freeBits = byteLength * 8 - field.getBitSize().intValue();
//
//			// bitSize beachten, alles da¸aber ignorieren
//			for (int i = 1; i <= freeBits; i++) {
//				fieldValue |= 1 << byteLength * 8 - i;
//			}
////			System.out.println("Chars to int: "+charsToInt(vbPacket.getPayloadChar(), field.getOffset().intValue(), byteLength));
//			System.out.println("Bytes to int: "+bytesToInt(vbPacket.getPayloadByte(), field.getOffset().intValue(), byteLength));
////			fieldValue = ~fieldValue & charsToInt(vbPacket.getPayloadChar(), field.getOffset().intValue(), byteLength);
//			fieldValue = ~fieldValue & bytesToInt(vbPacket.getPayloadByte(), field.getOffset().intValue(), byteLength);
////			if (fieldValue > (int) (Math.pow(2, field.getBitSize().intValue() - 1) - 1) / 2) {
////				for (int i = field.getBitSize().intValue(); i < 32; i++) {
////					fieldValue |= 1 << i;
////				}
////			}
//			System.out.println(fieldValue);
//			valueList.add(fieldValue);
//		}
//		return valueList;
//	}

	private static int charsToInt(char[] chars, int offset, int length) {
		int value = 0;
		for (int i = 0; i < length; i++) {
			value |= ((int) chars[offset + i] << (8 * i));
		}
		return value;
	}
	
	private static int bytesToInt(byte[] bytes, int offset, int length) {
		int value = 0;
		for (int i = 0; i < length; i++) {
			value |= ((int) bytes[offset + i] << (8 * i));
		}
		return value;
	}
	
	public static String bytesToHex(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String bytesToHexFormatted(byte[] bytes) {

		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}
