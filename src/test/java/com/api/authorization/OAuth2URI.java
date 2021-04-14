package com.api.authorization;

import com.api.constants.BaseURIs;
import com.api.constants.Endpoints;

/**
 * This class represents the OAuth2 uniform resource identifier (URI) [base uri + endpoint + query string]
 * using the Builder pattern because it has to accommodate all authorization flows
 * with different set of ordered or unordered query parameter-value pairs.
 * @author Muana Kimba
 */
public class OAuth2URI {
	
	// MEMBERS
	// Query Parameters
	private String clientID;
	private String responseType;
	private String redirectURI;
	private String codeChallengeMethod;
	private String codeChallenge;
	private String state;
	private String scopes;
	
	// Uniform Resource Locator (URL)
	private String url;
	
	// CONSTRUCTOR
	private OAuth2URI() {
	}
	
	// GETTERS
	public String getClientID() {
		return clientID;
	}
	public String getResponseType() {
		return responseType;
	}
	public String getRedirectURI() {
		return redirectURI;
	}
	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}
	public String getCodeChallenge() {
		return codeChallenge;
	}
	public String getState() {
		return state;
	}
	public String getScopes() {
		return scopes;
	}
	
	public String getURL() {
		return url;
	}

	// inner Builder class
	public static class Builder {
		
		// MEMBERS
		private String clientID;
		private String responseType;
		private String redirectURI;
		private String codeChallengeMethod;
		private String codeChallenge;
		private String state;
		private String scopes;
		
		private StringBuilder querySB;
		
		// CONSTRUCTOR
		public Builder() {
			clientID = "";					// used in the /api/token POST request body (OAuth2Provider)
			responseType = "";
			redirectURI = "";				// used in the /api/token POST request body (OAuth2Provider)
			codeChallengeMethod = "";
			codeChallenge = "";
			state = "";
			scopes = "";
			
			querySB = new StringBuilder();	// query string
		}
		
		// FLUENT SETTERS
		/*
		 * value:
		 * 		- null means don't add the query parameter to the query string
		 * 		- "" means add the query parameter to the query string without a value
		 * 		- not calling any of the 'with' method will have the same effect of passing a null value,
		 * 			but the attribute's value will remains ""
		 * 
		 * Simply put, null values are allowed in the POST request, but not in the query string
		 * because it doesn't make sense. If you want a null value, send it as a string e.g. "null".
		 */
		public Builder withClientID(String clientID) {
			this.clientID = clientID;
			appendToQueryString("client_id", clientID);
			return this;
		}
		public Builder withResponseType(String responseType) {
			this.responseType = responseType;
			appendToQueryString("response_type", responseType);
			return this;
		}
		public Builder withRedirectURI(String redirectURI) {
			this.redirectURI = redirectURI;
			appendToQueryString("redirect_uri", redirectURI);
			return this;
		}
		public Builder withCodeChallengeMethod(String codeChallengeMethod) {
			this.codeChallengeMethod = codeChallengeMethod;
			appendToQueryString("code_challenge_method", codeChallengeMethod);
			return this;
		}
		public Builder withCodeChallenge(String codeChallenge) {
			this.codeChallenge = codeChallenge;
			appendToQueryString("code_challenge", codeChallenge);
			return this;
		}
		public Builder withState(String state) {
			this.state = state;
			appendToQueryString("state", state);
			return this;
		}
		public Builder withScopes(String scopes) {
			this.scopes = scopes;
			appendToQueryString("scope", scopes);
			return this;
		}
		// customized query parameter for testing purposes
		public Builder with(String queryParameter, String value) {
			appendToQueryString(queryParameter, value);
			return this;
		}
		private void appendToQueryString(String queryParameter, String value) {
			if(value != null)
				querySB.append(queryParameter + "=" + value + '&');
		}
		
		// BUILDER
		public OAuth2URI build() {
			OAuth2URI oauth2URI = new OAuth2URI();
			oauth2URI.clientID = this.clientID;
			oauth2URI.responseType = this.responseType;
			oauth2URI.redirectURI = this.redirectURI;
			oauth2URI.codeChallengeMethod = this.codeChallengeMethod;
			oauth2URI.codeChallenge = this.codeChallenge;
			oauth2URI.state = this.state;
			oauth2URI.scopes = this.scopes;
			
			oauth2URI.url = "";
			int queryStringLength = this.querySB.length();
			if(queryStringLength != 0) {
				this.querySB.deleteCharAt(queryStringLength - 1);	// remove the last & character
				oauth2URI.url += '?' + this.querySB.toString();
			}
			// base_uri and endpoint are mandatory even for testing otherwise we aren't verifying the URI
			oauth2URI.url = BaseURIs.ACCOUNTS + Endpoints.AUTHORIZE + oauth2URI.url;
			
			return oauth2URI;
		}
	}
}