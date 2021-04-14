package com.api.base;

import java.util.Properties;

import org.testng.annotations.BeforeSuite;

import com.api.authorization.OAuth2Provider;
import com.api.authorization.OAuth2ResponseBody;
import com.api.authorization.OAuth2URI;
import com.api.constants.BaseURIs;
import com.api.constants.FilePaths;
import com.api.constants.OAuth2Parameters;
import com.api.constants.Scopes;
import com.api.utils.OAuth2Util;
import com.api.utils.DataReader;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import com.google.common.collect.ImmutableMap;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import static org.hamcrest.Matchers.*;

public class BaseTest {

	// ATTRIBUTES
	protected static Properties prop;
	protected static OAuth2ResponseBody oauth2ResponseBody;
	protected static ResponseSpecification responseTime;

	// METHODS
	/**
	 * This method is executed once per suite to read the .properties file,
	 * obtain the OAuth2 Proof Key for Code Exchange (PKCE) access token
	 * and set the expected response time for each test.
	 */
	@BeforeSuite
	public void suiteSetup() throws Exception {
		System.out.println("Configuration reader");
		prop = DataReader.readConfig(FilePaths.CONFIG + "test-config.properties");
		
		// Allure Report environment section
		allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("API", BaseURIs.API)
                        .put("TOOL", "REST-assured")
                        .build());
		
		// OAuth2
		oauth2ResponseBody = obtainOAuth2ResponsePKCE(Scopes.ALL);
		
		// estimation of expected response time
		responseTime = new ResponseSpecBuilder().
				expectResponseTime(lessThan(Long.parseLong(prop.getProperty("RESPONSE_TIME")))).
				build();
	}
	
	/* I could've implemented this method inside the OAuth2Provider class, but I wanted
	 * test cases to only pass the scope as parameter instead of passing as well the code_verifier,
	 * code_challenge, state and many other values in the .properties file.
	*/
	/**
	 * This method obtains the OAuth2 Response object based on the Proof Key for Code Exchange (PKCE)<br>
	 * authorization code flow.
	 * @param scopes The desired scopes to access endpoints 
	 * @return The OAuth2ResponseBody object
	 * @throws Exception
	 */
	protected OAuth2ResponseBody obtainOAuth2ResponsePKCE(String scopes) throws Exception {
		// generate the code_verifier
		String codeVerifier = OAuth2Util.generateCodeVerifier(
				Integer.parseInt(prop.getProperty("CODE_VERIFIER_MIN_LENGTH")),
				Integer.parseInt(prop.getProperty("CODE_VERIFIER_MAX_LENGTH")));
		// generate the code_challenge
		String codeChallenge = OAuth2Util.generateCodeChallange(codeVerifier);
		// generate the state
		String state = OAuth2Util.generateState(Integer.parseInt(prop.getProperty("STATE_LENGTH")));
		
		// construct the authorization URI
		OAuth2URI oauth2URI = new OAuth2URI.Builder()
				.withClientID(prop.getProperty("CLIENT_ID"))
				.withResponseType(OAuth2Parameters.RESPONSE_TYPE)
				.withRedirectURI(prop.getProperty("REDIRECT_URI"))
				.withCodeChallengeMethod(OAuth2Parameters.CODE_CHALLENGE_METHOD)
				.withCodeChallenge(codeChallenge)
				.withState(state)
				.withScopes(scopes)
				.build();
		
		// return OAuth2 response body object for access and refresh tokens
		return
				OAuth2Provider.obtainOAuth2ResponsePKCE(codeVerifier,
				oauth2URI,
				Long.parseLong(prop.getProperty("PAGE_LOAD_TIMEOUT")),
				prop.getProperty("EMAIL_OR_USERNAME"),
				prop.getProperty("PASSWORD")).body().as(OAuth2ResponseBody.class);
	}
}