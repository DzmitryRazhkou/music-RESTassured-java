package com.api.authorization;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * This class is a POJO used to deserialize the OAuth2 Response body<br>
 * containing the access_token, token_type, expires_in, refresh_token and scope.
 * @author Muana Kimba
 */
public class OAuth2ResponseBody {
	
	// ATTRIBUTES
	@JsonAlias({"access_token"})
	private String accessToken;
	
	@JsonAlias({"token_type"})
	private String tokenType;
	
	@JsonAlias({"expires_in"})
	private int expiresIn;
	
	@JsonAlias({"refresh_token"})
	private String refreshToken;
	
	private String scope;
	
	// METHODS
	public String getAccessToken() {
		return accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public String getScope() {
		return scope;
	}
	
	@Override
	public String toString() {
		return "OAuth2ResponseBody [access_token=" + accessToken + ", token_type=" + tokenType + ", expires_in="
				+ expiresIn + ", refresh_token=" + refreshToken + ", scope=" + scope + "]";
	}
}