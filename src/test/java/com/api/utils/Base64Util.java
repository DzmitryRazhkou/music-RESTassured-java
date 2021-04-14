package com.api.utils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

/**
 * This class contains utility methods for Base64 encoding and decoding.
 * @author Muana Kimba
 */
public final class Base64Util {

	// CONSTRUCTOR
	private Base64Util() throws Exception {
		throw new Exception();
	}
	
	// METHODS
	/**
	 * This method encodes any file extension into a Base64 format.
	 * @param filename The file to encode
	 * @return The Base64 encoded file
	 * @throws IOException
	 */
	public static String encodeFileToBase64(String filename) {
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(filename));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return Base64.getEncoder().encodeToString(fileContent);
	}
}