package com.api.constants;

/**
 * This class contains path for files (configuration, report and test data).
 * @author Muana Kimba
 */
public final class FilePaths {

	// CONSTRUCTOR
	private FilePaths() throws Exception {
		throw new Exception();
	}

	// PATHS
	public static final String CONFIG = "src/test/resources/com/api/config/";
	public static final String REPORTS = "reports/";
	public static final String TEST_DATA = "src/test/resources/com/api/testdata/";
	public static final String JSON_SCHEMA = "src/test/resources/com/api/jsonSchema/";
}