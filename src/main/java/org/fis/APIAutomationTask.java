package org.fis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class APIAutomationTask {

    @Test
    public void testBpiValues() {
        // Send GET request to the API
        Response response = RestAssured.given()
                .baseUri("https://api.coindesk.com")
                .basePath("/v1/bpi/currentprice.json")
                .when()
                .get();

        // Verify the response code
        response.then().statusCode(200);

        // Verify there are 3 BPIs: USD, GBP, EUR
        response.then().body("bpi.keySet()", hasItems("USD", "GBP", "EUR"));

        // Verify the GBP 'description' equals 'British Pound Sterling'
        response.then().body("bpi.GBP.description", equalTo("British Pound Sterling"));
    }
}
