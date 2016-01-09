package org.openhab.binding.pixtend.internal;

public enum PiXtendPort {
	AI0(true, false), AI1(true, false), AI2(true, false), AI3(true, false), DI0(true, false), DI1(true, false), DI2(true, false), DI3(true, false), DI4(
			true, false), DI5(true, false), DI6(true, false), DI7(true, false), GPIO0(true, true), GPIO1(true, true), GPIO2(true, true), GPIO3(true,
			true), REG_STATUS(true, false), FIRMWARE_VERSION(true, false), RESET_UC(false, true), AO0(false, true), AO1(false, true), DO0(false, true), DO1(
			false, true), DO2(false, true), DO3(false, true), DO4(false, true), DO5(false, true), REL0(false, true), REL1(false, true), REL2(false,
			true), REL3(false, true);
	private boolean writable;
	private boolean readable;

	public boolean isWritable() {
		return writable;
	}

	public boolean isReadable() {
		return readable;
	}

	private PiXtendPort(boolean canRead, boolean canWrite) {
		writable = canWrite;
		readable = canRead;
	}

	public static String getEnumNamesList() {
		StringBuffer sb = new StringBuffer();
		for (PiXtendPort port : PiXtendPort.values()) {
			sb.append(port);
			sb.append(" ");
		}
		return sb.toString();
	}

}