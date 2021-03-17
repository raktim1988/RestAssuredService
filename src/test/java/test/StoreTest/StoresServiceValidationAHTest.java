package test.StoreTest;

import io.restassured.response.Response;
import junit.framework.Assert;
import org.testng.annotations.Test;
import test.bean.ObjectMapperBean;
import test.util.APITestingUtilities;
import test.util.ConstantFields;
import test.util.ExtentReports.ExtentTestManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

/**
 * @author Raktim Biswas
 */
public class StoresServiceValidationAHTest {

	/*
	 * This is the first Rest Assured Test for our Store Service
	 */

	  @Test(description="Test AH Store Service Status , if it's OK or Bad Request")
	  public void testStoreService() {

          final String xAuth = "https://api-tst.ah.nl/member-service/v1/members/anonymous";
		  Response resp = given()
	    		  .headers("X-Authorization",xAuth)
	    		  .expect()
	    		    .statusCode(200)
	    		    .log().ifError()
	              .when()
	              .get(ConstantFields.apiURLAH);

          ExtentTestManager.getTest().setDescription( "Test Store Service Status Code" );
	  }

    /*
	   * This is to validate the JSON body for the stores service response
	   */

    @Test(description = "Test AH JSON response, if it contains all required attributes")
	  public void testStoreJSON() {

        final String xAuth = ConstantFields.authKey;

        /*
		   * You get data here from excel ?
		   */
		  Response resp = given()
	    		  .headers("X-Authorization",xAuth)
	    		  .expect()
	    		    .statusCode(200)
	    		    .log().ifError()
	              .when()
	              .get(ConstantFields.apiURLAH);
		      //System.out.println(resp.asString());

        String respJSONString = resp.asString();
		      if (respJSONString != null )
		    		  {
		    	  Assert.assertTrue(respJSONString.contains("id"));
		    	  Assert.assertTrue(respJSONString.contains("name"));
		    	  Assert.assertTrue(respJSONString.contains("phone"));
		    	  Assert.assertTrue(respJSONString.contains("openingHours"));
		    	  Assert.assertTrue(respJSONString.contains("address"));
		    	  Assert.assertTrue(respJSONString.contains("geoLocation"));
		    	  Assert.assertFalse(respJSONString.contains("storeType"));

                      }
		      ExtentTestManager.getTest().setDescription("Test JSON response, if it contains all required attributes");
	  }

    /*
	   * JSON object for each field
	   */
	  @Test(description="Test AH JSON response, if all the list of objects contains proper field value")
	  public void testJSONObject() throws IOException {
		  // API call which will return Json Object
		  final String xAuth =  ConstantFields.authKey;

          Response resp = given()
	    		  .headers("X-Authorization",xAuth)
	    		  .expect()
	    		    .statusCode(200)
	    		    .log().ifError()
	              .when()
	              .get(ConstantFields.apiURLAH);
	 /*
      * Test for the JSON response
	  */

          List<ObjectMapperBean> objMapperBeanList = APITestingUtilities.parseJSONdata( resp.asString() );

	      try {

			  if ((objMapperBeanList != null)||(!objMapperBeanList.isEmpty())) {

				  IntStream.rangeClosed( 0, objMapperBeanList.size() - 1 ).forEach( i -> {
					  Assert.assertNotNull( objMapperBeanList.get( i ).getName() );
					  Assert.assertNotNull( objMapperBeanList.get( i ).getPhone() );
					  Assert.assertNotNull( objMapperBeanList.get( i ).getOpeningHours() );
					  Assert.assertNotNull( objMapperBeanList.get( i ).getAddress() );
					  Assert.assertNotNull( objMapperBeanList.get( i ).getGeoLocation() );
					  Assert.assertNull( objMapperBeanList.get( i ).getStoreType() );
				  } );
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}

          ExtentTestManager.getTest().setDescription( "Test JSON response, if all the list of objects contains proper field value" );
      }

}
