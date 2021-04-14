package com.api.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class contains utility methods to read files.
 * @author Muana Kimba
 */
public final class DataReader {

	// CONSTRUCTOR
	private DataReader() throws Exception {
		throw new Exception();
	}

	// METHODS
	/**
	 * This method reads a .properties file for configuration
	 * @param filename the .properties file to read
	 * @return an object of the Properties class
	 */
	public static Properties readConfig(String filename) {
		Properties properties = null;

		try {
			properties = new Properties();
			FileInputStream fis = new FileInputStream(filename);
			properties.load(fis);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

		return properties;
	}

	/**
	 * This method reads a CSV file and returns a list of test data
	 * @param filename the CSV file to read
	 * @param delimiter used to separate data
	 * @return a 2D array of array of data
	 */
	public static String[][] readCSV(String filename, String delimiter) {
		List<String[]> testdata = new ArrayList<String[]>();

		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8);
			String line;
			
			while((line = br.readLine()) != null)
				testdata.add(line.split(delimiter));
			
			br.close();
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return twoDimensionalListToTwoDimensionalArray(testdata);
	}
	
	/**
	 * This method reads a TXT file and returns a list of test data.
	 * @param filename The TXT file to read
	 * @return An array of data
	 */
	// One dimensional test data array because .txt file doesn't have a delimiter
	// where each line is a test case therefore an array[index] is a data for a Test Case
	public static String[] readTXT(String filename) {
		List<String> testdata = new ArrayList<>();
		
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8);
			String line;
			
			while((line = br.readLine()) != null)
				testdata.add(line);
			
			br.close();
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return testdata.toArray(new String[0]);
	}
	
	// HELPER METHODS
	/**
	 * This method convert a 2D List of String array to a 2D array of String array.
	 * @param list The List of String array 
	 * @return The 2D array of String array
	 */
	private static String[][] twoDimensionalListToTwoDimensionalArray(List<String[]> list) {
		String[][] array = new String[list.size()][];	// second dimension is optional
		
		for(int i = 0; i < array.length; i++)
			array[i] = list.get(i);
		
		return array;
	}
}