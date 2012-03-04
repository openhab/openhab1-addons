package org.openhab.persistence.db4o.internal;

import java.util.List;

public class PagingUtility {
	public static <T> List<T> paging(List<T> listToPage, int limit) {
		return paging(listToPage, 0, limit);
	}

	public static <T> List<T> paging(List<T> listToPage, int start, int limit) {
		if (start > listToPage.size()) {
			throw new IllegalArgumentException(
					"You cannot start the paging outside the list."
							+ " List-size: " + listToPage.size() + " start: "
							+ start);
		}
		int end = calculateEnd(listToPage, start, limit);
		return listToPage.subList(start, end);

	}

	private static <T> int calculateEnd(List<T> resultList, int start, int limit) {
		int end = start + limit;
		if (end >= resultList.size()) {
			return resultList.size();
		}
		return end;
	}
}