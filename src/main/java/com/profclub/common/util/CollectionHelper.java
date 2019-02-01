package com.profclub.common.util;

import java.util.*;

/**
 * Utility methods for data manipulations with collections.
 */
public class CollectionHelper {
	private static final Random _random = new Random();
	private static final String _default_delimiter = ",";

    public static boolean isBlank(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotBlank(Collection collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isNotBlank(Object[] objects) {
        return objects != null && objects.length > 0;
    }

    public static boolean isBlank(Object[] objects) {
        return objects == null || objects.length == 0;
    }

	public static String getContentAsString(Object[] array) {
		return getContentAsString(array, null, true);
	}

	public static String getContentAsString(Object[] array, boolean trimContents) {
		return getContentAsString(array, null, trimContents);
	}

	public static String getContentAsString(Object[] array, String delimiter, boolean trimContents) {
		if (array == null || array.length == 0)
			return "";
		if (delimiter == null || delimiter.length() == 0)
			delimiter = _default_delimiter;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length - 1; i++) {
			if (trimContents) {
				sb.append(String.valueOf(array[i]).trim());
			} else {
				sb.append(array[i]);
			}
			sb.append(delimiter);
		}
		sb.append(array[array.length - 1]);

		return sb.toString();
	}

	public static <T> T getRandomObject(Collection<T> items) {
		if (isBlank(items))
			return null;

		int len = items.size();
		int index = _random.nextInt(len);

		return ((T[])items.toArray())[index];
	}

	public static <T> T getLastObject(List<T> items) {
    	if (isBlank(items))
    		return null;

    	int len = items.size();
		return items.get(len - 1);
	}

	public static void printContentToConsole(List list) {
		if (isNotBlank(list)) {
			for (Object o : list) {
				System.out.println(o);
			}
		}
	}

}
