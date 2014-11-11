package org.openhab.binding.enigma2.internal.xml;

public final class XmlUtils {

	private XmlUtils() {
		// hide constructor
	}

	public static String getContentOfElement(String content, String element) {

		final String beginTag = "<" + element + ">";
		final String endTag = "</" + element + ">";

		final int startIndex = content.indexOf(beginTag) + beginTag.length();
		final int endIndex = content.indexOf(endTag);

		if (startIndex != -1 && endIndex != -1) {
			return content.substring(startIndex, endIndex);
		} else {
			return null;
		}
	}
}
