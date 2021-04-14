package com.api.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.api.base.BaseTest;
import com.api.constants.Endpoints;
import com.api.endpoints.PlaylistsAPI;
import com.api.utils.JsonUtil;
import com.api.utils.OAuth2Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;

@Test(groups = {"Playlists API", "End-to-end (E2E)"})
public class AddItemsToAPlaylistTest extends BaseTest {
	
	// ATTRIBUTES
	private static final int MAX_ITEM_PER_REQUEST = 100;
	private static final int MAX_ITEM_ERROR_BODY_SIZE = 1;
	private static final int MAX_ITEM_ERROR_OBJECT_SIZE = 2;
	private static final String ERROR_MESSAGE = "You can add a maximum of " + MAX_ITEM_PER_REQUEST + " tracks per request.";
	private static RequestSpecification reqSpec;
	
	@BeforeClass
	public void specSetup() throws Exception {
		reqSpec = new RequestSpecBuilder().
				addRequestSpecification(OAuth2Util.getOAuth2ReqSpec(oauth2ResponseBody.getAccessToken())).
				setBasePath(Endpoints.ADD_ITEMS_TO_A_PLAYLIST).
				setContentType(ContentType.JSON).
				build();
	}
	
	@Test(priority = 1)
	@Description("Verify the Add Items to a Playlist endpoint by adding " + MAX_ITEM_PER_REQUEST + " tracks to a public playlist.")
	@Epic("Allow authorized accounts to access the Playlists API")
	@Feature("Add items to a playlist")
	@Story("The Add Items to a Playlist endpoint should allow the maximum number of items to be added to a public playlist per request.")
	@Severity(SeverityLevel.NORMAL)
	@Step("Verify Add Items to a Playlist by adding " + MAX_ITEM_PER_REQUEST + " tracks to a public playlist")
	public void validAddTrackToPublicPlaylist() throws Exception {
		// create a public playlist
		String playlistID = PlaylistsAPI.createAPlaylist(
				oauth2ResponseBody.getAccessToken(), prop.getProperty("USER_ID"), true);
		
		// create a payload of the maximum number of track allowed per request
		JSONObject payload = new JSONObject();
		payload.put("uris", JsonUtil.repeat(MAX_ITEM_PER_REQUEST, "spotify:track:0HU5JnVaKNTWf6GykV9Zn8"));
		
		// add the maximum number of track to the playlist
		given().
			filter(new AllureRestAssured()).
			spec(reqSpec).
			pathParam("playlist_id", playlistID).
			body(payload.toString()).
		when().
			post().
		then().
			statusCode(HttpStatus.SC_CREATED).
		spec(responseTime);
		
		// unfollow (delete) the playlist
		PlaylistsAPI.unfollowAPlaylist(oauth2ResponseBody.getAccessToken(), playlistID);
	}
	
	@Test(priority = 2)
	@Description("Verify the Add Items to a Playlist endpoint by adding " + (MAX_ITEM_PER_REQUEST + 1) + " tracks to a private playlist.")
	@Epic("Allow authorized accounts to access the Playlists API")
	@Feature("Add items to a playlist")
	@Story("The Add Items to a Playlist endpoint should not exceed the maximum allowed items to be added to a private playlist per request.")
	@Severity(SeverityLevel.NORMAL)
	@Step("Verify Add Items to a Playlist by adding " + (MAX_ITEM_PER_REQUEST + 1) + " tracks to a private playlist")
	public void invalidAddTrackToPrivatePlaylist() throws Exception {
		// create a private playlist
		String playlistID = PlaylistsAPI.createAPlaylist(
				oauth2ResponseBody.getAccessToken(), prop.getProperty("USER_ID"), false);
		
		// create a payload of more than the maximum number of track allowed per request
		JSONObject payload = new JSONObject();
		payload.put("uris", JsonUtil.repeat(MAX_ITEM_PER_REQUEST + 1, "spotify:track:0HU5JnVaKNTWf6GykV9Zn8"));
		
		// add more than the maximum number of track to the playlist
		given().
			filter(new AllureRestAssured()).
			spec(reqSpec).
			pathParam("playlist_id", playlistID).
			body(payload.toString()).
		when().
			post().
		then().
			statusCode(HttpStatus.SC_BAD_REQUEST).
			body("$", aMapWithSize(MAX_ITEM_ERROR_BODY_SIZE),
					"error", aMapWithSize(MAX_ITEM_ERROR_OBJECT_SIZE),
					"error.status", equalTo(HttpStatus.SC_BAD_REQUEST),
					"error.message", equalTo(ERROR_MESSAGE)).
			spec(responseTime);
		
		// unfollow (delete) the playlist
		PlaylistsAPI.unfollowAPlaylist(oauth2ResponseBody.getAccessToken(), playlistID);
	}
}