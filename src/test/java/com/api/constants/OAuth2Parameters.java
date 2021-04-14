package com.api.constants;

/**
 * This class contains authorization flow query and request body parameter constants.
 * @author Muana Kimba
 */
public final class OAuth2Parameters {

	// CONSTRUCTOR
	private OAuth2Parameters() throws Exception {
		throw new Exception();
	}
	
	// Query Parameters
	public static final String RESPONSE_TYPE = "code";
	public static final String CODE_CHALLENGE_METHOD = "S256";	// for Proof Key for Code Exchange (PKCE) flow
	
	// Request Body Parameters
	public static final String GRANT_TYPE = "authorization_code";
	
	// Error if the user decline the APP access to data and features
	public static final String ERROR = "access_denied";
}