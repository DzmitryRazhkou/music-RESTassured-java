package com.api.utils;

import org.json.simple.JSONArray;

/**
 * This class contains utility methods for creating JSON data.
 * @author Muana Kimba
 */
public final class JsonUtil {

	// CONSTRUCTOR
	private JsonUtil() throws Exception {
		throw new Exception();
	}
	
	// METHODS
	/**
	 * This method creates a JSON array and fill it with the value.
	 * @param n The number of times
	 * @param value The value
	 * @return A JSON array of n times the value
	 */
	public static JSONArray repeat(int n, String value) {
		JSONArray array = new JSONArray();
		for(int i = 0; i < n; i++)
			array.add(value);
		
		return array;
	}
}