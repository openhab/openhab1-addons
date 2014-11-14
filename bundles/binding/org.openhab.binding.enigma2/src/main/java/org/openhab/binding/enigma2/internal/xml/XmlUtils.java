package org.openhab.binding.enigma2.internal.xml;

/**
 * Util method for xml response handling
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public final class XmlUtils {

	private XmlUtils() {
		// hide constructor
	}

	/**
	 * Processes an string containing xml and returning the content of a
	 * specific tag (alyways lowercase)
	 */
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
