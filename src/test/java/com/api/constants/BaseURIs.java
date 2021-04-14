package com.api.constants;

/**
 * This class contains the API base Uniform Resource Identifier (URI) in the testing scope.
 * @author Muana Kimba
 */
public final class BaseURIs {

	// CONSTRUCTOR
	private BaseURIs() throws Exception {
		throw new Exception();
	}
	
	// BASE URI
	/**
	 * Spotify Web API.
	 */
	public static final String API = "https://api.spotify.com";
	
	/**
	 * Spotify Accounts service.<br>
	 * <a href="https://developer.spotify.com/documentation/general/guides/authorization-guide/#1-have-your-application-request-authorization-the-user-logs-in-and-authorizes-access">Reference</a>
	 */
	public static final String ACCOUNTS = "https://accounts.spotify.com";
}