package com.api.authorization;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.chrome.ChromeDriver;

import com.api.constants.BaseURIs;
import com.api.constants.Endpoints;
import com.api.constants.OAuth2Parameters;
import com.api.pages.AuthorizePage;
import com.api.pages.LoginPage;
import com.api.utils.ChromeHeadlessFactory;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * This class completes authorization flows for accessing the APIs.
 * @author Muana Kimba
 */
public final class OAuth2Provider {
	
	/**
	 * This method completes the Proof Key for Code Exchange (PKCE) authorization code flow<br>
	 * and returns the OAuth2 Response object.
	 * @param codeVerifier The code_verifier used to generate the code_challenge
	 * @param oauth2URI The OAuth2 URI object
	 * @param pageLoadTimeout The maximum allowed time for a web page to load
	 * @param emailOrUsername The user email or username
	 * @param password The user password
	 * @return The Response object with the access token
	 */
	public static Response obtainOAuth2ResponsePKCE(String codeVerifier,
			OAuth2URI oauth2URI, long pageLoadTimeout,
			String emailOrUsername, String password) {
		// get final URL from oauth2 flow
		String finalURL = getURLfromOAuth2Flow(pageLoadTimeout, oauth2URI.getURL(), emailOrUsername, password);
		
		// get oauth2 code
		String code = getOAuth2Code(finalURL);
		
		// get and return oauth2 response
		return getOAuth2ResponsePKCE(codeVerifier, oauth2URI, code);
	}

	/**
	 * This method completes the authentication flow and returns the final URL.
	 * @param pageLoadTimeout The maximum allowed time for a web page to load
	 * @param startingURL The starting URL with the query string
	 * @param emailOrUsername The user email or username
	 * @param password The user password
	 * @return The redirect_uri with the query string containing the code and state (optional)
	 */
	private static String getURLfromOAuth2Flow(long pageLoadTimeout, String startingURL,
			String emailOrUsername, String password) {
		// assign an unique Chrome driver in headless mode to the current thread
		ChromeHeadlessFactory.set();

		// retrieve the newly added driver
		ChromeDriver chrome = ChromeHeadlessFactory.get();

		// driver configuration
		chrome.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.SECONDS);
		chrome.manage().deleteAllCookies();

		// --- authorization flow started
		chrome.get(startingURL);
		LoginPage loginPage = new LoginPage(chrome);
		AuthorizePage authPage = loginPage.login(emailOrUsername, password);
		try {
			authPage.approveApp();

		} catch(TimeoutException te) {
			/* Agree button not found because the API didn't ask the user for confirmation.
			 * Therefore the user has directly been redirected to the redirect_uri
			 * with the query string containing the code and state (optional).
			 */
		}
		// authorization flow completed ---

		// retrieve the final url
		String finalUrl = chrome.getCurrentUrl();

		// close and terminate the session
		chrome.quit();
		ChromeHeadlessFactory.remove();

		return finalUrl;
	}

	/**
	 * This method extracts the authentication code from the final URL.
	 * @param finalURL The redirect_uri with the query string
	 * @return The authentication code
	 */
	private static String getOAuth2Code(String finalURL) {
		String code = "";

		try {
			int codeValueIndex = finalURL.indexOf("code=");		// code parameter index
			if(codeValueIndex == -1)
				throw new NoSuchElementException("code parameter not found in redirect_uri!");
			codeValueIndex += 5;	// code parameter value index
			
			int ampersandIndex = finalURL.indexOf("&");
			if(ampersandIndex == -1)	// state parameter not present
				code = finalURL.substring(codeValueIndex);
			else	// state parameter present
				code = finalURL.substring(codeValueIndex, ampersandIndex);
		} catch(NullPointerException npe) {
			npe.printStackTrace();
		} catch(NoSuchElementException nsee) {
			nsee.printStackTrace();
		}

		return code;
	}
	
	/**
	 * This methods exchanges the authorization code for an access token.
	 * @param codeVerifier The code_verifier used to generate the code_challenge
	 * @param oauth2URI The OAuth2 URI object
	 * @param code The authorization code
	 * @return The Response object with the access token
	 */
	private static Response getOAuth2ResponsePKCE(String codeVerifier, OAuth2URI oauth2URI, String code) {
		return
				given().
					baseUri(BaseURIs.ACCOUNTS).
					basePath(Endpoints.ACCESS).
					header("Content-Type", ContentType.URLENC.withCharset("UTF-8")).
					formParams("client_id", oauth2URI.getClientID(),
						"grant_type", OAuth2Parameters.GRANT_TYPE,
						"code", code,
						"redirect_uri", oauth2URI.getRedirectURI(),
						"code_verifier", codeVerifier).
				when().
					post().
				then().
					statusCode(HttpStatus.SC_OK).
					body("$", hasKey("access_token")).	// GPath to verify if the access token is present
				extract().
					response();
	}
}