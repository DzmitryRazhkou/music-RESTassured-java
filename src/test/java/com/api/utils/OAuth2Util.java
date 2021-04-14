package com.api.utils;

import java.security.SecureRandom;
import java.util.Base64;

import com.api.constants.BaseURIs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.oauth2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class contains utility methods for authorization flows.
 * @author Muana Kimba
 */
public final class OAuth2Util {

	// CONSTRUCTOR
	private OAuth2Util() throws Exception {
		throw new Exception();
	}

	// ATTRIBUTES
	private static final String STATE_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			+ "abcdefghijklmnopqrstuvwxyz"
			+ "0123456789";
	
	private static final String CODE_VERIFIER_POOL = STATE_POOL + "_.-~";
	
	private static final SecureRandom RANDOM = new SecureRandom();
	
	// METHODS
	/**
	 * This method creates the OAuth2 request specification based on the given access token<br>
	 * containing the OAuth2 authentication scheme and the base uri.
	 * @param accessToken The access token
	 * @return The OAuth2 request specification
	 */
	public static RequestSpecification getOAuth2ReqSpec(String accessToken) {
		return new RequestSpecBuilder().
				setAuth(oauth2(accessToken)).
				setBaseUri(BaseURIs.API).
				build();
	}
	
	/**
	 * This method generates the state containing a random alphanumeric string.
	 * @param length The desired length
	 * @return The state of given length
	 */
	public static String generateState(int length) {
		// StringBuilder is more efficient at concatenating long string than the String object 
		StringBuilder state = new StringBuilder(length);
		
		for(int i = 0; i < length; i++)
			state.append(STATE_POOL.charAt(RANDOM.nextInt(STATE_POOL.length())));
		
		return state.toString();
	}
	
	/**
	 * This method generates a random cryptographic code_verifier string.
	 * @param min The minimum string length
	 * @param max The maximum string length
	 * @return The cryptographic code_verifier string of random length between min and max
	 */
	public static String generateCodeVerifier(int min, int max) {
		int length = RANDOM.nextInt(max - min + 1) + min;
		StringBuilder codeVerifier = new StringBuilder(length);
		
		for(int i = 0; i < length; i++)
			codeVerifier.append(CODE_VERIFIER_POOL.charAt(RANDOM.nextInt(CODE_VERIFIER_POOL.length())));
		
		return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier.toString().getBytes());
	}
	
	/**
	 * This method generates the code_challenge based on the code_verifier.<br><br>
	 * code_challenge = BASE64URL-ENCODE(SHA256(ASCII(code_verifier)))
	 * @param codeVerifier The cryptographic code_verifier string
	 * @return The base64url encoded code_challenge
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @see <a href="https://www.appsdeveloperblog.com/pkce-code-verifier-and-code-challenge-in-java/">Source</a>
	 */
    public static String generateCodeChallange(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");						// convert code_verifier to ASCII
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");		// create SHA-256 encryption
        messageDigest.update(bytes, 0, bytes.length);							// set ASCII code_verifier
        byte[] digest = messageDigest.digest();									// perform SHA-256 encryption to the ASCII code_verifier
        
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);	// URLEncode the ASCII SHA-256 encrypted code_verifier
    }
}