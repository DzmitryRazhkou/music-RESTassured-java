package com.api.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.api.base.BaseTest;
import com.api.constants.Endpoints;
import com.api.constants.FilePaths;
import com.api.utils.OAuth2Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;

import com.api.utils.DataReader;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;

@Test(groups = {"Artists API"})
public class GetAnArtistTest extends BaseTest {
	
	@Test(priority = 1, dataProvider = "validArtistDp")
	@Description("Verify the Get an Artist endpoint simplified with a valid artist id and OAuth2 access token.")
	@Epic("Allow authorized accounts to access the Artists API")
	@Feature("View artist catalog information")
	@Story("As a authenticated user,"
			+ " I want to be able to retrieve a artist"
			+ " so that I can view their catalog information.")
	@Severity(SeverityLevel.NORMAL)
	@Step("Verify Get an Artist simplified catalog information")
	public void validGetAnArtistSimplified(String id, String external_urls, String href, String name,
			String type, String uri) throws Exception {
		
		// REQUEST SPECIFICATION
		given().
			filter(new AllureRestAssured()).
			spec(OAuth2Util.getOAuth2ReqSpec(oauth2ResponseBody.getAccessToken())).
			basePath(Endpoints.GET_AN_ARTIST).
			pathParam("id", id).
		
		/* RESPONSE VERIFICATION
		 * based on the artist object (simplified):
		 * https://developer.spotify.com/documentation/web-api/reference/object-model/#artist-object-simplified
		 */
		expect().
			statusCode(HttpStatus.SC_OK).
			body("external_urls.spotify", equalTo(external_urls),
					"href", equalTo(href),
					"id" , equalTo(id),
					"name" ,equalTo(name),
					"type", equalTo(type),
					"uri", equalTo(uri)).
			spec(responseTime).
        
		// REQUEST METHOD
		when().
			get();
	}
	
	@DataProvider
	public String[][] validArtistDp() throws Exception {
		return DataReader.readCSV(FilePaths.TEST_DATA + "validArtistsSimplified.csv", ",");
	}
}