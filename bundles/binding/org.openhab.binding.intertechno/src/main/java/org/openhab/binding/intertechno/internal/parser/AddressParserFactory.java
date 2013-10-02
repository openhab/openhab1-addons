package org.openhab.binding.intertechno.internal.parser;

public class AddressParserFactory {

	public static IntertechnoAddressParser getParser(String type) {

		if ("classic".equals(type)) {
			return new ClassicParser();
		} else if ("fls".equals(type)) {
			return new FLSParser();
		} else if ("rev".equals(type)) {
			return new REVParser();
		} else if ("raw".equals(type)) {
			return new RawParser();
		}

		return null;

	}

}
