package com.api.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.api.base.BaseTest;
import com.api.constants.Endpoints;
import com.api.constants.FilePaths;
import com.api.utils.DataReader;
import com.api.utils.OAuth2Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.apache.http.HttpStatus;

// Get Multiple Artists		or		Get Several Artists
@Test(groups = {"Artists API"})
public class GetMultipleArtistsTest extends BaseTest {
	
	@Test(priority = 1, groups = {"JSON Schema Validation"}, dataProvider = "validArtistsIdsDp")
	@Description("Verify the Get Multiple Artists endpoint JSON schema with two or more artists ids.")
	@Epic("Allow authorized accounts to access the Artists API")
	@Feature("View multiple artists catalog information")
	@Story("The Get Multiple Artists endpoint response body should complies with its defined JSON schema.")
	@Severity(SeverityLevel.CRITICAL)
	@Step("Verify Get Multiple Artists JSON schema")
	public void verifyGetMultipleArtistsJSONSchema(String artistsIds) throws Exception {
		// REQUEST SPECIFICATION
		given().
			filter(new AllureRestAssured()).
			spec(OAuth2Util.getOAuth2ReqSpec(oauth2ResponseBody.getAccessToken())).
			basePath(Endpoints.GET_MULTIPLE_ARTISTS).
			queryParam("ids", artistsIds).
		// REQUEST METHOD
		when().
			get().
		// RESPONSE SPECIFICATION 
		then().
			statusCode(HttpStatus.SC_OK).
			spec(responseTime).
			// schema validation of the Response body
			body(matchesJsonSchema(new File(FilePaths.JSON_SCHEMA + "GetMultipleArtists.json")));
	}
	
	@DataProvider
	public String [] validArtistsIdsDp() throws Exception {
		return DataReader.readTXT(FilePaths.TEST_DATA + "validArtistsIds.txt");
	}
}