package test.util;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import test.util.ExtentReports.ExtentTestManager;


public class ValidatePostRequest {

    private static final Logger logger = Logger.getLogger( ValidatePostRequest.class );

    @Test(description = "Test a sample POST request")
    public void validate_response_headers_test() {
        RestAssured.baseURI ="http://restapi.demoqa.com/customer";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("FirstName", "Raktim"); // Cast
        requestParams.put("LastName", "Biswas");
        requestParams.put("UserName", "testUser");
        requestParams.put("Password", "password@123");

        requestParams.put("Email",  "rktmbsws@gmail.com");
        request.body(requestParams.toJSONString());
        Response response = request.post("/register");

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        logger.debug( "StatusCode " + statusCode);

        ExtentTestManager.getTest().setDescription("Test a sammple POST request");
    }
}