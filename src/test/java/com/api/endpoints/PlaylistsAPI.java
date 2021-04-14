package com.api.endpoints;

import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

import com.api.constants.Endpoints;
import com.api.utils.OAuth2Util;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import static org.hamcrest.Matchers.*;

/**
 * This class contains valid endpoint methods for the Playlist API
 * used in End-to-end (E2E) test cases.
 * @author Muana Kimba
 */
public final class PlaylistsAPI {

	// CONSTRUCTOR
	private PlaylistsAPI() throws Exception {
		throw new Exception();
	}

	// METHODS
	/**
	 * This method creates an empty playlist. 
	 * @param accessToken The access token
	 * @param userID The user id
	 * @param visibility True for public and false for private
	 * @return The playlist id
	 */
	@Step("Create a playlist")
	public static String createAPlaylist(String accessToken, String userID, boolean visibility) {
		JSONObject playlist = new JSONObject();
		playlist.put("name", "Ma liste de chansons");
		playlist.put("description", "Musique de tout genre");
		playlist.put("public", visibility);
		
		return
				given().
					spec(OAuth2Util.getOAuth2ReqSpec(accessToken)).
					basePath(Endpoints.CREATE_A_PLAYLIST).
					pathParam("user_id", userID).
					contentType(ContentType.JSON).
					body(playlist.toString()).
				when().
					post().
				then().
					statusCode(HttpStatus.SC_CREATED).
					body("owner.id", equalTo(userID),
							"public", equalTo(visibility)).
					extract().response().getBody().jsonPath().getString("id");
	}
	
	/**
	 * This method creates an empty playlist. 
	 * @param accessToken The access token
	 * @param userID The user id
	 * @param visibility True for public and false for private
	 */
	@Step("Unfollow a playlist")
	public static void unfollowAPlaylist(String accessToken, String playlistID) {
		given().
			spec(OAuth2Util.getOAuth2ReqSpec(accessToken)).
			basePath(Endpoints.UNFOLLOW_A_PLAYLIST).
			pathParam("playlist_id", playlistID).
		when().
			delete().
		then().
			statusCode(HttpStatus.SC_OK);
	}
}