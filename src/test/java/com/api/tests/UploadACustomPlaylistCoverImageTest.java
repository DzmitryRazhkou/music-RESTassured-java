package com.api.tests;

import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.api.base.BaseTest;
import com.api.constants.Endpoints;
import com.api.constants.FilePaths;
import com.api.constants.Scopes;
import com.api.endpoints.PlaylistsAPI;
import com.api.utils.Base64Util;
import com.api.utils.OAuth2Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

@Test(groups = {"Playlists API", "End-to-end (E2E)"})
public class UploadACustomPlaylistCoverImageTest extends BaseTest {
	
	// TEST METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////////
	@Test(priority = 1, dataProvider = "validCoverImagesDp")
	@Description("Verify the Upload a Custom Playlist Cover Image endpoint with all scopes and a public playlist.")
	@Epic("Allow authorized accounts to access the Playlists API")
	@Feature("Upload a custom playlist cover image")
	@Story("As a authenticated user,"
			+ " I want to be able to upload a playlist cover image"
					+ " so that I can change it.")
	@Step("Verify Upload a Custom Playlist Cover Image with all scopes")
	@Severity(SeverityLevel.MINOR)
	public void validUploadACustomPlaylistCoverImageWithAllScopes(String playlistCoverImageFile) throws Exception {
		// create a private playlist
		String playlistID = PlaylistsAPI.createAPlaylist(oauth2ResponseBody.getAccessToken(),
				prop.getProperty("USER_ID"), true);
		
		// upload a valid custom playlist cover image
		given().
			filter(new AllureRestAssured()).
			// use the @BeforeSuite access token
			spec(OAuth2Util.getOAuth2ReqSpec(oauth2ResponseBody.getAccessToken())).
			basePath(Endpoints.UPLOAD_A_CUSTOM_PLAYLIST_COVER_IMAGE).
			pathParam("playlist_id", playlistID).
			// specify the configuration to encode the text content to a jpeg image
			config(config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("image/jpeg", ContentType.TEXT))).
			// encode playlist cover image to Base64 format
			body(Base64Util.encodeFileToBase64(playlistCoverImageFile)).
		when().
			put().
		then().
			statusCode(HttpStatus.SC_ACCEPTED).
			spec(responseTime);
		
		// unfollow (delete) the playlist
		PlaylistsAPI.unfollowAPlaylist(oauth2ResponseBody.getAccessToken(), playlistID);
	}
	
	@Test(priority = 2, groups = {"Security"}, dataProvider = "invalidTwoScopesDp")
	@Description("Verify the Upload a Custom Playlist Cover Image endpoint with two of the three required scopes and a public playlist.")
	@Epic("Allow authorized accounts to access the Playlists API")
	@Feature("Upload a custom playlist cover image")
	@Story("Authenticated users should not be able to upload a custom playlist cover image with insufficient scopes.")
	@Severity(SeverityLevel.CRITICAL)
	@Step("Verify Upload a Custom Playlist Cover Image with two scopes")
	public void invalidUploadACustomPlaylistCoverImageWithTwoScopes(String playlistCoverImageFile, String scopes) throws Exception {
		// create a private playlist
		String playlistID = PlaylistsAPI.createAPlaylist(oauth2ResponseBody.getAccessToken(),
				prop.getProperty("USER_ID"), true);
		
		// upload a valid custom playlist cover image with two of three required scopes
		given().
			filter(new AllureRestAssured()).
			// obtain an access token with two of the three required scopes
			spec(OAuth2Util.getOAuth2ReqSpec(obtainOAuth2ResponsePKCE(scopes).getAccessToken())).
			basePath(Endpoints.UPLOAD_A_CUSTOM_PLAYLIST_COVER_IMAGE).
			pathParam("playlist_id", playlistID).
			// specify the configuration to encode the text content to a jpeg image
			config(config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs("image/jpeg", ContentType.TEXT))).
			// encode playlist cover image to Base64 format
			body(Base64Util.encodeFileToBase64(playlistCoverImageFile)).
		when().
			put().
		then().
			statusCode(HttpStatus.SC_UNAUTHORIZED).
			spec(responseTime);
		
		// unfollow (delete) the playlist
		PlaylistsAPI.unfollowAPlaylist(oauth2ResponseBody.getAccessToken(), playlistID);
	}
	
	// DATA PROVIDER METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////////
	@DataProvider
	public String[] validCoverImagesDp() throws Exception {
		return new String[] {FilePaths.TEST_DATA + "playlistCover.JPEG"};
	}
	
	@DataProvider
	public String[][] invalidTwoScopesDp() throws Exception {
		// Upload a Custom Playlist Cover Image endpoint requires more scopes than just the ones in Playlist
		return new String[][] {{FilePaths.TEST_DATA + "playlistCover.JPEG", Scopes.PLAYLIST}};
	}
}